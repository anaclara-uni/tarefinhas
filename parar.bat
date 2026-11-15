@echo off
REM ============================================================
REM  Tarefinhas - DESLIGA o projeto (os dados continuam salvos)
REM ============================================================
title Tarefinhas
cd /d "%~dp0"

echo.
echo  Desligando o Tarefinhas...
docker compose down
echo.
echo  Pronto, tudo desligado. Os dados continuam salvos.
echo.
pause
