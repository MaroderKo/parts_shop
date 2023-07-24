package com.autosale.shop.service;

import java.util.List;

public interface CsvObjectMapperService {

    //SOLID
    // S - Single responsibility
    // сервіс обджект мапінгу реалізовує тільки свій функціонал по перетворенню об'єктів у CSV і навпаки

    <T> String encode(List<T> objects, Class<T> type);

    <T> List<T> decode(String data, Class<T> type);

}
