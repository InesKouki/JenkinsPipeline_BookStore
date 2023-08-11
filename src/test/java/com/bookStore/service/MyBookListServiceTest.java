package com.bookStore.service;

import static org.junit.jupiter.api.Assertions.*;

import com.bookStore.entity.MyBookList;
import com.bookStore.repository.MyBookRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringJUnitConfig
@SpringBootTest
public class MyBookListServiceTest {

    @Autowired
    private MyBookListService myBookListService;

    @MockBean
    private MyBookRepository myBookRepository;

    @Test
    public void testSaveMyBooks() {
        MyBookList book = new MyBookList();
        myBookListService.saveMyBooks(book);
        verify(myBookRepository, times(1)).save(book);
    }

    @Test
    public void testGetAllMyBooks() {
        List<MyBookList> expectedBooks = new ArrayList<>();
        when(myBookRepository.findAll()).thenReturn(expectedBooks);

        List<MyBookList> actualBooks = myBookListService.getAllMyBooks();

        assertEquals(expectedBooks, actualBooks);
    }

    @Test
    public void testDeleteById() {
        int id = 1;
        myBookListService.deleteById(id);
        verify(myBookRepository, times(1)).deleteById(id);
    }
}
