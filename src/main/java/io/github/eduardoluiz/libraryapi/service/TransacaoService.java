package io.github.eduardoluiz.libraryapi.service;

import io.github.eduardoluiz.libraryapi.model.Autor;
import io.github.eduardoluiz.libraryapi.model.GeneroLivro;
import io.github.eduardoluiz.libraryapi.model.Livro;
import io.github.eduardoluiz.libraryapi.repository.AutorRepository;
import io.github.eduardoluiz.libraryapi.repository.LivroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.UUID;

@Service
public class TransacaoService {

    @Autowired
    private AutorRepository autorRepository;

    @Autowired
    private LivroRepository livroRepository;

    //livro (titulo, ..., nome_arquivo) -> id.png
    @Transactional
    public void salvarLivroComFoto() {
        //Salva o livro
        //repository.save(livro);

        //Pega o id do livro = livro.getId();
        //var id = livro.getId();

        //Salvar foto do livro -> bucket na nuvem
        //bucketService.salvar(livro.getFoto(), id + ".png");

        //Atualizar o nome do arquivo que foi salvo
        //livro.setNomeArquivoFoto(id + ".png");
        //repository.save(livro);
    }

    @Transactional
    public void atualizacaoSemAtualizar() {
        var livro = livroRepository.findById(UUID.fromString("5e1d29e9-804d-403b-ad5b-19ba250217b9")).orElse(null);

        livro.setDataPublicacao(LocalDate.of(2024, 1, 6).atStartOfDay());

        //Não preciso chamar o repository com .save pois a transação ainda está aberta
    }

    @Transactional
    public void executar() {

        //Salva o autor
        Autor autor = new Autor();
        autor.setNome("Jacós");
        autor.setNacionalidade("Brasileiro");
        autor.setDataNascimento(LocalDate.of(1980, 11, 11));

        autorRepository.save(autor);

        //Salva o livro
        Livro livro = new Livro();
        livro.setTitulo("Algoritmos e Programação");
        livro.setIsbn("7659");
        livro.setPreco(BigDecimal.valueOf(2090.50));
        livro.setGenero(GeneroLivro.CIENCIA);
        livro.setDataPublicacao(LocalDate.of(2000, 1, 1).atStartOfDay());

        livro.setAutor(autor);

        livroRepository.save(livro);

        if (autor.getNome().equals("Jorge")) {
            throw new RuntimeException("Rollback!");
        }
    }
}
