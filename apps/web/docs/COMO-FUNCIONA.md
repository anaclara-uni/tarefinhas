# Frontend (site) — como funciona

O site é feito em **Angular** (versão 22), com **TypeScript** e **SCSS**. Ele mostra as telas
e conversa com a API para buscar e salvar os dados.

## As 4 peças do Angular usadas aqui

| Peça | O que é | Exemplo |
|---|---|---|
| **Componente** | Um pedaço de tela: um `.ts` (lógica), um `.html` (estrutura) e um `.scss` (visual) | `paginas/login/` |
| **Service** | Uma classe que conversa com a API | `servicos/tarefa.service.ts` |
| **Rota** | Qual tela aparece em cada endereço | `app.routes.ts` |
| **Signal** | Uma "caixinha" de valor; quando muda, a tela se atualiza sozinha | `tarefas = signal<Tarefa[]>([])` |

## Pastas

```
apps/web/
├── package.json         ← bibliotecas do projeto (npm)
├── angular.json         ← configurações do Angular (inclui as fontes Fredoka e Nunito)
├── proxy.conf.json      ← no "npm start", manda /api para a API em localhost:8080
├── Dockerfile           ← como montar a imagem Docker do site
├── nginx.conf           ← servidor que entrega o site no Docker e repassa /api para a API
└── src/
    ├── index.html
    ├── styles.scss      ← cores e estilos globais (do guia de estilo do design)
    └── app/
        ├── app.ts / app.config.ts / app.routes.ts   ← "esqueleto" do app
        ├── servicos/        ← conversa com a API e funções de apoio
        │   ├── modelos.ts           ← formatos dos dados (Tarefa, Lista...)
        │   ├── auth.service.ts      ← entrar, cadastrar, sair, perfil
        │   ├── lista.service.ts     ← CRUD de listas
        │   ├── tarefa.service.ts    ← CRUD de tarefas + resumo
        │   ├── usuario.interceptor.ts ← coloca X-Usuario-Id em toda chamada
        │   ├── auth.guard.ts        ← impede entrar nas telas sem login
        │   └── datas.ts             ← "Hoje", "Amanhã", "18 nov"...
        ├── compartilhado/
        │   └── autofoco.ts          ← coloca o cursor no campo quando ele aparece
        └── paginas/
            ├── login/               ← tela Entrar / Criar conta
            ├── tarefas/             ← tela principal
            │   └── componentes/     ← pedaços da tela principal
            │       ├── barra-lateral/
            │       ├── listas-menu/     ← "Minhas listas" (usado no menu e na gaveta do celular)
            │       ├── item-tarefa/
            │       ├── cartao-progresso/
            │       ├── calendario/
            │       ├── notificacoes/
            │       └── proximas-entregas/
            ├── tarefa-form/         ← tela Nova tarefa / Editar tarefa
            └── perfil/              ← tela Perfil (editar dados, sair, excluir conta)
```

## Como o site fala com a API

```
Componente  →  Service  →  HttpClient  →  (interceptor coloca X-Usuario-Id)  →  /api/...  →  API Java
```

Exemplo real (`paginas/tarefas/tarefas.ts`):

```ts
concluir(tarefa: Tarefa) {
  this.tarefaService.alternarConcluida(tarefa.id)   // PATCH /api/tarefas/6/concluir
    .subscribe(() => this.carregarTudo());          // quando responder, recarrega a tela
}
```

`subscribe` quer dizer "quando a resposta chegar, faça isso".

## Como o login funciona no site

1. Na tela de login, `AuthService.entrar()` chama `POST /api/auth/login`.
2. Se der certo, o usuário (`{ id, nome, email }`) é guardado no navegador:
   - **Lembrar de mim** marcado → `localStorage` (continua logado depois de fechar o navegador)
   - desmarcado → `sessionStorage` (sai ao fechar o navegador)
3. O **interceptor** coloca `X-Usuario-Id` em todas as chamadas.
4. O **guard** (`auth.guard.ts`) manda para `/login` quem tentar abrir as tarefas sem estar logado.

## Design 1:1

As telas seguem exatamente o design (cores, tamanhos, espaçamentos e ícones). As cores estão
como variáveis em `src/styles.scss` (ex: `var(--rosa-500)`), iguais ao guia de estilo.
As fontes **Fredoka** (títulos) e **Nunito** (textos) vêm do pacote `@fontsource`, então
funcionam **sem internet**.

Em telas pequenas (até 820px de largura, como um celular), a tela de tarefas troca para o
layout de celular do design, com a barra de navegação embaixo.

## Rodar só o site (sem Docker)

Precisa do Node 22+ e da API rodando em `localhost:8080`.

```bash
cd apps/web
npm install
npm start        # abre em http://localhost:4200
```

Veja também: [TELAS.md](TELAS.md)
