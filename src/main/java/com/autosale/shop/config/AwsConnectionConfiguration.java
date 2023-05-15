package com.autosale.shop.config;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AwsConnectionConfiguration {
    private final String accessKey;
    private final String secretKey;
    private final String awsRegion;


    public AwsConnectionConfiguration(@Value("${spring.aws.access-key}")String accessKey, @Value("${spring.aws.secret-key}") String secretKey, @Value("${spring.aws.region}") String awsRegion) {
        this.accessKey = accessKey;
        this.secretKey = secretKey;
        this.awsRegion = awsRegion;
    }

    @Bean
    public AmazonS3 amazonS3Client()
    {
        AWSCredentials credentials = new BasicAWSCredentials(accessKey,secretKey);
        return AmazonS3ClientBuilder
                .standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(awsRegion)
                .build();
    }
}
