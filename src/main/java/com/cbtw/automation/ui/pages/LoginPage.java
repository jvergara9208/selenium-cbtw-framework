package com.cbtw.automation.ui.pages;

import com.cbtw.automation.ui.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;

public class LoginPage extends BasePage {

    // Locators
    private final By usernameInput = By.cssSelector("[data-test='username']");
    private final By passwordInput = By.cssSelector("[data-test='password']");
    private final By loginButton = By.cssSelector("[data-test='login-button']");
    private final By errorMessageBanner = By.cssSelector("[data-test='error']");

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    // Granular Fluent Actions
    public LoginPage enterUsername(String username) {
        type(usernameInput, username);
        return this;
    }

    public LoginPage enterPassword(String password) {
        type(passwordInput, password);
        return this;
    }

    public InventoryPage submitLogin() {
        click(loginButton);
        return new InventoryPage(driver);
    }

    // High-level Business Flow
    public InventoryPage loginAs(String username, String password) {
        return enterUsername(username)
                .enterPassword(password)
                .submitLogin();
    }

    // State & Validations
    public String getErrorMessage() {
        return getText(errorMessageBanner);
    }

    public boolean isErrorDisplayed() {
        try {
            return waitForVisibility(errorMessageBanner).isDisplayed();
        } catch (TimeoutException e) {
            return false;
        }
    }
}