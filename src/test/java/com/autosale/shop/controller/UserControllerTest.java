package com.autosale.shop.controller;

import com.autosale.shop.model.User;
import com.autosale.shop.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static com.autosale.shop.generator.UserGenerator.generate;
import static java.nio.charset.StandardCharsets.UTF_8;
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

    @Test
    void findAll() throws Exception {
        List<User> users = List.of(generate(), generate(), generate());
        when(service.findAll()).thenReturn(users);
        MvcResult result = mock.perform(MockMvcRequestBuilders.get("/users"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON)).andReturn();
        assertThat(result.getResponse().getContentAsString(), is(mapper.writeValueAsString(users)));
    }

    @Test
    void create() throws Exception {
        User user = generate();
        when(service.create(user)).thenReturn(user.getId());
        MvcResult result = mock.perform(post("/users")
                        .content(mapper.writeValueAsString(user))
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        assertThat(HttpStatus.OK.value(), is(result.getResponse().getStatus()));
}

    @Test
    void findById() throws Exception {
        User user = generate();
        when(service.findById(user.getId())).thenReturn(user);
        MvcResult result = mock.perform(get("/users/" + user.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        assertThat(HttpStatus.OK.value(), is(result.getResponse().getStatus()));
        assertThat(new String(result.getResponse().getContentAsByteArray(), UTF_8), is(mapper.writeValueAsString(user)));
    }

    @Test
    void delete() throws Exception {
        User user = generate();
        when(service.delete(user.getId())).thenReturn(1);
        MvcResult result = mock.perform(MockMvcRequestBuilders.delete("/users/" + user.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        assertThat(HttpStatus.OK.value(), is(result.getResponse().getStatus()));
        assertThat(new String(result.getResponse().getContentAsByteArray(), UTF_8), is("1"));
    }

    @Test
    void update() throws Exception {
        User user = generate();
        MvcResult result = mock.perform(put("/users")
                        .content(mapper.writeValueAsString(user))
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        assertThat(HttpStatus.OK.value(), is(result.getResponse().getStatus()));
        verify(service).edit(user);
    }
}