package io.github.eduardoluiz.libraryapi.repository;

import io.github.eduardoluiz.libraryapi.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.UUID;

public interface UsuarioRepository extends JpaRepository<Usuario, UUID>, JpaSpecificationExecutor<Usuario> {

    Usuario findByLogin(String login);

    Usuario findByEmail(String email);

    List<Usuario> findByLoginContainingIgnoreCase(String login);

    List<Usuario> findByEmailContainingIgnoreCase(String email);
}
