package br.com.tarefinhas.lista;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Acesso à tabela "listas".
 */
public interface ListaRepository extends JpaRepository<Lista, Long> {

    // SELECT * FROM listas WHERE id = ? AND usuario_id = ?
    // (garante que a pessoa só mexe nas listas dela)
    Optional<Lista> findByIdAndUsuarioId(Long id, Long usuarioId);

    // SELECT COUNT(*) FROM listas WHERE usuario_id = ?
    long countByUsuarioId(Long usuarioId);

    /**
     * CONSULTA SQL: listas do usuário com a quantidade de tarefas PENDENTES em cada uma.
     *
     * - LEFT JOIN: traz a lista mesmo se ela não tiver nenhuma tarefa (contagem 0).
     * - COUNT(...) FILTER (WHERE ...): conta só as tarefas não concluídas.
     * - GROUP BY: junta as linhas por lista para poder contar.
     */
    @Query(value = """
            SELECT l.id   AS id,
                   l.nome AS nome,
                   l.cor  AS cor,
                   COUNT(t.id) FILTER (WHERE t.concluida = FALSE) AS pendentes
              FROM listas l
              LEFT JOIN tarefas t ON t.lista_id = l.id
             WHERE l.usuario_id = :usuarioId
             GROUP BY l.id, l.nome, l.cor
             ORDER BY l.id
            """, nativeQuery = true)
    List<ListaComContagem> listarComPendentes(@Param("usuarioId") Long usuarioId);

    // "Projeção": o Spring preenche esses métodos com as colunas do SELECT acima
    interface ListaComContagem {
        Long getId();
        String getNome();
        String getCor();
        Long getPendentes();
    }
}
