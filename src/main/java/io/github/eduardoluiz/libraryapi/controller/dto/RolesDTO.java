package io.github.eduardoluiz.libraryapi.controller.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.util.List;

public record RolesDTO(
        @NotEmpty(message = "roles não podem ser vazio")
        List<String> roles
) {}

