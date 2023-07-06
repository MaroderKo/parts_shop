package com.autosale.shop.config;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.EnvironmentVariableCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class BeanConfiguration {

    private final RedisProperties redisProperties;

    @Bean
    public AWSCredentialsProvider awsCredentialsProvider() {
        return new EnvironmentVariableCredentialsProvider();
    }

    @Bean
    public AmazonS3 amazonS3Client(AWSCredentialsProvider credentialsProvider) {
        return AmazonS3ClientBuilder.standard()
                .withCredentials(credentialsProvider)
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CsvMapper csvMapper() {
        return new CsvMapper();
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean
    public RedisClient redisClient() {
        return RedisClient.create(RedisURI.create(redisProperties.getHost(), redisProperties.getPort()));
    }


}
