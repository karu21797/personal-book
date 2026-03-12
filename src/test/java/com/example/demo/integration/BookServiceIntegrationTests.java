package com.example.demo.integration;

import com.example.demo.db.Book;
import com.example.demo.db.repository.BookRepository;
import com.example.demo.service.BookService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
@ActiveProfiles("test")
@Transactional
class BookServiceIntegrationTests {

    @Autowired
    private BookService bookService;

    @Autowired
    private BookRepository bookRepository;

    @Test
    void shouldSaveBookFromGoogleApi() {

        String googleId = "x8BvqSRbR3cC";

        Book savedBook = bookService.addBook(googleId).getBody();

        assertNotNull(savedBook);
        assertEquals("x8BvqSRbR3cC", savedBook.getId());
        assertTrue(bookRepository.existsById("x8BvqSRbR3cC"));
    }

}