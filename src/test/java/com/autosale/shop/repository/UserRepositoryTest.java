package com.autosale.shop.repository;

import com.autosale.shop.configuration.TestBeanFactory;
import com.autosale.shop.model.User;
import com.autosale.shop.repository.impl.UserRepositoryImpl;
import org.apache.naming.factory.BeanFactory;
import org.jooq.impl.DSL;
import org.junit.jupiter.api.*;
import structure.tables.Users;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.autosale.shop.generator.UserGenerator.generate;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;


public class UserRepositoryTest {
    private final UserRepository repository = new UserRepositoryImpl(TestBeanFactory.testDSLContext());

    static User user;

    @AfterEach
    void clear()
    {
        TestBeanFactory.testDSLContext().delete(Users.USERS).execute();
    }

    @BeforeEach
    void setUp()
    {
        user = generate();
    }

    @Test
    void findAll() {
        User user1 = generate();
        User user2 = generate();
        repository.save(user);
        repository.save(user1);
        repository.save(user2);
        assertThat(repository.findAll(), containsInAnyOrder(user, user1, user2));
    }

    @Test
    void findById() {
        repository.save(user);
        assertThat(repository.findById(user.getId()).isPresent(), is(true));
        assertThat(repository.findById(user.getId()).get(), is(user));

    }

    @Test
    void save() {
        Optional<Integer> save = repository.save(user);
        assertThat(save.isPresent(), is(true));
        assertThat(repository.findById(save.get()).get(), is(user));
    }

    @Test
    void update() {
        repository.save(user);

        User newUser = new User(user.getId(), "ChangedUsername", user.getPassword(), user.getRole());
        repository.update(newUser);
        assertThat(repository.findById(newUser.getId()).get(), is(newUser));

    }

    @Test
    void deleteById() {
        repository.save(user);

        int i = repository.deleteById(user.getId());
        assertThat(i, is(1));
        assertThat(repository.findById(user.getId()).isPresent(), is(false));
    }
}
