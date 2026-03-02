package com.example.demo.service;


import com.example.demo.db.Book;
import com.example.demo.db.repository.BookRepository;
import com.example.demo.exception.InvalidGoogleBookException;
import com.example.demo.google.GoogleBook;
import com.example.demo.google.GoogleBookService;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
/**
 * Unit tests for {@code BookService}.
 *
 * <p>
 * This class verifies business logic implemented in the service layer.
 * It ensures correct behavior independent of the web layer and database.
 * </p>
 *
 * <p>
 * Dependencies such as {@code BookRepository} and external API clients
 * are typically mocked using frameworks like Mockito.
 * </p>
 *
 * <p>
 * These tests focus on isolated business logic validation.
 * </p>
 *
 * @author Karunya L
 */
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class BookServiceTests {

    @Autowired
    private BookService bookService;

    @MockitoBean
    private GoogleBookService googleBookService;

    @MockitoBean
    private BookRepository bookRepository;


    @Test
    void shouldThrowException_whenGoogleBookIsNull() {

        when(googleBookService.getBookById("id")).thenReturn(null);

        InvalidGoogleBookException exception = assertThrows(InvalidGoogleBookException.class,
                () -> bookService.addBook("id")
        );

        assertEquals(" Invalid Google Id for book ", exception.getMessage());
    }

    @Test
    void shouldThrowException_whenItemsAreEmpty() {

        GoogleBook googleBook = mock(GoogleBook.class);
        when(googleBook.items()).thenReturn(Collections.emptyList());

        when(googleBookService.getBookById("id")).thenReturn(googleBook);

        InvalidGoogleBookException exception = assertThrows(InvalidGoogleBookException.class,
                () -> bookService.addBook("id")
        );

        assertEquals(" Invalid Google Id for book ", exception.getMessage());
    }

    @Test
    void shouldThrowException_whenVolumeInfoIsNull() {

        GoogleBook.Item item = mock(GoogleBook.Item.class);
        when(item.volumeInfo()).thenReturn(null);

        GoogleBook googleBook = mock(GoogleBook.class);
        when(googleBook.items()).thenReturn(List.of(item));

        when(googleBookService.getBookById("id")).thenReturn(googleBook);

        InvalidGoogleBookException exception = assertThrows(InvalidGoogleBookException.class,
                () -> bookService.addBook("id")
        );

        assertEquals(" Invalid Google Id for book ", exception.getMessage());
    }

    @Test
    void shouldSaveBookSuccessfully() {

        GoogleBook.VolumeInfo volumeInfo = mock(GoogleBook.VolumeInfo.class);
        when(volumeInfo.title()).thenReturn("Test Book");
        when(volumeInfo.authors()).thenReturn(List.of("Author1"));
        when(volumeInfo.pageCount()).thenReturn(200);

        GoogleBook.Item item = mock(GoogleBook.Item.class);
        when(item.id()).thenReturn("id123");
        when(item.volumeInfo()).thenReturn(volumeInfo);

        GoogleBook googleBook = mock(GoogleBook.class);
        when(googleBook.items()).thenReturn(List.of(item));

        when(googleBookService.getBookById("id123")).thenReturn(googleBook);

        Book savedBook = new Book();
        savedBook.setId("id123");

        when(bookRepository.save(any(Book.class))).thenReturn(savedBook);

        ResponseEntity<Book> response = bookService.addBook("id123");

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }
}