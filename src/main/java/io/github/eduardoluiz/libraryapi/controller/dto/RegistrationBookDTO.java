package io.github.eduardoluiz.libraryapi.controller.dto;

import io.github.eduardoluiz.libraryapi.model.BookGenre;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import org.hibernate.validator.constraints.ISBN;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Schema(name = "RegistrationBook")
public record RegistrationBookDTO(
        @ISBN(message = "Invalid ISBN")
        @NotBlank(message = "ISBN is required")
        String isbn,

        @NotBlank(message = "Title is required")
        String title,

        @NotNull(message = "Publication date is required")
        @Past(message = "Publication date must be in the past")
        LocalDate publicationDate,

        @NotNull(message = "Genre is required")
        BookGenre genre,

        BigDecimal price,

        @NotNull(message = "Author ID is required")
        UUID authorId) {
}
