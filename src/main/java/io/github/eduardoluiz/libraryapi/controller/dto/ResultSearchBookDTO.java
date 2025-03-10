package io.github.eduardoluiz.libraryapi.controller.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.github.eduardoluiz.libraryapi.model.BookGenre;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Schema(name = "ResultSearchBook")
public record ResultSearchBookDTO(
        UUID id,
        String isbn,
        String title,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime publicationDate,
        BookGenre genre,
        BigDecimal price,
        AuthorDTO authorId) {

}
