package io.github.eduardoluiz.libraryapi.service;

import io.github.eduardoluiz.libraryapi.controller.dto.UsuarioRequestDTO;
import io.github.eduardoluiz.libraryapi.controller.dto.UsuarioResponseDTO;
import io.github.eduardoluiz.libraryapi.exceptions.ResourceNotFoundException;
import io.github.eduardoluiz.libraryapi.model.Usuario;
import io.github.eduardoluiz.libraryapi.repository.UsuarioRepository;
import io.github.eduardoluiz.libraryapi.repository.specs.UsuarioSpecs;
import io.github.eduardoluiz.libraryapi.validator.UsuarioValidator;
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
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final UsuarioValidator usuarioValidator;

    public UsuarioResponseDTO salvar(UsuarioRequestDTO dto) {
        Usuario usuario = new Usuario();
        usuario.setLogin(dto.login());
        usuario.setSenha(passwordEncoder.encode(dto.senha()));
        usuario.setEmail(dto.email());
        usuario.setRoles(dto.roles());
        Usuario salvo = usuarioRepository.save(usuario);

        return new UsuarioResponseDTO(
                salvo.getId(),
                salvo.getLogin(),
                salvo.getEmail(),
                salvo.getRoles()
        );
    }

    public void salvar(Usuario usuario) {
        var senha = usuario.getSenha();
        usuario.setSenha(passwordEncoder.encode(senha));
        usuarioRepository.save(usuario);
    }

    public UsuarioResponseDTO buscarPorId(UUID id){
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado. ID: " + id));

        return new UsuarioResponseDTO(usuario.getId(), usuario.getLogin(), usuario.getEmail(), usuario.getRoles());
    }

    public Page<Usuario> pesquisa (
            String login,
            String email,
            String role,
            Integer pagina,
            Integer tamanhoPagina) {

        Specification<Usuario> specs = Specification.where((root, query, cb) -> cb.conjunction());

        if (login != null && !login.isBlank()) {
            specs = specs.and(UsuarioSpecs.loginLikeIgnoreCase(login));
        }

        if (email != null && !email.isBlank()) {
            specs = specs.and(UsuarioSpecs.emailLikeIgnoreCase(email));
        }

        if (role != null && !role.isBlank()) {
            specs = specs.and(UsuarioSpecs.roleLikeIgnoreCase(role));
        }

        Pageable pageRequest = PageRequest.of(pagina, tamanhoPagina);

        return usuarioRepository.findAll(specs, pageRequest);
    }

    public UsuarioResponseDTO atualizar(UUID id, UsuarioRequestDTO dto) {
        Usuario usuarioExistente = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado. ID: " + id));

        if (dto.login() != null && !dto.login().isBlank()) {
            usuarioExistente.setLogin(dto.login());
        }

        if (dto.email() != null && !dto.email().isBlank()) {
            usuarioExistente.setEmail(dto.email());
        }

        if (dto.senha() != null && !dto.senha().isBlank()) {
            usuarioExistente.setSenha(passwordEncoder.encode(dto.senha()));
        }

        Usuario usuarioAtualizado = usuarioRepository.save(usuarioExistente);
        return new UsuarioResponseDTO(
                usuarioAtualizado.getId(), usuarioAtualizado.getLogin(), usuarioAtualizado.getEmail(), usuarioAtualizado.getRoles()
        );
    }

    public UsuarioResponseDTO atualizarRoles(UUID id, List<String> rolesNovas) {
        Usuario usuarioExistente = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado. ID: " + id));

        usuarioExistente.setRoles(rolesNovas);

        Usuario salvo = usuarioRepository.save(usuarioExistente);
        return new UsuarioResponseDTO(
                salvo.getId(), salvo.getLogin(), salvo.getEmail(), salvo.getRoles()
        );
    }

    public void deletar(UUID id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado. ID: " + id));

        usuarioValidator.validarAssociacoesParaExcluir(usuario);
        usuarioRepository.delete(usuario);
    }

    public Usuario obterPorLogin(String login) {
        return usuarioRepository.findByLogin(login);
    }

    public Usuario obterPorEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }
}
