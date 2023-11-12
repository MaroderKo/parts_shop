package com.autosale.shop.service

import java.time.LocalDate

interface ProductBackupService {
    fun save()
    fun restore(date: LocalDate)
}
