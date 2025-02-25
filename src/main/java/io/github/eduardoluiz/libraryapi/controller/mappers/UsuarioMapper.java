package io.github.eduardoluiz.libraryapi.controller.mappers;

import io.github.eduardoluiz.libraryapi.controller.dto.UsuarioRequestDTO;
import io.github.eduardoluiz.libraryapi.controller.dto.UsuarioResponseDTO;
import io.github.eduardoluiz.libraryapi.model.Usuario;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {

    @Mapping(target = "id", ignore = true)
    Usuario toEntity(UsuarioRequestDTO usuarioRequestDTO);

    UsuarioResponseDTO toResponseDTO(Usuario usuario);
}
