#!/usr/bin/env bash
# ============================================================
#  Gera o tarefinhas.zip para mandar para outra pessoa.
#  Fica FORA do zip: ref/, node_modules, target, dist, .angular,
#  .git e arquivos do sistema. O zip é criado na pasta de cima.
#  Uso: ./gerar-zip.sh
# ============================================================
set -e
cd "$(dirname "$0")"
PASTA="$(basename "$PWD")"
DESTINO="$(cd .. && pwd)/tarefinhas.zip"

rm -f "$DESTINO"
cd ..
zip -rq "$DESTINO" "$PASTA" \
  -x "$PASTA/ref/*" \
  -x "*/node_modules/*" \
  -x "*/target/*" \
  -x "*/dist/*" \
  -x "*/.angular/*" \
  -x "*/.git/*" \
  -x "*.DS_Store" \
  -x "*.zip"

echo "Zip criado em: $DESTINO"
echo "Tamanho: $(du -h "$DESTINO" | cut -f1)"
