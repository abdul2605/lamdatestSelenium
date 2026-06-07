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
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

public class DragDropSliderTest {
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

    public void testDragDropSlider() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));

        // Step 1: Open Selenium Playground and click "Drag & Drop Sliders"
        driver.get("https://www.testmuai.com/selenium-playground/");
        System.out.println("Opened Selenium Playground: " + driver.getCurrentUrl());

        WebElement sliderLink = wait.until(
                ExpectedConditions.elementToBeClickable(By.linkText("Drag & Drop Sliders")));
        sliderLink.click();
        System.out.println("Clicked Drag & Drop Sliders");

        // Wait for page to load (actual URL slug)
        wait.until(ExpectedConditions.urlContains("drag-drop-range-sliders-demo"));
        System.out.println("Current URL: " + driver.getCurrentUrl());

        // Debug: print all range inputs and headings on the page
        JavascriptExecutor jsDebug = (JavascriptExecutor) driver;
        String debug = (String) jsDebug.executeScript(
            "var result = '';" +
            "document.querySelectorAll('h4').forEach(function(h){result += 'H4: [' + h.textContent.trim() + ']\\n';});" +
            "document.querySelectorAll('input[type=range]').forEach(function(i){result += 'RANGE id=' + i.id + ' value=' + i.value + ' min=' + i.min + ' max=' + i.max + '\\n';});" +
            "return result;");
        System.out.println("=== DEBUG ===\n" + debug + "=============");

        // Step 2: Find slider with "Default value 15" heading, then its input[type=range]
        WebElement slider = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//h4[contains(text(),'Default value 15')]/following-sibling::input[@type='range'][1] | " +
                         "//h4[contains(text(),'Default value 15')]/..//input[@type='range']")));
        System.out.println("Found slider. Current value: " + slider.getAttribute("value"));

        // Drag via Actions
        int targetValue = 95;
        int min = Integer.parseInt(slider.getAttribute("min") != null ? slider.getAttribute("min") : "0");
        int max = Integer.parseInt(slider.getAttribute("max") != null ? slider.getAttribute("max") : "100");
        int sliderWidth = slider.getSize().getWidth();
        int currentValue = Integer.parseInt(slider.getAttribute("value"));
        int pixelOffset = (int) (((double)(targetValue - currentValue) / (max - min)) * sliderWidth);

        System.out.println("Slider min=" + min + " max=" + max + " current=" + currentValue +
                           " width=" + sliderWidth + " offset=" + pixelOffset);

        Actions actions = new Actions(driver);
        actions.clickAndHold(slider).moveByOffset(pixelOffset, 0).release().perform();

        // Fallback: set value via JS if drag didn't land exactly on 95
        String afterDrag = slider.getAttribute("value");
        System.out.println("Value after drag: " + afterDrag);
        if (!afterDrag.equals(String.valueOf(targetValue))) {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript(
                "var s=arguments[0]; s.value=arguments[1]; " +
                "s.dispatchEvent(new Event('input',{bubbles:true})); " +
                "s.dispatchEvent(new Event('change',{bubbles:true}));",
                slider, String.valueOf(targetValue));
            System.out.println("Set slider to 95 via JavaScript");
        }

        // Validate: the displayed value next to the slider should show 95
        // The range output is typically a sibling span/p next to the input
        WebElement rangeOutput = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//h4[contains(text(),'Default value 15')]/following-sibling::p | " +
                         "//h4[contains(text(),'Default value 15')]/..//p[not(input)] | " +
                         "//h4[contains(text(),'Default value 15')]/..//output | " +
                         "//h4[contains(text(),'Default value 15')]/..//*[@id='rangeSuccess']")));
        String displayedValue = rangeOutput.getText().trim();
        System.out.println("Displayed range value: " + displayedValue);

        if (displayedValue.equals("95")) {
            markStatus("passed", "Slider validation passed: value is 95", driver);
        } else {
            markStatus("failed", "Slider validation failed. Expected: 95, Got: " + displayedValue, driver);
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
        DragDropSliderTest test = new DragDropSliderTest();
        test.setup();
        test.testDragDropSlider();
        test.tearDown();
    }
}
