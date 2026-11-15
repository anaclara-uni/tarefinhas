# 🌸 Tarefinhas

App de tarefas feito para o trabalho da faculdade.

- **Site** em Angular (`apps/web`)
- **API** em Java com Spring Boot (`apps/api`)
- **Banco de dados** PostgreSQL
- Tudo roda dentro do **Docker** — não precisa instalar Java, Node nem Postgres.

---

## ▶️ Como rodar (3 passos)

1. **Instale o Docker Desktop** (só na primeira vez): https://www.docker.com/products/docker-desktop/
2. **Abra o Docker Desktop** e espere ele ficar pronto (ícone da baleia parado).
3. **Dê dois cliques no arquivo `iniciar.bat`** (no Mac: `./iniciar.sh`).

O navegador abre sozinho em **http://localhost:4200**. 🎉

> ⏳ A **primeira vez** demora alguns minutos e precisa de internet (o Docker baixa tudo).
> Das próximas vezes é bem rápido. **Rode uma vez antes do dia da apresentação!**

### Login de teste

| E-mail | Senha |
|---|---|
| `maria@email.com` | `123456` |

### Os outros arquivos de duplo clique

| Arquivo | O que faz |
|---|---|
| `iniciar.bat` | Liga tudo e abre o site |
| `parar.bat` | Desliga tudo (os dados ficam salvos) |
| `resetar-dados.bat` | Apaga tudo e volta os dados de exemplo |

### Endereços úteis

| O quê | Endereço |
|---|---|
| Site | http://localhost:4200 |
| Swagger (testar a API) | http://localhost:8080/swagger-ui.html |
| Adminer (ver o banco) | http://localhost:8081 |

---

## 🎤 Apresentação (slides)

Abra **`apresentacao/apresentacao.html`** com dois cliques (abre no navegador, funciona sem internet).

- **← →** (ou espaço) passam os slides · **F** liga a tela cheia
- Para gerar PDF: **Ctrl+P → Salvar como PDF**
- Antes de apresentar, troque `[Seu nome] · [Disciplina] · [Professor(a)]` no primeiro slide
  (abra o arquivo no Bloco de Notas e procure por `Seu nome`).

## 📚 Documentação

| Assunto | Onde |
|---|---|
| Passo a passo detalhado para rodar | [docs/COMO-RODAR.md](docs/COMO-RODAR.md) |
| Roteiro para a apresentação | [docs/ROTEIRO-APRESENTACAO.md](docs/ROTEIRO-APRESENTACAO.md) |
| Como as peças se conectam | [docs/ARQUITETURA.md](docs/ARQUITETURA.md) |
| Deu problema? | [docs/PROBLEMAS-COMUNS.md](docs/PROBLEMAS-COMUNS.md) |
| Enviar para o GitHub | [docs/ENVIAR-PARA-O-GITHUB.md](docs/ENVIAR-PARA-O-GITHUB.md) |
| Backend (API, banco, SQL) | [apps/api/docs/](apps/api/docs/) |
| Frontend (telas, Angular) | [apps/web/docs/](apps/web/docs/) |

## 🗂️ Pastas

```
tarefinhas/
├── apps/
│   ├── api/      ← backend (Java + Spring Boot)
│   └── web/      ← frontend (Angular)
├── docs/         ← documentação geral
├── apresentacao/ ← slides da apresentação (HTML + CSS)
├── docker-compose.yml   ← "receita" que liga banco + API + site
├── iniciar.bat / parar.bat / resetar-dados.bat   ← Windows
└── iniciar.sh  / parar.sh  / resetar-dados.sh    ← Mac/Linux
```
