package com.autosale.shop.controller;

import com.autosale.shop.model.User;
import com.autosale.shop.service.UserService;
import org.jooq.exception.DataAccessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private static final Logger LOG = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<User> findAll()
    {
        return userService.findAll();
    }

    @PostMapping("/create")
    public ResponseEntity<Integer> create(@RequestBody User user)
    {
        try {
            return ResponseEntity.ok(userService.create(user));
        }
        catch (DataAccessException e)
        {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }

    @GetMapping("/get/{id}")
    public ResponseEntity<User> findById(@PathVariable int id){
        try {
            return ResponseEntity.ok(userService.findById(id));
        }
        catch (DataAccessException e)
        {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PostMapping("/delete/{id}")
    public ResponseEntity<Integer> delete(@PathVariable int id)
    {
        try {
            return ResponseEntity.ok(userService.delete(id));
        }
        catch (DataAccessException e)
        {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PostMapping("/update")
    public ResponseEntity<Integer> update(@RequestBody User user)
    {
        try {
            return ResponseEntity.ok(userService.edit(user));
        }
        catch (DataAccessException e)
        {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
