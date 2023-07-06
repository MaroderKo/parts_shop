package com.autosale.shop.logger;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class LoggerFactory {
    private final Map<LoggerType, AbstractLogger> loggers;

    public LoggerFactory(List<AbstractLogger> abstractLoggers)
    {
        loggers = abstractLoggers.stream().collect(Collectors.toMap(AbstractLogger::getType, Function.identity()));
    }

    public AbstractLogger getLogger(LoggerType type) {
        return Optional.ofNullable(loggers.get(type)).orElseThrow(() -> new RuntimeException("Logger with name %s not found!".formatted(type.name())));
    }
}
