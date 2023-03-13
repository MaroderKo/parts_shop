package com.autosale.shop.service;

import com.autosale.shop.model.User;
import com.autosale.shop.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.jooq.exception.DataAccessException;
import org.springframework.stereotype.Service;
import shop.domain.tables.Users;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;

    public List<User> findAll() {
        return repository.findAll();
    }

    public User findById(int id)
    {
        return repository.findById(id)
                .orElseThrow(() -> new DataAccessException("Cannot find user with id "+id));
    }

    public Integer create(User user) {

        return repository.save(user)
                .orElseThrow(() -> new DataAccessException("Cannot save user to database"))
                .map(record -> record.get(Users.USERS.ID));
    }

    public int edit(User user) {
        return repository.update(user);
    }

    public int delete(int id)
    {
        return repository.deleteById(id);
    }


}
