package com.autosale.shop.logger;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbstractLogger {

    //Абстракція
    //Дозволяє скривати реалізацію, звертаючи увагу на саму структуру об'єкту

    //Abstract class
    //Був створений абстрактний клас у якому продекларовано метод, який має бути імплементований класами що його наслідують
    static {
        log.info("Logger initialised");
    }
    @Getter
    private LoggerType type;

    public abstract void log(String message);
}
