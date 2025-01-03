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
}
