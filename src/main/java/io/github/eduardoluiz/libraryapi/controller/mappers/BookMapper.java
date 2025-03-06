package io.github.eduardoluiz.libraryapi.controller.mappers;

import io.github.eduardoluiz.libraryapi.controller.dto.RegistrationBookDTO;
import io.github.eduardoluiz.libraryapi.controller.dto.ResultSearchBookDTO;
import io.github.eduardoluiz.libraryapi.model.Book;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = AuthorMapper.class)
public interface BookMapper {

    Book toEntity(RegistrationBookDTO dto);

    @Mapping(target = "authorId", source = "author")
    ResultSearchBookDTO toBookSearchResultDTO(Book book);

}
