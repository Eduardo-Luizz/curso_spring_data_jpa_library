package io.github.eduardoluiz.libraryapi.service;

import io.github.eduardoluiz.libraryapi.controller.dto.CadastroLivroDTO;
import io.github.eduardoluiz.libraryapi.controller.dto.ResultadoPesquisaLivroDTO;
import io.github.eduardoluiz.libraryapi.controller.mappers.LivroMapper;
import io.github.eduardoluiz.libraryapi.model.Autor;
import io.github.eduardoluiz.libraryapi.model.GeneroLivro;
import io.github.eduardoluiz.libraryapi.model.Livro;
import io.github.eduardoluiz.libraryapi.model.Usuario;
import io.github.eduardoluiz.libraryapi.repository.AutorRepository;
import io.github.eduardoluiz.libraryapi.repository.LivroRepository;
import io.github.eduardoluiz.libraryapi.repository.specs.LivroSpecs;
import io.github.eduardoluiz.libraryapi.security.SecurityService;
import io.github.eduardoluiz.libraryapi.validator.LivroValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    private final LivroValidator livroValidator;
    private final SecurityService securityService;

    public ResultadoPesquisaLivroDTO salvarLivro(CadastroLivroDTO dto) {
        Livro livro = livroMapper.toEntity(dto);
        Autor autor = autorRepository.findById(dto.idAutor())
                .orElseThrow(() -> new IllegalArgumentException("Autor não encontrado!"));
        livro.setAutor(autor);
        livroValidator.validar(livro);
        Usuario usuario = securityService.obterUsuarioLogado();
        livro.setUsuario(usuario);
        Livro livroSalvo = livroRepository.save(livro);
        return livroMapper.toResultadoPesquisaLivroDTO(livroSalvo);
    }

    public Optional<Livro> obterPorId(UUID id) {
        return livroRepository.findById(id);
    }

    public void deletar(Livro livro) {
        livroRepository.delete(livro);
    }

    public Page<Livro> pesquisa(
            String isbn,
            String titulo,
            String nomeAutor,
            GeneroLivro genero,
            String anoPublicacao,
            Integer pagina,
            Integer tamanhoPagina
    ) {

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

        Pageable pageRequest = PageRequest.of(pagina, tamanhoPagina);

        return livroRepository.findAll(specs, pageRequest);
    }

    public boolean atualizar(UUID id, CadastroLivroDTO dto) {

        Livro livroAntigo = livroRepository.findById(id).orElse(null);

        if (livroAntigo == null) {
            return false;
        }

        Autor autor = autorRepository.findById(dto.idAutor())
                .orElseThrow(() -> new IllegalArgumentException("Autor não encontrado!"));


        Livro livroAtualizado = livroMapper.toEntity(dto);
        livroAtualizado.setId(livroAntigo.getId());
        livroAtualizado.setAutor(autor);

        livroValidator.validar(livroAtualizado);
        livroRepository.save(livroAtualizado);

        return true;
    }
}
