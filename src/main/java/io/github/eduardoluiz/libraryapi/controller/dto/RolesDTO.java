package io.github.eduardoluiz.libraryapi.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

@Schema(name = "Roles")
public record RolesDTO(
        @NotEmpty(message = "Roles cannot be empty")
        List<String> roles
) {}

