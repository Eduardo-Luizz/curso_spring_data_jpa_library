package io.github.eduardoluiz.libraryapi.repository;

import io.github.eduardoluiz.libraryapi.model.Autor;
import io.github.eduardoluiz.libraryapi.model.GeneroLivro;
import io.github.eduardoluiz.libraryapi.model.Livro;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@SpringBootTest
public class LivroRepositoryTest {

    @Autowired
    LivroRepository livroRepository;

    @Autowired
    AutorRepository autorRepository;

    @Test
    public void salvarLivroTest() {
        Livro livro = new Livro();
        livro.setTitulo("Livro Test");
        livro.setIsbn("1234567890");
        livro.setPreco(BigDecimal.valueOf(100.0));
        livro.setGenero(GeneroLivro.FICCAO);
        livro.setDataPublicacao(LocalDate.of(1980, 1, 1).atStartOfDay());

        Autor autor = autorRepository.findById(UUID.fromString("17577308-98e5-44a5-9dd6-bdf8e9f9f812")).orElse(null);
        livro.setAutor(autor);

        livroRepository.save(livro);
    }

    @Test
    public void salvarLivroTestCacade() {
        Livro livro = new Livro();
        livro.setTitulo("Livro Test");
        livro.setIsbn("1234567890");
        livro.setPreco(BigDecimal.valueOf(100.0));
        livro.setGenero(GeneroLivro.FICCAO);
        livro.setDataPublicacao(LocalDate.of(1980, 1, 1).atStartOfDay());

        Autor autor = new Autor();
        autor.setNome("João - Teste");
        autor.setNacionalidade("Masculino");
        autor.setDataNascimento(LocalDate.of(1990, 1, 1));

        livro.setAutor(autor);

        livroRepository.save(livro);
    }

    @Test
    public void atualizarAutorDoLivroTest() {
        UUID id = UUID.fromString("f0c60601-32cb-4cfb-9106-ac0fc8fd6299");
        var livroParaAtualizar = livroRepository.findById(id).orElse(null);

        UUID idAutor = UUID.fromString("f86a37db-cff6-4ff4-be9a-dccceebafd83");
        Autor autor = autorRepository.findById(idAutor).orElse(null);

        livroParaAtualizar.setAutor(autor);
        livroRepository.save(livroParaAtualizar);
    }

    @Test
    public void deletarLivroTest() {
        var id = UUID.fromString("0a8444ef-11f6-4a9f-8f88-f194fe198253");
        livroRepository.deleteById(id);
    }

    @Test
    public void buscarLivrosTest() {
        UUID id = UUID.fromString("f0c60601-32cb-4cfb-9106-ac0fc8fd6299");
        Livro livro = livroRepository.findById(id).orElse(null);

        System.out.println("Título livro: " + livro.getTitulo());
//        System.out.println("Autor livro: " + livro.getAutor().getNome()); Com o Lazy devemos comentar essa linha
    }
}
