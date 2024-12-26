package io.github.eduardoluiz.libraryapi.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "livro", schema = "public")
@Data
public class Livro {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private int id;

    @Column(name = "isbn", length = 20, nullable = false)
    private String isbn;

    @Column(name = "titulo", length = 150, nullable = false)
    private String titulo;

    @Column(name = "data_publicacao", nullable = false)
    private LocalDateTime dataPublicacao;

    @Enumerated(EnumType.STRING)
    @Column(name = "genero", length = 30, nullable = false)
    private GeneroLivro genero;

    @Column(name = "preco", precision = 18, scale = 2)
    private Double preco;

    @JoinColumn(name = "id_autor")
    @ManyToOne
    private Autor autor;

}
