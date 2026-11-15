package br.com.tarefinhas.tarefa;

import java.time.LocalDate;
import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Formatos dos dados de tarefa que entram e saem da API.
 */
public class TarefaDtos {

    // O que chega para criar ou editar uma tarefa
    public record TarefaRequest(
            @NotBlank(message = "A tarefa precisa de um título.")
            @Size(max = 200, message = "O título pode ter no máximo 200 letras.") String titulo,
            String descricao,
            LocalDate dataEntrega,
            Long listaId,
            @NotNull(message = "Escolha uma prioridade.") Prioridade prioridade,
            boolean lembrete,
            @Valid List<SubtarefaDto> subtarefas) {
    }

    // Uma subtarefa (usada tanto na entrada quanto na saída)
    public record SubtarefaDto(
            Long id,
            @NotBlank(message = "A subtarefa precisa de um texto.") String titulo,
            boolean concluida) {
    }

    // O que devolvemos para o front
    public record TarefaResponse(
            Long id,
            String titulo,
            String descricao,
            LocalDate dataEntrega,
            Prioridade prioridade,
            boolean concluida,
            boolean favorita,
            boolean lembrete,
            Long listaId,
            String listaNome,
            String listaCor,
            List<SubtarefaDto> subtarefas) {

        // Converte a entidade do banco no formato de resposta
        static TarefaResponse de(Tarefa t) {
            var lista = t.getLista();
            return new TarefaResponse(
                    t.getId(), t.getTitulo(), t.getDescricao(), t.getDataEntrega(), t.getPrioridade(),
                    t.isConcluida(), t.isFavorita(), t.isLembrete(),
                    lista != null ? lista.getId() : null,
                    lista != null ? lista.getNome() : null,
                    lista != null ? lista.getCor() : null,
                    t.getSubtarefas().stream()
                            .map(s -> new SubtarefaDto(s.getId(), s.getTitulo(), s.isConcluida()))
                            .toList());
        }
    }
}
