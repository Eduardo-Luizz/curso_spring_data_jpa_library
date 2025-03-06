package io.github.eduardoluiz.libraryapi.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;
import java.util.UUID;

@Schema(name = "UserResponse")
public record UserResponseDTO(
        UUID id,
        String login,
        String email,
        List<String> roles) {
}
