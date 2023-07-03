package com.autosale.shop.logger;

public class LoggerFactory {
    AbstractLogger getLogger(LoggerType type) {
        switch (type) {
            case INFO -> {
                return new InfoLogger();
            }
            case DEBUG -> {
                return new DebugLogger();
            }
            case ERROR -> {
                return new ErrorLogger();
            }
            default -> throw new IllegalArgumentException();
        }

    }
}
