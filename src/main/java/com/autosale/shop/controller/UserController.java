package com.autosale.shop.controller;

import com.autosale.shop.model.User;
import com.autosale.shop.service.UserService;
import org.jooq.exception.DataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<User> findAll() {
        return userService.findAll();
    }

    @PostMapping()
    public ResponseEntity<Integer> create(@RequestBody User user) {

        return ResponseEntity.ok(userService.create(user));


    }

    @GetMapping("/{id}")
    public ResponseEntity<User> findById(@PathVariable int id) {

        return ResponseEntity.ok(userService.findById(id));


    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Integer> delete(@PathVariable int id) {

        return ResponseEntity.ok(userService.delete(id));


    }

    @PutMapping
    public ResponseEntity<Integer> update(@RequestBody User user) {

        return ResponseEntity.ok(userService.edit(user));

    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<?> handleException(Exception e) {
        return ResponseEntity.status(501).body(e.getMessage());
    }
}
