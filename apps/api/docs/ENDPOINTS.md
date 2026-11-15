# Endpoints da API

Endereço base: `http://localhost:8080` (ou `http://localhost:4200`, que repassa para a API).
Para testar com cliques, use o **Swagger**: http://localhost:8080/swagger-ui.html

Todas as rotas, menos as de `/api/auth`, precisam do cabeçalho **`X-Usuario-Id`** (o id de quem está logado).

## Autenticação

| Método | Rota | O que faz |
|---|---|---|
| POST | `/api/auth/cadastro` | Cria uma conta (já vem com 4 listas) |
| POST | `/api/auth/login` | Entra |

```http
POST /api/auth/login
Content-Type: application/json

{ "email": "maria@email.com", "senha": "123456" }
```
Resposta `200`:
```json
{ "id": 1, "nome": "Maria", "email": "maria@email.com", "papel": "Estudante" }
```

## Perfil (CRUD de usuários)

O **C**reate do usuário é o cadastro acima. O resto fica aqui ("me" = quem está logado):

| Método | Rota | O que faz |
|---|---|---|
| GET | `/api/usuarios/me` | Meus dados |
| PUT | `/api/usuarios/me` | Edita nome, papel e (opcional) senha |
| DELETE | `/api/usuarios/me` | Exclui a conta (e todas as listas e tarefas dela) |

```json
{ "nome": "Maria", "papel": "Estudante de ADS", "novaSenha": "" }
```

## Listas (CRUD)

| Método | Rota | O que faz |
|---|---|---|
| GET | `/api/listas` | Lista as listas com a quantidade de pendentes |
| POST | `/api/listas` | Cria (`{ "nome": "Academia" }` — a cor é escolhida sozinha) |
| PUT | `/api/listas/{id}` | Renomeia (`{ "nome": "Academia 2" }`) |
| DELETE | `/api/listas/{id}` | Exclui (as tarefas dela ficam sem lista) |

Resposta de `GET /api/listas`:
```json
[ { "id": 1, "nome": "Faculdade", "cor": "#E86A92", "pendentes": 3 } ]
```

## Tarefas (CRUD)

| Método | Rota | O que faz |
|---|---|---|
| GET | `/api/tarefas?visao=&listaId=&busca=&data=&ordem=` | Lista com filtros (todos opcionais) |
| GET | `/api/tarefas/{id}` | Uma tarefa |
| POST | `/api/tarefas` | Cria |
| PUT | `/api/tarefas/{id}` | Edita (inclusive as subtarefas) |
| PATCH | `/api/tarefas/{id}/concluir` | Marca / desmarca como concluída |
| PATCH | `/api/tarefas/{id}/favoritar` | Coloca / tira a estrela |
| DELETE | `/api/tarefas/{id}` | Exclui (e as subtarefas junto) |

- `visao`: `todas` (padrão), `hoje`, `semana` (hoje + 7 dias) ou `concluidas`
- `data`: um dia específico, ex: `2026-11-19` (clique no calendário)
- `ordem`: `data` (padrão, data de entrega), `prioridade`, `titulo` (A–Z) ou `recentes`

Corpo para criar/editar:
```json
{
  "titulo": "Montar o diagrama ER do projeto",
  "descricao": "Entidades: usuário, tarefa e lista.",
  "dataEntrega": "2026-11-20",
  "listaId": 1,
  "prioridade": "ALTA",
  "lembrete": true,
  "subtarefas": [
    { "titulo": "Listar as entidades", "concluida": true },
    { "titulo": "Desenhar os relacionamentos", "concluida": false }
  ]
}
```

Resposta (tarefa):
```json
{
  "id": 6, "titulo": "Montar o diagrama ER do projeto", "descricao": "...",
  "dataEntrega": "2026-11-20", "prioridade": "ALTA",
  "concluida": false, "favorita": true, "lembrete": true,
  "listaId": 1, "listaNome": "Faculdade", "listaCor": "#E86A92",
  "subtarefas": [ { "id": 1, "titulo": "Listar as entidades", "concluida": true } ]
}
```

## Resumo (painel)

| Método | Rota | O que faz |
|---|---|---|
| GET | `/api/resumo?mes=2026-11` | Contadores, dias com tarefa no mês, próximas 3 entregas e notificações |

```json
{
  "contagens": { "total": 8, "concluidas": 3, "pendentes": 5, "hoje": 2, "semana": 5 },
  "diasComTarefa": [15, 16, 19, 20, 21, 26],
  "proximasEntregas": [
    { "id": 1, "titulo": "Revisar slides...", "dataEntrega": "2026-11-15", "prioridade": "ALTA", "listaNome": "Faculdade" }
  ],
  "notificacoes": [
    { "id": 1, "titulo": "Revisar slides...", "dataEntrega": "2026-11-15", "tipo": "HOJE" },
    { "id": 4, "titulo": "Fazer a lista 5 de Cálculo II", "dataEntrega": "2026-11-16", "tipo": "LEMBRETE" }
  ]
}
```

## Testando pelo terminal (curl)

```bash
curl -H "X-Usuario-Id: 1" http://localhost:8080/api/tarefas?visao=hoje
```
