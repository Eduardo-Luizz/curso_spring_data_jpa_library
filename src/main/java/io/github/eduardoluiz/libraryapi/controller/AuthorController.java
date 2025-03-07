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
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/authors")
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
    public ResponseEntity<AuthorDTO> save(@RequestBody @Valid AuthorDTO dto) {
        Author author = authorMapper.toEntity(dto);
        Author savedAuthor = authorService.save(author);
        URI location = generateHeaderLocation(author.getId());
        AuthorDTO savedDto =  authorMapper.toDto(savedAuthor);
        return ResponseEntity.created(location).body(savedDto);
    }

    @GetMapping("{id}")
    @PreAuthorize("hasAnyRole('OPERADOR', 'GERENTE')")
    @Operation(summary = "List", description = "Returns the details of a author based on the given ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Author found successfully"),
            @ApiResponse(responseCode = "404", description = "Author not found"),
            @ApiResponse(responseCode = "403", description = "Access denied – the user does not have the necessary permissions"),
    })
    public ResponseEntity<AuthorDTO> searchById(@PathVariable("id") String id) {
        UUID authorId = UUID.fromString(id);
        Author author = authorService.getById(authorId);
        AuthorDTO dto = authorMapper.toDto(author);
        return ResponseEntity.ok(dto);
    }

    @DeleteMapping("{id}")
    @PreAuthorize("hasRole('GERENTE')")
    @Operation(summary = "Delete", description = "Delete author")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Author deleted successfully"),
            @ApiResponse(responseCode = "403", description = "Access denied – the user does not have the necessary permissions"),
            @ApiResponse(responseCode = "404", description = "Author not found")
    })
    public ResponseEntity<Void> delete(@PathVariable("id") String id) {
        UUID authorId = UUID.fromString(id);
        Author author = authorService.getById(authorId);
        authorService.delete(author);
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
    public ResponseEntity<List<AuthorDTO>> search(
            @RequestParam(value = "name", required = false) String nome,
            @RequestParam(value = "nationality", required = false) String nationality) {

        List<Author> result = authorService.searchByExample(nome, nationality);
        List<AuthorDTO> list = result
                .stream()
                .map(authorMapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(list);
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
    public ResponseEntity<Void> update(
            @PathVariable("id") String id,
            @RequestBody @Valid AuthorDTO dto) {

        UUID authorId = UUID.fromString(id);
        Author author = authorService.getById(authorId);
        author.setName(dto.name());
        author.setBirthDate(dto.birthDate());
        author.setNationality(dto.nationality());
        authorService.update(author);
        return ResponseEntity.noContent().build();
    }
}
