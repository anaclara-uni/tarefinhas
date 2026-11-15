#!/usr/bin/env bash
# Tarefinhas - APAGA tudo do banco e volta os dados de exemplo
cd "$(dirname "$0")"
read -r -p "Isso apaga TODAS as tarefas, listas e contas criadas. Continuar? [s/N] " resposta
[[ "$resposta" =~ ^[sS]$ ]] || exit 0
docker compose down -v
./iniciar.sh
