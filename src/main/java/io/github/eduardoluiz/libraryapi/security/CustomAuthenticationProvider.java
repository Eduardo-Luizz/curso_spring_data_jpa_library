package io.github.eduardoluiz.libraryapi.security;

import io.github.eduardoluiz.libraryapi.model.User;
import io.github.eduardoluiz.libraryapi.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String login = authentication.getName();
        String enteredPassword = authentication.getCredentials().toString();

        User foundUser = userService.getByLogin(login);

        if (foundUser == null) {
            throw getUserNotFoundError();
        }

        String encryptedPassword = foundUser.getPassword();

        boolean passwordsMatch = passwordEncoder.matches(enteredPassword, encryptedPassword);

        if (passwordsMatch) {
            return new CustomAuthentication(foundUser);
        }

        throw getUserNotFoundError();
    }

    private UsernameNotFoundException getUserNotFoundError() {
        return new UsernameNotFoundException("Incorrect username and/or password!");
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.isAssignableFrom(UsernamePasswordAuthenticationToken.class);
    }
}
