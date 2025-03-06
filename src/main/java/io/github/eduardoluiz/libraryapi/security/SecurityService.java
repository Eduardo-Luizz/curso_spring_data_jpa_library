package io.github.eduardoluiz.libraryapi.security;

import io.github.eduardoluiz.libraryapi.model.User;
import io.github.eduardoluiz.libraryapi.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SecurityService {

    private final UserService userService;

    public User getLoggedInUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication instanceof CustomAuthentication customAuthentication){
            return customAuthentication.getUser();
        }

        return null;
    }
}
