package com.autosale.shop.service;


import com.autosale.shop.model.User;
import com.autosale.shop.repository.UserRepository;
import com.autosale.shop.repository.impl.UserRepositoryImpl;
import com.autosale.shop.service.impl.UserServiceImpl;
import org.jooq.exception.DataAccessException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static com.autosale.shop.generator.UserGenerator.generate;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;

public class UserServiceTest {
    private final UserRepository repository = Mockito.mock(UserRepositoryImpl.class);

    private final PasswordEncoder passwordEncoder = Mockito.mock(BCryptPasswordEncoder.class);

    private final UserService userService = new UserServiceImpl(repository, passwordEncoder);

    @Test
    void findAll() {
        User user1 = generate();
        User user2 = generate();
        User user3 = generate();

        List<User> users = List.of(user1, user2, user3);
        when(repository.findAll()).thenReturn(users);
        List<User> returned = userService.findAll();

        assertThat(returned, containsInAnyOrder(user1, user2, user3));
    }

    @Test
    void findById() {
        User user = generate();
        when(repository.findById(user.getId())).thenReturn(Optional.of(user));

        User returned = userService.findById(user.getId());
        assertThat(returned, is(user));
    }

    @Test
    void findById_with_illegal_id() {
        when(repository.findById(-1)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> userService.findById(-1));
    }

    @Test
    void create() {
        User user = generate();
        when(passwordEncoder.encode(user.getPassword())).thenReturn("xxxxx");

        when(repository.save(new User(user.getId(), user.getUserName(), "xxxxx", user.getRole()))).thenReturn(Optional.of(user.getId()));

        assertThat(userService.create(user), is(user.getId()));
    }

    @Test
    void create_with_broken_database_connection() {
        User user = generate();
        when(passwordEncoder.encode(user.getPassword())).thenReturn("xxxxx");
        when(repository.save(new User(user.getId(), user.getUserName(), "xxxxx", user.getRole()))).thenThrow(DataAccessException.class);
        assertThrows(DataAccessException.class,
                () -> userService.create(user));
    }

    @Test
    void edit() {
        User user = generate();
        when(passwordEncoder.encode(user.getPassword())).thenReturn("xxxxx");
        userService.edit(user);
        verify(passwordEncoder).encode(user.getPassword());
        verify(repository).update(new User(user.getId(), user.getUserName(), "xxxxx", user.getRole()));
    }

    @Test
    void delete() {
        User user = generate();
        when(repository.deleteById(user.getId())).thenReturn(1);

        assertThat(userService.delete(user.getId()), is(1));
    }
}
