package io.github.eduardoluiz.libraryapi.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.UUID;

@Schema(name = "Client")
public record ClientDTO(
        UUID id,

        @NotBlank(message = "Client ID is required")
        @Size(max = 150, message = "Client ID must be up to 150 characters")
        String clientId,

        @NotBlank(message = "Client secret is required")
        @Size(max = 400, message = "Client secret must be up to 400 characters")
        String clientSecret,

        @NotBlank(message = "Redirect URI is required")
        @Size(max = 200, message = "Redirect URI must be up to 200 characters")
        String redirectUri,

        @Size(max = 50, message = "Scope must be up to 50 characters")
        String scope ) {
}
