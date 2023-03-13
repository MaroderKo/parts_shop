package com.autosale.shop.controller;

import com.autosale.shop.model.User;
import com.autosale.shop.service.impl.UserServiceImpl;
import org.jooq.exception.DataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserServiceImpl userServiceImpl;

    public UserController(UserServiceImpl userServiceImpl) {
        this.userServiceImpl = userServiceImpl;
    }

    @GetMapping
    public List<User> findAll() {
        return userServiceImpl.findAll();
    }

    @PostMapping()
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
    public ResponseEntity<Integer> update(@RequestBody User user) {

        return ResponseEntity.ok(userServiceImpl.edit(user));

    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<?> handleException(Exception e) {
        return ResponseEntity.status(501).body(e.getMessage());
    }
}
