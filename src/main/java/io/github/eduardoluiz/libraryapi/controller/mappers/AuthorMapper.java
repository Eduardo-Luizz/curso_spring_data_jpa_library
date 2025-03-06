package io.github.eduardoluiz.libraryapi.controller.mappers;


import io.github.eduardoluiz.libraryapi.controller.dto.AuthorDTO;
import io.github.eduardoluiz.libraryapi.model.Author;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AuthorMapper {

    Author toEntity(AuthorDTO dto);

    AuthorDTO toDto(Author author);
}
