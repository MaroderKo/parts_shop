package com.autosale.shop.logger;

import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Value
public class DebugLogger extends AbstractLogger{
    LoggerType type = LoggerType.DEBUG;
    @Override
    public void log(String message) {
        log.debug(message);
    }
}
