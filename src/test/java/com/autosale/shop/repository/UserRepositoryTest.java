package com.autosale.shop.repository;

import com.autosale.shop.configuration.TestBeanFactory;
import com.autosale.shop.model.User;
import com.autosale.shop.repository.impl.UserRepositoryImpl;
import org.jooq.impl.DSL;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.autosale.shop.generator.UserGenerator.generate;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.is;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserRepositoryTest {
    UserRepository repository = new UserRepositoryImpl(TestBeanFactory.testDSLContext());

    @Test
    @Order(1)
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
    void save() {
        User user = generate();
        Optional<Integer> save = repository.save(user);
        assertThat(save.isPresent(), is(true));
        assertThat(save.get(), is(user.getId()));
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
    }
}
