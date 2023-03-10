package com.autosale.shop.controller;

import com.autosale.shop.model.User;
import com.autosale.shop.service.UserService;
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
    public List<User> findAll()
    {
        return userService.findAll();
    }

    @PostMapping("/create")
    public Integer create(@RequestBody User user)
    {
        return userService.create(user);
    }

    @PostMapping("/get/{id}")
    public User findById(@PathVariable int id){
        return userService.findById(id);
    }

    @PostMapping("/delete/{id}")
    public int delete(@PathVariable int id)
    {
        return userService.delete(id);
    }

    @PostMapping("/update")
    public int update(@RequestBody User user)
    {
        return userService.edit(user);
    }
}
