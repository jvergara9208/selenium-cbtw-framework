package com.cbtw.automation.ui.factories;

import net.datafaker.Faker;

public class UserFactory {
    private static final Faker faker = new Faker();

    public record UserCredentials(String username, String password) {}

    public static UserCredentials validStandardUser() {
        return new UserCredentials("standard_user", "secret_sauce");
    }

    public static UserCredentials invalidRandomUser() {
        return new UserCredentials(faker.internet().username(), faker.internet().password());
    }
}