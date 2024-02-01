package com.autosale.shop.service

interface CsvObjectMapperService {
    fun <T> encode(objects: List<T>, type: Class<T>): String
    fun <T> decode(data: String, type: Class<T>): List<T>
}
