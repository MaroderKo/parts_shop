package com.autosale.shop.service;


import com.autosale.shop.model.User;
import com.autosale.shop.model.UserRole;
import com.autosale.shop.repository.UserRepository;
import com.autosale.shop.repository.impl.UserRepositoryImpl;
import com.autosale.shop.service.impl.UserServiceImpl;
import net.bytebuddy.utility.RandomString;
import org.jooq.exception.DataAccessException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class UserServiceTest {
    private final UserRepository repository = Mockito.mock(UserRepositoryImpl.class);
    private final UserService userService = new UserServiceImpl(repository);

    List<User> users = new ArrayList<>();

    @BeforeEach
    void setUp() {
        Random rand = new Random();
        for (int i = 0; i < 3; i++) {
            users.add(new User(rand.nextInt(100), RandomString.make(50), RandomString.make(25), rand.nextBoolean() ? UserRole.USER : UserRole.ADMIN));
        }
    }

    @AfterEach
    void tearDown() {
        users.clear();
    }

    @Test
    void findAll() {
        when(repository.findAll()).thenReturn(users);
        List<User> returned = userService.findAll();

        assertThat(returned, is(equalTo(users)));
    }

    @Test
    void findById() {
        User user = users.get(0);
        when(repository.findById(user.getId())).thenReturn(Optional.of(user));

        User returned = userService.findById(user.getId());
        assertThat(returned, is(equalTo(user)));
    }

    @Test
    void findById_with_illegal_id() {
        when(repository.findById(-1)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> userService.findById(-1));
    }

    @Test
    void create() {
        User user = users.get(0);
        when(repository.save(user)).thenReturn(Optional.of(user.getId()));

        assertThat(userService.create(user), is(equalTo(user.getId())));
    }

    @Test
    void create_with_broken_database_connection() {
        User user = users.get(0);
        when(repository.save(user)).thenThrow(DataAccessException.class);
        assertThrows(DataAccessException.class,
                () -> userService.create(user));
    }

    @Test
    void edit() {
        User user = users.get(0);
        userService.edit(user);

        verify(repository).update(user);
    }

    @Test
    void delete() {
        User user = users.get(0);
        when(repository.deleteById(user.getId())).thenReturn(1);

        assertThat(userService.delete(user.getId()), is(equalTo(1)));
    }
}
