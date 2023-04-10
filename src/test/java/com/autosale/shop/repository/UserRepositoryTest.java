package com.autosale.shop.repository;

import com.autosale.shop.configuration.TestBeanFactory;
import com.autosale.shop.model.User;
import com.autosale.shop.repository.impl.UserRepositoryImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import structure.tables.Users;

import java.util.Optional;

import static com.autosale.shop.generator.UserGenerator.generate;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.is;


public class UserRepositoryTest {
    private final UserRepository repository = new UserRepositoryImpl(TestBeanFactory.testDSLContext());

    @AfterEach
    @BeforeEach
    void clear() {
        TestBeanFactory.testDSLContext().delete(Users.USERS).execute();
    }

    @Test
    void findAll() {
        User user = generate();
        User user1 = generate();
        User user2 = generate();
        repository.save(user);
        repository.save(user1);
        repository.save(user2);
        assertThat(repository.findAll(), containsInAnyOrder(user, user1, user2));
    }

    @Test
    void findById() {
        User user = generate();
        repository.save(user);
        assertThat(repository.findById(user.getId()).isPresent(), is(true));
        assertThat(repository.findById(user.getId()).get(), is(user));

    }

    @Test
    void findByUsername() {
        User user = generate();
        repository.save(user);

        Optional<User> userOptional = repository.findByUsername(user.getUserName());
        assertThat(userOptional.isPresent(), is(true));
        assertThat(userOptional.get(), is(user));
    }

    @Test
    void save() {
        User user = generate();
        Optional<Integer> save = repository.save(user);
        assertThat(save.isPresent(), is(true));
        assertThat(repository.findById(save.get()).get(), is(user));
    }

    @Test
    void update() {
        User user = generate();
        repository.save(user);
        User newUser = new User(user.getId(), "ChangedUsername", user.getPassword(), user.getRole());
        repository.update(newUser);
        assertThat(repository.findById(newUser.getId()).get(), is(newUser));

    }

    @Test
    void deleteById() {
        User user = generate();
        repository.save(user);
        int i = repository.deleteById(user.getId());
        assertThat(i, is(1));
        assertThat(repository.findById(user.getId()).isPresent(), is(false));
    }
}
