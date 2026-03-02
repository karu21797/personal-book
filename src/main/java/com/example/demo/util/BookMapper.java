package com.example.demo.util;

import com.example.demo.db.Book;
import com.example.demo.google.GoogleBook;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
/**
 * Mapper interface for converting Google Books API response objects
 * into internal {@link Book} entity objects.
 *
 * <p>
 * This mapper uses MapStruct to automatically generate the implementation
 * at compile time. It transforms {@link GoogleBook.Item} objects
 * into {@link Book} entities that can be persisted in the database.
 * </p>
 *
 * <p>
 * Custom mapping logic is applied for nested properties and
 * complex fields such as authors.
 * </p>
 *
 * <p><b>Component Model:</b> Spring</p>
 * <p>
 * The generated implementation will be registered as a Spring Bean
 * and can be injected using {@code @Autowired} or constructor injection.
 * </p>
 *
 * @author Karunya L
 */
@Mapper(componentModel = "spring")
public interface BookMapper {

    @Mapping(target = "id", source = "item.id")
    @Mapping(target = "title", source = "item.volumeInfo.title")
    @Mapping(target = "pageCount", source = "item.volumeInfo.pageCount")
    @Mapping(target = "author", source = "item.volumeInfo.authors", qualifiedByName = "mapAuthor")
    Book toBook(GoogleBook.Item item);

    @Named("mapAuthor")
    default String mapAuthor(java.util.List<String> authors) {
        return (authors != null && !authors.isEmpty()) ? authors.get(0) : "Unknown Author";
    }
}