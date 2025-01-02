package io.github.eduardoluiz.libraryapi.repository;

import io.github.eduardoluiz.libraryapi.model.Autor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

@SpringBootTest
public class AutorRepositoryTest {

    @Autowired
    AutorRepository autorRepository;

    @Test
    public void salvarTest() {
        Autor autor = new Autor();
        autor.setNome("João");
        autor.setNacionalidade("Masculino");
        autor.setDataNascimento(LocalDate.of(1990, 1, 1));
        var autorSalvo = autorRepository.save(autor);
        System.out.println(autorSalvo);
    }

    @Test
    public void buscarTest() {
        var id = UUID.fromString("de3243be-c6a2-4d6b-926b-daa01addc4fc");
        Optional<Autor> possivelAutor = autorRepository.findById(id);

        if (possivelAutor.isPresent()) {

            Autor autorEncontrado = possivelAutor.get();
            autorEncontrado.setDataNascimento(LocalDate.of(1990, 2, 2));
            autorRepository.save(autorEncontrado);

            System.out.println(autorEncontrado);
        }
    }
}
