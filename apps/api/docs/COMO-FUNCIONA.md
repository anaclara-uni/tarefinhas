# Backend (API) — como funciona

A API é feita em **Java 21** com **Spring Boot**. Ela recebe pedidos do site, conversa com o
banco PostgreSQL e devolve respostas em **JSON**.

## Analogia do restaurante 🍝

| Camada | No restaurante | No código |
|---|---|---|
| **Controller** | O garçom: recebe o pedido e entrega o prato | `*Controller.java` |
| **Service** | O cozinheiro: segue as regras e prepara | `*Service.java` |
| **Repository** | A despensa: onde busca e guarda os ingredientes | `*Repository.java` |
| **Entity** | O ingrediente: uma linha de uma tabela | `Usuario`, `Lista`, `Tarefa`, `Subtarefa` |
| **DTO** | O cardápio: o formato do que entra e sai | `*Dtos.java`, `record`s |

Um pedido faz o caminho: **Controller → Service → Repository → Banco** e volta.

## Pastas

```
apps/api/
├── pom.xml                          ← lista de bibliotecas (dependências) do Maven
├── Dockerfile                       ← como montar a imagem Docker da API
├── docs/                            ← você está aqui
└── src/main/
    ├── resources/
    │   ├── application.properties   ← configurações (banco, porta...)
    │   └── db/migration/            ← arquivos SQL que criam as tabelas (Flyway)
    │       ├── V1__criar_tabelas.sql
    │       ├── V2__dados_exemplo.sql
    │       └── V3__perfil_do_usuario.sql
    └── java/br/com/tarefinhas/
        ├── TarefinhasApplication.java   ← o "main" que liga tudo
        ├── usuario/   ← cadastro, login e perfil (ver, editar, excluir conta)
        ├── lista/     ← CRUD de listas
        ├── tarefa/    ← CRUD de tarefas e subtarefas
        ├── resumo/    ← números do painel (consultas SQL de relatório)
        └── erro/      ← transforma erros em mensagens amigáveis
```

O código está separado **por assunto**: tudo de "tarefa" fica na pasta `tarefa/`, e assim por diante.

## Bibliotecas usadas (pom.xml)

| Biblioteca | Para quê |
|---|---|
| `spring-boot-starter-webmvc` | Criar a API REST (endpoints, JSON, servidor) |
| `spring-boot-starter-data-jpa` | Conversar com o banco usando classes Java |
| `spring-boot-starter-validation` | Validar dados (`@NotBlank`, `@Email`...) |
| `spring-boot-starter-flyway` | Rodar os arquivos `.sql` que criam as tabelas |
| `postgresql` | Driver do PostgreSQL |
| `spring-security-crypto` | Só o **BCrypt**, para guardar a senha com hash |
| `springdoc-openapi` | Página do **Swagger** para ver e testar a API |
| `lombok` | Gera getters e setters sozinho (`@Getter`, `@Setter`) |

## Como as tabelas são criadas? (Flyway)

Quando a API liga, o **Flyway** olha a pasta `db/migration` e roda, em ordem, os arquivos que ainda
não rodaram:

1. `V1__criar_tabelas.sql` → cria as tabelas
2. `V2__dados_exemplo.sql` → coloca a Maria e as tarefas de exemplo
3. `V3__perfil_do_usuario.sql` → adiciona a coluna `papel` em `usuarios` (com `ALTER TABLE`)

Ele anota o que já rodou numa tabela chamada `flyway_schema_history`, então não roda duas vezes.

## Exemplo completo: marcar uma tarefa como concluída

1. O site chama `PATCH /api/tarefas/6/concluir` com o cabeçalho `X-Usuario-Id: 1`.
2. `TarefaController.concluir()` recebe e chama o service.
3. `TarefaService.alternarConcluida()` busca a tarefa **6 da usuária 1**
   (`findByIdAndUsuarioId`). Se não achar → erro 404 "Tarefa não encontrada.".
4. Inverte o `concluida` (true ↔ false) e salva com `tarefaRepository.save()`.
5. Devolve a tarefa atualizada como JSON (`TarefaResponse`).

## Erros

Os services lançam `ResponseStatusException` com uma mensagem em português. O `TratadorDeErros`
transforma isso em `{ "mensagem": "..." }`, que o site mostra na tela.

| Situação | Código HTTP | Mensagem |
|---|---|---|
| Senha errada | 401 | E-mail ou senha incorretos. |
| E-mail já cadastrado | 409 | Esse e-mail já está cadastrado. |
| Título vazio | 400 | A tarefa precisa de um título. |
| Tarefa de outra pessoa / inexistente | 404 | Tarefa não encontrada. |
| Sem o cabeçalho `X-Usuario-Id` | 401 | Faça login para continuar. |

Veja também: [BANCO-DE-DADOS.md](BANCO-DE-DADOS.md) · [CONSULTAS-SQL.md](CONSULTAS-SQL.md) · [ENDPOINTS.md](ENDPOINTS.md)
