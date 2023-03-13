package com.autosale.shop.model;

import lombok.Data;

@Data
public class User {
    Integer id;
    String userName;
    String password;
    UserRole role;
}
