package br.com.erudio.services;

import br.com.erudio.controllers.BookController;
import br.com.erudio.controllers.PersonController;
import br.com.erudio.data.vo.v1.BookVO;
import br.com.erudio.data.vo.v1.PersonVO;
import br.com.erudio.exceptions.RequiredObjectIsNullException;
import br.com.erudio.exceptions.ResourceNotFoundException;
import br.com.erudio.mapper.DozerMapper;
import br.com.erudio.models.BookModel;
import br.com.erudio.repositories.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class BookService {

    private Logger logger = Logger.getLogger(BookService.class.getName());

    @Autowired
    BookRepository bookRepository;

    @Autowired
    PagedResourcesAssembler<PersonVO> pagedResourcesAssembler;

    public PagedModel<EntityModel<PersonVO>> findAll(Pageable pageable) {
        logger.info("Finding all books");
        var bookPage = bookRepository.findAll(pageable);
        var bookVoPage = bookPage.map(book -> DozerMapper.parseObject(book, PersonVO.class));
        bookVoPage.map(book -> book.add(linkTo(methodOn(PersonController.class).findById(book.getKey())).withSelfRel()));

        var link = linkTo(methodOn(BookController.class)
                .findAll(pageable.getPageNumber(), pageable.getPageSize(), "asc")).withSelfRel();
        return pagedResourcesAssembler.toModel(bookVoPage, link);
    }

    public BookVO findById(Long id) {
        logger.info("Finding one book with id " + id);
        var entity = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records found for id " + id));
        var vo = DozerMapper.parseObject(entity, BookVO.class);
        vo.add(linkTo(methodOn(BookController.class).findById(id)).withSelfRel());
        return vo;
    }

    public BookVO create(BookVO bookVO) {
        if (bookVO == null) throw new RequiredObjectIsNullException();
        logger.info("Creating one book.");
        var entity = DozerMapper.parseObject(bookVO, BookModel.class);
        var newBook = bookRepository.save(entity);
        var vo = DozerMapper.parseObject(newBook, BookVO.class);
        vo.add(linkTo(methodOn(BookController.class).findById(vo.getKey())).withSelfRel());
        return vo;
    }

    public BookVO update(BookVO bookVO) {
        if (bookVO == null) throw new RequiredObjectIsNullException();
        logger.info("Updating one book.");
        var updateBookVo = bookRepository.findById(bookVO.getKey())
                .orElseThrow(() -> new ResourceNotFoundException("No records found for id " + bookVO.getKey()));
        updateBookVo.setAuthor(bookVO.getAuthor());
        updateBookVo.setLaunchDate(bookVO.getLaunchDate());
        updateBookVo.setPrice(bookVO.getPrice());
        updateBookVo.setTitle(bookVO.getTitle());

        var vo = DozerMapper.parseObject(bookRepository.save(updateBookVo), BookVO.class);
        vo.add(linkTo(methodOn(BookController.class).findById(vo.getKey())).withSelfRel());
        return vo;
    }

    public void delete(Long id) {
        logger.info("Deleting one person with id " + id);
        var bookModel = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records found for id " + id));
        bookRepository.delete(bookModel);
    }
}
