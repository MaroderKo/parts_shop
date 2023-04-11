package com.autosale.shop.service.impl;

import com.autosale.shop.model.User;
import com.autosale.shop.repository.UserRepository;
import com.autosale.shop.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository repository;
    private final PasswordEncoder encoder;

    @Override
    public List<User> findAll() {
        return repository.findAll();
    }

    @Override
    @Cacheable(value = "user", key = "#id")
    public User findById(int id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cannot find user with id " + id));
    }

    @Override
    public Integer create(User user) {
        return repository.save(copyWithPasswordEncoded(user))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Cannot save user to database"));
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = "user", key = "#user.id"),
            @CacheEvict(value = "userByUsername", key = "#user.userName"),
    })
    public void edit(User user) {
        repository.update(copyWithPasswordEncoded(user));
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = "user", key = "#id"),
            @CacheEvict(value = "userByUsername", allEntries = true)
    })
    public int delete(int id) {
        return repository.deleteById(id);
    }

    @Override
    @Cacheable("userByUsername")
    public User findByUsername(String username) {
        return repository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Bad credentials"));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = repository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUserName())
                .password(user.getPassword())
                .roles(user.getRole().name())
                .build();
    }

    @Override
    public User getVerifiedUser(String username, String password) {
        Optional<User> user = repository.findByUsername(username);
        if (user.isPresent() && encoder.matches(password, user.get().getPassword())) {
            return user.get();
        }
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Bad credentials");

    }
    private User copyWithPasswordEncoded(User user) {
        return new User(user.getId(), user.getUserName(), encoder.encode(user.getPassword()), user.getRole());
    }


}
