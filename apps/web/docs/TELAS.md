# Telas do site

## 1. Entrar / Criar conta — `/login`

**Arquivos:** `src/app/paginas/login/`

| O que tem | O que faz | Chamada na API |
|---|---|---|
| Abas Entrar / Criar conta | Troca o formulário | — |
| Botão Entrar | Faz login | `POST /api/auth/login` |
| Botão Criar conta | Cadastra (e já entra) | `POST /api/auth/cadastro` |
| Olhinho na senha | Mostra/esconde a senha | — |
| Lembrar de mim | Continua logado depois de fechar o navegador | — |

## 2. Minhas tarefas — `/tarefas`

**Arquivos:** `src/app/paginas/tarefas/` + `componentes/`

| Parte da tela | Componente | Chamada na API |
|---|---|---|
| Menu lateral (visões, dica do dia, usuário) | `barra-lateral` | — |
| Minhas listas (criar, renomear, excluir) | `listas-menu` | `GET /api/listas`, `POST/PUT/DELETE /api/listas` |
| Busca | `tarefas` | `GET /api/tarefas?busca=...` |
| Sino de notificações | `notificacoes` | `GET /api/resumo` |
| Adicionar rápido | `tarefas` | `POST /api/tarefas` |
| Filtros Todas / Pendentes / Concluídas | `tarefas` | — (filtra o que já veio) |
| Ordenar por | `tarefas` | `GET /api/tarefas?ordem=...` |
| Cada linha de tarefa | `item-tarefa` | `PATCH .../concluir`, `PATCH .../favoritar`, `DELETE /api/tarefas/{id}` |
| Seu progresso | `cartao-progresso` | `GET /api/resumo` |
| Calendário | `calendario` | `GET /api/resumo?mes=...` e `GET /api/tarefas?data=...` |
| Próximas entregas | `proximas-entregas` | `GET /api/resumo` |

Detalhes:
- **Clicar no título** de uma tarefa abre a edição.
- **Hoje / Próximos 7 dias / Todas / Concluídas** filtram a lista. O número ao lado mostra as
  tarefas **pendentes** (Todas e Concluídas mostram o total).
- **Minhas listas:** clique para filtrar. Passe o mouse para ver os botões de **renomear** e **excluir**.
  **Nova lista** abre um campo: digite e aperte Enter (Esc cancela).
- **Ordenar por:** clique em "data de entrega" para escolher entre data de entrega, prioridade,
  nome (A–Z) ou mais recentes.
- **Calendário:** clique em um dia para ver só as tarefas daquele dia (aparece uma faixa rosa com
  "Ver todas"). Clicar no mesmo dia de novo desmarca. Com um dia escolhido, o "adicionar rápido"
  cria a tarefa naquele dia.
- **Dica do dia:** muda a cada dia. Clique nela para ver outra dica.
- **Ícone de ajustes** (ao lado do nome, embaixo) abre o **Perfil**. O texto embaixo do nome
  ("Estudante") é editado lá.
- **Sino de notificações:** a bolinha rosa aparece quando há algo para avisar. Ao clicar, abre uma
  caixinha com as tarefas pendentes **atrasadas**, que **vencem hoje**, e os **lembretes** (tarefas com
  "Me lembrar" ligado que vencem amanhã). Clicar num item abre a tarefa. Esc ou clicar fora fecha.
- **No celular:** tem busca, os botões Hoje / Semana / Feitas, estrela e lixeira em cada cartão,
  e a barra de baixo: Hoje, Agenda (7 dias), **+** (nova tarefa), **Listas** (abre a gaveta com as
  listas para filtrar, criar, renomear e excluir) e **Perfil**. Quando uma lista está filtrada,
  aparece um botão com o nome dela; tocar nele tira o filtro.

## 3. Nova tarefa / Editar tarefa — `/tarefas/nova` e `/tarefas/{id}/editar`

**Arquivos:** `src/app/paginas/tarefa-form/`

| Campo | Observação |
|---|---|
| Título | Obrigatório |
| Descrição | Opcional |
| Data de entrega | Começa com a data de hoje |
| Lista | As listas da pessoa + "Sem lista" |
| Prioridade | Baixa, Média ou Alta |
| Subtarefas | Marque, remova (×) ou adicione (digite e aperte Enter) |
| Me lembrar | Um dia antes da entrega, a tarefa aparece no sino de notificações |

Botões: **Salvar tarefa** (`POST` ou `PUT /api/tarefas`), **Cancelar**, e na edição também **Excluir**.

## 4. Perfil — `/perfil`

**Arquivos:** `src/app/paginas/perfil/`

Abre pelo ícone de ajustes (computador) ou pelo avatar / botão "Perfil" (celular).

| O que tem | O que faz | Chamada na API |
|---|---|---|
| Nome | Muda o nome ("Oi, Maria!") | `PUT /api/usuarios/me` |
| Embaixo do nome | Muda o texto embaixo do nome no menu (ex: "Estudante") | `PUT /api/usuarios/me` |
| Nova senha | Troca a senha (em branco = não muda; mínimo 6 caracteres) | `PUT /api/usuarios/me` |
| Sair da conta | Volta para a tela de entrar | — |
| Excluir conta | Apaga a conta e todas as listas e tarefas dela | `DELETE /api/usuarios/me` |

## O que é só visual

- O link **"Esqueci minha senha"** da tela de login (recuperar senha exigiria envio de e-mail).
