package br.com.tarefinhas.resumo;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import br.com.tarefinhas.tarefa.Tarefa;

/**
 * Consultas SQL de "relatório" que alimentam os números da tela:
 * progresso, contadores do menu, bolinhas do calendário e próximas entregas.
 */
public interface ResumoRepository extends Repository<Tarefa, Long> {

    /**
     * CONSULTA SQL: vários contadores de uma vez só.
     * COUNT(*) FILTER (WHERE ...) conta só as linhas que passam na condição.
     */
    @Query(value = """
            SELECT COUNT(*)                                          AS total,
                   COUNT(*) FILTER (WHERE concluida = TRUE)          AS concluidas,
                   COUNT(*) FILTER (WHERE concluida = FALSE)         AS pendentes,
                   COUNT(*) FILTER (WHERE concluida = FALSE
                                      AND data_entrega = CURRENT_DATE) AS hoje,
                   COUNT(*) FILTER (WHERE concluida = FALSE
                                      AND data_entrega BETWEEN CURRENT_DATE AND CURRENT_DATE + 7) AS semana
              FROM tarefas
             WHERE usuario_id = :usuarioId
            """, nativeQuery = true)
    Contagens contar(@Param("usuarioId") Long usuarioId);

    /**
     * CONSULTA SQL: quais dias do mês têm alguma tarefa (as bolinhas do calendário).
     * EXTRACT(DAY FROM data) pega só o número do dia. DISTINCT tira os repetidos.
     */
    @Query(value = """
            SELECT DISTINCT CAST(EXTRACT(DAY FROM data_entrega) AS INTEGER) AS dia
              FROM tarefas
             WHERE usuario_id = :usuarioId
               AND data_entrega >= :inicio
               AND data_entrega <  :fim
             ORDER BY dia
            """, nativeQuery = true)
    List<Integer> diasComTarefa(@Param("usuarioId") Long usuarioId,
                                @Param("inicio") LocalDate inicio,
                                @Param("fim") LocalDate fim);

    /**
     * CONSULTA SQL: as 3 próximas entregas ainda pendentes, com o nome da lista.
     * LEFT JOIN traz a tarefa mesmo que ela não tenha lista. LIMIT 3 pega só três.
     * Os apelidos entre aspas ("dataEntrega") mantêm as letras maiúsculas do nome.
     */
    @Query(value = """
            SELECT t.id,
                   t.titulo,
                   TO_CHAR(t.data_entrega, 'YYYY-MM-DD') AS "dataEntrega",
                   t.prioridade,
                   l.nome AS "listaNome"
              FROM tarefas t
              LEFT JOIN listas l ON l.id = t.lista_id
             WHERE t.usuario_id = :usuarioId
               AND t.concluida = FALSE
               AND t.data_entrega >= CURRENT_DATE
             ORDER BY t.data_entrega, t.id
             LIMIT 3
            """, nativeQuery = true)
    List<ProximaEntrega> proximasEntregas(@Param("usuarioId") Long usuarioId);

    /**
     * CONSULTA SQL: notificações do sino.
     * Pega as tarefas pendentes que estão ATRASADAS, que vencem HOJE, ou que estão com
     * "Me lembrar" ligado e vencem AMANHÃ (o lembrete é "1 dia antes").
     * O CASE dá um "tipo" para cada uma, que o site usa para escolher a cor e o texto.
     */
    @Query(value = """
            SELECT t.id,
                   t.titulo,
                   TO_CHAR(t.data_entrega, 'YYYY-MM-DD') AS "dataEntrega",
                   CASE
                       WHEN t.data_entrega < CURRENT_DATE THEN 'ATRASADA'
                       WHEN t.data_entrega = CURRENT_DATE THEN 'HOJE'
                       ELSE 'LEMBRETE'
                   END AS tipo
              FROM tarefas t
             WHERE t.usuario_id = :usuarioId
               AND t.concluida = FALSE
               AND (    t.data_entrega <= CURRENT_DATE
                     OR (t.lembrete = TRUE AND t.data_entrega = CURRENT_DATE + 1) )
             ORDER BY t.data_entrega, t.id
            """, nativeQuery = true)
    List<Notificacao> notificacoes(@Param("usuarioId") Long usuarioId);

    // "Projeções": o Spring preenche esses métodos com as colunas dos SELECTs acima
    interface Contagens {
        Long getTotal();
        Long getConcluidas();
        Long getPendentes();
        Long getHoje();
        Long getSemana();
    }

    interface ProximaEntrega {
        Long getId();
        String getTitulo();
        String getDataEntrega();   // data como texto: 2026-11-19
        String getPrioridade();
        String getListaNome();
    }

    interface Notificacao {
        Long getId();
        String getTitulo();
        String getDataEntrega();
        String getTipo();          // ATRASADA, HOJE ou LEMBRETE
    }
}
