package com.autosale.shop.controller;

import com.autosale.shop.service.UserSessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/sessions")
@RequiredArgsConstructor
public class SessionController {

    private final UserSessionService userSessionService;

    @PostMapping("/terminate/{id}")
    public ResponseEntity<Void> terminate(@PathVariable int id) {
        if (!userSessionService.terminateSession(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok().build();
    }

}
