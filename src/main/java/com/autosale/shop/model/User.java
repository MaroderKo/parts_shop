package com.autosale.shop.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

@Value
public class User {
    Integer id;
    String userName;
    String password;
    UserRole role;

    public User(@JsonProperty("id") Integer id, @JsonProperty("userName") String userName, @JsonProperty("password") String password, @JsonProperty("role") UserRole role) {
        this.id = id;
        this.userName = userName;
        this.password = password;
        this.role = role;
    }
}
