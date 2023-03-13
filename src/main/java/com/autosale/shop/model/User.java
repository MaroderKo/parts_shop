package com.autosale.shop.model;

import lombok.Value;

@Value
public class User {
    Integer id;
    String userName;
    String password;
    UserRole role;
}
