package com.cbtw.automation.tests;

import com.cbtw.automation.factories.UserFactory;
import com.cbtw.automation.hooks.BaseTest;
import com.cbtw.automation.pages.InventoryPage;
import com.cbtw.automation.pages.LoginPage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Tag("E2E")
public class LoginTest extends BaseTest {
    private LoginPage loginPage;

    @BeforeEach
    public void initPage() {
        loginPage = new LoginPage(driver);
    }

    @Test
    @Tag("Smoke")
    @Tag("Test")
    @DisplayName("Successful login redirects to Products inventory page")
    public void testSuccessfulLogin() {
        var user = UserFactory.validStandardUser();
        InventoryPage inventoryPage = loginPage.loginAs(user.username(), user.password());

        assertTrue(inventoryPage.isLoaded(), "Inventory page should be loaded after login");
        assertEquals("Products", inventoryPage.getPageTitle());
    }

    @Test
    @Tag("Regression")
    @Tag("Test")
    @DisplayName("Failed login with invalid credentials displays error banner")
    public void testInvalidLogin() {
        var invalidUser = UserFactory.invalidRandomUser();
        loginPage.loginAs(invalidUser.username(), invalidUser.password());

        assertTrue(loginPage.isErrorDisplayed(), "Error banner should be visible");
        assertTrue(loginPage.getErrorMessage().contains("Username and password do not match"));
    }
}