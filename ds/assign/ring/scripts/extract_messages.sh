#!/bin/bash

# Ir para o diretorio ds.assig.ring (estando .../Sistemas_Distribuidos)
cd ds/assign/ring/

# Verifica se o número da porta foi fornecido
if [ $# -ne 1 ]; then
    echo "Uso: $0 <port_number>"
    exit 1
fi

PORT_NUMBER="$1"
INPUT_FILE="./logs/CalculatorServer.txt"
OUTPUT_DIR="out"
OUTPUT_FILE="${OUTPUT_DIR}/${PORT_NUMBER}.txt"

# Cria o diretório "out" se não existir
mkdir -p "$OUTPUT_DIR"

# Filtra as mensagens do ficheiro de entrada e as escreve no ficheiro de saída
grep "@${PORT_NUMBER}" "$INPUT_FILE" > "$OUTPUT_FILE"

# Verifica se o comando grep foi bem-sucedido
if [ $? -eq 0 ]; then
    echo "Mensagens para a porta ${PORT_NUMBER} foram guardadas em ${OUTPUT_FILE}"
else
    echo "Não foram encontradas mensagens para a porta ${PORT_NUMBER}"
fi
