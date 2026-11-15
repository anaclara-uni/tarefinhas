@echo off
REM ============================================================
REM  Tarefinhas - APAGA tudo do banco e volta os dados de exemplo
REM  (bom para deixar tudo bonitinho antes da apresentacao)
REM ============================================================
title Tarefinhas
cd /d "%~dp0"

echo.
echo  ATENCAO: isso apaga TODAS as tarefas, listas e contas criadas
echo  e volta os dados de exemplo (login maria@email.com / 123456).
echo.
choice /c SN /m " Quer continuar"
if errorlevel 2 exit /b 0

docker compose down -v
call "%~dp0iniciar.bat"
