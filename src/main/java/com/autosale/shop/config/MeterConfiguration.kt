package com.autosale.shop.config

import com.autosale.shop.service.AuthenticationService
import com.autosale.shop.service.ProductService
import io.micrometer.core.instrument.Counter
import io.micrometer.core.instrument.Gauge
import io.micrometer.core.instrument.MeterRegistry
import io.micrometer.core.instrument.binder.MeterBinder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class MeterConfiguration {
    @Bean
    fun meterBinder(productService: ProductService, authenticationService: AuthenticationService) : MeterBinder
    {
        return MeterBinder {
            registry ->
            Gauge.builder("active_products", productService::getActiveProducts).register(registry)
        }
    }

    @Bean
    fun userLoginCounter(meterRegistry: MeterRegistry) : Counter
    {
        return Counter.builder("users_login").register(meterRegistry)
    }


}