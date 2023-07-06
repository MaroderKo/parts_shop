package com.autosale.shop.logger;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component

public class InfoLogger extends AbstractLogger {

    @Override
    public LoggerType getType() {
        return LoggerType.INFO;
    }

    @Override
    public void log(String message) {
        log.info(message);
    }
}
