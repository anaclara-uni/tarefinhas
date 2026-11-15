# Deu problema? Calma 🙂

## "O Docker não está instalado"

Instale o Docker Desktop (veja [COMO-RODAR.md](COMO-RODAR.md)) e **reinicie o computador**.

## O Docker Desktop não abre / fica carregando para sempre

- Reinicie o computador e abra o Docker Desktop de novo.
- Se aparecer algo sobre **WSL**, abra o *Prompt de Comando* e rode: `wsl --update`
- Se aparecer algo sobre **virtualização** ("Virtualization must be enabled"), ela precisa ser ligada
  na BIOS do computador. Pesquise "ativar virtualização" + a marca do notebook.

## A janela preta mostra erro de "port is already allocated" (porta em uso)

Algum outro programa está usando uma das portas (4200, 8080, 8081 ou 5544).

- Feche outros projetos/programas de desenvolvimento e rode o `iniciar.bat` de novo.
- Se não resolver, abra o `docker-compose.yml` e troque o **primeiro** número da porta.
  Exemplo: `"4200:80"` vira `"4300:80"` — aí o site fica em http://localhost:4300.

## A primeira vez falhou no meio do download

Provavelmente a internet caiu. Rode o `iniciar.bat` de novo — ele continua de onde parou.

## O site abre, mas aparece "Não consegui falar com o servidor"

A API ainda está ligando. Espere uns 20 segundos e atualize a página (F5).

## Entrei e as tarefas "de hoje" estão com datas estranhas

As tarefas de exemplo usam a data do dia em que o banco foi criado. Rode o
`resetar-dados.bat` para recriar tudo com a data de hoje.

## Quero ver o que está acontecendo por dentro

No Docker Desktop, vá em **Containers → tarefinhas** e clique em `tarefinhas-api` para ver os logs.
Ou, no Prompt de Comando, dentro da pasta do projeto: `docker compose logs -f api`

## Nada funciona e a apresentação é daqui a pouco 😱

1. Rode o `parar.bat`.
2. Feche e abra o Docker Desktop.
3. Rode o `iniciar.bat`.
4. Se ainda assim não for, rode o `resetar-dados.bat`.
