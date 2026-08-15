# AsyncFlow - Script de Teste (Java)

Envia mensagens de teste para as filas SQS do LocalStack para testar o **validation-worker** e o **persistence-worker**.

---

## Pré-requisitos

- **Java 17+**
- **Maven 3.8+**
- **Docker** com LocalStack e PostgreSQL rodando:
  ```bash
  cd AsyncFlowApplication
  docker-compose up -d
  ```

---

## Como executar

```bash
# 1. Entrar na pasta do script
cd AsyncFlowApplication/test-scripts/send-test-messages

# 2. Compilar e empacotar
mvn package -q

# 3. Executar
java -jar target/send-test-messages-1.0.0.jar
```

---

## O que o script faz

1. **Cria as filas** no LocalStack (se não existirem):
   - `validacao-vendas`
   - `notas-processadas`
   - `fila-monitor`

2. **Envia 2 notas para o validation-worker** (`validacao-vendas`):
   - `NF-2026-001` — João da Silva, 2 itens, sem arquivo
   - `NF-2026-002` — Maria Oliveira, 3 itens, **com XML em Base64**

3. **Envia 2 notas para o persistence-worker** (`notas-processadas`):
   - `NF-2026-001` — status `VALIDADA`, sem arquivo
   - `NF-2026-002` — status `VALIDADA`, **com XML** → upload no S3

---

## Dados de Teste

| Campo  | Valor                  |
|--------|------------------------|
| CNPJ   | `11.222.333/0001-81`   |
| CPF 1  | `12345678901` (João)   |
| CPF 2  | `98765432100` (Maria)  |

| Série  | Produto          | Preço     |
|--------|------------------|-----------|
| SN-001 | Notebook Dell    | R$ 3.500  |
| SN-002 | Mouse Logitech   | R$ 150    |
| SN-003 | Monitor LG 27"   | R$ 1.200  |
| SN-004 | Teclado Mecânico | R$ 450    |
| SN-005 | Headset Sony     | R$ 320    |

---

## Verificando os resultados

### Dashboard de monitoramento
```
http://localhost:8083
```

### Swagger dos workers
| Service             | URL                                    |
|---------------------|----------------------------------------|
| nota-api            | http://localhost:8080/swagger-ui.html  |
| validation-worker   | http://localhost:8081/swagger-ui.html  |
| persistence-worker  | http://localhost:8082/swagger-ui.html  |
| monitor-worker      | http://localhost:8083/swagger-ui.html  |

### PostgreSQL
```sql
SELECT * FROM clientes;
SELECT numero_nota, status, s3_url FROM notas_ficais;
SELECT * FROM itens;
```

### S3 (LocalStack)
```bash
aws --endpoint-url=http://localhost:4566 s3 ls s3://notas-fiscais-bucket/
```
