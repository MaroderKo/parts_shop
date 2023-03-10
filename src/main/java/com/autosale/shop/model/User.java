package com.autosale.shop.model;

import lombok.Data;

@Data
public class User {
    Integer id;
    String login;
    String password;
    UserRole role;
}
