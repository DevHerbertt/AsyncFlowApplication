package com.example.AsyncFlow.demo.infrastructure.aws.s3;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.net.URI;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Serviço que lista os arquivos armazenados no S3 (LocalStack)
 * para exibição no dashboard de monitoramento.
 */
@Log4j2
@Service
public class S3MonitorService {

    private final String bucketName;
    private final String localstackEndpoint;

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss").withZone(ZoneId.of("America/Sao_Paulo"));

    public S3MonitorService(
            @Value("${persistence.s3.bucket-name:notas-fiscais-bucket}") String bucketName,
            @Value("${monitor.sqs.endpoint:http://localhost:4566}") String localstackEndpoint) {
        this.bucketName = bucketName;
        this.localstackEndpoint = localstackEndpoint;
    }

    /**
     * Lista todos os arquivos do bucket S3.
     */
    public List<S3FileInfo> listFiles() {
        List<S3FileInfo> files = new ArrayList<>();
        try {
            S3Client s3 = buildS3Client();
            ListObjectsV2Response response = s3.listObjectsV2(
                    ListObjectsV2Request.builder().bucket(bucketName).build()
            );
            for (S3Object obj : response.contents()) {
                String url = localstackEndpoint + "/" + bucketName + "/" + obj.key();
                String lastModified = obj.lastModified() != null
                        ? FORMATTER.format(obj.lastModified()) : "-";
                String size = formatSize(obj.size());
                files.add(new S3FileInfo(obj.key(), size, lastModified, url));
            }
            s3.close();
        } catch (NoSuchBucketException e) {
            log.warn("Bucket {} nao encontrado no S3.", bucketName);
        } catch (Exception e) {
            log.warn("Erro ao listar arquivos do S3: {}", e.getMessage());
        }
        return files;
    }

    public String getBucketName() {
        return bucketName;
    }

    // ── Helpers ──────────────────────────────────────────────

    private S3Client buildS3Client() {
        return S3Client.builder()
                .endpointOverride(URI.create(localstackEndpoint))
                .region(Region.US_EAST_1)
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create("test", "test")))
                .forcePathStyle(true)
                .build();
    }

    private String formatSize(Long bytes) {
        if (bytes == null) return "0 B";
        if (bytes < 1024) return bytes + " B";
        if (bytes < 1024 * 1024) return String.format("%.1f KB", bytes / 1024.0);
        return String.format("%.1f MB", bytes / (1024.0 * 1024));
    }

    public record S3FileInfo(
            String nome,
            String tamanho,
            String ultimaModificacao,
            String url
    ) {}
}
