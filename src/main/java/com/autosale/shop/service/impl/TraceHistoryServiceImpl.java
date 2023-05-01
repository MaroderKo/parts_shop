package com.autosale.shop.service.impl;

import com.autosale.shop.repository.TraceHistoryRepository;
import com.autosale.shop.service.TraceHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TraceHistoryServiceImpl implements TraceHistoryService {

    private final TraceHistoryRepository repository;

    @Override
    public void log(String message) {
        repository.create(message);
    }

    @Scheduled(cron = "0 0 0 ? * *")
    public void clear() {
        repository.truncate();
    }

}
