package io.github.eduardoluiz.libraryapi.service;

import io.github.eduardoluiz.libraryapi.controller.dto.CadastroLivroDTO;
import io.github.eduardoluiz.libraryapi.controller.dto.ResultadoPesquisaLivroDTO;
import io.github.eduardoluiz.libraryapi.controller.mappers.LivroMapper;
import io.github.eduardoluiz.libraryapi.model.Autor;
import io.github.eduardoluiz.libraryapi.model.GeneroLivro;
import io.github.eduardoluiz.libraryapi.model.Livro;
import io.github.eduardoluiz.libraryapi.repository.AutorRepository;
import io.github.eduardoluiz.libraryapi.repository.LivroRepository;
import io.github.eduardoluiz.libraryapi.repository.specs.LivroSpecs;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
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

    public void deletar(Livro livro) {
        livroRepository.delete(livro);
    }

    public List<Livro> pesquisa(String isbn, String titulo, String nomeAutor, GeneroLivro genero, String anoPublicacao) {

        Specification<Livro> specs = Specification.where((root, query, cb) -> cb.conjunction());

        if (isbn != null) {
            specs = specs.and(LivroSpecs.isbnEqual(isbn));
        }

        if (titulo != null) {
            specs = specs.and(LivroSpecs.tituloLike(titulo));
        }

        if (genero != null) {
            specs = specs.and(LivroSpecs.generoEqual(genero));
        }

        if (anoPublicacao != null) {
            specs = specs.and(LivroSpecs.anoPublicacaoEqual(Integer.valueOf(anoPublicacao)));
        }

        if (nomeAutor != null) {
            specs = specs.and(LivroSpecs.nomeAutorLike(nomeAutor));
        }

        return livroRepository.findAll(specs);
    }
}
