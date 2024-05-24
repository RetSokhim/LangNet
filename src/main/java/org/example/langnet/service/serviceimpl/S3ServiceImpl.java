package org.example.langnet.service.serviceimpl;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.S3Object;
import lombok.extern.log4j.Log4j2;
import org.example.langnet.service.S3Service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@Log4j2
public class S3ServiceImpl implements S3Service {
    private final AmazonS3 s3client;

    @Value("${aws.s3.bucket}")
    private String bucketName;

    @Value("${aws.s3.endpointUrl}")
    private String endpointUrl;

    public S3ServiceImpl(AmazonS3 s3client) {
        this.s3client = s3client;
    }

    @Override
    public String uploadFile(String keyName, MultipartFile file) throws IOException {
        try {
            PutObjectResult putObjectResult = s3client.putObject(bucketName, keyName, file.getInputStream(), null);
            if (putObjectResult != null) {
                log.info("File uploaded successfully with metadata: {}", putObjectResult.getMetadata());
                log.info("File content MD5: {}", putObjectResult.getContentMd5());
                return generateFileUrl(keyName);
            } else {
                log.warn("PutObjectResult is null. File might not have been uploaded properly.");
                throw new IOException("File upload failed, result is null");
            }
        } catch (AmazonS3Exception e) {
            log.error("AmazonS3Exception occurred while uploading file with key: {}", keyName, e);
            throw new IOException("Error occurred while uploading file to S3", e);
        }
    }

    public S3Object getFile(String keyName) {
        try {
            S3Object s3Object = s3client.getObject(bucketName, keyName);
            if (s3Object != null) {
                log.info("Successfully retrieved file with key: {}", keyName);
                return s3Object;
            } else {
                log.warn("S3Object is null. File might not exist with key: {}", keyName);
                return null;
            }
        } catch (AmazonS3Exception e) {
            log.error("Error occurred while retrieving file with key: {}", keyName, e);
            return null;
        }
    }

    @Override
    public void deleteFile(String keyName) {
        try {
            s3client.deleteObject(bucketName, keyName);
            log.info("Successfully deleted file with key: {}", keyName);
        } catch (AmazonS3Exception e) {
            log.error("Error occurred while deleting file with key: {}", keyName, e);
        }
    }

    private String generateFileUrl(String keyName) {
        return String.format("%s/%s/%s", endpointUrl, bucketName, keyName);
    }
}