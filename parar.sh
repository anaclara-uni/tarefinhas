#!/usr/bin/env bash
# Tarefinhas - DESLIGA o projeto (os dados continuam salvos)
cd "$(dirname "$0")"
docker compose down
echo "Pronto, tudo desligado. Os dados continuam salvos."
