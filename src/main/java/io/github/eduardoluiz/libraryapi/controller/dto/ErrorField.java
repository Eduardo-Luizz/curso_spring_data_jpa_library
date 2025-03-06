package io.github.eduardoluiz.libraryapi.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "ErrorField")
public record ErrorField(String field, String error) {

}
