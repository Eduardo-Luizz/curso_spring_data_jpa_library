package io.github.eduardoluiz.libraryapi.validator;

import io.github.eduardoluiz.libraryapi.exceptions.OperacaoNaoPermitidaException;
import io.github.eduardoluiz.libraryapi.model.Usuario;
import io.github.eduardoluiz.libraryapi.repository.AutorRepository;
import io.github.eduardoluiz.libraryapi.repository.LivroRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class UsuarioValidator {

    private final AutorRepository autorRepository;
    private final LivroRepository livroRepository;

    public void validarAssociacoesParaExcluir(Usuario usuario){
        boolean temAutorRelacionado = autorRepository.existsByUsuario(usuario);
        boolean temLivroRelacionado = livroRepository.existsByUsuario(usuario);

        if(temAutorRelacionado || temLivroRelacionado){
            throw new OperacaoNaoPermitidaException("Não é possível excluir o usuário pois existem referências em Autor/Livro.");
        }
    }
}
