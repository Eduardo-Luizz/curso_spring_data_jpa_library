package io.github.eduardoluiz.libraryapi.controller.dto;

import io.github.eduardoluiz.libraryapi.model.BookGenre;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Schema(name = "ResultSearchBook")
public record ResultSearchBookDTO(
        UUID id,
        String isbn,
        String title,
        LocalDate publicationDate,
        BookGenre genre,
        BigDecimal price,
        AuthorDTO authorId) {

}
