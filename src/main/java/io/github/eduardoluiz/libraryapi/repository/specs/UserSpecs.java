package io.github.eduardoluiz.libraryapi.repository.specs;

import io.github.eduardoluiz.libraryapi.model.User;
import org.springframework.data.jpa.domain.Specification;

public class UserSpecs {

    public static Specification<User> loginLikeIgnoreCase(String login) {
        return  (root, query, cb) -> cb.like(
                cb.upper(root.get("login")),
                "%" + login.toUpperCase() + "%"
        );
    }

    public static Specification<User> emailLikeIgnoreCase(String email) {
        return  (root, query, cb) -> cb.like(
                cb.upper(root.get("login")),
                "%" + email.toUpperCase() + "%"
        );
    }

    public static Specification<User> roleLikeIgnoreCase(String role) {
        return  (root, query, cb) -> cb.like(
                cb.upper(root.get("login")),
                "%" + role.toUpperCase() + "%"
        );
    }
}
