package com.autosale.shop.service.impl;

import com.autosale.shop.service.CsvObjectMapperService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvParser;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CsvObjectMapperServiceImpl implements CsvObjectMapperService {

    private final CsvMapper mapper;

    //SOLID
    // O - Open-closed principle
    // Метод працює з абстрактним класом, при зміні типу об'єкту метод не потрыбно переписувати

    @Override
    public <T> String encode(List<T> objects, Class<T> type) {
        CsvSchema schema = mapper.schemaFor(type);
        try {
            return mapper.writer(schema).writeValueAsString(objects);
        }
        catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public <T> List<T> decode(String data, Class<T> type) {
        CsvSchema schema = mapper.schemaFor(type);
        try (MappingIterator<T> it = mapper
                .readerFor(type)
                .with(schema)
                .with(CsvParser.Feature.WRAP_AS_ARRAY)
                .readValues(data))
        {
            return it.readAll();
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
