package com.autosale.shop.repository;

public interface TraceHistoryRepository {

    void create(String message);

    void truncate();

}
