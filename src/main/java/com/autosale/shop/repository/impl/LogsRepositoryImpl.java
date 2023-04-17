package com.autosale.shop.repository.impl;

import com.autosale.shop.repository.LogsRepository;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;
import structure.tables.Logs;
import structure.tables.records.LogsRecord;

import java.time.LocalDateTime;

@Repository
@RequiredArgsConstructor
public class LogsRepositoryImpl implements LogsRepository {

    private final DSLContext dsl;

    @Override
    public void log(String message) {
        dsl.insertInto(Logs.LOGS)
                .set(new LogsRecord(LocalDateTime.now(), message))
                .execute();

    }
}
