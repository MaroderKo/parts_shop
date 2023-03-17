package com.autosale.shop.service.impl;

import com.autosale.shop.model.User;
import com.autosale.shop.repository.UserRepository;
import com.autosale.shop.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository repository;

    @Override
    public List<User> findAll() {
        return repository.findAll();
    }

    @Override
    public User findById(int id)
    {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Cannot find user with id "+id));
    }

    @Override
    public Integer create(User user) {

        return repository.save(user)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Cannot save user to database"));
    }

    @Override
    public void edit(User user) {
        repository.update(user);
    }

    @Override
    public int delete(int id)
    {
        return repository.deleteById(id);
    }


}
