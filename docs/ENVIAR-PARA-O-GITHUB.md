# Enviar o projeto para o GitHub

Faça isso **no seu computador**, depois que o projeto já estiver funcionando nele.

> A pasta `ref/` (referências do design) **nunca vai para o GitHub** — ela está no arquivo
> `.gitignore`, então o Git ignora ela sozinho. Também ficam de fora `node_modules`, `target` etc.

## 1. Instalar o Git (só uma vez)

Baixe em https://git-scm.com/download/win e instale clicando em **Next** em tudo.

## 2. Dizer ao Git quem você é (só uma vez)

Abra o **Git Bash** (instalado junto com o Git) e rode, trocando pelos seus dados:

```bash
git config --global user.name "Seu Nome"
git config --global user.email "seu-email-do-github@exemplo.com"
```

## 3. Criar o repositório no GitHub

1. Entre em https://github.com e faça login.
2. Clique em **New** (ou no **+** lá em cima → *New repository*).
3. Nome: `tarefinhas`. **Não** marque "Add a README" (o projeto já tem um).
4. Clique em **Create repository** e deixe a página aberta.

## 4. Enviar os arquivos

No Git Bash, entre na pasta do projeto (dica: clique com o botão direito dentro da pasta →
*Open Git Bash here*) e rode um comando de cada vez:

```bash
git init
git add .
git commit -m "Primeira versão do Tarefinhas"
git branch -M main
git remote add origin https://github.com/SEU-USUARIO/tarefinhas.git
git push -u origin main
```

Troque `SEU-USUARIO` pelo seu usuário do GitHub (o endereço aparece na página do passo 3).
No `git push`, uma janela pede para entrar na sua conta do GitHub — é só autorizar.

## 5. Conferir

Atualize a página do repositório no GitHub. Os arquivos devem aparecer — e a pasta `ref` **não**.

## Depois, quando mudar alguma coisa

```bash
git add .
git commit -m "Explique aqui o que mudou"
git push
```
