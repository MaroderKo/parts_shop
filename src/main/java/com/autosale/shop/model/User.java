package com.autosale.shop.model;

import lombok.Data;

@Data
public class User {
    private final Integer id;
    private final String userName;
    private final String password;
    private final UserRole role;
}
