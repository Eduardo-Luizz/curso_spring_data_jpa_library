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

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LivroService {

    private final LivroRepository livroRepository;
    private final LivroMapper livroMapper;
    private final AutorRepository autorRepository;

    public ResultadoPesquisaLivroDTO salvarLivro(CadastroLivroDTO dto) {
        Livro livro = livroMapper.toEntity(dto);
        Autor autor = autorRepository.findById(dto.idAutor()).orElse(null);
        livro.setAutor(autor);
        Livro livroSalvo = livroRepository.save(livro);
        return livroMapper.toResultadoPesquisaLivroDTO(livroSalvo);
    }

    public Optional<Livro> obterPorId(UUID id) {
        return livroRepository.findById(id);
    }
}
