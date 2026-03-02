package com.example.demo.integration;

import com.example.demo.db.repository.BookRepository;
import com.example.demo.exception.InvalidGoogleBookException;
import com.example.demo.google.GoogleBook;
import com.example.demo.google.GoogleBookService;
import com.example.demo.service.BookService;
import com.example.demo.db.Book;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
/**
 * Integration tests for {@code BookService}.
 *
 * <p>
 * This class verifies end-to-end interaction between:
 * </p>
 *
 * <ul>
 *     <li>Service layer</li>
 *     <li>Repository layer</li>
 *     <li>Database (typically in-memory, e.g., H2)</li>
 * </ul>
 *
 * <p>
 * Unlike unit tests, this test loads the full Spring application context
 * using {@code @SpringBootTest}.
 * </p>
 *
 * <ul>
 *     <li>Ensures actual persistence behavior</li>
 *     <li>Validates transaction management</li>
 *     <li>Confirms real database interactions</li>
 * </ul>
 *
 * <p>
 * These tests provide confidence that all layers of the application
 * work together correctly.
 * </p>
 *
 * @author Karunya L
 */
@SpringBootTest
@Transactional
class BookServiceIntegrationTests {

    @Autowired
    private BookService bookService;

    @Autowired
    private BookRepository bookRepository;

    @MockitoBean
    private GoogleBookService googleBookService;

    @Test
    void addBook_shouldSaveBook_whenValidGoogleId() {

        GoogleBook.VolumeInfo volumeInfo = mock(GoogleBook.VolumeInfo.class);
        when(volumeInfo.title()).thenReturn("Spring Boot Guide");
        when(volumeInfo.authors()).thenReturn(List.of("John Doe"));
        when(volumeInfo.pageCount()).thenReturn(250);

        GoogleBook.Item item = mock(GoogleBook.Item.class);
        when(item.id()).thenReturn("g123");
        when(item.volumeInfo()).thenReturn(volumeInfo);

        GoogleBook googleBook = mock(GoogleBook.class);
        when(googleBook.items()).thenReturn(List.of(item));

        when(googleBookService.getBookById("g123")).thenReturn(googleBook);

        ResponseEntity<Book> response = bookService.addBook("g123");

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Spring Boot Guide", response.getBody().getTitle());

        Optional<Book> saved = bookRepository.findById("g123");
        assertTrue(saved.isPresent());
    }

    @Test
    void addBook_shouldThrowInvalidGoogleBookException_whenInvalidGoogleId() {

        when(googleBookService.getBookById("invalid")).thenReturn(null);

        assertThrows(InvalidGoogleBookException.class,
                () -> bookService.addBook("invalid"));
    }
}