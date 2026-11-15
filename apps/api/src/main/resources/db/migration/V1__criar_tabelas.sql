-- ============================================================
--  V1 - Criação das tabelas do Tarefinhas
--  O Flyway roda este arquivo UMA vez, quando o banco está vazio.
-- ============================================================

-- Pessoas que usam o app
CREATE TABLE usuarios (
    id          BIGSERIAL    PRIMARY KEY,
    nome        VARCHAR(100) NOT NULL,
    email       VARCHAR(150) NOT NULL UNIQUE,      -- não pode repetir e-mail
    senha_hash  VARCHAR(100) NOT NULL,             -- senha "embaralhada" com BCrypt
    criado_em   TIMESTAMP    NOT NULL DEFAULT NOW()
);

-- Listas de cada usuário (Faculdade, Pessoal, Casa...)
CREATE TABLE listas (
    id          BIGSERIAL    PRIMARY KEY,
    usuario_id  BIGINT       NOT NULL REFERENCES usuarios (id) ON DELETE CASCADE,
    nome        VARCHAR(60)  NOT NULL,
    cor         VARCHAR(7)   NOT NULL              -- cor da bolinha, ex: #E86A92
);

-- Tarefas
CREATE TABLE tarefas (
    id            BIGSERIAL    PRIMARY KEY,
    usuario_id    BIGINT       NOT NULL REFERENCES usuarios (id) ON DELETE CASCADE,
    -- Se a lista for apagada, a tarefa continua existindo, só fica "sem lista"
    lista_id      BIGINT       REFERENCES listas (id) ON DELETE SET NULL,
    titulo        VARCHAR(200) NOT NULL,
    descricao     TEXT,
    data_entrega  DATE,
    prioridade    VARCHAR(10)  NOT NULL DEFAULT 'MEDIA'
                  CHECK (prioridade IN ('BAIXA', 'MEDIA', 'ALTA')),
    concluida     BOOLEAN      NOT NULL DEFAULT FALSE,
    favorita      BOOLEAN      NOT NULL DEFAULT FALSE,
    lembrete      BOOLEAN      NOT NULL DEFAULT FALSE,
    criado_em     TIMESTAMP    NOT NULL DEFAULT NOW()
);

-- Subtarefas (passinhos de uma tarefa). Apagou a tarefa, apaga as subtarefas.
CREATE TABLE subtarefas (
    id          BIGSERIAL    PRIMARY KEY,
    tarefa_id   BIGINT       NOT NULL REFERENCES tarefas (id) ON DELETE CASCADE,
    titulo      VARCHAR(200) NOT NULL,
    concluida   BOOLEAN      NOT NULL DEFAULT FALSE
);

-- Índices para deixar as buscas mais rápidas
CREATE INDEX idx_listas_usuario   ON listas (usuario_id);
CREATE INDEX idx_tarefas_usuario  ON tarefas (usuario_id);
CREATE INDEX idx_tarefas_lista    ON tarefas (lista_id);
CREATE INDEX idx_subtarefas_tarefa ON subtarefas (tarefa_id);
