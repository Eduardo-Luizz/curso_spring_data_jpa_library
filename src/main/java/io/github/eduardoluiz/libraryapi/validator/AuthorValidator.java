package io.github.eduardoluiz.libraryapi.validator;

import io.github.eduardoluiz.libraryapi.exceptions.DuplicateRecordException;
import io.github.eduardoluiz.libraryapi.model.Author;
import io.github.eduardoluiz.libraryapi.repository.AuthorRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AuthorValidator {

    private final AuthorRepository authorRepository;

    public AuthorValidator(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    public void validate(Author author) {
        if (isAuthorAlreadyRegistered(author)) {
            throw new DuplicateRecordException("Author already registered!");
        }
    }

    private boolean isAuthorAlreadyRegistered(Author author) {
        Optional<Author> foundAuthor = authorRepository.findByNameAndBirthDateAndNationality(
                author.getName(), author.getBirthDate(), author.getNationality()
        );

        if (author.getId() == null) {
            return foundAuthor.isPresent();
        }

        return foundAuthor.isPresent() && !author.getId().equals(foundAuthor.get().getId());
    }
}
