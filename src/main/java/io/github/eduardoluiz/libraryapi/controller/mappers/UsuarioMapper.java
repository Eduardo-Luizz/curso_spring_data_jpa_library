package io.github.eduardoluiz.libraryapi.controller.mappers;

import io.github.eduardoluiz.libraryapi.controller.dto.UsuarioDTO;
import io.github.eduardoluiz.libraryapi.model.Usuario;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {

    Usuario toEntity(UsuarioDTO usuarioDTO);
}
