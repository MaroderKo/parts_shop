package com.autosale.shop.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

@Value
public class JwtTokensDTO {
    String accessToken;

    String refreshToken;

    public JwtTokensDTO(@JsonProperty("access_token") String accessToken, @JsonProperty("refresh_token") String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
