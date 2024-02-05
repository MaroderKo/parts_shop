package com.autosale.shop.config

import com.autosale.shop.service.ProductService
import io.micrometer.core.instrument.Gauge
import io.micrometer.core.instrument.binder.MeterBinder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class MeterConfiguration {
    @Bean
    fun meterBinder(productService: ProductService) : MeterBinder
    {
        return MeterBinder {
            registry ->  Gauge.builder("active_products", productService::getActiveProducts).register(registry)
        }
    }


}