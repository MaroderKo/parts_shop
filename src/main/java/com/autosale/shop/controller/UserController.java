package com.autosale.shop.controller;

import com.autosale.shop.model.User;
import com.autosale.shop.service.UserService;
import lombok.RequiredArgsConstructor;
import org.jooq.exception.DataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userServiceImpl;

    @GetMapping
    public List<User> findAll() {
        return userServiceImpl.findAll();
    }

    @PostMapping
    public ResponseEntity<Integer> create(@RequestBody User user) {

        return ResponseEntity.ok(userServiceImpl.create(user));
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> findById(@PathVariable int id) {

        return ResponseEntity.ok(userServiceImpl.findById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Integer> delete(@PathVariable int id) {

        return ResponseEntity.ok(userServiceImpl.delete(id));


    }

    @PutMapping
    public ResponseEntity<Void> update(@RequestBody User user) {

        userServiceImpl.edit(user);
        return ResponseEntity.ok().build();

    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<String> dataAccessExceptionHandle(Exception e) {
        return ResponseEntity.status(501).body(e.getMessage());
    }
}
