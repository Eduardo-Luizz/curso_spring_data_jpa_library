package io.github.eduardoluiz.libraryapi.repository;

import io.github.eduardoluiz.libraryapi.model.Livro;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface LivroRepository extends JpaRepository<Livro, UUID> {
}
