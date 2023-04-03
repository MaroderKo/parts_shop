package com.autosale.shop.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

@Value
public class JwtTokensDAO {
    String accessToken;

    String refreshToken;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public JwtTokensDAO(@JsonProperty("access_token") String accessToken, @JsonProperty("refresh_token") String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
