package com.autosale.shop.service;

import com.fasterxml.jackson.core.JsonProcessingException;

import java.io.IOException;
import java.util.List;

public interface CsvObjectMapperService {

    public <T> String encode(List<T> objects, Class<T> type);

    public <T> List<T> decode(String data, Class<T> type);

}
