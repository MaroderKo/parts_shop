package com.autosale.shop.config

import org.apache.kafka.clients.admin.NewTopic
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.kafka.support.converter.JsonMessageConverter
import org.springframework.kafka.support.converter.RecordMessageConverter

@Configuration
@Profile("Kafka")
class KafkaConfiguration {
    @Bean
    fun salesTopic(): NewTopic {
        return NewTopic("shopping", 1, 1.toShort())
    }

    @Bean
    fun converter(): RecordMessageConverter {
        return JsonMessageConverter()
    }
}
