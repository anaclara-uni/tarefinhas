@echo off
REM ============================================================
REM  Tarefinhas - LIGA o projeto (banco + API + site)
REM  Basta dar dois cliques neste arquivo.
REM ============================================================
title Tarefinhas
cd /d "%~dp0"

echo.
echo  ==========================================
echo      TAREFINHAS - ligando o projeto...
echo  ==========================================
echo.

REM --- 1. O Docker esta instalado? ---
where docker >nul 2>nul
if errorlevel 1 goto semDocker

REM --- 2. O Docker Desktop esta aberto? Se nao, abre e espera ---
docker info >nul 2>nul
if not errorlevel 1 goto dockerPronto

echo  O Docker Desktop esta fechado. Abrindo ele para voce...
if exist "%ProgramFiles%\Docker\Docker\Docker Desktop.exe" start "" "%ProgramFiles%\Docker\Docker\Docker Desktop.exe"
echo  Esperando o Docker ficar pronto (pode levar 1 ou 2 minutos)...
set tentativasDocker=0
:esperaDocker
set /a tentativasDocker+=1
if %tentativasDocker% gtr 60 goto dockerNaoAbriu
timeout /t 5 /nobreak >nul
docker info >nul 2>nul
if errorlevel 1 goto esperaDocker

:dockerPronto
echo  Docker pronto!
echo.

REM --- 3. Sobe tudo ---
echo  Subindo banco de dados, API e site...
echo  (Na PRIMEIRA vez demora alguns minutos e precisa de internet.)
echo.
docker compose up -d --build
if errorlevel 1 goto erroCompose

REM --- 4. Espera o site responder ---
echo.
echo  Quase la! Esperando o site responder...
set tentativasSite=0
:esperaSite
set /a tentativasSite+=1
if %tentativasSite% gtr 90 goto siteDemorou
curl.exe -s -f -o nul -H "X-Usuario-Id: 1" http://localhost:4200/api/resumo
if errorlevel 1 (
  timeout /t 2 /nobreak >nul
  goto esperaSite
)

REM --- 5. Abre o navegador ---
start "" http://localhost:4200

echo.
echo  ==========================================
echo      PRONTO! O Tarefinhas esta no ar.
echo  ==========================================
echo.
echo   Site ............ http://localhost:4200
echo   Login de teste .. maria@email.com  /  senha 123456
echo.
echo   Ver o banco ..... http://localhost:8081  (Adminer)
echo                     Sistema: PostgreSQL / Servidor: db
echo                     Usuario: tarefinhas / Senha: tarefinhas / Base: tarefinhas
echo   Ver a API ....... http://localhost:8080/swagger-ui.html
echo.
echo   Pode fechar esta janela. Para DESLIGAR, use o parar.bat
echo.
pause
exit /b 0

:semDocker
echo  [ERRO] O Docker nao esta instalado neste computador.
echo  Instale o Docker Desktop: https://www.docker.com/products/docker-desktop/
echo  Depois reinicie o computador e rode este arquivo de novo.
echo.
pause
exit /b 1

:dockerNaoAbriu
echo  [ERRO] O Docker Desktop nao abriu a tempo.
echo  Abra o Docker Desktop manualmente, espere ficar verde e rode este arquivo de novo.
echo.
pause
exit /b 1

:erroCompose
echo.
echo  [ERRO] Nao consegui subir o projeto. Veja a mensagem acima.
echo  Dica: confira o arquivo docs\PROBLEMAS-COMUNS.md
echo.
pause
exit /b 1

:siteDemorou
echo.
echo  [AVISO] O site esta demorando para responder.
echo  Tente abrir http://localhost:4200 no navegador daqui a pouco.
echo  Se nao abrir, veja docs\PROBLEMAS-COMUNS.md
echo.
pause
exit /b 1
