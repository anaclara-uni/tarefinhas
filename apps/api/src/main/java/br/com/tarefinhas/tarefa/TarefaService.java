package br.com.tarefinhas.tarefa;

import java.time.LocalDate;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import br.com.tarefinhas.lista.Lista;
import br.com.tarefinhas.lista.ListaRepository;
import br.com.tarefinhas.tarefa.TarefaDtos.SubtarefaDto;
import br.com.tarefinhas.tarefa.TarefaDtos.TarefaRequest;
import br.com.tarefinhas.tarefa.TarefaDtos.TarefaResponse;

/**
 * Regras das tarefas: listar, criar, editar, concluir, favoritar e excluir.
 */
@Service
public class TarefaService {

    private final TarefaRepository tarefaRepository;
    private final ListaRepository listaRepository;

    public TarefaService(TarefaRepository tarefaRepository, ListaRepository listaRepository) {
        this.tarefaRepository = tarefaRepository;
        this.listaRepository = listaRepository;
    }

    @Transactional(readOnly = true)
    public List<TarefaResponse> listar(Long usuarioId, String visao, Long listaId, String busca,
            LocalDate data, String ordem) {
        if (visao == null || visao.isBlank()) {
            visao = "todas";
        }
        if (ordem == null || ordem.isBlank()) {
            ordem = "data";
        }
        if (busca != null && busca.isBlank()) {
            busca = null;
        }
        return tarefaRepository.buscar(usuarioId, visao, listaId, busca, data, ordem).stream()
                .map(TarefaResponse::de)
                .toList();
    }

    @Transactional(readOnly = true)
    public TarefaResponse detalhar(Long usuarioId, Long id) {
        return TarefaResponse.de(buscar(usuarioId, id));
    }

    @Transactional
    public TarefaResponse criar(Long usuarioId, TarefaRequest dados) {
        Tarefa tarefa = new Tarefa();
        tarefa.setUsuarioId(usuarioId);
        preencher(tarefa, dados);
        return TarefaResponse.de(tarefaRepository.save(tarefa));
    }

    @Transactional
    public TarefaResponse editar(Long usuarioId, Long id, TarefaRequest dados) {
        Tarefa tarefa = buscar(usuarioId, id);
        preencher(tarefa, dados);
        return TarefaResponse.de(tarefaRepository.save(tarefa));
    }

    // Marca como concluída / volta para pendente
    @Transactional
    public TarefaResponse alternarConcluida(Long usuarioId, Long id) {
        Tarefa tarefa = buscar(usuarioId, id);
        tarefa.setConcluida(!tarefa.isConcluida());
        return TarefaResponse.de(tarefaRepository.save(tarefa));
    }

    // Coloca / tira a estrelinha
    @Transactional
    public TarefaResponse alternarFavorita(Long usuarioId, Long id) {
        Tarefa tarefa = buscar(usuarioId, id);
        tarefa.setFavorita(!tarefa.isFavorita());
        return TarefaResponse.de(tarefaRepository.save(tarefa));
    }

    @Transactional
    public void excluir(Long usuarioId, Long id) {
        // As subtarefas são apagadas junto (cascade)
        tarefaRepository.delete(buscar(usuarioId, id));
    }

    // Copia os dados que chegaram para a entidade
    private void preencher(Tarefa tarefa, TarefaRequest dados) {
        tarefa.setTitulo(dados.titulo().trim());
        tarefa.setDescricao(dados.descricao() == null || dados.descricao().isBlank() ? null : dados.descricao().trim());
        tarefa.setDataEntrega(dados.dataEntrega());
        tarefa.setPrioridade(dados.prioridade());
        tarefa.setLembrete(dados.lembrete());
        tarefa.setLista(buscarLista(tarefa.getUsuarioId(), dados.listaId()));

        // Subtarefas: limpamos e colocamos as que vieram do formulário.
        // Por causa do orphanRemoval, as que saíram são apagadas do banco.
        tarefa.getSubtarefas().clear();
        if (dados.subtarefas() != null) {
            for (SubtarefaDto s : dados.subtarefas()) {
                Subtarefa sub = new Subtarefa();
                sub.setTarefa(tarefa);
                sub.setTitulo(s.titulo().trim());
                sub.setConcluida(s.concluida());
                tarefa.getSubtarefas().add(sub);
            }
        }
    }

    private Lista buscarLista(Long usuarioId, Long listaId) {
        if (listaId == null) {
            return null; // tarefa sem lista
        }
        return listaRepository.findByIdAndUsuarioId(listaId, usuarioId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Lista inválida."));
    }

    private Tarefa buscar(Long usuarioId, Long id) {
        return tarefaRepository.findByIdAndUsuarioId(id, usuarioId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tarefa não encontrada."));
    }
}
