package com.dd25.dietiestates25.service.utilityService;

import java.io.IOException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;

import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.ObjectCannedACL;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.core.sync.RequestBody;

@Service
@RequiredArgsConstructor
public class S3Service
{
    private final S3Client s3Client;

    @Value("${BUCKET_NAME}")
    private String bucketName;

    public String uploadFile(MultipartFile file) throws IOException
    {
        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();

        try
        {
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(fileName)
                    .contentType(file.getContentType())
                    .acl(ObjectCannedACL.PUBLIC_READ)
                    .build();

            s3Client.putObject(putObjectRequest, 
                    RequestBody.fromInputStream(file.getInputStream(), file.getSize()));

            return String.format("https://%s.s3.amazonaws.com/%s", bucketName, fileName);

        } catch (IOException e)
        {
            throw new RuntimeException("S3 upload error", e);
        }
    }
}
