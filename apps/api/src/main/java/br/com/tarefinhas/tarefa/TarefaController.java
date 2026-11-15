package br.com.tarefinhas.tarefa;

import java.time.LocalDate;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.tarefinhas.tarefa.TarefaDtos.TarefaRequest;
import br.com.tarefinhas.tarefa.TarefaDtos.TarefaResponse;
import jakarta.validation.Valid;

/**
 * Endpoints das tarefas (CRUD + concluir + favoritar).
 */
@RestController
@RequestMapping("/api/tarefas")
public class TarefaController {

    private final TarefaService tarefaService;

    public TarefaController(TarefaService tarefaService) {
        this.tarefaService = tarefaService;
    }

    // GET /api/tarefas?visao=hoje&listaId=1&busca=slides&data=2026-11-19&ordem=prioridade
    // (todos os filtros são opcionais)
    @GetMapping
    public List<TarefaResponse> listar(@RequestHeader("X-Usuario-Id") Long usuarioId,
            @RequestParam(required = false) String visao,
            @RequestParam(required = false) Long listaId,
            @RequestParam(required = false) String busca,
            @RequestParam(required = false) LocalDate data,
            @RequestParam(required = false) String ordem) {
        return tarefaService.listar(usuarioId, visao, listaId, busca, data, ordem);
    }

    // GET /api/tarefas/6
    @GetMapping("/{id}")
    public TarefaResponse detalhar(@RequestHeader("X-Usuario-Id") Long usuarioId, @PathVariable Long id) {
        return tarefaService.detalhar(usuarioId, id);
    }

    // POST /api/tarefas
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TarefaResponse criar(@RequestHeader("X-Usuario-Id") Long usuarioId,
            @RequestBody @Valid TarefaRequest dados) {
        return tarefaService.criar(usuarioId, dados);
    }

    // PUT /api/tarefas/6
    @PutMapping("/{id}")
    public TarefaResponse editar(@RequestHeader("X-Usuario-Id") Long usuarioId, @PathVariable Long id,
            @RequestBody @Valid TarefaRequest dados) {
        return tarefaService.editar(usuarioId, id, dados);
    }

    // PATCH /api/tarefas/6/concluir  -> marca/desmarca como concluída
    @PatchMapping("/{id}/concluir")
    public TarefaResponse concluir(@RequestHeader("X-Usuario-Id") Long usuarioId, @PathVariable Long id) {
        return tarefaService.alternarConcluida(usuarioId, id);
    }

    // PATCH /api/tarefas/6/favoritar -> coloca/tira a estrelinha
    @PatchMapping("/{id}/favoritar")
    public TarefaResponse favoritar(@RequestHeader("X-Usuario-Id") Long usuarioId, @PathVariable Long id) {
        return tarefaService.alternarFavorita(usuarioId, id);
    }

    // DELETE /api/tarefas/6
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@RequestHeader("X-Usuario-Id") Long usuarioId, @PathVariable Long id) {
        tarefaService.excluir(usuarioId, id);
    }
}
