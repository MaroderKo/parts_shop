package com.autosale.shop.logger;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ErrorLogger extends AbstractLogger{
    @Override
    public void log(String message) {
        log.error(message);
    }
}
