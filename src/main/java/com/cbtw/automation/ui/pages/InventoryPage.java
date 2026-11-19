package com.cbtw.automation.ui.pages;

import com.cbtw.automation.ui.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class InventoryPage extends BasePage {

    private final By pageTitle = By.cssSelector("[data-test='title']");

    public InventoryPage(WebDriver driver) {
        super(driver);
    }

    public String getPageTitle() {
        return getText(pageTitle);
    }

    public boolean isLoaded() {
        return "Products".equalsIgnoreCase(getPageTitle());
    }
}