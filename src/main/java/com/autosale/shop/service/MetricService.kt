package com.autosale.shop.service

interface MetricService {
    fun increment (metricName: String)
    fun increment (metricName: String, value: Int)
}