# Como rodar o Tarefinhas (passo a passo)

Este guia é para **Windows**. No Mac é igual, só troque `iniciar.bat` por `./iniciar.sh`.

## O que você precisa

Só do **Docker Desktop**. Ele é um programa que "liga mini-computadores" (chamados *containers*)
já com tudo instalado: o banco de dados, a API e o site.

## 1. Instalar o Docker Desktop (só uma vez)

1. Entre em https://www.docker.com/products/docker-desktop/ e clique em **Download for Windows**.
2. Abra o instalador e vá clicando em **Next / OK**. Deixe marcada a opção **Use WSL 2**.
3. Quando terminar, **reinicie o computador**.
4. Abra o **Docker Desktop**. Na primeira vez ele pode pedir para aceitar os termos — aceite.
   Pode pular o login (botão *Skip* / *Continue without signing in*).
5. Se aparecer um aviso pedindo para **atualizar o WSL**, clique no link do aviso ou abra o
   *Prompt de Comando* e rode `wsl --update`. Depois abra o Docker de novo.

Pronto quando: o ícone da baleia 🐳 perto do relógio fica parado e o Docker Desktop mostra
"Engine running" (ou uma bolinha verde).

## 2. Ligar o projeto

1. Descompacte o `tarefinhas.zip` em uma pasta fácil, por exemplo `Documentos\tarefinhas`.
2. Abra a pasta e dê **dois cliques em `iniciar.bat`**.
   - Se o Windows mostrar "O Windows protegeu o computador", clique em **Mais informações** → **Executar assim mesmo**.
3. Uma janela preta abre e mostra o progresso. **Na primeira vez demora** (5 a 10 minutos),
   porque o Docker baixa e monta tudo. Precisa de internet.
4. Quando terminar, o navegador abre sozinho em **http://localhost:4200**.
5. Entre com **maria@email.com** e senha **123456** (ou crie sua conta).

## 3. Desligar

Dê dois cliques em **`parar.bat`**. Os dados ficam guardados para a próxima vez.

## 4. Voltar aos dados de exemplo

Criou muita coisa de teste? Dê dois cliques em **`resetar-dados.bat`** e confirme com **S**.
Ele apaga tudo e coloca de volta as tarefas de exemplo — com as datas ajustadas para **hoje**.

> 💡 Dica para o dia da apresentação: rode o `resetar-dados.bat` um pouco antes, assim as
> tarefas "de hoje" aparecem certinhas.

## Para quem programa: rodar sem Docker (opcional)

Precisa de Java 21+ e Node 22+.

```bash
docker compose up -d db               # só o banco (porta 5544)
cd apps/api && ./mvnw spring-boot:run # API em http://localhost:8080
cd apps/web && npm install && npm start  # site em http://localhost:4200
```
