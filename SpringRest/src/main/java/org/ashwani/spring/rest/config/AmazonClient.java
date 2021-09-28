package org.ashwani.spring.rest.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.ashwani.spring.rest.exceptions.S3ClientNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Objects;

@Service
public class AmazonClient {

    private AmazonS3 amazonS3;
    @Value("${bucket.file-upload-ashwani.accessKey}")
    String accessKey;
    @Value("${bucket.file-upload-ashwani.secretKey}")
    String secretKey;

    @PostConstruct
    public void init() {
        BasicAWSCredentials basicAWSCredentials = new BasicAWSCredentials(accessKey, secretKey);
        this.amazonS3 = AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(basicAWSCredentials)).build();
    }

    public AmazonS3 getS3Client() {
        if (Objects.nonNull(amazonS3)) {
            return amazonS3;
        }
        throw new S3ClientNotFoundException();
    }

}
