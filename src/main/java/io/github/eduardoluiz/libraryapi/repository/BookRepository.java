package io.github.eduardoluiz.libraryapi.repository;

import io.github.eduardoluiz.libraryapi.model.Author;
import io.github.eduardoluiz.libraryapi.model.Book;
import io.github.eduardoluiz.libraryapi.model.BookGenre;
import io.github.eduardoluiz.libraryapi.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BookRepository extends JpaRepository<Book, UUID>, JpaSpecificationExecutor<Book> {

    // Query Methods
    List<Book> findByAuthor(Author author);
    List<Book> findByTitle(String title);
    Optional<Book> findByIsbn(String isbn);
    List<Book> findByTitleAndPrice(String title, BigDecimal price);
    List<Book> findByTitleAndIsbn(String title, String isbn);
    List<Book> findByPublicationDateBetween(LocalDateTime start, LocalDateTime end);

    // JPQL Queries
    @Query("select b from Book b order by b.title, b.price")
    List<Book> findAllOrderedByTitleAndPrice();

    @Query("select a from Book b join b.author a")
    List<Author> findAuthorsOfBooks();

    @Query("select distinct b.title from Book b")
    List<String> findDistinctBookTitles();

    @Query("""
           select distinct b.genre
           from Book b
           join b.author a
           where a.nationality = 'American'
           order by b.genre
           """)
    List<String> findGenresOfAmericanAuthors();

    //Named parameters
    @Query("select b from Book b where b.genre = :genre order by :orderBy")
    List<Book> findByGenre(@Param("genre") BookGenre bookGenre, @Param("orderBy") String propertyName);

    //Positional parameters
    @Query("select b from Book b where b.genre = ?1 order by ?2")
    List<Book> findByGenrePositionalParameters(BookGenre bookGenre, String propertyName);

    @Modifying
    @Transactional
    @Query("delete from Book b where b.genre = ?1")
    void deleteByGenre(BookGenre bookGenre);

    @Modifying
    @Transactional
    @Query("update Book b set b.publicationDate = ?1")
    void updatePublicationDate(LocalDateTime newPublicationDate);

    boolean existsByAuthor(Author author);
    boolean existsByUser(User user);
}
