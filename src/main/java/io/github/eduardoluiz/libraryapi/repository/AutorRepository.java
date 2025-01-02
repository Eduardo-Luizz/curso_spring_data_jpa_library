package io.github.eduardoluiz.libraryapi.repository;

import io.github.eduardoluiz.libraryapi.model.Autor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AutorRepository extends JpaRepository<Autor, UUID> {

}
