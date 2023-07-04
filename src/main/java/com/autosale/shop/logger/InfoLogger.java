package com.autosale.shop.logger;

import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Value
public class InfoLogger extends AbstractLogger{
    LoggerType type = LoggerType.INFO;
    @Override
    public void log(String message) {
        log.info(message);
    }
}
