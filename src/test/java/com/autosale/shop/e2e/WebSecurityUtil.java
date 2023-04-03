package com.autosale.shop.e2e;

import com.autosale.shop.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.util.Map;

public class WebSecurityUtil {

    public static HttpHeaders getTokenFromCredentialsAndReturnHttpHeaders(String login, String password, TestRestTemplate testRestTemplate, int port)
    {
        User baseUser = new User(null, login, password, null);
        ResponseEntity<Map<String, String>> tokens = testRestTemplate.exchange("http://localhost:" + port + "/login", HttpMethod.POST, new HttpEntity<>(baseUser), new ParameterizedTypeReference<>() {});
        String accessToken = tokens.getBody().get("access_token");
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        return headers;
    }
}
