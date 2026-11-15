package br.com.tarefinhas.tarefa;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import br.com.tarefinhas.lista.Lista;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/**
 * Representa uma linha da tabela "tarefas".
 */
@Entity
@Table(name = "tarefas")
@Getter
@Setter
public class Tarefa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "usuario_id")
    private Long usuarioId;

    // Muitas tarefas -> uma lista. Carregamos a lista junto para mostrar nome e cor.
    @ManyToOne
    @JoinColumn(name = "lista_id")
    private Lista lista;

    private String titulo;

    private String descricao;

    @Column(name = "data_entrega")
    private LocalDate dataEntrega;

    @Enumerated(EnumType.STRING) // salva o nome ('ALTA') e não um número
    private Prioridade prioridade = Prioridade.MEDIA;

    private boolean concluida;

    private boolean favorita;

    private boolean lembrete;

    @Column(name = "criado_em")
    private LocalDateTime criadoEm = LocalDateTime.now();

    // Uma tarefa -> muitas subtarefas.
    // cascade ALL: salvar/apagar a tarefa salva/apaga as subtarefas junto.
    // orphanRemoval: tirar uma subtarefa da lista apaga ela do banco.
    @OneToMany(mappedBy = "tarefa", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("id")
    private List<Subtarefa> subtarefas = new ArrayList<>();
}
