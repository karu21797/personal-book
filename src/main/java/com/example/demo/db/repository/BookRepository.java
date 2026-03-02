package com.example.demo.db.repository;

import com.example.demo.db.Book;
import org.springframework.data.jpa.repository.JpaRepository;
/**
 * Repository interface for managing {@link Book} entities.
 *
 * <p>
 * This interface extends {@link JpaRepository}, which provides
 * built-in CRUD operations and pagination support for the
 * {@link Book} entity.
 * </p>
 */
public interface BookRepository extends JpaRepository<Book, String> {
}
