package com.example.AsyncFlow.test;

import com.example.AsyncFlow.test.model.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.*;

import java.math.BigDecimal;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;

/**
 * ============================================================
 *  AsyncFlow - Script de Teste (Java)
 * ============================================================
 *  Envia mensagens de teste para as filas SQS do LocalStack.
 *
 *  Pré-requisitos:
 *    - Docker rodando: docker-compose up -d
 *    - Maven instalado
 *
 *  Como executar:
 *    cd test-scripts/send-test-messages
 *    mvn package -q
 *    java -jar target/send-test-messages-1.0.0.jar
 * ============================================================
 */
public class SendTestMessages {

    // ── Configuração LocalStack ──────────────────────────────
    private static final String LOCALSTACK_ENDPOINT = "http://localhost:4566";
    private static final String ACCOUNT_ID          = "000000000000";
    private static final Region REGION              = Region.US_EAST_1;

    // ── Nomes das filas ──────────────────────────────────────
    private static final String QUEUE_VALIDATION  = "validacao-vendas";
    private static final String QUEUE_PERSISTENCE = "notas-processadas";
    private static final String QUEUE_MONITOR     = "fila-monitor";

    // ── Dados de teste ───────────────────────────────────────
    private static final ClientDto CLIENTE_1 = new ClientDto("12345678901", "Joao da Silva");
    private static final ClientDto CLIENTE_2 = new ClientDto("98765432100", "Maria Oliveira");

    private static final List<ItemDto> ITENS_1 = List.of(
            new ItemDto("SN-001", "Notebook Dell",    new BigDecimal("3500.00")),
            new ItemDto("SN-002", "Mouse Logitech",   new BigDecimal("150.00"))
    );
    private static final List<ItemDto> ITENS_2 = List.of(
            new ItemDto("SN-003", "Monitor LG 27",    new BigDecimal("1200.00")),
            new ItemDto("SN-004", "Teclado Mecanico", new BigDecimal("450.00")),
            new ItemDto("SN-005", "Headset Sony",     new BigDecimal("320.00"))
    );

    // XML simulado de NF
    private static final String XML_NF = """
            <?xml version="1.0" encoding="UTF-8"?>
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
            </NotaFiscal>""";

    // ────────────────────────────────────────────────────────
    public static void main(String[] args) throws Exception {
        System.out.println("=".repeat(60));
        System.out.println("  AsyncFlow - Script de Teste de Mensagens SQS (Java)");
        System.out.println("=".repeat(60));

        // Configura Jackson com suporte a LocalDateTime
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        // Cria cliente SQS apontando para LocalStack
        SqsClient sqs = SqsClient.builder()
                .endpointOverride(URI.create(LOCALSTACK_ENDPOINT))
                .region(REGION)
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create("test", "test")))
                .build();

        // 1. Garante que as filas existem
        System.out.println("\n[1] Verificando/criando filas no LocalStack...");
        String urlValidation  = getOrCreateQueue(sqs, QUEUE_VALIDATION);
        String urlPersistence = getOrCreateQueue(sqs, QUEUE_PERSISTENCE);
        String urlMonitor     = getOrCreateQueue(sqs, QUEUE_MONITOR);

        // 2. Mensagens para o VALIDATION-WORKER
        System.out.println("\n[2] Enviando mensagens para o VALIDATION-WORKER...");

        NotaFiscalDto nf1Validation = new NotaFiscalDto(
                "NF-2026-001", CLIENTE_1, new BigDecimal("3650.00"),
                ITENS_1, LocalDateTime.now(), "11222333000181",
                "PROCESSANDO", null, null
        );
        sendMessage(sqs, urlValidation, new MessageRequest(nf1Validation), mapper,
                "NF-2026-001 (sem arquivo) -> validation-worker");

        String xmlBase64 = Base64.getEncoder().encodeToString(XML_NF.getBytes());
        NotaFiscalDto nf2Validation = new NotaFiscalDto(
                "NF-2026-002", CLIENTE_2, new BigDecimal("1970.00"),
                ITENS_2, LocalDateTime.now(), "11222333000181",
                "PROCESSANDO", xmlBase64, "nf-2026-002.xml"
        );
        sendMessage(sqs, urlValidation, new MessageRequest(nf2Validation), mapper,
                "NF-2026-002 (com XML)     -> validation-worker");

        // 3. Mensagens para o PERSISTENCE-WORKER
        System.out.println("\n[3] Enviando mensagens para o PERSISTENCE-WORKER...");

        NotaFiscalDto nf1Persistence = new NotaFiscalDto(
                "NF-2026-001", CLIENTE_1, new BigDecimal("3650.00"),
                ITENS_1, LocalDateTime.now(), "11222333000181",
                "VALIDADA", null, null
        );
        sendMessage(sqs, urlPersistence, new MessageRequest(nf1Persistence), mapper,
                "NF-2026-001 (sem arquivo) -> persistence-worker");

        NotaFiscalDto nf2Persistence = new NotaFiscalDto(
                "NF-2026-002", CLIENTE_2, new BigDecimal("1970.00"),
                ITENS_2, LocalDateTime.now(), "11222333000181",
                "VALIDADA", xmlBase64, "nf-2026-002.xml"
        );
        sendMessage(sqs, urlPersistence, new MessageRequest(nf2Persistence), mapper,
                "NF-2026-002 (com XML)     -> persistence-worker");

        System.out.println("\n" + "=".repeat(60));
        System.out.println("  Mensagens enviadas com sucesso!");
        System.out.println("  Acesse o dashboard: http://localhost:8083");
        System.out.println("  Swagger monitor:    http://localhost:8083/swagger-ui.html");
        System.out.println("=".repeat(60));

        sqs.close();
    }

    // ── Helpers ──────────────────────────────────────────────

    private static String getOrCreateQueue(SqsClient sqs, String queueName) {
        String queueUrl = buildQueueUrl(queueName);
        try {
            sqs.getQueueAttributes(GetQueueAttributesRequest.builder()
                    .queueUrl(queueUrl)
                    .attributeNames(QueueAttributeName.APPROXIMATE_NUMBER_OF_MESSAGES)
                    .build());
            System.out.println("  [OK]     Fila existente : " + queueName);
        } catch (Exception e) {
            sqs.createQueue(CreateQueueRequest.builder().queueName(queueName).build());
            System.out.println("  [CRIADA] Fila criada    : " + queueName);
        }
        return queueUrl;
    }

    private static void sendMessage(SqsClient sqs, String queueUrl,
                                    MessageRequest payload, ObjectMapper mapper,
                                    String label) throws Exception {
        String body = mapper.writeValueAsString(payload);
        sqs.sendMessage(SendMessageRequest.builder()
                .queueUrl(queueUrl)
                .messageBody(body)
                .build());
        System.out.println("\n  [ENVIADO] " + label);
        System.out.println("  Fila  : " + queueUrl);
        System.out.println("  Body  : " + body.substring(0, Math.min(body.length(), 120)) + "...");
    }

    private static String buildQueueUrl(String queueName) {
        return LOCALSTACK_ENDPOINT + "/" + ACCOUNT_ID + "/" + queueName;
    }
}
