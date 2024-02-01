package com.autosale.shop.logger

import lombok.extern.slf4j.Slf4j
import org.slf4j.LoggerFactory

@Slf4j
sealed class Logger(val log: () -> Unit) {

    fun log() {
        log.invoke()
    }

    data class Info(val message: String) : Logger({ slf4jLogger.info(message) })
    data class Debug(val message: String) : Logger({ slf4jLogger.debug(message) })
    data class Warn(val message: String) : Logger({ slf4jLogger.warn(message) })
    data class Error(val message: String) : Logger({ slf4jLogger.error(message) })

    companion object {
        private val slf4jLogger: org.slf4j.Logger = LoggerFactory.getLogger("Logger")
    }


}
