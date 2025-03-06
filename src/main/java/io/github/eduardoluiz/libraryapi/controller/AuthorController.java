package io.github.eduardoluiz.libraryapi.controller;

import io.github.eduardoluiz.libraryapi.controller.dto.AuthorDTO;
import io.github.eduardoluiz.libraryapi.controller.mappers.AuthorMapper;
import io.github.eduardoluiz.libraryapi.model.Author;
import io.github.eduardoluiz.libraryapi.service.AuthorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/autores")
@RequiredArgsConstructor
@Tag(name = "Authors")
public class AuthorController implements GenericController {

    private final AuthorService authorService;
    private final AuthorMapper authorMapper;

    @PostMapping
    @PreAuthorize("hasRole('GERENTE')")
    @Operation(summary = "Save", description = "Register a new author")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Author registered successfully"),
            @ApiResponse(responseCode = "403", description = "Access denied – the user does not have the necessary permissions"),
            @ApiResponse(responseCode = "409", description = "Conflict – Author already registered"),
            @ApiResponse(responseCode = "422", description = "Validation error"),

    })
    public ResponseEntity<Void> salvarAutor(@RequestBody @Valid AuthorDTO dto) {

        Author author = authorMapper.toEntity(dto);
        authorService.save(author);
        URI location = gerarHeaderLocation(author.getId());
        return ResponseEntity.created(location).build();
    }

    @GetMapping("{id}")
    @PreAuthorize("hasAnyRole('OPERADOR', 'GERENTE')")
    @Operation(summary = "List", description = "Returns the details of a author based on the given ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Author found successfully"),
            @ApiResponse(responseCode = "404", description = "Author not found"),
            @ApiResponse(responseCode = "403", description = "Access denied – the user does not have the necessary permissions"),
    })
    public ResponseEntity<AuthorDTO> obterDetalhes(@PathVariable("id") String id) {

        var idAutor = UUID.fromString(id);
        return authorService
                .getById(idAutor)
                .map(autor -> {
                    AuthorDTO dto = authorMapper.toDto(autor);
                    return ResponseEntity.ok(dto);
                }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("{id}")
    @PreAuthorize("hasRole('GERENTE')")
    @Operation(summary = "Delete", description = "Delete author")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Author deleted successfully"),
            @ApiResponse(responseCode = "403", description = "Access denied – the user does not have the necessary permissions"),
            @ApiResponse(responseCode = "404", description = "Author not found")
    })
    public ResponseEntity<Void> deleteAutor(@PathVariable("id") String id) {

        var idAutor = UUID.fromString(id);
        Optional<Author> autorOptional = authorService.getById(idAutor);
        if (autorOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        authorService.delete(autorOptional.get());
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('OPERADOR', 'GERENTE')")
    @Operation(
            summary = "List",
            description = "Performs author search with optional filters (Name and Nationality) and supports pagination")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Search carried out successfully"),
            @ApiResponse(responseCode = "403", description = "Access denied – the user does not have the necessary permissions")
    })
    public ResponseEntity<List<AuthorDTO>> pesquisar(
            @RequestParam(value = "nome", required = false) String nome,
            @RequestParam(value = "nacionalidade", required = false) String nacionalidade) {

        List<Author> resultado = authorService.searchByExample(nome, nacionalidade);
        List<AuthorDTO> lista = resultado
                .stream()
                .map(authorMapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(lista);
    }

    @PutMapping("{id}")
    @PreAuthorize("hasRole('GERENTE')")
    @Operation(summary = "Update", description = "Update author by UUID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Author updated successfully"),
            @ApiResponse(responseCode = "422", description = "Data validation error"),
            @ApiResponse(responseCode = "404", description = "Author not found"),
            @ApiResponse(responseCode = "403", description = "Access denied – the user does not have the necessary permissions"),
    })
    public ResponseEntity<Void> atualizar(
            @PathVariable("id") String id,
            @RequestBody @Valid AuthorDTO dto) {

        var idAutor = UUID.fromString(id);
        Optional<Author> autorOptional = authorService.getById(idAutor);
        if (autorOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        var autor = autorOptional.get();
        autor.setName(dto.name());
        autor.setBirthDate(dto.birthDate());
        autor.setNationality(dto.nationality());
        authorService.update(autor);
        return ResponseEntity.noContent().build();
    }
}
