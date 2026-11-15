# Roteiro para a apresentação

Uma sugestão de ordem para mostrar o projeto ao professor (uns 10 minutos).

## Os slides

Estão em **`apresentacao/apresentacao.html`** (20 slides, o foco é nas consultas SQL e nos CRUDs).
Setas ← → passam os slides, **F** liga a tela cheia. Sugestão: apresente os slides até o
"Vamos ver funcionando" (slide 19) e então troque para o navegador com o app para a demonstração abaixo.

## Antes de começar

- [ ] Docker Desktop aberto
- [ ] Rodou o `resetar-dados.bat` (dados de exemplo com as datas de hoje)
- [ ] Trocou `[Seu nome] · [Disciplina] · [Professor(a)]` no primeiro slide
- [ ] Slides abertos em `apresentacao/apresentacao.html`
- [ ] Site aberto em http://localhost:4200
- [ ] Abas extras abertas: Adminer (http://localhost:8081) e Swagger (http://localhost:8080/swagger-ui.html)

## 1. Explicar a ideia (1 min)

"O Tarefinhas é um app para organizar tarefas da faculdade, trabalho e casa. Tem login,
listas, tarefas com prioridade, data de entrega e subtarefas."

## 2. Mostrar a arquitetura (1 min)

Abra o [ARQUITETURA.md](ARQUITETURA.md) e mostre o desenho:
**Site (Angular) → API (Java/Spring) → Banco (PostgreSQL)**, tudo no Docker.

## 3. Login e cadastro (1 min)

- Clique em **Criar conta**, cadastre alguém. Mostre que ela já ganha 4 listas.
- Clique no ícone de ajustes (canto de baixo) para abrir o **Perfil**: mude o texto "Estudante",
  salve e veja mudar no menu. Mostre que dá para trocar a senha e **excluir a conta** (CRUD de usuários completo).
- Clique em **Sair da conta** e entre com `maria@email.com` / `123456`.
- Tente uma senha errada para mostrar a mensagem de erro.

## 4. CRUD de tarefas (3 min)

| Operação | Como mostrar |
|---|---|
| **Create** | Digite no campo "Adicionar uma tarefa..." e aperte Enter. Depois use **Nova tarefa** para o formulário completo (descrição, data, lista, prioridade, subtarefas). |
| **Read** | A lista principal, os filtros (Hoje, Próximos 7 dias, Concluídas), as listas da esquerda, a **busca**, o **Ordenar por** e o **clique num dia do calendário**. |
| **Update** | Clique no título de uma tarefa e altere. Marque como concluída e veja o progresso mudar. Favorite com a estrela. |
| **Delete** | Lixeira na tarefa. |

Mostre também o **sino** (canto de cima): ele lista tarefas atrasadas, de hoje e os lembretes de amanhã.
Marque uma delas como concluída e veja ela sumir do sino.

## 5. CRUD de listas (1 min)

- **Nova lista** (embaixo de "Minhas listas").
- Passe o mouse em uma lista → lápis para **renomear**, lixeira para **excluir**.
- Mostre que, ao excluir uma lista, as tarefas dela continuam (ficam "sem lista").

## 6. O banco de dados (2 min)

No **Adminer** (http://localhost:8081):
- Sistema **PostgreSQL**, Servidor **db**, Usuário **tarefinhas**, Senha **tarefinhas**, Base **tarefinhas**.
- Mostre as tabelas `usuarios`, `listas`, `tarefas`, `subtarefas`.
- Clique em **Comando SQL** e rode uma das consultas de [CONSULTAS-SQL.md](../apps/api/docs/CONSULTAS-SQL.md).
- Mostre que a senha está salva **embaralhada** (hash BCrypt), não em texto.

## 7. A API (1 min)

No **Swagger** (http://localhost:8080/swagger-ui.html) mostre a lista de endpoints.
Para testar um, clique nele → **Try it out** → coloque `1` no `X-Usuario-Id` → **Execute**.

## Perguntas que o professor pode fazer

- **Onde estão as consultas SQL?** Em `apps/api/src/main/java/.../*Repository.java` (anotações `@Query`)
  e explicadas em `apps/api/docs/CONSULTAS-SQL.md`.
- **Onde as tabelas são criadas?** Em `apps/api/src/main/resources/db/migration/V1__criar_tabelas.sql`
  (o Flyway roda esse arquivo sozinho).
- **Como a senha é guardada?** Com hash BCrypt (`AuthService.java`).
- **Como o site fala com a API?** Pelos *services* do Angular (`apps/web/src/app/servicos/`), usando HTTP + JSON.
