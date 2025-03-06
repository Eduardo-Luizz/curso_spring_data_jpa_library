package io.github.eduardoluiz.libraryapi.controller;

import io.github.eduardoluiz.libraryapi.controller.dto.RegistrationBookDTO;
import io.github.eduardoluiz.libraryapi.controller.dto.ResultSearchBookDTO;
import io.github.eduardoluiz.libraryapi.controller.mappers.BookMapper;
import io.github.eduardoluiz.libraryapi.model.BookGenre;
import io.github.eduardoluiz.libraryapi.model.Book;
import io.github.eduardoluiz.libraryapi.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("livros")
@RequiredArgsConstructor
@Tag(name = "Books")
public class BookController implements GenericController {

    private final BookService bookService;
    private final BookMapper bookMapper;

    @PostMapping
    @PreAuthorize("hasAnyRole('OPERADOR', 'GERENTE')")
    @Operation(summary = "Save", description = "Register a new book")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Book registered successfully"),
            @ApiResponse(responseCode = "422", description = "Data validation error"),
            @ApiResponse(responseCode = "403", description = "Access denied – the user does not have the necessary permissions"),
            @ApiResponse(responseCode = "409", description = "Conflict – possibly duplicate ISBN")
    })
    public ResponseEntity<Object> salvar(@RequestBody @Valid RegistrationBookDTO dto) {

        ResultSearchBookDTO livroDTO = bookService.save(dto);
        var url = gerarHeaderLocation(livroDTO.id());
        return ResponseEntity.created(url).body(livroDTO);
    }

    @GetMapping("{id}")
    @PreAuthorize("hasAnyRole('OPERADOR', 'GERENTE')")
    @Operation(summary = "List", description = "Returns the details of a book based on the given ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Book found successfully"),
            @ApiResponse(responseCode = "404", description = "Book not found"),
            @ApiResponse(responseCode = "403", description = "Access denied – the user does not have the necessary permissions"),
    })
    public ResponseEntity<ResultSearchBookDTO> obterDetalhesLivro(@PathVariable("id") String id) {
        return bookService.getById(UUID.fromString(id))
                .map(livro -> {
                    var dto = bookMapper.toBookSearchResultDTO(livro);
                    return ResponseEntity.ok(dto);
                }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("{id}")
    @PreAuthorize("hasAnyRole('OPERADOR', 'GERENTE')")
    @Operation(summary = "Delete", description = "Delete book")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Book deleted successfully"),
            @ApiResponse(responseCode = "403", description = "Access denied – the user does not have the necessary permissions"),
            @ApiResponse(responseCode = "404", description = "Book not found")
    })
    public ResponseEntity<Object> deletar(@PathVariable("id") String id) {
        return bookService.getById(UUID.fromString(id))
                .map(livro -> {
                    bookService.delete(livro);
                    return ResponseEntity.noContent().build();
                }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('OPERADOR', 'GERENTE')")
    @Operation(summary = "List", description = "Performs book search with optional filters (ISBN, title, author name, genre and year of publication) and supports pagination.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Search carried out successfully"),
            @ApiResponse(responseCode = "403", description = "Access denied – the user does not have the necessary permissions"),
    })
    public ResponseEntity<Page<ResultSearchBookDTO>> pesquisa(
            @RequestParam(value = "isbn", required = false)
            String isbn,
            @RequestParam(value = "titulo", required = false)
            String titulo,
            @RequestParam(value = "nome-autor", required = false)
            String nomeAutor,
            @RequestParam(value = "genero", required = false)
            BookGenre genero,
            @RequestParam(value = "ano-publicacao", required = false)
            String anoPublicacao,
            @RequestParam(value = "pagina", defaultValue = "0")
            Integer pagina,
            @RequestParam(value = "tamanho-pagina", defaultValue = "10")
            Integer tamanhoPagina
    ) {
        Page<Book> paginaResultado = bookService.search(isbn, titulo, nomeAutor, genero, anoPublicacao, pagina, tamanhoPagina);
        Page<ResultSearchBookDTO> resultado = paginaResultado.map(bookMapper::toBookSearchResultDTO);
        return ResponseEntity.ok(resultado);
    }

    @PutMapping("{id}")
    @PreAuthorize("hasAnyRole('OPERADOR', 'GERENTE')")
    @Operation(summary = "Update", description = "Update book by UUID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Book updated successfully"),
            @ApiResponse(responseCode = "422", description = "Data validation error"),
            @ApiResponse(responseCode = "404", description = "Book not found"),
            @ApiResponse(responseCode = "403", description = "Access denied – the user does not have the necessary permissions"),
    })
    public ResponseEntity<Object> atualizar(
            @PathVariable("id") UUID id,
            @RequestBody @Valid RegistrationBookDTO dto) {

        boolean atualizado = bookService.update(id, dto);

        if (!atualizado) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }
}
