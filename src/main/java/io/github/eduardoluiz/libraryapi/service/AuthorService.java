package io.github.eduardoluiz.libraryapi.service;

import io.github.eduardoluiz.libraryapi.exceptions.OperationNotAllowedException;
import io.github.eduardoluiz.libraryapi.model.Author;
import io.github.eduardoluiz.libraryapi.model.User;
import io.github.eduardoluiz.libraryapi.repository.AuthorRepository;
import io.github.eduardoluiz.libraryapi.repository.BookRepository;
import io.github.eduardoluiz.libraryapi.security.SecurityService;
import io.github.eduardoluiz.libraryapi.validator.AuthorValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthorService {

    private final AuthorRepository authorRepository;
    private final AuthorValidator authorValidator;
    private final BookRepository bookRepository;
    private final SecurityService securityService;

    public Author save(Author author) {
        authorValidator.validate(author);
        User user = securityService.getLoggedInUser();
        author.setUser(user);
        return authorRepository.save(author);
    }

    public void update(Author author) {
        if (author.getId() == null) {
            throw new IllegalArgumentException("To update, the author must already be saved in the database.");
        }

        authorValidator.validate(author);
        authorRepository.save(author);
    }

    public Optional<Author> getById(UUID id) {
        return authorRepository.findById(id);
    }

    public void delete(Author author) {
        if (hasBook(author)) {
            throw new OperationNotAllowedException("It is not allowed to delete an author who has registered books.");
        }
        authorRepository.delete(author);
    }

    public List<Author> search(String name, String nationality) {
        if (name != null && nationality != null) {
            return authorRepository.findByNameAndNationality(name, nationality);
        }

        if (name != null) {
            return authorRepository.findByName(name);
        }

        if (nationality != null) {
            return authorRepository.findByNationality(nationality);
        }

        return authorRepository.findAll();
    }

    public List<Author> searchByExample(String name, String nationality) {
        Author exampleAuthor = new Author();
        exampleAuthor.setName(name);
        exampleAuthor.setNationality(nationality);

        ExampleMatcher matcher = ExampleMatcher
                .matching()
                .withIgnoreCase()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
        Example<Author> example = Example.of(exampleAuthor, matcher);
        return authorRepository.findAll(example);
    }

    public boolean hasBook(Author author) {
        return bookRepository.existsByAuthor(author);
    }
}
