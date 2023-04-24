package com.autosale.shop.repository.impl;

import com.autosale.shop.repository.TraceHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;
import structure.tables.SessionHistory;
import structure.tables.records.SessionHistoryRecord;

import java.time.LocalDateTime;

@Repository
@RequiredArgsConstructor
public class TraceHistoryRepositoryImpl implements TraceHistoryRepository {

    private final DSLContext dsl;

    @Override
    public void create(String message) {
        dsl.insertInto(SessionHistory.SESSION_HISTORY)
                .set(new SessionHistoryRecord(LocalDateTime.now(), message))
                .execute();

    }

    @Override
    public void truncate() {
        dsl.truncate(SessionHistory.SESSION_HISTORY).execute();
    }
}
