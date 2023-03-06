package br.com.erudio.services;

import br.com.erudio.controllers.PersonController;
import br.com.erudio.data.vo.v1.PersonVO;
import br.com.erudio.exceptions.RequiredObjectIsNullException;
import br.com.erudio.exceptions.ResourceNotFoundException;
import br.com.erudio.mapper.DozerMapper;
import br.com.erudio.models.PersonModel;
import br.com.erudio.repositories.PersonRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class PersonService {

    private Logger logger = Logger.getLogger(PersonService.class.getName());

    @Autowired
    PersonRepository personRepository;

    public Page<PersonVO> findAll(Pageable pageable) {
        logger.info("Finding all people");
        var personPage = personRepository.findAll(pageable);
        var personVoPage = personPage.map(person -> DozerMapper.parseObject(person, PersonVO.class));
        personVoPage.map(person -> person.add(linkTo(methodOn(PersonController.class).findById(person.getKey())).withSelfRel()));
        return personVoPage;
    }

    public PersonVO findById(Long id) {
        logger.info("Finding one person with id " + id);
        var entity = personRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records found for id " + id));
        var vo = DozerMapper.parseObject(entity, PersonVO.class);
        vo.add(linkTo(methodOn(PersonController.class).findById(id)).withSelfRel());
        return vo;
    }

    public PersonVO create(PersonVO personVO) {
        if (personVO == null) throw new RequiredObjectIsNullException();
        logger.info("Creating one person");
        var entity = DozerMapper.parseObject(personVO, PersonModel.class);
        var vo = DozerMapper.parseObject(personRepository.save(entity), PersonVO.class);
        vo.add(linkTo(methodOn(PersonController.class).findById(vo.getKey())).withSelfRel());
        return vo;
    }

    public PersonVO update(PersonVO personVO) {
        if (personVO == null) throw new RequiredObjectIsNullException();
        logger.info("Updating one person");
        var updatePersonVO = personRepository.findById(personVO.getKey())
                .orElseThrow(() -> new ResourceNotFoundException("No records found for id " + personVO.getKey()));
        updatePersonVO.setFirstName(personVO.getFirstName());
        updatePersonVO.setLastName(personVO.getLastName());
        updatePersonVO.setGender(personVO.getGender());
        updatePersonVO.setAddress(personVO.getAddress());

        var vo = DozerMapper.parseObject(personRepository.save(updatePersonVO), PersonVO.class);
        vo.add(linkTo(methodOn(PersonController.class).findById(vo.getKey())).withSelfRel());
        return vo;
    }

    @Transactional // This annotation tells spring to manage this method as transactional because it modifies the database with a method that is not built-in, PersonRepository.disablePerson(id)
    public PersonVO disablePerson(Long id) {
        logger.info("Disabling one person with id " + id);
        personRepository.disablePerson(id);
        var entity = personRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records found for id " + id));
        var vo = DozerMapper.parseObject(entity, PersonVO.class);
        vo.add(linkTo(methodOn(PersonController.class).findById(id)).withSelfRel());
        return vo;
    }

    public void delete(Long id) {
        logger.info("Deleting one person with id " + id);
        var personModel = personRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records found for id " + id));
        personRepository.delete(personModel);
    }
}
