#!/usr/bin/env bash
# ============================================================
#  Tarefinhas - LIGA o projeto (banco + API + site) no Mac/Linux
#  Uso: ./iniciar.sh
# ============================================================
set -e
cd "$(dirname "$0")"

if ! command -v docker >/dev/null 2>&1; then
  echo "[ERRO] Docker não instalado. Baixe em https://www.docker.com/products/docker-desktop/"
  exit 1
fi

if ! docker info >/dev/null 2>&1; then
  echo "O Docker está fechado. Abrindo..."
  [ "$(uname)" = "Darwin" ] && open -a Docker || true
  for _ in $(seq 1 60); do docker info >/dev/null 2>&1 && break; sleep 5; done
fi

echo "Subindo banco de dados, API e site (a primeira vez demora alguns minutos)..."
docker compose up -d --build

echo "Esperando o site responder..."
for _ in $(seq 1 90); do
  curl -sf -o /dev/null -H "X-Usuario-Id: 1" http://localhost:4200/api/resumo && break
  sleep 2
done

URL=http://localhost:4200
if command -v open >/dev/null 2>&1; then open "$URL"; elif command -v xdg-open >/dev/null 2>&1; then xdg-open "$URL"; fi

cat <<'FIM'

  PRONTO! O Tarefinhas está no ar.

  Site ............ http://localhost:4200
  Login de teste .. maria@email.com / senha 123456
  Ver o banco ..... http://localhost:8081 (Adminer: PostgreSQL / db / tarefinhas / tarefinhas / tarefinhas)
  Ver a API ....... http://localhost:8080/swagger-ui.html

  Para desligar: ./parar.sh
FIM
