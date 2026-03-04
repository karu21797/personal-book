package com.example.demo.integration;

import com.example.demo.db.Book;
import com.example.demo.db.repository.BookRepository;
import com.example.demo.google.GoogleBook;
import com.example.demo.google.GoogleBookService;
import com.example.demo.service.BookService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
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

    @MockitoBean
    private GoogleBookService googleBookService;

    @Test
    void shouldSaveBookFromGoogleApi() {

        GoogleBook.VolumeInfo volumeInfo = new GoogleBook.VolumeInfo(
                "Clean Code",
                List.of("Robert C. Martin"),
                "2008",
                "Prentice Hall",
                464,
                "BOOK",
                "NOT_MATURE",
                List.of("Programming"),
                "en",
                null,
                null
        );

        GoogleBook.Item item = new GoogleBook.Item(
                "x8BvqSRbR3cC",
                null,
                volumeInfo,
                null
        );

        GoogleBook mockBook = new GoogleBook(
                "books#volumes",
                1,
                List.of(item)
        );

        Mockito.when(
                googleBookService.getBookById(Mockito.anyString())).thenReturn(mockBook);

        Book savedBook = bookService.addBook("x8BvqSRbR3cC").getBody();

        assertNotNull(savedBook);
        assertEquals("x8BvqSRbR3cC", savedBook.getId());
        assertEquals("Clean Code", savedBook.getTitle());
        assertEquals(1, bookRepository.count());
    }
}