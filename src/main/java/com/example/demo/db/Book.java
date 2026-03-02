package com.example.demo.db;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * JPA entity representing a Book stored in the application database.
 *
 * <p>
 * This entity maps book data retrieved from the Google Books API
 * into a persistent database model.
 * </p>
 *
 * <p>
 * Annotations used:
 * <ul>
 *     <li>{@link Entity} – Marks this class as a JPA entity</li>
 *     <li>{@link Id} – Defines the primary key</li>
 *     <li>{@link Data} – Generates getters, setters, equals, hashCode, toString</li>
 *     <li>{@link NoArgsConstructor} – Required by JPA</li>
 *     <li>{@link AllArgsConstructor} – Generates full-args constructor</li>
 * </ul>
 * </p>
 *
 * <p>
 * The {@code id} field corresponds to the Google Books API identifier
 * and serves as the primary key in the database.
 * </p>
 *
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Book {
    /**
     * Google Books API id.
     */
    @Id
    private String id;
    private String title;
    private String author;
    private Integer pageCount;

    public Book(String id, String title, String author) {
        this.id = id;
        this.title = title;
        this.author = author;
    }
}
