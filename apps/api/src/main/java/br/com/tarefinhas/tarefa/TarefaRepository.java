package br.com.tarefinhas.tarefa;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Acesso à tabela "tarefas".
 */
public interface TarefaRepository extends JpaRepository<Tarefa, Long> {

    // SELECT * FROM tarefas WHERE id = ? AND usuario_id = ?
    Optional<Tarefa> findByIdAndUsuarioId(Long id, Long usuarioId);

    /**
     * CONSULTA SQL: busca as tarefas do usuário com filtros opcionais.
     *
     * - visao:   'todas', 'hoje', 'semana' (hoje + 7 dias) ou 'concluidas'
     * - listaId: se vier, mostra só as tarefas daquela lista
     * - busca:   se vier, procura o texto no título (ILIKE = sem diferenciar maiúscula/minúscula)
     * - data:    se vier, só as tarefas daquele dia (clique no calendário)
     * - ordem:   'data' (padrão), 'prioridade', 'titulo' ou 'recentes'
     *
     * O "CAST(... ) IS NULL OR ..." faz o filtro valer só quando o valor foi enviado.
     *
     * ORDER BY com CASE: cada linha do ORDER BY só "vale" quando a ordem escolhida bate;
     * nas outras ordens o CASE dá NULL (igual para todas as tarefas) e não muda nada.
     * No fim, sempre desempata pela data de entrega e pelo id.
     */
    @Query(value = """
            SELECT t.*
              FROM tarefas t
             WHERE t.usuario_id = :usuarioId
               AND (CAST(:listaId AS BIGINT) IS NULL OR t.lista_id = CAST(:listaId AS BIGINT))
               AND (CAST(:busca AS TEXT) IS NULL OR t.titulo ILIKE '%' || CAST(:busca AS TEXT) || '%')
               AND (CAST(:data AS DATE) IS NULL OR t.data_entrega = CAST(:data AS DATE))
               AND (    :visao = 'todas'
                     OR (:visao = 'hoje'       AND t.data_entrega = CURRENT_DATE)
                     OR (:visao = 'semana'     AND t.data_entrega BETWEEN CURRENT_DATE AND CURRENT_DATE + 7)
                     OR (:visao = 'concluidas' AND t.concluida = TRUE) )
             ORDER BY
                   CASE WHEN :ordem = 'prioridade' THEN
                        CASE t.prioridade WHEN 'ALTA' THEN 1 WHEN 'MEDIA' THEN 2 ELSE 3 END
                   END,
                   CASE WHEN :ordem = 'titulo'   THEN LOWER(t.titulo) END,
                   CASE WHEN :ordem = 'recentes' THEN t.criado_em END DESC,
                   t.data_entrega NULLS LAST,
                   t.id
            """, nativeQuery = true)
    List<Tarefa> buscar(@Param("usuarioId") Long usuarioId,
                        @Param("visao") String visao,
                        @Param("listaId") Long listaId,
                        @Param("busca") String busca,
                        @Param("data") LocalDate data,
                        @Param("ordem") String ordem);
}
