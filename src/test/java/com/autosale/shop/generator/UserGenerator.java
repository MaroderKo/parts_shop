package com.autosale.shop.generator;

import com.autosale.shop.model.User;
import com.autosale.shop.model.UserRole;
import net.bytebuddy.utility.RandomString;

import java.util.Random;
import java.util.UUID;

public class UserGenerator {
    private static final Random rand = new Random();
    public static User generate()
    {
        return new User(rand.nextInt(100), UUID.randomUUID().toString(), UUID.randomUUID().toString(), rand.nextBoolean() ? UserRole.USER : UserRole.ADMIN);
    }
}
