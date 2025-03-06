package io.github.eduardoluiz.libraryapi.service;

import io.github.eduardoluiz.libraryapi.model.Author;
import io.github.eduardoluiz.libraryapi.model.Book;
import io.github.eduardoluiz.libraryapi.model.BookGenre;
import io.github.eduardoluiz.libraryapi.repository.AuthorRepository;
import io.github.eduardoluiz.libraryapi.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Service
public class TransactionService {

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private BookRepository bookRepository;

    // Save book with photo (title, ..., file_name) -> id.png
    @Transactional
    public void saveBookWithPhoto() {
        // Save the book
        // bookRepository.save(book);

        // Get the book id = book.getId();
        // var id = book.getId();

        // Save the book's photo -> bucket in the cloud
        // bucketService.save(book.getPhoto(), id + ".png");

        // Update the file name that was saved
        // book.setPhotoFileName(id + ".png");
        // bookRepository.save(book);
    }

    @Transactional
    public void updateWithoutSaving() {
        Book book = bookRepository.findById(UUID.fromString("5e1d29e9-804d-403b-ad5b-19ba250217b9")).orElse(null);
        assert book != null;
        book.setPublicationDate(LocalDate.of(2024, 1, 6).atStartOfDay());
    }

    @Transactional
    public void execute() {
        // Save the author
        Author author = new Author();
        author.setName("Jacob");
        author.setNationality("Brazilian");
        author.setBirthDate(LocalDate.of(1980, 11, 11));

        authorRepository.save(author);

        // Save the book
        Book book = new Book();
        book.setTitle("Algorithms and Programming");
        book.setIsbn("7659");
        book.setPrice(BigDecimal.valueOf(2090.50));
        book.setGenre(BookGenre.SCIENCE);
        book.setPublicationDate(LocalDate.of(2000, 1, 1).atStartOfDay());
        book.setAuthor(author);

        bookRepository.save(book);

        if (author.getName().equals("Jorge")) {
            throw new RuntimeException("Rollback!");
        }
    }
}
