package io.github.eduardoluiz.libraryapi.repository;

import io.github.eduardoluiz.libraryapi.model.Autor;
import io.github.eduardoluiz.libraryapi.model.GeneroLivro;
import io.github.eduardoluiz.libraryapi.model.Livro;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@SpringBootTest
public class AutorRepositoryTest {

    @Autowired
    AutorRepository autorRepository;

    @Autowired
    LivroRepository livroRepository;

    @Test
    public void salvarTest() {
        Autor autor = new Autor();
        autor.setNome("Maria");
        autor.setNacionalidade("Feminino");
        autor.setDataNascimento(LocalDate.of(1990, 1, 1));
        var autorSalvo = autorRepository.save(autor);
        System.out.println(autorSalvo);
    }

    @Test
    public void buscarTest() {
        var id = UUID.fromString("de3243be-c6a2-4d6b-926b-daa01addc4fc");
        Optional<Autor> possivelAutor = autorRepository.findById(id);

        if (possivelAutor.isPresent()) {

            Autor autorEncontrado = possivelAutor.get();
            autorEncontrado.setDataNascimento(LocalDate.of(1990, 2, 2));
            autorRepository.save(autorEncontrado);

            System.out.println(autorEncontrado);
        }
    }

    @Test
    public void listarTest() {
        List<Autor> autores = autorRepository.findAll();
        autores.forEach(System.out::println);
    }

    @Test
    public void countTest() {
        System.out.println("Total de Autores: " + autorRepository.count());
    }

    @Test
    public void deletarPorIdTest() {
        var id = UUID.fromString("de3243be-c6a2-4d6b-926b-daa01addc4fc");
        autorRepository.deleteById(id);
    }

    @Test
    public void deleteTest() {
        var id = UUID.fromString("e10c5592-05ec-4ec6-9553-a0e1009ffd4c");
        var joao = autorRepository.findById(id).get();

        autorRepository.delete(joao);
    }

    @Test
    public void salvarAutorComLivroTest() {
        Autor autor = new Autor();
        autor.setNome("Antônio");
        autor.setNacionalidade("Americano");
        autor.setDataNascimento(LocalDate.of(1980, 11, 11));

        Livro livro = new Livro();
        livro.setTitulo("Algoritmos");
        livro.setIsbn("76564831239");
        livro.setPreco(BigDecimal.valueOf(204.50));
        livro.setGenero(GeneroLivro.CIENCIA);
        livro.setDataPublicacao(LocalDate.of(2000, 1, 1).atStartOfDay());
        livro.setAutor(autor);

        Livro livro2 = new Livro();
        livro2.setTitulo("O roubo a casa da moeda");
        livro2.setIsbn("735135123");
        livro2.setPreco(BigDecimal.valueOf(20.50));
        livro2.setGenero(GeneroLivro.FANTASIA);
        livro2.setDataPublicacao(LocalDate.of(2004, 1, 1).atStartOfDay());
        livro2.setAutor(autor);

        autor.setLivros(new ArrayList<>());
        autor.getLivros().add(livro);
        autor.getLivros().add(livro2);

        autorRepository.save(autor);
        livroRepository.saveAll(autor.getLivros()); // Se usar cascade não precisa dessa linha nesse caso faz sentido usar cascade
    }
}
