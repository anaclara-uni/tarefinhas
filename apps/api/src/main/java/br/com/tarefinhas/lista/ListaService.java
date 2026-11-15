package br.com.tarefinhas.lista;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import br.com.tarefinhas.lista.ListaRepository.ListaComContagem;

/**
 * Regras das listas: listar, criar, editar e excluir (CRUD).
 */
@Service
public class ListaService {

    // Cores usadas nas bolinhas das listas, em ordem
    private static final List<String> CORES = List.of(
            "#E86A92", "#8B6CF0", "#3DBE85", "#F2994A", "#4FA3E0", "#E0B84F");

    private final ListaRepository listaRepository;

    public ListaService(ListaRepository listaRepository) {
        this.listaRepository = listaRepository;
    }

    public List<ListaComContagem> listar(Long usuarioId) {
        return listaRepository.listarComPendentes(usuarioId);
    }

    @Transactional
    public Lista criar(Long usuarioId, String nome, String cor) {
        Lista lista = new Lista();
        lista.setUsuarioId(usuarioId);
        lista.setNome(nome.trim());
        // Se não escolheram cor, pegamos a próxima da paleta
        if (cor == null || cor.isBlank()) {
            long quantidade = listaRepository.countByUsuarioId(usuarioId);
            cor = CORES.get((int) (quantidade % CORES.size()));
        }
        lista.setCor(cor);
        return listaRepository.save(lista);
    }

    @Transactional
    public Lista editar(Long usuarioId, Long id, String nome, String cor) {
        Lista lista = buscar(usuarioId, id);
        lista.setNome(nome.trim());
        if (cor != null && !cor.isBlank()) {
            lista.setCor(cor);
        }
        return listaRepository.save(lista);
    }

    @Transactional
    public void excluir(Long usuarioId, Long id) {
        // As tarefas dessa lista não somem: o banco deixa lista_id = NULL (ON DELETE SET NULL)
        listaRepository.delete(buscar(usuarioId, id));
    }

    // Listas que toda conta nova recebe
    @Transactional
    public void criarListasPadrao(Long usuarioId) {
        for (String nome : List.of("Faculdade", "Pessoal", "Casa", "Estágio")) {
            criar(usuarioId, nome, null);
        }
    }

    private Lista buscar(Long usuarioId, Long id) {
        return listaRepository.findByIdAndUsuarioId(id, usuarioId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Lista não encontrada."));
    }
}
