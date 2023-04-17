package com.autosale.shop.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

import java.util.UUID;

@Value
public class UserSession {
    UUID id;
    User user;

    @JsonCreator
    public UserSession(@JsonProperty("id") UUID id, @JsonProperty("user") User user) {
        this.id = id;
        this.user = user;
    }
}
