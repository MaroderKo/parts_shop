package com.autosale.shop.repository.impl;

import com.autosale.shop.repository.TraceHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;
import structure.tables.TraceHistory;
import structure.tables.records.TraceHistoryRecord;

import java.time.LocalDateTime;

@Repository
@RequiredArgsConstructor
public class TraceHistoryRepositoryImpl implements TraceHistoryRepository {

    private final DSLContext dsl;

    @Override
    public void create(String message) {
        dsl.insertInto(TraceHistory.TRACE_HISTORY)
                .set(new TraceHistoryRecord(LocalDateTime.now(), message))
                .execute();

    }

    @Override
    public void truncate() {
        dsl.truncate(TraceHistory.TRACE_HISTORY).execute();
    }
}
