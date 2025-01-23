package io.github.eduardoluiz.libraryapi.controller.mappers;

import io.github.eduardoluiz.libraryapi.controller.dto.CadastroLivroDTO;
import io.github.eduardoluiz.libraryapi.controller.dto.ResultadoPesquisaLivroDTO;
import io.github.eduardoluiz.libraryapi.model.Livro;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = AutorMapper.class)
public interface LivroMapper {

    Livro toEntity(CadastroLivroDTO dto);

    @Mapping(target = "autor", source = "autor")
    ResultadoPesquisaLivroDTO toResultadoPesquisaLivroDTO(Livro livro);

}
