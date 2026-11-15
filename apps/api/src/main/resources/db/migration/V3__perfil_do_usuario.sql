-- ============================================================
--  V3 - Perfil do usuário
--  Adiciona a coluna "papel" (o texto embaixo do nome, ex: "Estudante").
--  ALTER TABLE muda uma tabela que já existe, sem perder os dados.
-- ============================================================

ALTER TABLE usuarios
    ADD COLUMN papel VARCHAR(60) NOT NULL DEFAULT 'Estudante';
