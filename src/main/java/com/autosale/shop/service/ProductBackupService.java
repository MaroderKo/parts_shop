package com.autosale.shop.service;

import java.time.LocalDate;

public interface ProductBackupService {
    void save();

    void restore(LocalDate date);
}
