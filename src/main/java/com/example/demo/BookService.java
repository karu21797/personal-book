package com.example.demo;


import com.example.demo.db.Book;
import com.example.demo.db.BookRepository;
import com.example.demo.exception.InvalidGoogleBookException;
import com.example.demo.google.GoogleBook;
import com.example.demo.google.GoogleBookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;

@Service
@Slf4j
public class BookService {

    private final BookRepository bookRepository;
    private final GoogleBookService googleBookService;

    @Autowired
    public BookService(BookRepository bookRepository, GoogleBookService googleBookService) {
        this.bookRepository = bookRepository;
        this.googleBookService = googleBookService;
    }

    @Transactional
    public ResponseEntity<Book> addBook(@PathVariable String googleId) {

        GoogleBook googleBook = googleBookService.getBookById(googleId);

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

        Book book = new Book();
        book.setId(item.id());
        book.setTitle(info.title());
        book.setAuthor(info.authors() != null && !info.authors().isEmpty()
                ? info.authors().get(0) : "Unknown Author");
        book.setPageCount(info.pageCount());

        Book saved = bookRepository.save(book);
        log.info("Book saved successfully with id={}", saved.getId());

        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }



}
