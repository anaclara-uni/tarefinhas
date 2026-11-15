package br.com.tarefinhas.lista;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.tarefinhas.lista.ListaRepository.ListaComContagem;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Endpoints das listas (CRUD).
 * O cabeçalho X-Usuario-Id diz quem está logado.
 */
@RestController
@RequestMapping("/api/listas")
public class ListaController {

    // Formato do JSON que chega para criar/editar uma lista
    public record ListaRequest(
            @NotBlank(message = "Dê um nome para a lista.")
            @Size(max = 60, message = "O nome da lista pode ter no máximo 60 letras.") String nome,
            String cor) {
    }

    private final ListaService listaService;

    public ListaController(ListaService listaService) {
        this.listaService = listaService;
    }

    // GET /api/listas -> todas as listas com a contagem de pendentes
    @GetMapping
    public List<ListaComContagem> listar(@RequestHeader("X-Usuario-Id") Long usuarioId) {
        return listaService.listar(usuarioId);
    }

    // POST /api/listas -> cria uma lista
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Lista criar(@RequestHeader("X-Usuario-Id") Long usuarioId, @RequestBody @Valid ListaRequest dados) {
        return listaService.criar(usuarioId, dados.nome(), dados.cor());
    }

    // PUT /api/listas/5 -> edita a lista 5
    @PutMapping("/{id}")
    public Lista editar(@RequestHeader("X-Usuario-Id") Long usuarioId, @PathVariable Long id,
            @RequestBody @Valid ListaRequest dados) {
        return listaService.editar(usuarioId, id, dados.nome(), dados.cor());
    }

    // DELETE /api/listas/5 -> exclui a lista 5
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@RequestHeader("X-Usuario-Id") Long usuarioId, @PathVariable Long id) {
        listaService.excluir(usuarioId, id);
    }
}
