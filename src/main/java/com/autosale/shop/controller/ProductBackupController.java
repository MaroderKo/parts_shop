package com.autosale.shop.controller;

import com.autosale.shop.service.ProductBackupService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDate;

@Controller
@RequestMapping("/backup")
@RequiredArgsConstructor
public class ProductBackupController {
    private final ProductBackupService service;

    @PostMapping("/save")
    public ResponseEntity<String> save() {
        service.save();
        return ResponseEntity.ok("Backup saved with name " + LocalDate.now());
    }

    @PostMapping("/restore/{backupName}")
    public ResponseEntity<Void> load(@PathVariable("backupName") LocalDate date) {
        service.restore(date);
        return ResponseEntity.ok().build();
    }
}
