package com.autosale.shop.service.impl

import com.autosale.shop.service.CsvObjectMapperService
import com.fasterxml.jackson.dataformat.csv.CsvMapper
import com.fasterxml.jackson.dataformat.csv.CsvParser
import org.springframework.stereotype.Service

@Service
class CsvObjectMapperServiceImpl(
    private val mapper: CsvMapper
) : CsvObjectMapperService {
    
    override fun <T> encode(objects: List<T>, type: Class<T>): String {
        val schema = mapper.schemaFor(type)
        return mapper.writer(schema).writeValueAsString(objects)
    }

    override fun <T> decode(data: String, type: Class<T>): List<T> {
        val schema = mapper.schemaFor(type)
        mapper
                .readerFor(type)
                .with(schema)
                .with(CsvParser.Feature.WRAP_AS_ARRAY)
                .readValues<T>(data).use { return it.readAll() }

    }
}
