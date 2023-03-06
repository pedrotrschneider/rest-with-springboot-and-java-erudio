package br.com.erudio.unittests.mockito.services;

import br.com.erudio.exceptions.RequiredObjectIsNullException;
import br.com.erudio.repositories.BookRepository;
import br.com.erudio.services.BookService;
import br.com.erudio.unittests.mapper.mock.MockBook;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    MockBook input;

    @InjectMocks
    private BookService bookService;

    @Mock
    BookRepository bookRepository;

    @BeforeEach
    void setUpMocks() throws Exception {
        input = new MockBook();
        MockitoAnnotations.openMocks(this);
    }

//    @Test
//    void findAll() {
//        var entityList = input.mockEntityList();
//
//        when(bookRepository.findAll()).thenReturn(entityList);
//
//        var result = bookService.findAll();
//        assertNotNull(result);
//        assertEquals(14, result.size());
//        for (int i = 0; i < result.size(); i++) {
//            var book = result.get(i);
//            assertNotNull(book.getKey());
//            assertNotNull(book.getLinks());
//            assertTrue(book.toString().contains("links: [</api/v1/book/" + i + ">;rel=\"self\"]"));
//            assertEquals("Author Test" + i, book.getAuthor());
//            assertTrue(book.getLaunchDate().equals(new Date(i * 10000)));
//            assertEquals(Double.valueOf(i * 10), book.getPrice());
//            assertEquals("Title Test" + i, book.getTitle());
//        }
//    }

    @Test
    void findById() {
        var entity = input.mockEntity(1);
        entity.setId(1L);

        when(bookRepository.findById(1L)).thenReturn(Optional.of(entity));

        var result = bookService.findById(1L);
        assertNotNull(result);
        assertNotNull(result.getKey());
        assertNotNull(result.getLinks());
        assertTrue(result.toString().contains("links: [</api/v1/book/1>;rel=\"self\"]"));
        assertEquals("Author Test1", result.getAuthor());
        assertEquals(new Date(10000), result.getLaunchDate());
        assertEquals(Double.valueOf(10), result.getPrice());
        assertEquals("Title Test1", result.getTitle());
    }

    @Test
    void create() {
        var entity = input.mockEntity(1);
        var persisted = entity;
        persisted.setId(1L);

        var vo = input.mockVO(1);
        vo.setKey(1L);

        when(bookRepository.save(entity)).thenReturn(persisted);

        var result = bookService.create(vo);
        assertNotNull(result);
        assertNotNull(result.getKey());
        assertNotNull(result.getLinks());
        assertTrue(result.toString().contains("links: [</api/v1/book/1>;rel=\"self\"]"));
        assertEquals("Author Test1", result.getAuthor());
        assertEquals(new Date(10000), result.getLaunchDate());
        assertEquals(Double.valueOf(10), result.getPrice());
        assertEquals("Title Test1", result.getTitle());
    }

    @Test
    void createWithNullBook() {
        var exception = assertThrows(RequiredObjectIsNullException.class, () -> {
           bookService.create(null);
        });
        String expectedMessage = "It is not allowed to persist a null object.";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void update() {
        var entity = input.mockEntity(1);
        entity.setId(1L);
        var persisted = entity;
        persisted.setId(1L);

        var vo = input.mockVO(1);
        vo.setKey(1L);

        when(bookRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(bookRepository.save(entity)).thenReturn(persisted);

        var result = bookService.update(vo);
        assertNotNull(result);
        assertNotNull(result.getKey());
        assertNotNull(result.getLinks());
        assertTrue(result.toString().contains("links: [</api/v1/book/1>;rel=\"self\"]"));
        assertEquals("Author Test1", result.getAuthor());
        assertEquals(new Date(10000), result.getLaunchDate());
        assertEquals(Double.valueOf(10), result.getPrice());
        assertEquals("Title Test1", result.getTitle());
    }

    @Test
    void updateWithNullBook() {
        var exception = assertThrows(RequiredObjectIsNullException.class, () -> {
            bookService.create(null);
        });
        String expectedMessage = "It is not allowed to persist a null object.";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void delete() {
        var entity = input.mockEntity(1);
        entity.setId(1L);

        when(bookRepository.findById(1L)).thenReturn(Optional.of(entity));

        bookService.delete(1L);
    }
}