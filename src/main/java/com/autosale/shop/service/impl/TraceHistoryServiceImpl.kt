package com.autosale.shop.service.impl

import com.autosale.shop.repository.TraceHistoryRepository
import com.autosale.shop.service.TraceHistoryService
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service

@Service
class TraceHistoryServiceImpl(
    private val repository: TraceHistoryRepository
) : TraceHistoryService {
    override fun log(message: String) {
        repository.create(message)
    }

    @Scheduled(cron = "0 0 0 ? * *")
    fun clear() {
        repository.truncate()
    }
}
