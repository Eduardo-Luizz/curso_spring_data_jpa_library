package io.github.eduardoluiz.libraryapi.repository;

import io.github.eduardoluiz.libraryapi.model.Author;
import io.github.eduardoluiz.libraryapi.model.Book;
import io.github.eduardoluiz.libraryapi.model.BookGenre;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@SpringBootTest
public class BookRepositoryTest {

    @Autowired
    BookRepository bookRepository;

    @Autowired
    AuthorRepository authorRepository;

    @Test
    public void salvarLivroTest() {
        Book book = new Book();
        book.setTitle("Livro Test");
        book.setIsbn("1234567890");
        book.setPrice(BigDecimal.valueOf(100.0));
        book.setGenre(BookGenre.FICTION);
        book.setPublicationDate(LocalDate.of(1980, 1, 1).atStartOfDay());

        Author author = authorRepository.findById(UUID.fromString("523ed437-2efc-4a9b-9202-acb12121c8df")).orElse(null);
        book.setAuthor(author);

        bookRepository.save(book);
    }

    @Test
    public void salvarLivroTestCacade() {
        Book book = new Book();
        book.setTitle("Livro Test");
        book.setIsbn("1234567890");
        book.setPrice(BigDecimal.valueOf(100.0));
        book.setGenre(BookGenre.FICTION);
        book.setPublicationDate(LocalDate.of(1980, 1, 1).atStartOfDay());

        Author author = new Author();
        author.setName("João - Teste");
        author.setNationality("Masculino");
        author.setBirthDate(LocalDate.of(1990, 1, 1));

        book.setAuthor(author);

        bookRepository.save(book);
    }

    @Test
    public void atualizarAutorDoLivroTest() {
        UUID id = UUID.fromString("f0c60601-32cb-4cfb-9106-ac0fc8fd6299");
        var livroParaAtualizar = bookRepository.findById(id).orElse(null);

        UUID idAutor = UUID.fromString("f86a37db-cff6-4ff4-be9a-dccceebafd83");
        Author author = authorRepository.findById(idAutor).orElse(null);

        livroParaAtualizar.setAuthor(author);
        bookRepository.save(livroParaAtualizar);
    }

    @Test
    public void deletarLivroTest() {
        var id = UUID.fromString("0a8444ef-11f6-4a9f-8f88-f194fe198253");
        bookRepository.deleteById(id);
    }

    @Test
    public void buscarLivrosTest() {
        UUID id = UUID.fromString("f0c60601-32cb-4cfb-9106-ac0fc8fd6299");
        Book book = bookRepository.findById(id).orElse(null);

        System.out.println("Título livro: " + book.getTitle());
//        System.out.println("Autor livro: " + livro.getAutor().getNome()); Com o Lazy devemos comentar essa linha
    }

    @Test
    public void pesquisaPorTituloTest() {
        List<Book> listaTitulo = bookRepository.findByTitle("O roubo a casa da moeda");
        listaTitulo.forEach(System.out::println);
    }

    @Test
    public void pesquisaPorIsbnTest() {
        Optional<Book> livro = bookRepository.findByIsbn("76564831239");
        livro.ifPresent(System.out::println);
    }

    @Test
    public void pesquisaPorTituloEPrecoTest() {
        List<Book> lista = bookRepository.findByTitleAndPrice("O roubo a casa da moeda", BigDecimal.valueOf(20.50));
        lista.forEach(System.out::println);
    }

    @Test
    public void listarLivrosComQueryJPQL() {
        var resultado = bookRepository.findAllOrderedByTitleAndPrice();
        resultado.forEach(System.out::println);
    }

    @Test
    public void findAuthorsOfBooks() {
        var resultado = bookRepository.findAuthorsOfBooks();
        resultado.forEach(System.out::println);
    }

    @Test
    public void listarTitulosNaoRepetidosDosLivros() {
        var resultado = bookRepository.findDistinctBookTitles();
        resultado.forEach(System.out::println);
    }

    @Test
    public void listarGenerrosDeLivrosBrasileiros() {
        var resultado = bookRepository.findGenresOfAmericanAuthors();
        resultado.forEach(System.out::println);
    }

    @Test
    public void listarPorGeneroQueryParamTest() {
        var resultado = bookRepository.findByGenre(BookGenre.SCIENCE, "dataPublicação");
        resultado.forEach(System.out::println);
    }

    @Test
    public void listarPorGeneroPositionalParamTest() {
        var resultado = bookRepository.findByGenrePositionalParameters(BookGenre.SCIENCE, "dataPublicação");
        resultado.forEach(System.out::println);
    }

    @Test
    public void deletePorGeneroQueryParamTest() {
        bookRepository.deleteByGenre(BookGenre.SCIENCE);
    }

    @Test
    public void updatePublicationDateTest() {
        bookRepository.updatePublicationDate(LocalDate.of(2020, 1, 1).atStartOfDay());
    }
}
