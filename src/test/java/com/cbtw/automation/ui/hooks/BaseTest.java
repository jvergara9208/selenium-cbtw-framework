package com.cbtw.automation.ui.hooks;

import com.cbtw.automation.utils.ConfigReader;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;

public class BaseTest {
    protected WebDriver driver;

    @BeforeEach
    public void setUp() {
        String browser = ConfigReader.get("browser").toLowerCase();
        boolean isHeadless = ConfigReader.getBoolean("headless");

        switch (browser) {
            case "edge" -> {
                EdgeOptions edgeOptions = new EdgeOptions();
                if (isHeadless) edgeOptions.addArguments("--headless=new");
                driver = new EdgeDriver(edgeOptions);
            }
            case "chrome" -> {
                ChromeOptions chromeOptions = new ChromeOptions();
                if (isHeadless) chromeOptions.addArguments("--headless=new");
                driver = new ChromeDriver(chromeOptions);
            }
            default -> throw new IllegalArgumentException("Unsupported browser in config.properties: " + browser);
        }

        driver.manage().window().maximize();
        driver.get(ConfigReader.get("base.url"));
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}