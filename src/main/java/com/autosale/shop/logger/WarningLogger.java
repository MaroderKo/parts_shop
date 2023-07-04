package com.autosale.shop.logger;

import lombok.Value;
import lombok.extern.slf4j.Slf4j;

@Value
@Slf4j
public class WarningLogger extends AbstractLogger{
    LoggerType type = LoggerType.WARN;
    @Override
    public void log(String message) {
        log.warn(message);
    }
}
