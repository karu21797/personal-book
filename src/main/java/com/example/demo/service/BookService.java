package com.example.demo.service;

import com.example.demo.db.Book;
import com.example.demo.db.repository.BookRepository;
import com.example.demo.exception.InvalidGoogleBookException;
import com.example.demo.google.GoogleBook;
import com.example.demo.google.GoogleBookService;
import com.example.demo.util.BookMapper;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
/**
 * Service layer responsible for managing Book domain operations.
 *
 * <p>Contains business logic for:
 * <ul>
 *     <li>Fetching book details from Google API</li>
 *     <li>Persisting books into local database</li>
 * </ul>
 * @author Karunya L
 */
@Service
@Slf4j
public class BookService {

    private final BookRepository bookRepository;
    private final GoogleBookService googleBookService;
    private final BookMapper bookMapper;

    @Autowired
    public BookService(BookRepository bookRepository, GoogleBookService googleBookService,
                       BookMapper bookMapper) {
        this.bookRepository = bookRepository;
        this.googleBookService = googleBookService;
        this.bookMapper = bookMapper;
    }

    @Transactional
    public ResponseEntity<Book> addBook(String googleId) {
        try {

            GoogleBook googleBook = fetchGoogleBook(googleId);

            GoogleBook.Item item = fetchValidItem(googleBook, googleId);

            Book book = mapToBook(item);

            Book savedBook = saveBook(book);

            return ResponseEntity.status(HttpStatus.CREATED).body(savedBook);

        } catch (InvalidGoogleBookException exception) {
            log.error("Business validation failed for googleId={}", googleId, exception);
            throw exception;

        } catch (Exception exception) {
            log.error("Unexpected error occurred while adding book for googleId={}", googleId, exception);
            throw new RuntimeException("Unable to add book at this time. Please try again later.");
        }
    }

    private GoogleBook fetchGoogleBook(String googleId) {
        return googleBookService.getBookById(googleId);
    }

    private GoogleBook.Item fetchValidItem(GoogleBook googleBook, String googleId) {
        if (googleBook == null || googleBook.items() == null || googleBook.items().isEmpty()) {
            log.error("Invalid Google ID: for book {}", googleId);
            throw new InvalidGoogleBookException(" Invalid Google Id for book ");
        }

        var item = googleBook.items().get(0);
        var info = item.volumeInfo();

        if (info == null) {
            log.error("VolumeInfo is null for googleId={}", googleId);
            throw new InvalidGoogleBookException(" Invalid Google Id for book ");
        }
        log.debug("Book title fetched from Google API: {}", info.title());
        return item;
    }

    private Book mapToBook(GoogleBook.Item item) {

        Book book = bookMapper.toBook(item);
        return book;

    }

    private Book saveBook(Book book) {
        Book savedBook = bookRepository.save(book);
        log.info("Book saved successfully");
        return savedBook;
    }


}
