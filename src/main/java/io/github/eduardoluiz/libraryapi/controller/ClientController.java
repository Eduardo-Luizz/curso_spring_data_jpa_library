package io.github.eduardoluiz.libraryapi.controller;

import io.github.eduardoluiz.libraryapi.controller.dto.ClientDTO;
import io.github.eduardoluiz.libraryapi.controller.mappers.ClientMapper;
import io.github.eduardoluiz.libraryapi.model.Client;
import io.github.eduardoluiz.libraryapi.service.ClientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("clients")
@RequiredArgsConstructor
public class ClientController {

    private final ClientService clientService;
    private final ClientMapper clientMapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('GERENTE')")
    public ResponseEntity<ClientDTO> salvar(@Valid @RequestBody ClientDTO dto) {
        Client client = clientMapper.toEntity(dto);
        Client saved = clientService.salvar(client);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(clientMapper.toDto(saved));
    }
}
