package io.github.eduardoluiz.libraryapi.controller;

import io.github.eduardoluiz.libraryapi.controller.dto.ClientDTO;
import io.github.eduardoluiz.libraryapi.controller.mappers.ClientMapper;
import io.github.eduardoluiz.libraryapi.model.Client;
import io.github.eduardoluiz.libraryapi.service.ClientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("clients")
@RequiredArgsConstructor
@Tag(name = "Clients")
public class ClientController {

    private final ClientService clientService;
    private final ClientMapper clientMapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('GERENTE')")
    @Operation(summary = "Save", description = "Register a new client")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Client created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "403", description = "Access denied – the user does not have the MANAGER role")
    })
    public ResponseEntity<ClientDTO> salvar(@Valid @RequestBody ClientDTO dto) {
        Client client = clientMapper.toEntity(dto);
        Client saved = clientService.save(client);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(clientMapper.toDto(saved));
    }
}
