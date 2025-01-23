package io.github.eduardoluiz.libraryapi.service;

import io.github.eduardoluiz.libraryapi.controller.dto.CadastroLivroDTO;
import io.github.eduardoluiz.libraryapi.controller.dto.ResultadoPesquisaLivroDTO;
import io.github.eduardoluiz.libraryapi.controller.mappers.LivroMapper;
import io.github.eduardoluiz.libraryapi.model.Autor;
import io.github.eduardoluiz.libraryapi.model.Livro;
import io.github.eduardoluiz.libraryapi.repository.AutorRepository;
import io.github.eduardoluiz.libraryapi.repository.LivroRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LivroService {

    private final LivroRepository livroRepository;
    private final LivroMapper livroMapper;
    private final AutorRepository autorRepository;

    public ResultadoPesquisaLivroDTO salvarLivro(CadastroLivroDTO dto) {
        // 1) Converte DTO -> Entidade (só campos básicos)
        Livro livro = livroMapper.toEntity(dto);
        // 2) Busca Autor
        Autor autor = autorRepository.findById(dto.idAutor()).orElse(null);
        // 3) Associa o autor
        livro.setAutor(autor);
        Livro livroSalvo = livroRepository.save(livro);
        // 4) Salva no banco
        return livroMapper.toResultadoPesquisaLivroDTO(livroSalvo);
    }
}
