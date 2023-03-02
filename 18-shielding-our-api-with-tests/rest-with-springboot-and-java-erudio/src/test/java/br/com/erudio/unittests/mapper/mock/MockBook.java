package br.com.erudio.unittests.mapper.mock;

import br.com.erudio.data.vo.v1.BookVO;
import br.com.erudio.models.BookModel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MockBook {


    public BookModel mockEntity() {
        return mockEntity(0);
    }
    
    public BookVO mockVO() {
        return mockVO(0);
    }
    
    public List<BookModel> mockEntityList() {
        List<BookModel> books = new ArrayList<BookModel>();
        for (int i = 0; i < 14; i++) {
            books.add(mockEntity(i));
        }
        return books;
    }

    public List<BookVO> mockVOList() {
        List<BookVO> books = new ArrayList<>();
        for (int i = 0; i < 14; i++) {
            books.add(mockVO(i));
        }
        return books;
    }
    
    public BookModel mockEntity(Integer number) {
        BookModel book = new BookModel();
        book.setId(number.longValue());
        book.setAuthor("Author Test" + number);
        book.setLaunchDate(new Date(number * 10000));
        book.setPrice(Double.valueOf(number * 10));
        book.setTitle("Title Test" + number);
        return book;
    }

    public BookVO mockVO(Integer number) {
        BookVO book = new BookVO();
        book.setKey(number.longValue());
        book.setAuthor("Author Test" + number);
        book.setLaunchDate(new Date(number * 10000));
        book.setPrice(Double.valueOf(number * 10));
        book.setTitle("Title Test" + number);
        return book;
    }

}
