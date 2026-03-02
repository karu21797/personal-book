package com.example.demo.google;

import java.util.List;

public record GoogleBook(
        String kind,
        int totalItems,
        List<Item> items
) {

    public record Item(
            String id,
            String selfLink,
            VolumeInfo volumeInfo,
            SearchInfo searchInfo
    ) {}

    public record SearchInfo(
            String textSnippet
    ) {}

    public record VolumeInfo(
            String title,
            List<String> authors,
            String publishedDate,
            String publisher,
            Integer pageCount,
            String printType,
            String maturityRating,
            List<String> categories,
            String language,
            String previewLink,
            String infoLink
    ) {}
}
/**
 * Represents the response received from the Google Books API.
 *
 * <p>This record mirrors the JSON structure returned by:
 * https://www.googleapis.com/books/v1/volumes
 *
 * <p>It contains metadata such as total results and a list of book items.
 */


