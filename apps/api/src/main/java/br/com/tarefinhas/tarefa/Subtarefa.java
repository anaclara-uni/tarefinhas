package br.com.tarefinhas.tarefa;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/**
 * Representa uma linha da tabela "subtarefas" (os passinhos de uma tarefa).
 */
@Entity
@Table(name = "subtarefas")
@Getter
@Setter
public class Subtarefa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Muitas subtarefas -> uma tarefa (coluna tarefa_id)
    @ManyToOne
    @JoinColumn(name = "tarefa_id")
    private Tarefa tarefa;

    private String titulo;

    private boolean concluida;
}
