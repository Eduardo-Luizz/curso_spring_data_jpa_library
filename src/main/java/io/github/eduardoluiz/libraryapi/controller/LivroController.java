package io.github.eduardoluiz.libraryapi.controller;

import io.github.eduardoluiz.libraryapi.controller.dto.CadastroLivroDTO;
import io.github.eduardoluiz.libraryapi.controller.dto.ResultadoPesquisaLivroDTO;
import io.github.eduardoluiz.libraryapi.controller.mappers.LivroMapper;
import io.github.eduardoluiz.libraryapi.model.GeneroLivro;
import io.github.eduardoluiz.libraryapi.service.LivroService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("livros")
@RequiredArgsConstructor
public class LivroController implements GenericController {

    private final LivroService livroService;
    private final LivroMapper livroMapper;

    @PostMapping
    public ResponseEntity<Object> salvar(@RequestBody @Valid CadastroLivroDTO dto) {

        ResultadoPesquisaLivroDTO livroDTO = livroService.salvarLivro(dto);
        var url = gerarHeaderLocation(livroDTO.id());
        return ResponseEntity.created(url).body(livroDTO);
    }

    @GetMapping("{id}")
    public ResponseEntity<ResultadoPesquisaLivroDTO> obterDetalhesLivro(@PathVariable("id") String id) {
        return livroService.obterPorId(UUID.fromString(id))
                .map(livro -> {
                    var dto = livroMapper.toResultadoPesquisaLivroDTO(livro);
                    return ResponseEntity.ok(dto);
                }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Object> deletar(@PathVariable("id") String id) {
        return livroService.obterPorId(UUID.fromString(id))
                .map(livro -> {
                    livroService.deletar(livro);
                    return ResponseEntity.noContent().build();
                }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<ResultadoPesquisaLivroDTO>> pesquisa(
            @RequestParam(value = "isbn", required = false)
            String isbn,
            @RequestParam(value = "titulo", required = false)
            String titulo,
            @RequestParam(value = "nome-autor", required = false)
            String nomeAutor,
            @RequestParam(value = "genero", required = false)
            GeneroLivro genero,
            @RequestParam(value = "ano-publicacao", required = false)
            String anoPublicacao
    ) {
        var resultado = livroService.pesquisa(isbn, titulo, nomeAutor, genero, anoPublicacao);
        var lista = resultado
                .stream()
                .map(livroMapper::toResultadoPesquisaLivroDTO)
                .toList();
        return ResponseEntity.ok(lista);
    }
}
