package com.autosale.shop.config

import com.amazonaws.auth.AWSCredentialsProvider
import com.amazonaws.auth.EnvironmentVariableCredentialsProvider
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration

@Configuration
data class AwsConfiguration(
    @Value("\${cloud.aws.region.static}")
    val region: String
)
{
        val credentials: AWSCredentialsProvider = EnvironmentVariableCredentialsProvider()
}