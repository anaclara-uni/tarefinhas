-- ============================================================
--  V2 - Dados de exemplo (os mesmos do design)
--  Login de teste:  maria@email.com  /  123456
--
--  As datas usam CURRENT_DATE (o dia de hoje), assim as tarefas
--  "de hoje" sempre aparecem como hoje, não importa quando o
--  banco foi criado. Para recriar os dados, rode resetar-dados.
-- ============================================================

-- Usuária de teste. A senha 123456 está guardada como hash BCrypt.
INSERT INTO usuarios (id, nome, email, senha_hash)
VALUES (1, 'Maria', 'maria@email.com', '$2a$10$753ygN0WtAGcSU/ElM92keFQUGK12t4fUOyM/UQsTc3g2varbxLMK');

-- Listas da Maria (cores iguais às do design)
INSERT INTO listas (id, usuario_id, nome, cor) VALUES
    (1, 1, 'Faculdade', '#E86A92'),
    (2, 1, 'Pessoal',   '#8B6CF0'),
    (3, 1, 'Casa',      '#3DBE85'),
    (4, 1, 'Estágio',   '#F2994A');

-- Tarefas
INSERT INTO tarefas (id, usuario_id, lista_id, titulo, descricao, data_entrega, prioridade, concluida, favorita, lembrete) VALUES
    (1, 1, 1, 'Revisar slides de Engenharia de Software', NULL,                          CURRENT_DATE,      'ALTA',  FALSE, TRUE,  FALSE),
    (2, 1, 4, 'Enviar relatório semanal do estágio',      NULL,                          CURRENT_DATE,      'ALTA',  FALSE, FALSE, FALSE),
    (3, 1, 3, 'Lavar a roupa',                            NULL,                          CURRENT_DATE,      'BAIXA', TRUE,  FALSE, FALSE),
    (4, 1, 1, 'Fazer a lista 5 de Cálculo II',            NULL,                          CURRENT_DATE + 1,  'MEDIA', FALSE, FALSE, TRUE),
    (5, 1, 1, 'Ler o capítulo 4 de Banco de Dados',       NULL,                          CURRENT_DATE + 4,  'BAIXA', TRUE,  FALSE, FALSE),
    (6, 1, 1, 'Montar o diagrama ER do projeto',
              'Entidades: usuário, tarefa e lista. Revisar as cardinalidades com o grupo.', CURRENT_DATE + 5,  'ALTA',  FALSE, TRUE,  TRUE),
    (7, 1, 2, 'Comprar presente de aniversário da Júlia', NULL,                          CURRENT_DATE + 6,  'MEDIA', FALSE, FALSE, FALSE),
    (8, 1, 2, 'Agendar consulta no dentista',             NULL,                          CURRENT_DATE + 11, 'BAIXA', TRUE,  FALSE, FALSE);

-- Subtarefas da tarefa "Montar o diagrama ER do projeto"
INSERT INTO subtarefas (tarefa_id, titulo, concluida) VALUES
    (6, 'Listar as entidades',         TRUE),
    (6, 'Desenhar os relacionamentos', FALSE);

-- Como inserimos os ids "na mão", ajustamos os contadores (sequences)
-- para que o próximo cadastro continue do número certo.
SELECT setval('usuarios_id_seq', (SELECT MAX(id) FROM usuarios));
SELECT setval('listas_id_seq',   (SELECT MAX(id) FROM listas));
SELECT setval('tarefas_id_seq',  (SELECT MAX(id) FROM tarefas));
