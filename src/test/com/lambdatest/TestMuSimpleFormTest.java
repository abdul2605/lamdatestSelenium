package com.lambdatest;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.time.Duration;

public class TestMuSimpleFormTest {
    public static String hubURL = "https://hub.lambdatest.com/wd/hub";
    private WebDriver driver;

    public void setup() throws MalformedURLException {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("browserName", "Chrome");
        capabilities.setCapability("browserVersion", "latest");
        Map<String, Object> ltOptions = new HashMap<>();
        ltOptions.put("user", System.getenv("LT_USERNAME"));
        ltOptions.put("accessKey", System.getenv("LT_ACCESS_KEY"));
        ltOptions.put("build", "Selenium 4");
        ltOptions.put("name", this.getClass().getName());
        ltOptions.put("platformName", "Windows 10");
        ltOptions.put("selenium_version", "latest");
        capabilities.setCapability("LT:Options", ltOptions);

        driver = new RemoteWebDriver(new URL(hubURL), capabilities);
        System.out.println(driver);
    }

    public void testSimpleFormDemo() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));

        // Step 1: Open TestMu AI Selenium Playground
        driver.get("https://www.testmuai.com/selenium-playground/");
        System.out.println("Opened Selenium Playground: " + driver.getCurrentUrl());

        // Step 2: Click "Simple Form Demo"
        WebElement simpleFormLink = wait.until(
                ExpectedConditions.elementToBeClickable(By.linkText("Simple Form Demo")));
        simpleFormLink.click();
        System.out.println("Clicked Simple Form Demo");

        // Step 3: Validate URL contains "simple-form-demo"
        wait.until(ExpectedConditions.urlContains("simple-form-demo"));
        String currentUrl = driver.getCurrentUrl();
        System.out.println("Current URL: " + currentUrl);
        if (currentUrl.contains("simple-form-demo")) {
            System.out.println("URL validation passed: contains 'simple-form-demo'");
        } else {
            markStatus("failed", "URL validation failed. URL: " + currentUrl, driver);
            return;
        }

        // Wait for Cloudflare challenge to resolve and actual page content to load
        WebDriverWait longWait = new WebDriverWait(driver, Duration.ofSeconds(60));
        longWait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//input[@id='user-message'] | //input[@placeholder='Enter Message']")));
        System.out.println("Page fully loaded");

        // Step 4 & 5: Create message variable and enter it in the "Enter Message" field
        String message = "Welcome to TestMu AI";
        // Try by id first, fall back to placeholder text
        WebElement messageInput = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.xpath("//input[@id='user-message'] | //input[@placeholder='Enter Message'] | //input[contains(@placeholder,'message') or contains(@placeholder,'Message')]")));
        messageInput.clear();
        messageInput.sendKeys(message);
        System.out.println("Entered message: " + message);

        // Step 6: Click "Get Checked Value" button
        WebElement showButton = wait.until(
                ExpectedConditions.elementToBeClickable(
                        By.xpath("//button[@id='showInput'] | //button[normalize-space()='Get Checked Value'] | //button[normalize-space()='Show Message']")));
        showButton.click();
        System.out.println("Clicked Get Checked Value button");

        // Step 7: Validate the message is displayed under "Your Message:" section
        WebElement displayedMessage = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.xpath("//*[@id='message'] | //*[contains(@class,'message') and not(self::input) and not(self::button)]//following-sibling::* | //p[@id='message']")));
        String actualMessage = displayedMessage.getText();
        System.out.println("Displayed message: " + actualMessage);

        if (actualMessage.equals(message)) {
            markStatus("passed", "Message validation passed: '" + actualMessage + "'", driver);
        } else {
            markStatus("failed",
                    "Message validation failed. Expected: '" + message + "', Got: '" + actualMessage + "'", driver);
        }
    }

    public void tearDown() {
        try {
            driver.quit();
        } catch (Exception e) {
            markStatus("failed", "Got exception during teardown!", driver);
            e.printStackTrace();
            driver.quit();
        }
    }

    public static void markStatus(String status, String reason, WebDriver driver) {
        JavascriptExecutor jsExecute = (JavascriptExecutor) driver;
        jsExecute.executeScript("lambda-status=" + status);
        System.out.println(reason);
    }

    public static void main(String[] args) throws MalformedURLException {
        TestMuSimpleFormTest test = new TestMuSimpleFormTest();
        test.setup();
        test.testSimpleFormDemo();
        test.tearDown();
    }
}
