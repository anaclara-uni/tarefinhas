package br.com.tarefinhas.usuario;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/**
 * Representa uma linha da tabela "usuarios".
 */
@Entity
@Table(name = "usuarios")
@Getter
@Setter
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // o banco gera o id (BIGSERIAL)
    private Long id;

    private String nome;

    private String email;

    // Texto que aparece embaixo do nome (ex: "Estudante")
    private String papel = "Estudante";

    @Column(name = "senha_hash")
    private String senhaHash;

    @Column(name = "criado_em")
    private LocalDateTime criadoEm = LocalDateTime.now();
}
