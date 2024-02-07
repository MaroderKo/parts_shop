package com.autosale.shop.service.impl

import com.autosale.shop.service.MetricService
import io.micrometer.core.instrument.MeterRegistry
import org.springframework.stereotype.Service

@Service
class MetricServiceImpl(
    val metricRegistry: MeterRegistry
) : MetricService{
    override fun increment(metricName: String) {
        metricRegistry.counter(metricName).increment()
    }

    override fun increment(metricName: String, value: Int) {
        metricRegistry.counter(metricName).increment(value.toDouble())
    }
}