package com.autosale.shop.repository;

import com.autosale.shop.generator.UserGenerator;
import com.autosale.shop.model.User;
import com.autosale.shop.repository.impl.UserRepositoryImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jooq.SQLDialect;
import org.jooq.impl.DefaultDSLContext;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.autosale.shop.generator.UserGenerator.generate;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserRepositoryTest {
    UserRepository repository = new UserRepositoryImpl(new DefaultDSLContext(SQLDialect.POSTGRES));
    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    @Order(1)
    private void findAll() {
        List<User> inMemory = new ArrayList<>();
        User user = generate();
        inMemory.add(user);
        repository.save(user);
        assertThat(repository.findAll(), is(inMemory));
    }

    @Test
    private void findById() {
        User user = generate();
        repository.save(user);
        assertThat(repository.findById(user.getId()), is(user));

    }

    @Test
    private void save() {
        User user = generate();
        Optional<Integer> save = repository.save(user);
        assertThat(save.isPresent(), is(true));
        assertThat(save.get(), is(user.getId()));
    }

    @Test
    private void update() {
        User user = generate();
        repository.save(user);

        User newUser = new User(user.getId(), "ChangedUsername", user.getPassword(), user.getRole());
        repository.update(newUser);
        assertThat(repository.findById(newUser.getId()), is(newUser));

    }

    @Test
    private void deleteById() {
        User user = generate();
        repository.save(user);

        int i = repository.deleteById(user.getId());
        assertThat(i, is(1));
    }
}
