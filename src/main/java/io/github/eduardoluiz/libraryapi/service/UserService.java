package io.github.eduardoluiz.libraryapi.service;

import io.github.eduardoluiz.libraryapi.controller.dto.UserRequestDTO;
import io.github.eduardoluiz.libraryapi.controller.dto.UserResponseDTO;
import io.github.eduardoluiz.libraryapi.exceptions.ResourceNotFoundException;
import io.github.eduardoluiz.libraryapi.model.User;
import io.github.eduardoluiz.libraryapi.repository.UserRepository;
import io.github.eduardoluiz.libraryapi.repository.specs.UserSpecs;
import io.github.eduardoluiz.libraryapi.validator.UserValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserValidator userValidator;

    public UserResponseDTO save(UserRequestDTO dto) {
        User user = new User();
        user.setLogin(dto.login());
        user.setPassword(passwordEncoder.encode(dto.password()));
        user.setEmail(dto.email());
        user.setRoles(dto.roles());
        User saved = userRepository.save(user);

        return new UserResponseDTO(
                saved.getId(),
                saved.getLogin(),
                saved.getEmail(),
                saved.getRoles()
        );
    }

    public void save(User user) {
        String pwd = user.getPassword();
        user.setPassword(passwordEncoder.encode(pwd));
        userRepository.save(user);
    }

    public UserResponseDTO getById(UUID id){
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found. ID: " + id));

        return new UserResponseDTO(user.getId(), user.getLogin(), user.getEmail(), user.getRoles());
    }

    public Page<User> search(
            String login,
            String email,
            String role,
            Integer page,
            Integer pageSize) {

        Specification<User> specs = Specification.where((root, query, cb) -> cb.conjunction());

        if (login != null && !login.isBlank()) {
            specs = specs.and(UserSpecs.loginLikeIgnoreCase(login));
        }

        if (email != null && !email.isBlank()) {
            specs = specs.and(UserSpecs.emailLikeIgnoreCase(email));
        }

        if (role != null && !role.isBlank()) {
            specs = specs.and(UserSpecs.roleLikeIgnoreCase(role));
        }

        Pageable pageRequest = PageRequest.of(page, pageSize);

        return userRepository.findAll(specs, pageRequest);
    }

    public UserResponseDTO update(UUID id, UserRequestDTO dto) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found. ID: " + id));

        if (dto.login() != null && !dto.login().isBlank()) {
            existingUser.setLogin(dto.login());
        }

        if (dto.email() != null && !dto.email().isBlank()) {
            existingUser.setEmail(dto.email());
        }

        if (dto.password() != null && !dto.password().isBlank()) {
            existingUser.setPassword(passwordEncoder.encode(dto.password()));
        }

        User updatedUser = userRepository.save(existingUser);
        return new UserResponseDTO(
                updatedUser.getId(), updatedUser.getLogin(), updatedUser.getEmail(), updatedUser.getRoles()
        );
    }

    public UserResponseDTO updateRoles(UUID id, List<String> newRoles) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found. ID: " + id));

        existingUser.setRoles(newRoles);

        User saved = userRepository.save(existingUser);
        return new UserResponseDTO(
                saved.getId(), saved.getLogin(), saved.getEmail(), saved.getRoles()
        );
    }

    public void delete(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found. ID: " + id));

        userValidator.validateAssociationsForDeletion(user);
        userRepository.delete(user);
    }

    public User getByLogin(String login) {
        return userRepository.findByLogin(login);
    }

    public User getByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}
