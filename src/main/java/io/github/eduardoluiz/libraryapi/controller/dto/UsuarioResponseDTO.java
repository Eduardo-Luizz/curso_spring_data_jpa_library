package io.github.eduardoluiz.libraryapi.controller.dto;

import java.util.List;
import java.util.UUID;

public record UsuarioResponseDTO(
        UUID id,
        String login,
        String email,
        List<String> roles) {
}
