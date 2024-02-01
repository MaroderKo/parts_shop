package com.autosale.shop.repository

interface TraceHistoryRepository {
    fun create(message: String)
    fun truncate()
}
