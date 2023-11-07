package com.autosale.shop.repository.impl

import com.autosale.shop.repository.TraceHistoryRepository
import org.jooq.DSLContext
import org.springframework.stereotype.Repository
import structure.tables.TraceHistory
import structure.tables.records.TraceHistoryRecord
import java.time.LocalDateTime

@Repository
class TraceHistoryRepositoryImpl(private val dsl: DSLContext) : TraceHistoryRepository {
    override fun create(message: String) {
        dsl.insertInto(TraceHistory.TRACE_HISTORY)
            .set(TraceHistoryRecord(LocalDateTime.now(), message))
            .execute()
    }

    override fun truncate() {
        dsl.truncate(TraceHistory.TRACE_HISTORY)
            .execute()
    }
}
