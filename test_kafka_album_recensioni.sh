#!/bin/bash
set -euo pipefail

echo "[1/5] POST album via API Gateway"
ALBUM_JSON=$(curl -s -X POST http://localhost:8080/album/album \
  -H 'Content-Type: application/json' \
  -d '{"titolo":"OK Computer","artista":"Radiohead","generi":["rock","alternative"]}')
echo "$ALBUM_JSON"

ALBUM_ID=$(echo "$ALBUM_JSON" | jq -r '.id')
if [ -z "${ALBUM_ID:-}" ] || [ "$ALBUM_ID" = "null" ]; then
  echo "Errore: ID album non ottenuto dal Gateway"
  exit 1
fi
echo "ID album: $ALBUM_ID"

echo "[2/5] Attesa breve per propagazione evento Kafka"
sleep 1

echo "[3/5] POST recensione via API Gateway"
REC_JSON=$(curl -s -X POST http://localhost:8080/recensioni/recensioni \
  -H 'Content-Type: application/json' \
  -d '{"recensore":"fra","titoloAlbum":"OK Computer","artistaAlbum":"Radiohead","testo":"Ottimo disco, produzione impeccabile.","sunto":"Capolavoro."}')
echo "$REC_JSON"

REC_ID=$(echo "$REC_JSON" | jq -r '.id')
if [ -z "${REC_ID:-}" ] || [ "$REC_ID" = "null" ]; then
  echo "Errore: ID recensione non ottenuto dal Gateway"
  exit 1
fi
echo "ID recensione: $REC_ID"

echo "[4/5] Verifica elenco recensioni via API Gateway"
curl -s http://localhost:8080/recensioni/recensioni | jq

echo "[5/5] Verifica album via API Gateway (lista)"
curl -s http://localhost:8080/album/album | jq

echo "OK: flusso album + recensione via API Gateway completato"