package io.github.eduardoluiz.libraryapi.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "livro", schema = "public")
@Data
@ToString(exclude = "autor")
public class Livro {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

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
    private BigDecimal preco;

    @JoinColumn(name = "id_autor")
    @ManyToOne(
            fetch = FetchType.LAZY // Quanto temos relacionamento ToOne dessa forma ele não trás o autor somente o livro, por padrão trás os dois (FetchType.EAGER)
            // cascade = CascadeType.ALL
    )
    private Autor autor;

}
