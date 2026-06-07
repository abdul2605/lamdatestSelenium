package com.lambdatest;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

public class TS3InputFormTest {
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

    public void testInputFormSubmit() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        WebDriverWait longWait = new WebDriverWait(driver, Duration.ofSeconds(60));

        // Step 1: Open Selenium Playground and click "Input Form Submit"
        driver.get("https://www.testmuai.com/selenium-playground/");
        System.out.println("Opened: " + driver.getCurrentUrl());

        WebElement formLink = wait.until(
                ExpectedConditions.elementToBeClickable(By.linkText("Input Form Submit")));
        formLink.click();
        System.out.println("Clicked Input Form Submit");

        wait.until(ExpectedConditions.urlContains("input-form-demo"));
        System.out.println("Current URL: " + driver.getCurrentUrl());

        // Debug: dump all buttons, inputs, forms and iframes on the page
        JavascriptExecutor jsDbg = (JavascriptExecutor) driver;
        try { Thread.sleep(3000); } catch (InterruptedException ignored) {}
        String dbg = (String) jsDbg.executeScript(
            "var r='';" +
            "document.querySelectorAll('button').forEach(function(b){r+='BUTTON type='+b.type+' text='+b.innerText.trim().substring(0,30)+'\\n';});" +
            "document.querySelectorAll('input').forEach(function(i){r+='INPUT type='+i.type+' id='+i.id+' name='+i.name+' placeholder='+i.placeholder+'\\n';});" +
            "document.querySelectorAll('form').forEach(function(f){r+='FORM id='+f.id+' action='+f.action+'\\n';});" +
            "document.querySelectorAll('iframe').forEach(function(f){r+='IFRAME src='+f.src+'\\n';});" +
            "return r;");
        System.out.println("=== PAGE ELEMENTS ===\n" + dbg + "====================");

        // Wait for Cloudflare to resolve and the Submit button to appear
        WebElement submitBtn = longWait.until(
                ExpectedConditions.elementToBeClickable(
                        By.xpath("//button[@type='submit'] | //input[@type='submit'] | //button[normalize-space()='Submit']")));
        System.out.println("Submit button found");

        // Step 2: Click Submit without filling any fields
        submitBtn.click();
        System.out.println("Clicked Submit (empty form)");

        // Step 3: Assert HTML5 validation message on the Name field
        WebElement nameField = driver.findElement(
                By.xpath("//input[@name='name'] | //input[@id='name'] | //input[@placeholder='Name']"));
        String validationMsg = (String) ((JavascriptExecutor) driver)
                .executeScript("return arguments[0].validationMessage;", nameField);
        System.out.println("Validation message: " + validationMsg);

        if (validationMsg != null && !validationMsg.isEmpty()) {
            System.out.println("PASS: Empty form validation triggered: " + validationMsg);
        } else {
            markStatus("failed", "Empty form validation not triggered", driver);
            return;
        }

        // Step 4: Fill Name
        nameField.clear();
        nameField.sendKeys("John Doe");
        System.out.println("Filled Name");

        // Email
        WebElement emailField = driver.findElement(
                By.xpath("//input[@type='email'] | //input[@name='email'] | //input[@id='email']"));
        emailField.clear();
        emailField.sendKeys("johndoe@example.com");
        System.out.println("Filled Email");

        // Password
        try {
            WebElement passwordField = driver.findElement(
                    By.xpath("//input[@type='password'] | //input[@name='password'] | //input[@id='password']"));
            passwordField.clear();
            passwordField.sendKeys("Password@123");
            System.out.println("Filled Password");
        } catch (Exception e) {
            System.out.println("No password field, skipping");
        }

        // Company
        try {
            WebElement companyField = driver.findElement(
                    By.xpath("//input[@name='company'] | //input[@id='company'] | //input[@placeholder='Company']"));
            companyField.clear();
            companyField.sendKeys("TestMu AI");
            System.out.println("Filled Company");
        } catch (Exception e) {
            System.out.println("No company field, skipping");
        }

        // Website
        try {
            WebElement websiteField = driver.findElement(
                    By.xpath("//input[@name='website'] | //input[@id='website'] | //input[@placeholder='Website']"));
            websiteField.clear();
            websiteField.sendKeys("https://www.testmuai.com");
            System.out.println("Filled Website");
        } catch (Exception e) {
            System.out.println("No website field, skipping");
        }

        // Step 5: Select "United States" from Country dropdown
        WebElement countryDropdown = driver.findElement(
                By.xpath("//select[@name='country'] | //select[@id='country']"));
        Select countrySelect = new Select(countryDropdown);
        countrySelect.selectByVisibleText("United States");
        System.out.println("Selected country: United States");

        // City
        try {
            WebElement cityField = driver.findElement(
                    By.xpath("//input[@name='city'] | //input[@id='city'] | //input[@placeholder='City']"));
            cityField.clear();
            cityField.sendKeys("San Francisco");
            System.out.println("Filled City");
        } catch (Exception e) {
            System.out.println("No city field, skipping");
        }

        // Address 1
        try {
            WebElement addressField = driver.findElement(
                    By.xpath("(//input[@name='address'] | //input[@id='address'] | //input[@placeholder='Address'])[1]"));
            addressField.clear();
            addressField.sendKeys("1 Sutter Street");
            System.out.println("Filled Address 1");
        } catch (Exception e) {
            System.out.println("No address field, skipping");
        }

        // Address 2
        try {
            WebElement address2Field = driver.findElement(
                    By.xpath("(//input[@name='address'] | //input[@placeholder='Address'])[2] | //input[@name='address2'] | //input[@id='address2']"));
            address2Field.clear();
            address2Field.sendKeys("Suite 500");
            System.out.println("Filled Address 2");
        } catch (Exception e) {
            System.out.println("No address2 field, skipping");
        }

        // State
        try {
            WebElement stateField = driver.findElement(
                    By.xpath("//input[@name='state'] | //input[@id='state'] | //input[@placeholder='State']"));
            stateField.clear();
            stateField.sendKeys("California");
            System.out.println("Filled State");
        } catch (Exception e) {
            System.out.println("No state field, skipping");
        }

        // Zip Code
        try {
            WebElement zipField = driver.findElement(
                    By.xpath("//input[@name='zip'] | //input[@id='zip'] | //input[@placeholder='Zip Code'] | //input[@name='postal']"));
            zipField.clear();
            zipField.sendKeys("94104");
            System.out.println("Filled Zip Code");
        } catch (Exception e) {
            System.out.println("No zip field, skipping");
        }

        // Step 6: Click Submit with all fields filled
        submitBtn = wait.until(
                ExpectedConditions.elementToBeClickable(
                        By.xpath("//button[@type='submit'] | //input[@type='submit'] | //button[normalize-space()='Submit']")));
        submitBtn.click();
        System.out.println("Clicked Submit (filled form)");

        // Step 7: Validate success message
        WebElement successMsg = longWait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//*[contains(text(),'Thanks for contacting us') or " +
                         "contains(text(),'we will get back to you')]")));
        String msgText = successMsg.getText().trim();
        System.out.println("Success message: " + msgText);

        if (msgText.contains("Thanks for contacting us") && msgText.contains("we will get back to you")) {
            markStatus("passed", "Form submission passed: " + msgText, driver);
        } else {
            markStatus("failed", "Unexpected success message: " + msgText, driver);
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
        TS3InputFormTest test = new TS3InputFormTest();
        test.setup();
        test.testInputFormSubmit();
        test.tearDown();
    }
}
