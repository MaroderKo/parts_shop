package com.autosale.shop.service;

import com.fasterxml.jackson.core.JsonProcessingException;

import java.io.IOException;
import java.util.List;

public interface CsvObjectMapperService {

    //SOLID
    // S - Single responsibility
    // сервіс обджект мапінгу реалізовує тільки свій функціонал по перетворенню об'єктів у CSV і навпаки

    public <T> String encode(List<T> objects, Class<T> type);

    public <T> List<T> decode(String data, Class<T> type);

}
