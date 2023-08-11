package com.bookStore.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.bookStore.entity.Book;
import com.bookStore.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

@SpringJUnitConfig
@SpringBootTest
public class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookService bookService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this); // Initialize mocks
    }

    @Test
    public void testSave() {
        Book book = new Book();
        bookService.save(book);
        verify(bookRepository).save(book);
    }

    @Test
    public void testGetAllBook() {
        List<Book> books = new ArrayList<>();
        when(bookRepository.findAll()).thenReturn(books);

        List<Book> result = bookService.getAllBook();
        assertEquals(books, result);
    }

    @Test
    public void testGetBookById() {
        Book book = new Book();
        when(bookRepository.findById(anyInt())).thenReturn(Optional.of(book));

        Book result = bookService.getBookById(1);
        assertEquals(book, result);
    }

    @Test
    public void testDeleteById() {
        bookService.deleteById(1);
        verify(bookRepository).deleteById(1);
    }
}