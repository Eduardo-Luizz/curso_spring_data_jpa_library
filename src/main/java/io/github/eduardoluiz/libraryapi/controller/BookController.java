package io.github.eduardoluiz.libraryapi.controller;

import io.github.eduardoluiz.libraryapi.controller.dto.RegistrationBookDTO;
import io.github.eduardoluiz.libraryapi.controller.dto.ResultSearchBookDTO;
import io.github.eduardoluiz.libraryapi.controller.mappers.BookMapper;
import io.github.eduardoluiz.libraryapi.model.Book;
import io.github.eduardoluiz.libraryapi.model.BookGenre;
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

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("books")
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
    public ResponseEntity<ResultSearchBookDTO> save(@RequestBody @Valid RegistrationBookDTO dto) {

        ResultSearchBookDTO bookDTO = bookService.save(dto);
        URI location = generateHeaderLocation(bookDTO.id());
        return ResponseEntity.created(location).body(bookDTO);
    }

    @GetMapping("{id}")
    @PreAuthorize("hasAnyRole('OPERADOR', 'GERENTE')")
    @Operation(summary = "List", description = "Returns the details of a book based on the given ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Book found successfully"),
            @ApiResponse(responseCode = "404", description = "Book not found"),
            @ApiResponse(responseCode = "403", description = "Access denied – the user does not have the necessary permissions"),
    })
    public ResponseEntity<ResultSearchBookDTO> searchById(@PathVariable("id") String id) {
        UUID bookId = UUID.fromString(id);
        Book book = bookService.getById(bookId);
        ResultSearchBookDTO dto = bookMapper.toBookSearchResultDTO(book);
        return ResponseEntity.ok(dto);
    }

    @DeleteMapping("{id}")
    @PreAuthorize("hasAnyRole('OPERADOR', 'GERENTE')")
    @Operation(summary = "Delete", description = "Delete book")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Book deleted successfully"),
            @ApiResponse(responseCode = "403", description = "Access denied – the user does not have the necessary permissions"),
            @ApiResponse(responseCode = "404", description = "Book not found")
    })
    public ResponseEntity<Object> delete(@PathVariable("id") String id) {
        UUID bookId = UUID.fromString(id);
        Book book = bookService.getById(bookId);
        bookService.delete(book);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('OPERADOR', 'GERENTE')")
    @Operation(summary = "List", description = "Performs book search with optional filters (ISBN, title, author name, genre and year of publication) and supports pagination.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Search carried out successfully"),
            @ApiResponse(responseCode = "403", description = "Access denied – the user does not have the necessary permissions"),
    })
    public ResponseEntity<Page<ResultSearchBookDTO>> search(
            @RequestParam(value = "isbn", required = false)
            String isbn,
            @RequestParam(value = "title", required = false)
            String title,
            @RequestParam(value = "nameAuthor", required = false)
            String nameAuthor,
            @RequestParam(value = "gender", required = false)
            BookGenre gender,
            @RequestParam(value = "yearPublication", required = false)
            String yearPublication,
            @RequestParam(value = "page", defaultValue = "0")
            Integer page,
            @RequestParam(value = "sizePage", defaultValue = "10")
            Integer sizePage
    ) {
        Page<Book> pageResult = bookService.search(isbn, title, nameAuthor, gender, yearPublication, page, sizePage);
        Page<ResultSearchBookDTO> result = pageResult.map(bookMapper::toBookSearchResultDTO);
        return ResponseEntity.ok(result);
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
    public ResponseEntity<ResultSearchBookDTO> update(
            @PathVariable("id") UUID id,
            @RequestBody @Valid RegistrationBookDTO dto) {
        ResultSearchBookDTO updatedBookDTO = bookService.update(id, dto);
        return ResponseEntity.ok(updatedBookDTO);
    }

}
