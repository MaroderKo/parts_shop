package com.autosale.shop.controller;

import com.autosale.shop.model.User;
import com.autosale.shop.model.UserRole;
import com.autosale.shop.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.bytebuddy.utility.RandomString;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

class UserControllerTest {

    private final UserService service = Mockito.mock(UserService.class);
    private final UserController userController = new UserController(service);
    private final MockMvc mock = MockMvcBuilders.standaloneSetup(userController).build();
    private final ObjectMapper mapper = new ObjectMapper();


    List<User> users = new ArrayList<>();

    @BeforeEach
    void setUp() {
        Random rand = new Random();
        for (int i = 0; i < 3; i++) {
            users.add(new User(rand.nextInt(100), RandomString.make(50), RandomString.make(25), rand.nextBoolean() ? UserRole.USER : UserRole.ADMIN));
        }
    }

    @AfterEach
    void finale() {
        users.clear();
    }

    @Test
    void findAll() throws Exception {

        when(service.findAll()).thenReturn(users);
        MvcResult result = mock.perform(MockMvcRequestBuilders.get("/users"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON)).andReturn();
        assertThat(new String(result.getResponse().getContentAsByteArray(), UTF_8), is(equalTo(mapper.writeValueAsString(users))));
    }

    @Test
    void create() throws Exception {
        User user = users.get(0);
        when(service.create(user)).thenReturn(user.getId());
        MvcResult result = mock.perform(post("/users")
                        .content(mapper.writeValueAsString(user))
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        assert (result.getRequest().getContentAsByteArray() != null);
        assertThat(HttpStatus.OK.value(), is(equalTo(result.getResponse().getStatus())));
        assertThat(new String(result.getRequest().getContentAsByteArray(), UTF_8), is(equalTo(mapper.writeValueAsString(user))));

    }

    @Test
    void findById() throws Exception {
        User user = users.get(0);
        when(service.findById(user.getId())).thenReturn(user);
        MvcResult result = mock.perform(get("/users/"+user.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        assertThat(HttpStatus.OK.value(), is(equalTo( result.getResponse().getStatus())));
        assertThat(new String(result.getResponse().getContentAsByteArray(),UTF_8),is(equalTo(mapper.writeValueAsString(user))));
    }

    @Test
    void delete() throws Exception {
        User user = users.get(0);
        when(service.delete(user.getId())).thenReturn(1);
        MvcResult result = mock.perform(MockMvcRequestBuilders.delete("/users/" + user.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        assertThat(HttpStatus.OK.value(), is(equalTo(result.getResponse().getStatus())));
        assertThat(new String(result.getResponse().getContentAsByteArray(), UTF_8), is(equalTo("1")));
    }

    @Test
    void update() throws Exception {
        User user = users.get(0);
        MvcResult result = mock.perform(put("/users")
                        .content(mapper.writeValueAsString(user))
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        assert (result.getRequest().getContentAsByteArray() != null);
        assertThat(HttpStatus.OK.value(), is(equalTo(result.getResponse().getStatus())));
        verify(service).edit(user);
    }
}