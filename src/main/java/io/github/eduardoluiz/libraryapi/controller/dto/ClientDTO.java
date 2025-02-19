package io.github.eduardoluiz.libraryapi.controller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record ClientDTO(
        UUID id,

        @NotBlank
        @Size(max = 150)
        String clientId,

        @NotBlank
        @Size(max = 400)
        String clientSecret,

        @NotBlank
        @Size(max = 200)
        String redirectUri,

        @Size(max = 50)
        String scope ) {
}
