package com.autosale.shop.model

import org.springframework.http.HttpStatus
import java.sql.Timestamp
import java.time.LocalDateTime

data class ExceptionResponse(
    val timestamp: Timestamp,
    val status: Int,
    val error: HttpStatus,
    val message: String
) {
    constructor(error: HttpStatus, message: String) : this(
        Timestamp.valueOf(LocalDateTime.now()),
        error.value(),
        error,
        message
    )
}
