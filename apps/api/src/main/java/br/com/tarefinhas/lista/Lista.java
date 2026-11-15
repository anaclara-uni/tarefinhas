package br.com.tarefinhas.lista;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/**
 * Representa uma linha da tabela "listas" (Faculdade, Pessoal, Casa...).
 */
@Entity
@Table(name = "listas")
@Getter
@Setter
public class Lista {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Guardamos só o número do dono (chave estrangeira), sem carregar o objeto Usuario
    @Column(name = "usuario_id")
    private Long usuarioId;

    private String nome;

    private String cor;
}
