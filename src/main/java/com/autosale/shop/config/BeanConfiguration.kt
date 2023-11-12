package com.autosale.shop.config

import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.AmazonS3ClientBuilder
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.csv.CsvMapper
import io.lettuce.core.RedisClient
import io.lettuce.core.RedisURI
import org.springframework.boot.autoconfigure.data.redis.RedisProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder

@Configuration
class BeanConfiguration(
    private val redisProperties: RedisProperties,
    private val awsConfiguration: AwsConfiguration
) {

    @Bean
    fun amazonS3Client(): AmazonS3 {
        return AmazonS3ClientBuilder.standard()
            .withCredentials(awsConfiguration!!.credentials)
            .withCredentials(awsConfiguration.credentials)
            .withRegion(awsConfiguration.region)
            .build()
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    fun csvMapper(): CsvMapper {
        return CsvMapper()
    }

    @Bean
    fun objectMapper(): ObjectMapper {
        return ObjectMapper()
    }

    @Bean
    fun redisClient(): RedisClient {
        return RedisClient.create(RedisURI.create(redisProperties.host, redisProperties.port))
    }
}
