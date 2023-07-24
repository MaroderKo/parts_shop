package com.autosale.shop.config;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.EnvironmentVariableCredentialsProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class AwsConfiguration {

    private final AWSCredentialsProvider awsCredentialsProvider = new EnvironmentVariableCredentialsProvider();
    @Value("${cloud.aws.region.static}")
    private String region;

    public AWSCredentialsProvider getCredentials() {
        return awsCredentialsProvider;
    }

    public String getRegion() {
        return region;
    }
}
