package com.autosale.shop.generator;

import com.autosale.shop.model.User;
import com.autosale.shop.model.UserRole;
import net.bytebuddy.utility.RandomString;

import java.util.Random;

public class UserGenerator {
    private static final Random rand = new Random();
    public static User generate()
    {
        return new User(rand.nextInt(100), RandomString.make(50), RandomString.make(25), rand.nextBoolean() ? UserRole.USER : UserRole.ADMIN);
    }
}
