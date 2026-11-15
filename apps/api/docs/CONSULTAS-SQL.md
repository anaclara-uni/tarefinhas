# Consultas SQL do projeto

Todas as consultas abaixo estão no código, dentro de anotações `@Query(nativeQuery = true)`
nos arquivos `*Repository.java`. "Native" quer dizer que é **SQL puro do PostgreSQL**.

Para testar no **Adminer** (http://localhost:8081 → *Comando SQL*), troque os parâmetros
`:usuarioId`, `:busca` etc. por valores. Exemplos prontos estão em cada item.

---

## 1. Buscar tarefas com filtros — `TarefaRepository.buscar`

Usada na lista principal. Os filtros são opcionais.

```sql
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
```

- `ILIKE` procura o texto ignorando maiúsculas/minúsculas; `'%' || texto || '%'` = "contém".
- `CAST(:listaId AS BIGINT) IS NULL OR ...` → se o filtro não foi enviado, a condição é ignorada.
- `CURRENT_DATE + 7` → daqui a 7 dias. `BETWEEN` inclui as duas pontas.
- `:data` → quando um dia do calendário é clicado, mostra só as tarefas daquele dia.
- **ORDER BY com CASE** ("Ordenar por"): cada linha só vale quando a ordem escolhida bate.
  Nas outras, o `CASE` dá `NULL` para todas as tarefas e não muda nada. Para ordenar por
  prioridade, o `CASE` transforma ALTA/MEDIA/BAIXA em 1/2/3. `DESC` = do maior para o menor
  (mais recentes primeiro).
- `NULLS LAST` → tarefas sem data vão para o fim.

**Teste no Adminer** (tarefas da Maria por prioridade):
```sql
SELECT titulo, prioridade FROM tarefas
 WHERE usuario_id = 1
 ORDER BY CASE prioridade WHEN 'ALTA' THEN 1 WHEN 'MEDIA' THEN 2 ELSE 3 END, data_entrega;
```

**Outro teste** (tarefas da Maria que têm "de" no título):
```sql
SELECT titulo, data_entrega FROM tarefas
 WHERE usuario_id = 1 AND titulo ILIKE '%de%'
 ORDER BY data_entrega NULLS LAST;
```

## 2. Listas com a quantidade de pendentes — `ListaRepository.listarComPendentes`

Usada no menu "Minhas listas" (o numerozinho ao lado de cada lista).

```sql
SELECT l.id, l.nome, l.cor,
       COUNT(t.id) FILTER (WHERE t.concluida = FALSE) AS pendentes
  FROM listas l
  LEFT JOIN tarefas t ON t.lista_id = l.id
 WHERE l.usuario_id = :usuarioId
 GROUP BY l.id, l.nome, l.cor
 ORDER BY l.id
```

- `LEFT JOIN` → a lista aparece **mesmo sem tarefas** (com 0).
- `GROUP BY` → agrupa as linhas de cada lista para poder contar.
- `COUNT(...) FILTER (WHERE ...)` → conta só as tarefas não concluídas.

**Teste no Adminer:** troque `:usuarioId` por `1`.

## 3. Contadores do painel — `ResumoRepository.contar`

Usada no cartão "Seu progresso" e nos números do menu (Hoje, Próximos 7 dias...).

```sql
SELECT COUNT(*)                                     AS total,
       COUNT(*) FILTER (WHERE concluida = TRUE)     AS concluidas,
       COUNT(*) FILTER (WHERE concluida = FALSE)    AS pendentes,
       COUNT(*) FILTER (WHERE concluida = FALSE
                          AND data_entrega = CURRENT_DATE) AS hoje,
       COUNT(*) FILTER (WHERE concluida = FALSE
                          AND data_entrega BETWEEN CURRENT_DATE AND CURRENT_DATE + 7) AS semana
  FROM tarefas
 WHERE usuario_id = :usuarioId
```

Uma consulta só calcula 5 números diferentes, cada um com seu `FILTER`.

## 4. Dias do mês com tarefa — `ResumoRepository.diasComTarefa`

Usada nas bolinhas do calendário.

```sql
SELECT DISTINCT CAST(EXTRACT(DAY FROM data_entrega) AS INTEGER) AS dia
  FROM tarefas
 WHERE usuario_id = :usuarioId
   AND data_entrega >= :inicio      -- primeiro dia do mês
   AND data_entrega <  :fim         -- primeiro dia do mês seguinte
 ORDER BY dia
```

- `EXTRACT(DAY FROM data)` → pega só o número do dia (ex: 19).
- `DISTINCT` → tira os repetidos (dois trabalhos no dia 19 viram um 19 só).

**Teste no Adminer:**
```sql
SELECT DISTINCT EXTRACT(DAY FROM data_entrega) AS dia FROM tarefas
 WHERE usuario_id = 1 AND data_entrega >= DATE_TRUNC('month', CURRENT_DATE)
 ORDER BY dia;
```

## 5. Próximas entregas — `ResumoRepository.proximasEntregas`

Usada no cartão "Próximas entregas".

```sql
SELECT t.id, t.titulo,
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
```

- `LEFT JOIN listas` → traz o nome da lista (ou nulo, se a tarefa não tiver lista).
- `LIMIT 3` → só as 3 primeiras.
- `TO_CHAR` → transforma a data em texto `2026-11-19`.

## 6. Notificações do sino — `ResumoRepository.notificacoes`

Usada na caixinha que abre ao clicar no sino.

```sql
SELECT t.id, t.titulo,
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
```

- `CASE WHEN ... THEN ... END` → é um "se" dentro do SQL: cria a coluna `tipo` conforme a data.
- A condição com `OR` junta dois grupos: o que já venceu ou vence hoje, **ou** o que tem
  lembrete ligado e vence amanhã (o "Me lembrar 1 dia antes").

**Teste no Adminer:** troque `:usuarioId` por `1`.

---

## Consultas "automáticas" do Spring Data

Além das consultas acima, o Spring cria SQL sozinho a partir do **nome do método**:

| Método | SQL gerado (simplificado) |
|---|---|
| `findByEmailIgnoreCase(email)` | `SELECT * FROM usuarios WHERE LOWER(email) = LOWER(?)` |
| `existsByEmailIgnoreCase(email)` | `SELECT COUNT(*) > 0 FROM usuarios WHERE LOWER(email) = LOWER(?)` |
| `findById(id)` | `SELECT * FROM usuarios WHERE id = ?` (perfil) |
| `findByIdAndUsuarioId(id, usuarioId)` | `SELECT * FROM tarefas WHERE id = ? AND usuario_id = ?` |
| `countByUsuarioId(usuarioId)` | `SELECT COUNT(*) FROM listas WHERE usuario_id = ?` |
| `save(objeto)` | `INSERT ...` (novo) ou `UPDATE ...` (já existe) |
| `delete(objeto)` | `DELETE FROM ... WHERE id = ?` |

> 💡 Com `spring.jpa.show-sql=true`, todo SQL executado aparece no log da API:
> `docker compose logs -f api`

## Mais consultas para mostrar no Adminer

```sql
-- Quantas tarefas cada pessoa tem
SELECT u.nome, COUNT(t.id) AS tarefas
  FROM usuarios u LEFT JOIN tarefas t ON t.usuario_id = u.id
 GROUP BY u.nome;

-- Tarefas por prioridade
SELECT prioridade, COUNT(*) FROM tarefas GROUP BY prioridade ORDER BY COUNT(*) DESC;

-- Tarefas atrasadas (data já passou e não foram feitas)
SELECT titulo, data_entrega FROM tarefas
 WHERE concluida = FALSE AND data_entrega < CURRENT_DATE;

-- Progresso das subtarefas de cada tarefa
SELECT t.titulo,
       COUNT(s.id) FILTER (WHERE s.concluida) || ' de ' || COUNT(s.id) AS subtarefas_feitas
  FROM tarefas t JOIN subtarefas s ON s.tarefa_id = t.id
 GROUP BY t.titulo;
```
