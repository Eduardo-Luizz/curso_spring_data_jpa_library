package io.github.eduardoluiz.libraryapi.repository.specs;

import io.github.eduardoluiz.libraryapi.model.Usuario;
import org.springframework.data.jpa.domain.Specification;

public class UsuarioSpecs {

    public static Specification<Usuario> loginLikeIgnoreCase(String login) {
        return  (root, query, cb) -> cb.like(
                cb.upper(root.get("login")),
                "%" + login.toUpperCase() + "%"
        );
    }

    public static Specification<Usuario> emailLikeIgnoreCase(String email) {
        return  (root, query, cb) -> cb.like(
                cb.upper(root.get("login")),
                "%" + email.toUpperCase() + "%"
        );
    }

    public static Specification<Usuario> roleLikeIgnoreCase(String role) {
        return  (root, query, cb) -> cb.like(
                cb.upper(root.get("login")),
                "%" + role.toUpperCase() + "%"
        );
    }
}
