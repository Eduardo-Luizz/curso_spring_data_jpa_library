package io.github.eduardoluiz.libraryapi.controller;

import io.github.eduardoluiz.libraryapi.controller.dto.RolesDTO;
import io.github.eduardoluiz.libraryapi.controller.dto.UsuarioRequestDTO;
import io.github.eduardoluiz.libraryapi.controller.dto.UsuarioResponseDTO;
import io.github.eduardoluiz.libraryapi.controller.mappers.UsuarioMapper;
import io.github.eduardoluiz.libraryapi.model.Usuario;
import io.github.eduardoluiz.libraryapi.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final UsuarioMapper usuarioMapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UsuarioResponseDTO postUsuario(@Valid @RequestBody UsuarioRequestDTO usuarioRequestDTO) {
        return usuarioService.salvar(usuarioRequestDTO);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('OPERADOR', 'GERENTE')")
    public UsuarioResponseDTO buscarPorId(@PathVariable UUID id) {
        return usuarioService.buscarPorId(id);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('OPERADOR', 'GERENTE')")
    public ResponseEntity<Page<UsuarioResponseDTO>> pesquisa (
            @RequestParam(value = "login", required = false)
            String login,
            @RequestParam(value = "roles", required = false)
            String roles,
            @RequestParam(value = "email", required = false)
            String email,
            @RequestParam(value = "pagina", defaultValue = "0")
            Integer pagina,
            @RequestParam(value = "tamanho-pagina", defaultValue = "10")
            Integer tamanhoPagina
    ) {
        Page<Usuario> paginaResultado = usuarioService.pesquisa(login, roles, email, pagina, tamanhoPagina);
        Page<UsuarioResponseDTO> resultado = paginaResultado.map(usuarioMapper::toResponseDTO);
        return ResponseEntity.ok(resultado);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('OPERADOR', 'GERENTE')")
    public UsuarioResponseDTO atualizar(
            @PathVariable UUID id,
            @RequestBody @Valid UsuarioRequestDTO dto
    ) {
        return usuarioService.atualizar(id, dto);
    }

    @PutMapping("/{id}/roles")
    @PreAuthorize("hasRole('GERENTE')")
    public UsuarioResponseDTO atualizarRoles(
            @PathVariable UUID id,
            @RequestBody @Valid RolesDTO rolesDTO
    ) {
        return usuarioService.atualizarRoles(id, rolesDTO.roles());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('GERENTE')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable UUID id) {
        usuarioService.deletar(id);
    }
}
