package com.autosale.shop.controller;

import com.autosale.shop.service.ProductAwsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/backup")
@RequiredArgsConstructor
public class ProductBackupController {
    private final ProductAwsService service;
    @PostMapping("/save")
    public ResponseEntity<Void> save()
    {
        service.save();
        return ResponseEntity.ok().build();
    }
    @PostMapping("/load/{backup_name}")
    public ResponseEntity<Void> load(@PathVariable("backup_name") String backupName)
    {
        service.load(backupName);
        return ResponseEntity.ok().build();
    }
}
