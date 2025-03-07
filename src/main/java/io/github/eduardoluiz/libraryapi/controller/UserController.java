package io.github.eduardoluiz.libraryapi.controller;

import io.github.eduardoluiz.libraryapi.controller.dto.RolesDTO;
import io.github.eduardoluiz.libraryapi.controller.dto.UserRequestDTO;
import io.github.eduardoluiz.libraryapi.controller.dto.UserResponseDTO;
import io.github.eduardoluiz.libraryapi.controller.mappers.UserMapper;
import io.github.eduardoluiz.libraryapi.model.User;
import io.github.eduardoluiz.libraryapi.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("users")
@RequiredArgsConstructor
@Tag(name = "Users")
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Save", description = "Register a new user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User created successfully"),
            @ApiResponse(responseCode = "422", description = "Data validation error"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public UserResponseDTO save(@Valid @RequestBody UserRequestDTO userRequestDTO) {
        return userService.save(userRequestDTO);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('OPERADOR', 'GERENTE')")
    @Operation(summary = "List", description = "List user by UUID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User found successfully"),
            @ApiResponse(responseCode = "401", description = "Access denied – the user does not have the necessary permissions"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public UserResponseDTO searchById(@PathVariable UUID id) {
        return userService.getById(id);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('OPERADOR', 'GERENTE')")
    @Operation(
            summary = "List",
            description = "Returns a page of users based on the filters entered (login, roles and email). If no filter is provided, returns all paged users.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Search carried out successfully"),
            @ApiResponse(responseCode = "401", description = "Access denied – the user does not have the necessary permissions"),
    })
    public ResponseEntity<Page<UserResponseDTO>> search(
            @RequestParam(value = "login", required = false)
            String login,
            @RequestParam(value = "roles", required = false)
            String roles,
            @RequestParam(value = "email", required = false)
            String email,
            @RequestParam(value = "page", defaultValue = "0")
            Integer page,
            @RequestParam(value = "sizePage", defaultValue = "10")
            Integer sizePage
    ) {
        Page<User> pageResult = userService.search(login, roles, email, page, sizePage);
        Page<UserResponseDTO> result = pageResult.map(userMapper::toResponseDTO);
        return ResponseEntity.ok(result);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('OPERADOR', 'GERENTE')")
    @Operation(summary = "Update", description = "Update user by UUID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User updated successfully"),
            @ApiResponse(responseCode = "422", description = "Data validation error"),
            @ApiResponse(responseCode = "403", description = "Access denied – the user does not have the necessary permissions"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public UserResponseDTO update(
            @PathVariable UUID id,
            @RequestBody @Valid UserRequestDTO dto
    ) {
        return userService.update(id, dto);
    }

    @PutMapping("/{id}/roles")
    @PreAuthorize("hasRole('GERENTE')")
    @Operation(summary = "Update", description = "Update user roles")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User role updated successfully"),
            @ApiResponse(responseCode = "422", description = "Data validation error"),
            @ApiResponse(responseCode = "403", description = "Access denied – the user does not have the MANAGER role"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public UserResponseDTO updateRoles(
            @PathVariable UUID id,
            @RequestBody @Valid RolesDTO rolesDTO
    ) {
        return userService.updateRoles(id, rolesDTO.roles());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('GERENTE')")
    @Operation(summary = "Delete", description = "Delete user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "User deleted successfully"),
            @ApiResponse(responseCode = "403", description = "Access denied – the user does not have the MANAGER role"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "409", description = "Operation not permitted – user has associations that prevent deletion")
    })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        userService.delete(id);
    }
}
