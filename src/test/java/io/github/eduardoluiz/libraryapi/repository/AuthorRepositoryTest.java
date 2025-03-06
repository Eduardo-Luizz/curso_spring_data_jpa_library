package io.github.eduardoluiz.libraryapi.repository;

import io.github.eduardoluiz.libraryapi.model.Author;
import io.github.eduardoluiz.libraryapi.model.Book;
import io.github.eduardoluiz.libraryapi.model.BookGenre;
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
public class AuthorRepositoryTest {

    @Autowired
    AuthorRepository authorRepository;

    @Autowired
    BookRepository bookRepository;

    @Test
    public void salvarTest() {
        Author author = new Author();
        author.setNome("Maria");
        author.setNacionalidade("Feminino");
        author.setDataNascimento(LocalDate.of(1990, 1, 1));
        var autorSalvo = authorRepository.save(author);
        System.out.println(autorSalvo);
    }

    @Test
    public void buscarTest() {
        var id = UUID.fromString("de3243be-c6a2-4d6b-926b-daa01addc4fc");
        Optional<Author> possivelAutor = authorRepository.findById(id);

        if (possivelAutor.isPresent()) {

            Author authorEncontrado = possivelAutor.get();
            authorEncontrado.setDataNascimento(LocalDate.of(1990, 2, 2));
            authorRepository.save(authorEncontrado);

            System.out.println(authorEncontrado);
        }
    }

    @Test
    public void listarTest() {
        List<Author> autores = authorRepository.findAll();
        autores.forEach(System.out::println);
    }

    @Test
    public void countTest() {
        System.out.println("Total de Autores: " + authorRepository.count());
    }

    @Test
    public void deletarPorIdTest() {
        var id = UUID.fromString("de3243be-c6a2-4d6b-926b-daa01addc4fc");
        authorRepository.deleteById(id);
    }

    @Test
    public void deleteTest() {
        var id = UUID.fromString("e10c5592-05ec-4ec6-9553-a0e1009ffd4c");
        var joao = authorRepository.findById(id).get();

        authorRepository.delete(joao);
    }

    @Test
    public void salvarAutorComLivroTest() {
        Author author = new Author();
        author.setNome("Antônio");
        author.setNacionalidade("Americano");
        author.setDataNascimento(LocalDate.of(1980, 11, 11));

        Book book = new Book();
        book.setTitulo("Algoritmos");
        book.setIsbn("76564831239");
        book.setPreco(BigDecimal.valueOf(204.50));
        book.setGenero(BookGenre.CIENCIA);
        book.setDataPublicacao(LocalDate.of(2000, 1, 1).atStartOfDay());
        book.setAuthor(author);

        Book book2 = new Book();
        book2.setTitulo("O roubo a casa da moeda");
        book2.setIsbn("735135123");
        book2.setPreco(BigDecimal.valueOf(20.50));
        book2.setGenero(BookGenre.FANTASIA);
        book2.setDataPublicacao(LocalDate.of(2004, 1, 1).atStartOfDay());
        book2.setAuthor(author);

        author.setBooks(new ArrayList<>());
        author.getBooks().add(book);
        author.getBooks().add(book2);

        authorRepository.save(author);
        bookRepository.saveAll(author.getBooks()); // Se usar cascade não precisa dessa linha nesse caso faz sentido usar cascade
    }

    @Test
    void listarLivrosAutorTest() {
        var id = UUID.fromString("e23e1e1b-b547-4a63-b6f6-538605d730ec");
        var autor = authorRepository.findById(id).get();

        //Buscar os livros do autor
        List<Book> livrosLista = bookRepository.findByAuthor(autor);
        autor.setBooks(livrosLista);

        autor.getBooks().forEach(System.out::println);
    }
}
