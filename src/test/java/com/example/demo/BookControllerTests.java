package com.example.demo;

import com.example.demo.db.Book;
import com.example.demo.db.BookRepository;
import com.example.demo.google.GoogleBook;
import com.example.demo.google.GoogleBookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.mockito.Mockito.when;
import java.util.List;
import java.util.Optional;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class BookControllerTests {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext context;
    @Autowired
    private BookRepository bookRepository;
    @MockitoBean
    private GoogleBookService googleBookService;

    @BeforeEach
    void setup() {
        bookRepository.deleteAll();
        bookRepository.save(new Book("lRtdEAAAQBAJ", "Spring in Action", "Craig Walls"));
        bookRepository.save(new Book("12muzgEACAAJ", "Effective Java", "Joshua Bloch"));
    }

    @Test
    void testGetAllBooks() throws Exception {
        mockMvc.perform(get("/books"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].title").value("Spring in Action"))
            .andExpect(jsonPath("$[1].title").value("Effective Java"));
    }


    @Test
    void testAddBook_HappyCase() throws Exception {

        GoogleBook.VolumeInfo volumeInfo = new GoogleBook.VolumeInfo(
                " Code",
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
                "sample-id",
                null,
                volumeInfo,
                null
        );

        GoogleBook googleBook = new GoogleBook(
                "books#volumes",
                1,
                List.of(item)
        );

        when(googleBookService.getBookById("id")).thenReturn(googleBook);

        mockMvc.perform(post("/books/id"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value(" Code"))
                .andExpect(jsonPath("$.author").value("Robert C. Martin"));

        // Verify DB
        Optional<Book> bookOptional = bookRepository.findById("random-id");
        assert(!bookOptional.isPresent());
    }

    @Test
    void testAddBook_InvalidGoogleIdForBook() throws Exception {

        when(googleBookService.getBookById("random-id")).thenReturn(null);

        mockMvc.perform(post("/books/random-id")).andExpect(status().isBadRequest());

        Optional<Book> bookOptional = bookRepository.findById("random-id");
        assert(!bookOptional.isPresent());

    }


    @Test
    void testAddBook_WhenVolumeInfoNull() throws Exception {

        GoogleBook.Item item = new GoogleBook.Item(
                "id",
                null,
                null,   // volumeInfo NULL
                null
        );

        GoogleBook googleBook = new GoogleBook(
                "books#volumes",
                1,
                List.of(item)
        );

        when(googleBookService.getBookById("id")).thenReturn(googleBook);

        mockMvc.perform(post("/books/id")).andExpect(status().isBadRequest());

        Optional<Book> bookOptional = bookRepository.findById("id");
        assert(!bookOptional.isPresent());

    }


    @Test
    void testAddBook_WhenAuthorMissing_ShouldSetUnknownAuthor() throws Exception {

        GoogleBook.VolumeInfo volumeInfo = new GoogleBook.VolumeInfo(
                "Test Book",
                null,   // authors NULL
                "2020",
                "Publisher",
                200,
                "BOOK",
                "NOT_MATURE",
                null,
                "en",
                null,
                null
        );

        GoogleBook.Item item = new GoogleBook.Item(
                "test-id",
                null,
                volumeInfo,
                null
        );

        GoogleBook googleBook = new GoogleBook(
                "books#volumes",
                1,
                List.of(item)
        );

        when(googleBookService.getBookById("test-id")).thenReturn(googleBook);

        mockMvc.perform(post("/books/test-id"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.author").value("Unknown Author"));
        System.out.println(" book kind: " + googleBook.kind());

    }


}
