package io.github.eduardoluiz.libraryapi.exceptions;

import lombok.Getter;

public class FieldInvalidException extends RuntimeException {

    @Getter
    private final String field;

    public FieldInvalidException(String field, String message) {
        super(message);
        this.field = field;
    }
}
