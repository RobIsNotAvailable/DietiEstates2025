package com.dd25.dietiestates25.service.utilityservice;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;

import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.core.sync.RequestBody;

@Service
@RequiredArgsConstructor
public class S3Service
{
    private final S3Client s3Client;
    private final S3Presigner s3Presigner;

    @Value("${BUCKET_NAME}")
    private String bucketName;
    @Value("${AWS_REGION}")
    private String region;

    public String uploadFile(MultipartFile file)
    {
        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();

        try
        {
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(fileName)
                    .contentType(file.getContentType())
                    .build();

            s3Client.putObject(putObjectRequest, 
                    RequestBody.fromInputStream(file.getInputStream(), file.getSize()));

            return String.format("https://%s.s3.%s.amazonaws.com/%s", bucketName, region, fileName);    
        } catch (IOException e)
        {
            throw new UncheckedIOException("S3 upload error", e);
        }
    }

    public String generatePresignedUrl(String fullUrlOrKey) 
    {
        if (fullUrlOrKey == null || fullUrlOrKey.isEmpty()) 
        {
            return null;
        }

        String objectKey = fullUrlOrKey.contains("/") 
                ? fullUrlOrKey.substring(fullUrlOrKey.lastIndexOf("/") + 1) 
                : fullUrlOrKey;

        try 
        {
            return s3Presigner.presignGetObject(presignRequest -> presignRequest
                        .signatureDuration(java.time.Duration.ofMinutes(60))
                        .getObjectRequest(getRequest -> getRequest
                                .bucket(bucketName)
                                .key(objectKey))
                ).url().toString();
        } 
        catch (Exception e) 
        {
            return null;
        }
    }
}
