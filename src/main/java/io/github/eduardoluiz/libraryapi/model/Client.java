package io.github.eduardoluiz.libraryapi.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Entity
@Table(name = "tb_client", schema = "public")
@Data
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "client_id", length = 150, nullable = false)
    private String clientId;

    @Column(name = "client_secret", length = 400, nullable = false)
    private String clientSecret;

    @Column(name = "redirect_uri", length = 200, nullable = false)
    private String redirectUri;

    @Column(name = "scope", length = 50)
    private String scope;
}
