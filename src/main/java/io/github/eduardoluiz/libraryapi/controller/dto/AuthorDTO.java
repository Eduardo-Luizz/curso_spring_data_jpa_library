package io.github.eduardoluiz.libraryapi.controller.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.UUID;

@Schema(name = "Author")
public record AuthorDTO(
        UUID id,
        @NotBlank(message = "Name is required")
        @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
        String name,

        @NotNull(message = "Birth date is required")
        @Past(message = "Birth date must be in the past")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        LocalDate birthDate,

        @NotBlank(message = "Nationality is required")
        @Size(max = 50, min = 2, message = "Nationality must be between 2 and 50 characters")
        String nationality) {
}
