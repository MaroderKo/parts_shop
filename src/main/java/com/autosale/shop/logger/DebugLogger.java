package com.autosale.shop.logger;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DebugLogger extends AbstractLogger {

    @Override
    public LoggerType getType() {
        return LoggerType.DEBUG;
    }

    @Override
    public void log(String message) {
        log.debug(message);
    }
}
