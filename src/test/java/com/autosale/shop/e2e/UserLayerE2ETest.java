package com.autosale.shop.e2e;

import com.autosale.shop.model.User;
import com.autosale.shop.model.UserRole;
import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.jdbc.Sql;

import java.util.Map;

import static com.autosale.shop.generator.UserGenerator.generate;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = {"JWT_TOKEN_SECRET=supersecretpassword123456789101101","AWS_ACCESS_KEY=","AWS_SECRET_KEY="})
public class UserLayerE2ETest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    @Sql({"/e2e/users/insert_one_admin_user.sql"})
    public void createUserTest() {
        HttpHeaders headers = getTokenFromCredentialsAndReturnHttpHeaders("admin", "admin");
        User user = generate();
        ResponseEntity<String> result = testRestTemplate.exchange("http://localhost:" + port + "/users", HttpMethod.POST, new HttpEntity<>(user, headers), String.class);
        assertThat(result.getStatusCode().value(), is(200));
    }

    @Test
    @Sql({"/e2e/users/insert_one_user.sql"})
    public void readUserById() {
        HttpHeaders headers = getTokenFromCredentialsAndReturnHttpHeaders("user", "user");
        ResponseEntity<User> result = testRestTemplate.exchange("http://localhost:" + port + "/users/1",HttpMethod.GET, new HttpEntity<>(headers), User.class);
        User returned = result.getBody();
        assertThat(returned, notNullValue());
        assertThat(returned.getId(), is(1));
        assertThat(returned.getUserName(), is("user"));
        assertThat(returned.getPassword(), is("$2a$12$rZ9Rc0mMxCc04zAQ1Hs9zepxe71BuccmovfTRvuHjEWVr2fbz2PZK"));
        assertThat(returned.getRole(), is(UserRole.USER));
    }

    @Test
    @Sql({"/e2e/users/insert_one_admin_user.sql"})
    public void updateUser() {
        HttpHeaders headers = getTokenFromCredentialsAndReturnHttpHeaders("admin", "admin");
        User user = new User(1, "ChangedUsername", "ChangedPassword", UserRole.USER);
        ResponseEntity<String> result = testRestTemplate.exchange("http://localhost:" + port + "/users", HttpMethod.PUT, new HttpEntity<>(user, headers), String.class);
        assertThat(result.getStatusCode().value(), is(200));
    }

    @Test
    @Sql({"/e2e/users/insert_one_admin_user.sql"})
    public void deleteUser() {
        HttpHeaders headers = getTokenFromCredentialsAndReturnHttpHeaders("admin", "admin");
        ResponseEntity<String> result = testRestTemplate.exchange("http://localhost:" + port + "/users/2", HttpMethod.DELETE, new HttpEntity<>(headers), String.class);
        assertThat(result.getStatusCode().value(), is(200));
        assertThat(result.getBody(), equalTo("1"));
    }

    @Test
    @Sql("/e2e/users/insert_one_user.sql")
    public void editUserWithoutPermission() {
        HttpHeaders headers = getTokenFromCredentialsAndReturnHttpHeaders("user", "user");
        User user = generate();
        ResponseEntity<String> result = testRestTemplate.exchange("http://localhost:" + port + "/users", HttpMethod.PUT, new HttpEntity<>(user, headers), String.class);
        assertThat(result.getStatusCode().value(), is(403));
        result = testRestTemplate.exchange("http://localhost:" + port + "/users",HttpMethod.POST, new HttpEntity<>(user,headers), String.class);
        assertThat(result.getStatusCode().value(), is(403));
        result = testRestTemplate.exchange("http://localhost:" + port + "/users/1", HttpMethod.DELETE, new HttpEntity<>(headers), String.class);
        assertThat(result.getStatusCode().value(), is(403));
    }

    @Test
    @Sql({"/e2e/users/insert_three_users.sql"})
    public void readAllUsers() {
        HttpHeaders headers = getTokenFromCredentialsAndReturnHttpHeaders("ExampleUser1", "ExamplePassword1");
        ResponseEntity<User[]> result = testRestTemplate.exchange("http://localhost:" + port + "/users", HttpMethod.GET, new HttpEntity<>(headers), User[].class);
        assertThat(result.getStatusCode(), is(HttpStatus.OK));
        User user1 = new User(4, "ExampleUser1", "$2a$12$R8seoU4Vqpb7OMhz8APn.OmWh8PqKgu.WmZwBPjNuESEzU8OtywRq", UserRole.ADMIN);
        User user2 = new User(5, "ExampleUser2", "$2a$12$HMPtzxWMBKdBMCpOBJkX1u6jhJaJhZtGD0ntMt9.OoS1LdpMCbwcW", UserRole.ADMIN);
        User user3 = new User(6, "ExampleUser3", "$2a$12$xUC.trmEPGjMpFmWy31P3.a3WCogiEcnS/glAkKyXYuTyjMDBbqry", UserRole.ADMIN);

        assertThat(Arrays.asList(result.getBody()), containsInAnyOrder(user1, user2, user3));
    }

    private HttpHeaders getTokenFromCredentialsAndReturnHttpHeaders(String login, String password)
    {
        User baseUser = new User(null, login, password, null);
        ResponseEntity<Map<String, String>> tokens = testRestTemplate.exchange("http://localhost:" + port + "/login", HttpMethod.POST, new HttpEntity<>(baseUser), new ParameterizedTypeReference<>() {});
        String accessToken = tokens.getBody().get("accessToken");
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        return headers;
    }

}
