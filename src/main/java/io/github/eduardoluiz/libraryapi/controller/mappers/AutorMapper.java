package io.github.eduardoluiz.libraryapi.controller.mappers;


import io.github.eduardoluiz.libraryapi.controller.dto.AutorDTO;
import io.github.eduardoluiz.libraryapi.model.Autor;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AutorMapper {

    Autor toEntity(AutorDTO dto);

    AutorDTO toDto(Autor autor);
}
