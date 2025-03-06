package io.github.eduardoluiz.libraryapi.service;

import io.github.eduardoluiz.libraryapi.controller.dto.RegistrationBookDTO;
import io.github.eduardoluiz.libraryapi.controller.dto.ResultSearchBookDTO;
import io.github.eduardoluiz.libraryapi.controller.mappers.BookMapper;
import io.github.eduardoluiz.libraryapi.model.Author;
import io.github.eduardoluiz.libraryapi.model.Book;
import io.github.eduardoluiz.libraryapi.model.BookGenre;
import io.github.eduardoluiz.libraryapi.model.User;
import io.github.eduardoluiz.libraryapi.repository.AuthorRepository;
import io.github.eduardoluiz.libraryapi.repository.BookRepository;
import io.github.eduardoluiz.libraryapi.repository.specs.BookSpecs;
import io.github.eduardoluiz.libraryapi.security.SecurityService;
import io.github.eduardoluiz.libraryapi.validator.BookValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final BookMapper bookMapper;
    private final AuthorRepository authorRepository;
    private final BookValidator bookValidator;
    private final SecurityService securityService;

    public ResultSearchBookDTO save(RegistrationBookDTO dto) {
        Book book = bookMapper.toEntity(dto);
        Author author = authorRepository.findById(dto.authorId())
                .orElseThrow(() -> new IllegalArgumentException("Author not found!"));
        book.setAuthor(author);
        bookValidator.validate(book);
        User user = securityService.getLoggedInUser();
        book.setUser(user);
        Book savedBook = bookRepository.save(book);
        return bookMapper.toBookSearchResultDTO(savedBook);
    }

    public Optional<Book> getById(UUID id) {
        return bookRepository.findById(id);
    }

    public void delete(Book book) {
        bookRepository.delete(book);
    }

    public Page<Book> search(
            String isbn,
            String title,
            String authorName,
            BookGenre genre,
            String publicationYear,
            Integer page,
            Integer pageSize
    ) {

        Specification<Book> specs = Specification.where((root, query, cb) -> cb.conjunction());

        if (isbn != null) {
            specs = specs.and(BookSpecs.isbnEqual(isbn));
        }

        if (title != null) {
            specs = specs.and(BookSpecs.titleLike(title));
        }

        if (genre != null) {
            specs = specs.and(BookSpecs.genreEqual(genre));
        }

        if (publicationYear != null) {
            specs = specs.and(BookSpecs.publicationYearEqual(Integer.valueOf(publicationYear)));
        }

        if (authorName != null) {
            specs = specs.and(BookSpecs.authorNameLike(authorName));
        }

        Pageable pageRequest = PageRequest.of(page, pageSize);

        return bookRepository.findAll(specs, pageRequest);
    }

    public boolean update(UUID id, RegistrationBookDTO dto) {

        Book oldBook = bookRepository.findById(id).orElse(null);

        if (oldBook == null) {
            return false;
        }

        Author author = authorRepository.findById(dto.authorId())
                .orElseThrow(() -> new IllegalArgumentException("Author not found!"));


        Book updatedBook = bookMapper.toEntity(dto);
        updatedBook.setId(oldBook.getId());
        updatedBook.setAuthor(author);

        bookValidator.validate(updatedBook);
        bookRepository.save(updatedBook);

        return true;
    }
}
