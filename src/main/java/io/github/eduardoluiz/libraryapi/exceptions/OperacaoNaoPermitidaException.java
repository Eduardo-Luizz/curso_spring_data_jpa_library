package io.github.eduardoluiz.libraryapi.exceptions;

public class OperacaoNaoPermitidaException extends RuntimeException {

    public OperacaoNaoPermitidaException(String message) {
        super(message);
    }
}
