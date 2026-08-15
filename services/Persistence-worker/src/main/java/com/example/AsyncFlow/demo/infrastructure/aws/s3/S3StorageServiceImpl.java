package com.example.AsyncFlow.demo.infrastructure.aws.s3;

import com.example.AsyncFlow.demo.application.Ports.out.StorageService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.CreateBucketRequest;
import software.amazon.awssdk.services.s3.model.HeadBucketRequest;
import software.amazon.awssdk.services.s3.model.NoSuchBucketException;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.util.Base64;

/**
 * Implementação da porta de saída StorageService.
 * Faz upload de arquivos XML/PDF para o S3 (LocalStack).
 */
@Log4j2
@Service
public class S3StorageServiceImpl implements StorageService {

    private final S3Client s3Client;
    private final String bucketName;
    private final String localstackEndpoint;

    public S3StorageServiceImpl(
            S3Client s3Client,
            @Value("${persistence.s3.bucket-name}") String bucketName,
            @Value("${persistence.s3.endpoint}") String localstackEndpoint) {
        this.s3Client = s3Client;
        this.bucketName = bucketName;
        this.localstackEndpoint = localstackEndpoint;
    }

    @Override
    public String uploadFile(String fileName, String fileBase64) {
        ensureBucketExists();

        byte[] fileBytes = Base64.getDecoder().decode(fileBase64);

        String contentType = resolveContentType(fileName);

        PutObjectRequest putRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(fileName)
                .contentType(contentType)
                .build();

        s3Client.putObject(putRequest, RequestBody.fromBytes(fileBytes));

        String s3Url = buildS3Url(fileName);
        log.info("Arquivo {} enviado para o S3. URL: {}", fileName, s3Url);
        return s3Url;
    }

    // -------------------------------------------------------------------------
    // Métodos auxiliares privados
    // -------------------------------------------------------------------------

    private void ensureBucketExists() {
        try {
            s3Client.headBucket(HeadBucketRequest.builder().bucket(bucketName).build());
        } catch (NoSuchBucketException e) {
            log.info("Bucket {} não encontrado. Criando...", bucketName);
            s3Client.createBucket(CreateBucketRequest.builder().bucket(bucketName).build());
            log.info("Bucket {} criado com sucesso.", bucketName);
        }
    }

    private String resolveContentType(String fileName) {
        if (fileName != null && fileName.toLowerCase().endsWith(".pdf")) {
            return "application/pdf";
        }
        return "application/xml";
    }

    private String buildS3Url(String fileName) {
        // Formato LocalStack: http://localhost:4566/<bucket>/<key>
        return localstackEndpoint + "/" + bucketName + "/" + fileName;
    }
}
