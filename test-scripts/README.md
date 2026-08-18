# AsyncFlow - Scripts de Teste

## `send-test-messages.py`

Script java que envia mensagens de teste para as filas SQS do LocalStack, cobrindo os dois workers:

| Worker              | Fila de entrada    | O que testa                                      |
|---------------------|--------------------|--------------------------------------------------|
| validation-worker   | `validacao-vendas` | Valida CNPJ, itens, datas, status                |
| persistence-worker  | `notas-processadas`| Persiste no PostgreSQL + upload XML no S3        |

---

## Pré-requisitos

1. **Docker** rodando com LocalStack e PostgreSQL:
   ```bash
   cd AsyncFlowApplication
   docker-compose up -d
   ```

2. **Python 3.8+** com boto3:
   ```bash
   pip install boto3
   ```

3. **Workers rodando** (em terminais separados):
   - `validation-worker` na porta `8081`
   - `persistence-worker` na porta `8082`

---

## Como executar

```bash
cd AsyncFlowApplication/test-scripts
python send-test-messages.py
```

---

## O que o script faz

1. **Cria as filas** no LocalStack (se não existirem):
   - `validacao-vendas`
   - `notas-processadas`
   - `fila-monitor`

2. **Envia 2 notas para o validation-worker** (`validacao-vendas`):
   - `NF-2026-001` — João da Silva, 2 itens (Notebook + Mouse), sem arquivo
   - `NF-2026-002` — Maria Oliveira, 3 itens (Monitor + Teclado + Headset), **com XML em Base64**

3. **Envia 2 notas para o persistence-worker** (`notas-processadas`):
   - `NF-2026-001` — status `VALIDADA`, sem arquivo → salva no PostgreSQL
   - `NF-2026-002` — status `VALIDADA`, **com XML** → faz upload no S3 + salva no PostgreSQL

---

## Dados de Teste

### Clientes
| CPF           | Nome           |
|---------------|----------------|
| 12345678901   | João da Silva  |
| 98765432100   | Maria Oliveira |

### Itens
| Série   | Produto          | Preço     |
|---------|------------------|-----------|
| SN-001  | Notebook Dell    | R$ 3.500  |
| SN-002  | Mouse Logitech   | R$ 150    |
| SN-003  | Monitor LG 27"   | R$ 1.200  |
| SN-004  | Teclado Mecânico | R$ 450    |
| SN-005  | Headset Sony     | R$ 320    |

### CNPJ de Teste
`11.222.333/0001-81` (válido para validação de dígitos verificadores)

---

## Verificando os resultados

### PostgreSQL
```sql
-- Clientes salvos
SELECT * FROM clientes;

-- Notas fiscais com status e URL do S3
SELECT numero_nota, status, s3_url, cnpj_emitente FROM notas_ficais;

-- Itens salvos
SELECT * FROM itens;
```

### S3 (LocalStack)
```bash
aws --endpoint-url=http://localhost:4566 s3 ls s3://notas-fiscais-bucket/
```

### Fila Monitor
```bash
aws --endpoint-url=http://localhost:4566 sqs receive-message \
  --queue-url http://localhost:4566/000000000000/fila-monitor
```
