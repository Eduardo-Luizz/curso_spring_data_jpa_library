package io.github.eduardoluiz.libraryapi.validator;

import io.github.eduardoluiz.libraryapi.exceptions.FieldInvalidException;
import io.github.eduardoluiz.libraryapi.exceptions.DuplicateRecordException;
import io.github.eduardoluiz.libraryapi.model.Book;
import io.github.eduardoluiz.libraryapi.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class BookValidator {

    private static final int PRICE_REQUIRED_YEAR = 2020;
    private final BookRepository bookRepository;

    public void validate(Book book) {
        if (isBookWithIsbnExists(book)) {
            throw new DuplicateRecordException("ISBN already registered!");
        }
        if (isPriceRequiredNull(book)) {
            throw new FieldInvalidException("price", "For books published in 2020 or later, the price is required!");
        }
    }

    private boolean isPriceRequiredNull(Book book) {
        return book.getPrice() == null && book.getPublicationDate().getYear() >= PRICE_REQUIRED_YEAR;
    }

    private boolean isBookWithIsbnExists(Book book) {
        Optional<Book> foundBook = bookRepository.findByIsbn(book.getIsbn());

        if(book.getId() == null) {
            return foundBook.isPresent();
        }

        return foundBook.isPresent() && !book.getId().equals(foundBook.get().getId());
    }
}
