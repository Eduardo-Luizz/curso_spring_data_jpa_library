package io.github.eduardoluiz.libraryapi.controller.common;

import io.github.eduardoluiz.libraryapi.controller.dto.ErroCampo;
import io.github.eduardoluiz.libraryapi.controller.dto.ErroResposta;
import io.github.eduardoluiz.libraryapi.exceptions.CampoInvalidoException;
import io.github.eduardoluiz.libraryapi.exceptions.OperacaoNaoPermitidaException;
import io.github.eduardoluiz.libraryapi.exceptions.RegistroDuplicadoException;
import io.github.eduardoluiz.libraryapi.exceptions.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ErroResposta handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        List<FieldError> fieldErrors = ex.getFieldErrors();
        List<ErroCampo> listaErros = fieldErrors
                .stream()
                .map(fe -> new ErroCampo(fe.getField(), fe.getDefaultMessage()))
                .collect(Collectors.toList());
        return new ErroResposta(HttpStatus.UNPROCESSABLE_ENTITY.value(), "Erro de validação", listaErros);
    }

    @ExceptionHandler(RegistroDuplicadoException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErroResposta handleRegistroDuplicadoException(RegistroDuplicadoException ex) {
        return ErroResposta.conflito(ex.getMessage());
    }

    @ExceptionHandler(OperacaoNaoPermitidaException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErroResposta handleOperacaoNaoPermitidaException(OperacaoNaoPermitidaException ex) {
        return ErroResposta.respostaPadrao(ex.getMessage());
    }

    @ExceptionHandler(CampoInvalidoException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ErroResposta handleCampoInvalidoException(CampoInvalidoException ex) {
        return new ErroResposta(
                HttpStatus.UNPROCESSABLE_ENTITY.value(),
                "Erro validação.",
                List.of(new ErroCampo(ex.getCampo(), ex.getMessage())));
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErroResposta handleAccessDeniedException(AccessDeniedException ex) {
        return new ErroResposta(HttpStatus.FORBIDDEN.value(), "Acesso negado", List.of());
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErroResposta handleResourceNotFoundException(ResourceNotFoundException ex) {
        return ErroResposta.naoEncontrado(ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErroResposta handleErrosNaoTratados(RuntimeException ex) {
        return new ErroResposta(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Erro inesperado. Entre em contato com o administrador",
                List.of());
    }
}
