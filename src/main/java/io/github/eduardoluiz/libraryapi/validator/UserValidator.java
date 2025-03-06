package io.github.eduardoluiz.libraryapi.validator;

import io.github.eduardoluiz.libraryapi.exceptions.OperationNotAllowedException;
import io.github.eduardoluiz.libraryapi.model.User;
import io.github.eduardoluiz.libraryapi.repository.AuthorRepository;
import io.github.eduardoluiz.libraryapi.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserValidator {

    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;

    public void validateAssociationsForDeletion(User user){
        boolean hasRelatedAuthor = authorRepository.existsByUser(user);
        boolean hasRelatedBook = bookRepository.existsByUser(user);

        if(hasRelatedAuthor || hasRelatedBook){
            throw new OperationNotAllowedException("User cannot be deleted because there are references in Author/Book.");
        }
    }
}
