package com.autosale.shop.e2e;

import com.autosale.shop.model.User;
import com.autosale.shop.model.UserRole;
import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;

import static com.autosale.shop.generator.UserGenerator.generate;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserLayerE2ETest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    public void createUserTest() {
        User user = generate();
        ResponseEntity<String> result = testRestTemplate.withBasicAuth("admin", "admin").postForEntity("http://localhost:" + port + "/users", user, String.class);
        assertThat(result.getStatusCode().value(), is(200));
    }

    @Test
    @Sql({"/e2e/users/insert_one_user.sql"})
    public void readUserById() {
        User returned = testRestTemplate.withBasicAuth("user", "user").getForObject("http://localhost:" + port + "/users/1", User.class);
        assertThat(returned.getId(), is(1));
        assertThat(returned.getUserName(), is("ExampleUser"));
        assertThat(returned.getPassword(), is("ExamplePassword"));
        assertThat(returned.getRole(), is(UserRole.ADMIN));
    }

    @Test
    @Sql({"/e2e/users/insert_one_user.sql"})
    public void updateUser() {
        User user = new User(1, "ChangedUsername", "ChangedPassword", UserRole.USER);
        ResponseEntity<String> result = testRestTemplate.withBasicAuth("admin", "admin").exchange("http://localhost:" + port + "/users", HttpMethod.PUT, new HttpEntity<>(user), String.class);
        assertThat(result.getStatusCode().value(), is(200));
    }

    @Test
    @Sql({"/e2e/users/insert_one_user.sql"})
    public void deleteUser() {
        ResponseEntity<String> result = testRestTemplate.withBasicAuth("admin", "admin").exchange("http://localhost:" + port + "/users/1", HttpMethod.DELETE, HttpEntity.EMPTY, String.class);
        assertThat(result.getStatusCode().value(), is(200));
        assertThat(result.getBody(), equalTo("1"));
    }

    @Test
    public void editUserWithoutPermission() {
        User user = generate();
        ResponseEntity<String> result = testRestTemplate.withBasicAuth("user", "user").exchange("http://localhost:" + port + "/users", HttpMethod.PUT, new HttpEntity<>(user), String.class);
        assertThat(result.getStatusCode().value(), is(403));
        result = testRestTemplate.withBasicAuth("user", "user").postForEntity("http://localhost:" + port + "/users", user, String.class);
        assertThat(result.getStatusCode().value(), is(403));
        result = testRestTemplate.withBasicAuth("user", "user").exchange("http://localhost:" + port + "/users/1", HttpMethod.DELETE, HttpEntity.EMPTY, String.class);
        assertThat(result.getStatusCode().value(), is(403));
    }

    @Test
    @Sql({"/e2e/users/insert_three_users.sql"})
    public void readAllUsers() {
        ResponseEntity<User[]> result = testRestTemplate.withBasicAuth("user", "user").getForEntity("http://localhost:" + port + "/users", User[].class);
        User user1 = new User(1, "ExampleUser1", "ExamplePassword1", UserRole.ADMIN);
        User user2 = new User(2, "ExampleUser2", "ExamplePassword2", UserRole.ADMIN);
        User user3 = new User(3, "ExampleUser3", "ExamplePassword3", UserRole.ADMIN);

        assertThat(Arrays.asList(result.getBody()), containsInAnyOrder(user1, user2, user3));
    }


}
