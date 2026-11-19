package com.cbtw.automation.tests;

import com.cbtw.automation.factories.UserFactory;
import com.cbtw.automation.hooks.BaseTest;
import com.cbtw.automation.pages.LoginPage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LoginTest extends BaseTest {
    private LoginPage loginPage;

    @BeforeEach
    public void initPage() {
        loginPage = new LoginPage(driver);
    }

    @Test
    @DisplayName("Successful login with valid standard user")
    public void testSuccessfulLogin() {
        var user = UserFactory.validStandardUser();
        loginPage.login(user.username(), user.password());
        assertEquals("Products", loginPage.getPageTitle(), "User should land on Products page");
    }

    @Test
    @DisplayName("Failed login with invalid credentials shows error banner")
    public void testInvalidLogin() {
        var invalidUser = UserFactory.invalidRandomUser();
        loginPage.login(invalidUser.username(), invalidUser.password());
        assertTrue(loginPage.getErrorMessage().contains("Username and password do not match"),
                "Error message should indicate invalid credentials");
    }
}