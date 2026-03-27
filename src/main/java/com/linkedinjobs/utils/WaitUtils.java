package com.linkedinjobs.utils;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WaitUtils {

    private static final Logger log = LoggerFactory.getLogger(WaitUtils.class);

    private final WebDriver driver;
    private final WebDriverWait wait;
    private final WebDriverWait shortWait;
    private final FluentWait<WebDriver> fluentWait;

    // ── Constructor ────────────────────────────────────────────────────────────
    // Reads timeout from ConfigReader so it's driven by config.properties
    // shortWait is for quick checks (e.g. is element present?) — avoids
    // waiting the full explicit timeout for negative assertions
    public WaitUtils(WebDriver driver) {
        this.driver = driver;
        int timeout = ConfigReader.getExplicitWait();         //default 10s

        this.wait = new WebDriverWait(driver,
                Duration.ofSeconds(timeout));

        this.shortWait = new WebDriverWait(driver,
                Duration.ofSeconds(3));

        // FluentWait polls every 500ms and ignores NoSuchElementException
        // while polling — useful for elements that appear intermittently
        this.fluentWait = new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(timeout))
                .pollingEvery(Duration.ofMillis(500))
                .ignoring(NoSuchElementException.class)
                .ignoring(StaleElementReferenceException.class);
    }

    // ══════════════════════════════════════════════════════════════════════════
    // VISIBILITY WAITS
    // ══════════════════════════════════════════════════════════════════════════

    // Waits until element is present in the DOM AND visible on screen
    // Use this before reading text or asserting an element is shown
    public WebElement waitForVisible(By locator) {
        log.debug("Waiting for element to be visible: {}", locator);
        return wait.until(
                ExpectedConditions.visibilityOfElementLocated(locator));
    }

    // Overload for when you already have a WebElement reference
    // Useful inside page objects that use @FindBy / PageFactory
    public WebElement waitForVisible(WebElement element) {
        log.debug("Waiting for WebElement to be visible");
        return wait.until(
                ExpectedConditions.visibilityOf(element));
    }

    // Waits for ALL elements matching the locator to be visible
    // Useful for tables, lists, search results
    public List<WebElement> waitForAllVisible(By locator) {
        log.debug("Waiting for all elements to be visible: {}", locator);
        return wait.until(
                ExpectedConditions.visibilityOfAllElementsLocatedBy(locator));
    }

    // ══════════════════════════════════════════════════════════════════════════
    // CLICKABILITY WAITS
    // ══════════════════════════════════════════════════════════════════════════

    // Waits until element is visible AND enabled (not disabled/greyed out)
    // Always use this before .click() — prevents ElementNotInteractableException
    public WebElement waitForClickable(By locator) {
        log.debug("Waiting for element to be clickable: {}", locator);
        return wait.until(
                ExpectedConditions.elementToBeClickable(locator));
    }

    public WebElement waitForClickable(WebElement element) {
        log.debug("Waiting for WebElement to be clickable");
        return wait.until(
                ExpectedConditions.elementToBeClickable(element));
    }

    // ══════════════════════════════════════════════════════════════════════════
    // PRESENCE WAITS
    // ══════════════════════════════════════════════════════════════════════════

    // Waits until element exists in DOM — does NOT mean it's visible
    // Use for hidden elements you need to read attributes from
    public WebElement waitForPresence(By locator) {
        log.debug("Waiting for element presence in DOM: {}", locator);
        return wait.until(
                ExpectedConditions.presenceOfElementLocated(locator));
    }

    public List<WebElement> waitForPresenceOfAll(By locator) {
        log.debug("Waiting for all elements presence: {}", locator);
        return wait.until(
                ExpectedConditions.presenceOfAllElementsLocatedBy(locator));
    }

    // ══════════════════════════════════════════════════════════════════════════
    // INVISIBILITY / DISAPPEARANCE WAITS
    // ══════════════════════════════════════════════════════════════════════════

    // Waits until a loading spinner / overlay disappears before proceeding
    // Essential after clicking buttons that trigger async operations
    public void waitForInvisibility(By locator) {
        log.debug("Waiting for element to disappear: {}", locator);
        wait.until(
                ExpectedConditions.invisibilityOfElementLocated(locator));
    }

    public void waitForInvisibility(WebElement element) {
        log.debug("Waiting for WebElement to disappear");
        wait.until(
                ExpectedConditions.invisibilityOf(element));
    }

    // ══════════════════════════════════════════════════════════════════════════
    // URL AND TITLE WAITS
    // ══════════════════════════════════════════════════════════════════════════

    // Waits until current URL contains the given fragment
    // Use after a click that triggers navigation
    public void waitForUrlContains(String urlFragment) {
        log.debug("Waiting for URL to contain: {}", urlFragment);
        wait.until(
                ExpectedConditions.urlContains(urlFragment));
    }

    public void waitForUrlToBe(String exactUrl) {
        log.debug("Waiting for URL to be: {}", exactUrl);
        wait.until(
                ExpectedConditions.urlToBe(exactUrl));
    }

    public void waitForTitleContains(String titleFragment) {
        log.debug("Waiting for page title to contain: {}", titleFragment);
        wait.until(
                ExpectedConditions.titleContains(titleFragment));
    }

    // ══════════════════════════════════════════════════════════════════════════
    // TEXT WAITS
    // ══════════════════════════════════════════════════════════════════════════

    // Waits until an element contains specific text
    // Useful for asserting dynamic content has loaded (e.g. search results count)
    public void waitForTextPresent(By locator, String text) {
        log.debug("Waiting for text '{}' in element: {}", text, locator);
        wait.until(
                ExpectedConditions.textToBePresentInElementLocated(locator, text));
    }

    public void waitForTextPresent(WebElement element, String text) {
        log.debug("Waiting for text '{}' in WebElement", text);
        wait.until(
                ExpectedConditions.textToBePresentInElement(element, text));
    }

    // Waits for an element's value attribute to contain text
    // Use for input fields where you need to verify text was typed correctly
    public void waitForValuePresent(By locator, String value) {
        log.debug("Waiting for value '{}' in element: {}", value, locator);
        wait.until(
                ExpectedConditions.textToBePresentInElementValue(locator, value));
    }

    // ══════════════════════════════════════════════════════════════════════════
    // ATTRIBUTE WAITS
    // ══════════════════════════════════════════════════════════════════════════

    // Waits until an element's attribute contains a specific value
    // Common use: waiting for a class like "active" or "selected" to appear
    public void waitForAttributeContains(By locator,
                                          String attribute,
                                          String value) {
        log.debug("Waiting for attribute '{}' to contain '{}' on: {}",
                attribute, value, locator);
        wait.until(
                ExpectedConditions.attributeContains(locator, attribute, value));
    }

    public void waitForAttributeContains(WebElement element,
                                          String attribute,
                                          String value) {
        log.debug("Waiting for attribute '{}' to contain '{}'", attribute, value);
        wait.until(
                ExpectedConditions.attributeContains(element, attribute, value));
    }

    // ══════════════════════════════════════════════════════════════════════════
    // ALERT WAITS
    // ══════════════════════════════════════════════════════════════════════════

    // Waits for a JavaScript alert/confirm/prompt to appear
    // Call this before driver.switchTo().alert()
    public Alert waitForAlert() {
        log.debug("Waiting for alert to appear");
        return wait.until(
                ExpectedConditions.alertIsPresent());
    }

    // ══════════════════════════════════════════════════════════════════════════
    // FRAME WAITS
    // ══════════════════════════════════════════════════════════════════════════

    // Waits for an iframe to load and switches into it in one call
    public void waitAndSwitchToFrame(By locator) {
        log.debug("Waiting for frame and switching: {}", locator);
        wait.until(
                ExpectedConditions.frameToBeAvailableAndSwitchToIt(locator));
    }

    public void waitAndSwitchToFrame(int index) {
        log.debug("Waiting for frame index {} and switching", index);
        wait.until(
                ExpectedConditions.frameToBeAvailableAndSwitchToIt(index));
    }

    // ══════════════════════════════════════════════════════════════════════════
    // FLUENT WAIT — for dynamic / intermittent elements
    // ══════════════════════════════════════════════════════════════════════════

    // FluentWait polls repeatedly until condition is met or timeout expires
    // Better than WebDriverWait for elements that flicker in and out of the DOM
    // (e.g. auto-suggestions, lazy-loaded content)
    public WebElement fluentWaitForVisible(By locator) {
        log.debug("Fluent waiting for element: {}", locator);
        return fluentWait.until(
                ExpectedConditions.visibilityOfElementLocated(locator));
    }

    // Custom condition version — pass any lambda as the condition
    // e.g. fluentWaitFor(d -> d.findElements(By.css(".item")).size() > 3)
    public <T> T fluentWaitFor(ExpectedCondition<T> condition) {
        log.debug("Fluent waiting for custom condition");
        return fluentWait.until(condition);
    }

    // ══════════════════════════════════════════════════════════════════════════
    // QUICK CHECKS — short timeout, no exception on failure
    // ══════════════════════════════════════════════════════════════════════════

    // Returns true if element appears within 3 seconds, false otherwise
    // Use in conditional logic: if (waitUtils.isDisplayed(By.id("banner"))) { ... }
    // Does NOT throw — safe to use as a boolean check
    public boolean isDisplayed(By locator) {
        try {
            shortWait.until(
                    ExpectedConditions.visibilityOfElementLocated(locator));
            return true;
        } catch (TimeoutException | NoSuchElementException e) {
            log.debug("Element not visible within short wait: {}", locator);
            return false;
        }
    }

    // Returns true if element is present in DOM within 3 seconds
    public boolean isPresent(By locator) {
        try {
            shortWait.until(
                    ExpectedConditions.presenceOfElementLocated(locator));
            return true;
        } catch (TimeoutException | NoSuchElementException e) {
            log.debug("Element not present within short wait: {}", locator);
            return false;
        }
    }

    // ══════════════════════════════════════════════════════════════════════════
    // STALE ELEMENT RETRY
    // ══════════════════════════════════════════════════════════════════════════

    // Retries finding and clicking an element up to maxRetries times
    // Handles StaleElementReferenceException caused by DOM refreshes
    // Common on SPAs where React/Angular re-renders parts of the page
    public void clickWithRetry(By locator, int maxRetries) {
        int attempts = 0;
        while (attempts < maxRetries) {
            try {
                waitForClickable(locator).click();
                log.debug("Clicked element on attempt {}: {}", attempts + 1, locator);
                return;
            } catch (StaleElementReferenceException e) {
                attempts++;
                log.warn("StaleElementReferenceException on attempt {} for: {}",
                        attempts, locator);
                if (attempts == maxRetries) {
                    throw new RuntimeException(
                            "Element still stale after " + maxRetries +
                            " retries: " + locator, e);
                }
            }
        }
    }

    // ══════════════════════════════════════════════════════════════════════════
    // PAGE LOAD WAIT
    // ══════════════════════════════════════════════════════════════════════════

    // Waits until document.readyState is "complete"
    // Use after navigation or form submission to ensure the page fully loaded
    public void waitForPageLoad() {
        log.debug("Waiting for page to fully load");
        wait.until((ExpectedCondition<Boolean>) d -> {
            String state = ((JavascriptExecutor) d)
                    .executeScript("return document.readyState")
                    .toString();
            return state.equals("complete");
        });
    }

    // Waits for all pending jQuery AJAX calls to finish
    // Use on pages that use jQuery for async data loading
    public void waitForJQueryLoad() {
        log.debug("Waiting for jQuery AJAX to complete");
        wait.until((ExpectedCondition<Boolean>) d -> {
            try {
                Long result = (Long) ((JavascriptExecutor) d)
                        .executeScript("return jQuery.active");
                return result == 0;
            } catch (Exception e) {
                // jQuery not present on this page — that's fine, carry on
                return true;
            }
        });
    }
}