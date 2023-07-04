package com.autosale.shop.logger;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class LoggerFactory {
    private Map<LoggerType, AbstractLogger> loggers;

    @Autowired
    public LoggerFactory(List<AbstractLogger> abstractLoggers)
    {
        loggers = abstractLoggers.stream().collect(Collectors.toMap(AbstractLogger::getType, Function.identity()));
    }

    public AbstractLogger getLogger(LoggerType type) {
        return loggers.get(type);
    }
}
