package com.autosale.shop.security;


import com.autosale.shop.model.User;
import com.autosale.shop.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserDetailService implements UserDetailsService {

    private final UserRepository repository;

    public UserDetailService(UserRepository repository) {
        this.repository = repository;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = repository.findByUsername(username);

        if (user.isEmpty())
            throw new UsernameNotFoundException("User not found");

        org.springframework.security.core.userdetails.User.UserBuilder builder = org.springframework.security.core.userdetails.User.builder();
        builder
                .username(user.get().getUserName())
                .password(user.get().getPassword())
                .roles(user.get().getRole().name());
        return builder.build();
    }
}
