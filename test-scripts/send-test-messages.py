"""
Script de Teste - AsyncFlow
============================
Envia mensagens de teste para as filas SQS do LocalStack para testar:
  - validation-worker  (fila: validacao-vendas)
  - persistence-worker (fila: notas-processadas)

Pré-requisitos:
  pip install boto3
  Docker rodando com LocalStack (docker-compose up -d)

Uso:
  python send-test-messages.py
"""

import boto3
import json
import base64
from datetime import datetime

# ─────────────────────────────────────────────
# Configuração do LocalStack
# ─────────────────────────────────────────────
LOCALSTACK_ENDPOINT = "http://localhost:4566"
REGION = "us-east-1"
AWS_ACCESS_KEY = "test"
AWS_SECRET_KEY = "test"

# Nomes das filas
QUEUE_VALIDATION  = "validacao-vendas"    # validation-worker consome desta fila
QUEUE_PERSISTENCE = "notas-processadas"   # persistence-worker consome desta fila
QUEUE_MONITOR     = "fila-monitor"        # persistence-worker publica aqui

# ─────────────────────────────────────────────
# Cliente SQS
# ─────────────────────────────────────────────
sqs = boto3.client(
    "sqs",
    endpoint_url=LOCALSTACK_ENDPOINT,
    region_name=REGION,
    aws_access_key_id=AWS_ACCESS_KEY,
    aws_secret_access_key=AWS_SECRET_KEY,
)


def get_or_create_queue(queue_name: str) -> str:
    """Cria a fila se não existir e retorna a URL."""
    try:
        response = sqs.get_queue_url(QueueName=queue_name)
        url = response["QueueUrl"]
        print(f"[OK] Fila existente: {queue_name} -> {url}")
        return url
    except sqs.exceptions.QueueDoesNotExist:
        response = sqs.create_queue(QueueName=queue_name)
        url = response["QueueUrl"]
        print(f"[CRIADA] Fila criada: {queue_name} -> {url}")
        return url


def send_message(queue_url: str, payload: dict, label: str):
    """Envia uma mensagem JSON para a fila."""
    body = json.dumps(payload, ensure_ascii=False, default=str)
    sqs.send_message(QueueUrl=queue_url, MessageBody=body)
    print(f"\n[ENVIADO] {label}")
    print(f"  Fila : {queue_url}")
    print(f"  Body : {body[:200]}{'...' if len(body) > 200 else ''}")


# ─────────────────────────────────────────────
# Dados de Teste
# ─────────────────────────────────────────────

# Cliente de teste
CLIENTE_1 = {"cpf": "12345678901", "name": "João da Silva"}
CLIENTE_2 = {"cpf": "98765432100", "name": "Maria Oliveira"}

# Itens de teste
ITENS_1 = [
    {"serieNumber": "SN-001", "name": "Notebook Dell", "price": 3500.00},
    {"serieNumber": "SN-002", "name": "Mouse Logitech", "price": 150.00},
]
ITENS_2 = [
    {"serieNumber": "SN-003", "name": "Monitor LG 27\"", "price": 1200.00},
    {"serieNumber": "SN-004", "name": "Teclado Mecânico", "price": 450.00},
    {"serieNumber": "SN-005", "name": "Headset Sony",    "price": 320.00},
]

# Nota Fiscal 1 - para validation-worker (sem arquivo)
NOTA_1_VALIDATION = {
    "notaFiscal": {
        "numeroNota": "NF-2026-001",
        "client": CLIENTE_1,
        "totalValue": 3650.00,
        "itens": ITENS_1,
        "buyDate": datetime.now().isoformat(),
        "cnpj": "11222333000181",   # CNPJ válido para teste
        "status": "PROCESSANDO",
        "fileBase64": None,
        "fileName": None,
    }
}

# Nota Fiscal 2 - para validation-worker (com arquivo XML simulado)
XML_CONTENT = b"""<?xml version="1.0" encoding="UTF-8"?>
<NotaFiscal>
  <numero>NF-2026-002</numero>
  <emitente>
    <cnpj>11222333000181</cnpj>
    <nome>Empresa Teste LTDA</nome>
  </emitente>
  <destinatario>
    <cpf>98765432100</cpf>
    <nome>Maria Oliveira</nome>
  </destinatario>
  <itens>
    <item><serie>SN-003</serie><descricao>Monitor LG 27</descricao><valor>1200.00</valor></item>
    <item><serie>SN-004</serie><descricao>Teclado Mecanico</descricao><valor>450.00</valor></item>
    <item><serie>SN-005</serie><descricao>Headset Sony</descricao><valor>320.00</valor></item>
  </itens>
  <total>1970.00</total>
</NotaFiscal>"""

NOTA_2_VALIDATION = {
    "notaFiscal": {
        "numeroNota": "NF-2026-002",
        "client": CLIENTE_2,
        "totalValue": 1970.00,
        "itens": ITENS_2,
        "buyDate": datetime.now().isoformat(),
        "cnpj": "11222333000181",
        "status": "PROCESSANDO",
        "fileBase64": base64.b64encode(XML_CONTENT).decode("utf-8"),
        "fileName": "nf-2026-002.xml",
    }
}

# Nota Fiscal 1 - para persistence-worker (já validada)
NOTA_1_PERSISTENCE = {
    "notaFiscal": {
        "numeroNota": "NF-2026-001",
        "client": CLIENTE_1,
        "totalValue": 3650.00,
        "itens": ITENS_1,
        "buyDate": datetime.now().isoformat(),
        "cnpj": "11222333000181",
        "status": "VALIDADA",
        "fileBase64": None,
        "fileName": None,
    }
}

# Nota Fiscal 2 - para persistence-worker (já validada, com arquivo XML)
NOTA_2_PERSISTENCE = {
    "notaFiscal": {
        "numeroNota": "NF-2026-002",
        "client": CLIENTE_2,
        "totalValue": 1970.00,
        "itens": ITENS_2,
        "buyDate": datetime.now().isoformat(),
        "cnpj": "11222333000181",
        "status": "VALIDADA",
        "fileBase64": base64.b64encode(XML_CONTENT).decode("utf-8"),
        "fileName": "nf-2026-002.xml",
    }
}


# ─────────────────────────────────────────────
# Main
# ─────────────────────────────────────────────
def main():
    print("=" * 60)
    print("  AsyncFlow - Script de Teste de Mensagens SQS")
    print("=" * 60)

    # Garante que as filas existem
    print("\n[1] Verificando/criando filas no LocalStack...")
    url_validation  = get_or_create_queue(QUEUE_VALIDATION)
    url_persistence = get_or_create_queue(QUEUE_PERSISTENCE)
    url_monitor     = get_or_create_queue(QUEUE_MONITOR)

    # ── Mensagens para o VALIDATION-WORKER ──────────────────
    print("\n[2] Enviando mensagens para o VALIDATION-WORKER...")
    send_message(url_validation, NOTA_1_VALIDATION,
                 "NF-2026-001 (sem arquivo) -> validation-worker")
    send_message(url_validation, NOTA_2_VALIDATION,
                 "NF-2026-002 (com XML)     -> validation-worker")

    # ── Mensagens para o PERSISTENCE-WORKER ─────────────────
    print("\n[3] Enviando mensagens para o PERSISTENCE-WORKER...")
    send_message(url_persistence, NOTA_1_PERSISTENCE,
                 "NF-2026-001 (sem arquivo) -> persistence-worker")
    send_message(url_persistence, NOTA_2_PERSISTENCE,
                 "NF-2026-002 (com XML)     -> persistence-worker")

    print("\n" + "=" * 60)
    print("  Mensagens enviadas com sucesso!")
    print("  Verifique os logs dos workers para acompanhar o processamento.")
    print("=" * 60)


if __name__ == "__main__":
    main()
