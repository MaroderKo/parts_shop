package com.autosale.shop.logger;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ErrorLogger extends AbstractLogger {

    @Override
    public LoggerType getType() {
        return LoggerType.ERROR;
    }

    @Override
    public void log(String message) {
        log.error(message);
    }
}
