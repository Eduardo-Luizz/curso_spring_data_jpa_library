package io.github.eduardoluiz.libraryapi.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

@Schema(name = "UserRequest")
public record UserRequestDTO(
        @NotBlank(message = "Login is required")
        String login,

        @NotBlank(message = "Password is required")
        String password,

        @Email(message = "Invalid email")
        @NotBlank(message = "Email is required")
        String email,

        List<String> roles) {

}
