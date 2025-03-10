package io.github.eduardoluiz.libraryapi.controller.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.github.eduardoluiz.libraryapi.model.BookGenre;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import org.hibernate.validator.constraints.ISBN;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime publicationDate,

        @NotNull(message = "Genre is required")
        BookGenre genre,

        BigDecimal price,

        @NotNull(message = "Author ID is required")
        UUID authorId) {
}
