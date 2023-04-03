package com.autosale.shop.e2e;

import com.autosale.shop.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SecurityLayerE2ETest {
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    @Sql({"/e2e/users/insert_one_user.sql"})
    public void createTokenTest() {
        User baseUser = new User(null, "user", "user", null);
        ResponseEntity<Map<String, String>> tokens = testRestTemplate.exchange("http://localhost:" + port + "/login", HttpMethod.POST, new HttpEntity<>(baseUser), new ParameterizedTypeReference<>() {});
        assertThat(tokens.getStatusCode().value(), is(200));
        assertThat(tokens.getBody(), notNullValue());
        assertThat(tokens.getBody().get("access_token"), notNullValue());
        assertThat(tokens.getBody().get("refresh_token"), notNullValue());
    }

    @Test
    @Sql({"/e2e/users/insert_one_user.sql"})
    public void verifyToken() {
        User baseUser = new User(null, "user", "user", null);
        ResponseEntity<Map<String, String>> tokens = testRestTemplate.exchange("http://localhost:" + port + "/login", HttpMethod.POST, new HttpEntity<>(baseUser), new ParameterizedTypeReference<>() {});
        Map<String, String> values = new HashMap<>();
        values.put("access_token", tokens.getBody().get("access_token"));
        ResponseEntity<Boolean> result = testRestTemplate.exchange("http://localhost:" + port + "/verify", HttpMethod.POST, new HttpEntity<>(values), new ParameterizedTypeReference<>() {});
        assertThat(result.getStatusCode(), is(HttpStatus.OK));
    }

    @Test
    @Sql({"/e2e/users/insert_one_user.sql"})
    public void refreshTokens() {
        User baseUser = new User(null, "user", "user", null);
        ResponseEntity<Map<String, String>> tokens = testRestTemplate.exchange("http://localhost:" + port + "/login", HttpMethod.POST, new HttpEntity<>(baseUser), new ParameterizedTypeReference<>() {});
        Map<String, String> values = new HashMap<>();
        values.put("refresh_token", tokens.getBody().get("refresh_token"));
        ResponseEntity<Map<String, String>> result = testRestTemplate.exchange("http://localhost:" + port + "/refresh", HttpMethod.POST, new HttpEntity<>(values), new ParameterizedTypeReference<>() {});
        assertThat(result.getStatusCode(), is(HttpStatus.OK));
        assertThat(result.getBody(), notNullValue());
        assertThat(result.getBody().get("access_token"), notNullValue());
        assertThat(result.getBody().get("refresh_token"), notNullValue());
    }
}
