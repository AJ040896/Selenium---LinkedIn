package com.linkedinjobs.pageObjects;

import java.util.List;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkedinjobs.utils.WaitUtils;

public class BasePage {

    // ── Core fields every page object inherits ─────────────────────────────
    protected final WebDriver driver;
    protected final WaitUtils waitUtils;
    protected static final Logger log = LoggerFactory.getLogger(BasePage.class);

    // ── Constructor ────────────────────────────────────────────────────────
    // Every child page class calls super(driver) which triggers this
    // PageFactory wires @FindBy annotations to live WebElements
    public BasePage(WebDriver driver) {
        this.driver = driver;
        this.waitUtils = new WaitUtils(driver);
        PageFactory.initElements(driver, this);
        log.debug("Initialised page: {}", this.getClass().getSimpleName());
    }

    // ══════════════════════════════════════════════════════════════════════
    // NAVIGATION
    // ══════════════════════════════════════════════════════════════════════

    // Navigate to any URL and wait for page to fully load
    public void navigateTo(String url) {
        log.info("Navigating to: {}", url);
        driver.get(url);
        waitUtils.waitForPageLoad();
    }

    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    public String getPageTitle() {
        return driver.getTitle();
    }

    public void goBack() {
        log.debug("Navigating back");
        driver.navigate().back();
        waitUtils.waitForPageLoad();
    }

    public void refreshPage() {
        log.debug("Refreshing page");
        driver.navigate().refresh();
        waitUtils.waitForPageLoad();
    }

    // ══════════════════════════════════════════════════════════════════════
    // CLICK ACTIONS
    // ══════════════════════════════════════════════════════════════════════

    // Standard click — waits for element to be clickable first
    // Always use this instead of element.click() directly
    protected void click(By locator) {
        log.debug("Clicking element: {}", locator);
        waitUtils.waitForClickable(locator).click();
    }

    protected void click(WebElement element) {
        log.debug("Clicking WebElement");
        waitUtils.waitForClickable(element).click();
    }

    // JavaScript click — use when normal click is blocked by an overlay
    // or when the element is not interactable through the standard API
    protected void jsClick(WebElement element) {
        log.debug("JS clicking WebElement");
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].click();", element);
    }

    protected void jsClick(By locator) {
        log.debug("JS clicking: {}", locator);
        WebElement element = waitUtils.waitForPresence(locator);
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].click();", element);
    }

    // Actions-based click — for elements that need mouse hover first
    // e.g. dropdown menus that only appear on hover
    protected void actionsClick(WebElement element) {
        log.debug("Actions clicking WebElement");
        new Actions(driver)
                .moveToElement(element)
                .click()
                .perform();
    }

    // Double click — for file managers, inline editors, grid rows
    protected void doubleClick(WebElement element) {
        log.debug("Double clicking WebElement");
        new Actions(driver)
                .doubleClick(element)
                .perform();
    }

    // Right click — for context menus
    protected void rightClick(WebElement element) {
        log.debug("Right clicking WebElement");
        new Actions(driver)
                .contextClick(element)
                .perform();
    }

    // ══════════════════════════════════════════════════════════════════════
    // TYPE / INPUT ACTIONS
    // ══════════════════════════════════════════════════════════════════════

    // Clears existing text then types — always use for input fields
    protected void type(By locator, String text) {
        log.debug("Typing '{}' into: {}", text, locator);
        WebElement el = waitUtils.waitForVisible(locator);
        el.clear();
        el.sendKeys(text);
    }

    protected void type(WebElement element, String text) {
        log.debug("Typing '{}' into WebElement", text);
        waitUtils.waitForVisible(element).clear();
        element.sendKeys(text);
    }

    // Clears a field using keyboard shortcut — more reliable than .clear()
    // on some React/Angular inputs that ignore the clear() command
    protected void clearAndType(WebElement element, String text) {
        log.debug("Clear-and-type '{}' into WebElement", text);
        waitUtils.waitForClickable(element).click();
        element.sendKeys(Keys.chord(Keys.CONTROL, "a"));
        element.sendKeys(Keys.DELETE);
        element.sendKeys(text);
    }

    // Types without clearing — for appending text to existing content
    protected void sendKeys(WebElement element, String text) {
        log.debug("Sending keys '{}' to WebElement", text);
        waitUtils.waitForVisible(element).sendKeys(text);
    }

    // Press a single keyboard key — Enter, Tab, Escape etc.
    protected void pressKey(WebElement element, Keys key) {
        log.debug("Pressing key {} on WebElement", key.name());
        element.sendKeys(key);
    }

    // ══════════════════════════════════════════════════════════════════════
    // READ / GET ACTIONS
    // ══════════════════════════════════════════════════════════════════════

    // Gets visible text of an element — waits for it to be visible first
    protected String getText(By locator) {
        log.debug("Getting text from: {}", locator);
        return waitUtils.waitForVisible(locator).getText().trim();
    }

    protected String getText(WebElement element) {
        log.debug("Getting text from WebElement");
        return waitUtils.waitForVisible(element).getText().trim();
    }

    // Gets an attribute value (href, value, class, data-* etc.)
    protected String getAttribute(By locator, String attribute) {
        log.debug("Getting attribute '{}' from: {}", attribute, locator);
        return waitUtils.waitForPresence(locator).getAttribute(attribute);
    }

    protected String getAttribute(WebElement element, String attribute) {
        log.debug("Getting attribute '{}' from WebElement", attribute);
        return element.getAttribute(attribute);
    }

    // Gets the value of a CSS property (color, font-size, display etc.)
    protected String getCssValue(WebElement element, String property) {
        log.debug("Getting CSS '{}' from WebElement", property);
        return element.getCssValue(property);
    }

    // Gets the current value inside an input field
    protected String getInputValue(WebElement element) {
        return getAttribute(element, "value");
    }

    // ══════════════════════════════════════════════════════════════════════
    // STATE CHECKS
    // ══════════════════════════════════════════════════════════════════════

    // Returns true if element is visible on screen within short wait
    // Does NOT throw exception — safe for use in if-statements
    protected boolean isDisplayed(By locator) {
        return waitUtils.isDisplayed(locator);
    }

    protected boolean isDisplayed(WebElement element) {
        try {
            return element.isDisplayed();
        } catch (NoSuchElementException | StaleElementReferenceException e) {
            return false;
        }
    }

    // Returns true if element exists in DOM (may be hidden)
    protected boolean isPresent(By locator) {
        return waitUtils.isPresent(locator);
    }

    // Returns true if an input/checkbox/radio is enabled
    protected boolean isEnabled(WebElement element) {
        return element.isEnabled();
    }

    // Returns true if a checkbox or radio button is checked
    protected boolean isSelected(WebElement element) {
        return element.isSelected();
    }

    // Returns true if element has a specific CSS class
    protected boolean hasClass(WebElement element, String className) {
        String classes = getAttribute(element, "class");
        return classes != null && classes.contains(className);
    }

    // ══════════════════════════════════════════════════════════════════════
    // DROPDOWN ACTIONS
    // ══════════════════════════════════════════════════════════════════════

    // For native HTML <select> dropdowns only
    // For custom dropdowns (div/ul based) use click() + click()

    protected void selectByVisibleText(WebElement element, String text) {
        log.debug("Selecting '{}' by visible text", text);
        new Select(element).selectByVisibleText(text);
    }

    protected void selectByValue(WebElement element, String value) {
        log.debug("Selecting '{}' by value", value);
        new Select(element).selectByValue(value);
    }

    protected void selectByIndex(WebElement element, int index) {
        log.debug("Selecting index {} in dropdown", index);
        new Select(element).selectByIndex(index);
    }

    protected String getSelectedOption(WebElement element) {
        return new Select(element).getFirstSelectedOption().getText().trim();
    }

    protected List<WebElement> getAllOptions(WebElement element) {
        return new Select(element).getOptions();
    }

    // ══════════════════════════════════════════════════════════════════════
    // SCROLL ACTIONS
    // ══════════════════════════════════════════════════════════════════════

    // Scrolls element into the visible viewport using JavaScript
    // Use when element is off-screen and click/interaction fails
    protected void scrollToElement(WebElement element) {
        log.debug("Scrolling to element");
        ((JavascriptExecutor) driver)
                .executeScript("arguments[0].scrollIntoView({block: 'center'});",
                        element);
    }

    // Scrolls to the very bottom of the page
    protected void scrollToBottom() {
        log.debug("Scrolling to bottom of page");
        ((JavascriptExecutor) driver)
                .executeScript("window.scrollTo(0, document.body.scrollHeight);");
    }

    // Scrolls to the very top of the page
    protected void scrollToTop() {
        log.debug("Scrolling to top of page");
        ((JavascriptExecutor) driver)
                .executeScript("window.scrollTo(0, 0);");
    }

    // Scrolls by a pixel amount — positive = down, negative = up
    protected void scrollBy(int xPixels, int yPixels) {
        log.debug("Scrolling by ({}, {})", xPixels, yPixels);
        ((JavascriptExecutor) driver)
                .executeScript("window.scrollBy(arguments[0], arguments[1]);",
                        xPixels, yPixels);
    }

    // ══════════════════════════════════════════════════════════════════════
    // HOVER ACTIONS
    // ══════════════════════════════════════════════════════════════════════

    protected void hover(WebElement element) {
        log.debug("Hovering over WebElement");
        new Actions(driver)
                .moveToElement(element)
                .perform();
    }

    protected void hoverAndClick(WebElement hoverTarget, WebElement clickTarget) {
        log.debug("Hover then click");
        new Actions(driver)
                .moveToElement(hoverTarget)
                .pause(java.time.Duration.ofMillis(500))
                .click(clickTarget)
                .perform();
    }

    // ══════════════════════════════════════════════════════════════════════
    // DRAG AND DROP
    // ══════════════════════════════════════════════════════════════════════

    protected void dragAndDrop(WebElement source, WebElement target) {
        log.debug("Drag and drop");
        new Actions(driver)
                .dragAndDrop(source, target)
                .perform();
    }

    // ══════════════════════════════════════════════════════════════════════
    // WINDOW AND FRAME HANDLING
    // ══════════════════════════════════════════════════════════════════════

    // Switches to a newly opened browser tab or popup window
    // Call this right after an action that opens a new window
    protected void switchToNewWindow() {
        String original = driver.getWindowHandle();
        log.debug("Switching from window: {}", original);
        for (String handle : driver.getWindowHandles()) {
            if (!handle.equals(original)) {
                driver.switchTo().window(handle);
                log.debug("Switched to window: {}", handle);
                return;
            }
        }
        throw new RuntimeException("No new window found to switch to");
    }

    protected void switchToMainWindow(String mainWindowHandle) {
        log.debug("Switching back to main window");
        driver.switchTo().window(mainWindowHandle);
    }

    protected String getMainWindowHandle() {
        return driver.getWindowHandle();
    }

    protected void closeCurrentWindow() {
        log.debug("Closing current window");
        driver.close();
    }

    // Switches to an iframe — use By locator to wait for it first
    protected void switchToFrame(By locator) {
        log.debug("Switching to frame: {}", locator);
        waitUtils.waitAndSwitchToFrame(locator);
    }

    protected void switchToFrame(int index) {
        log.debug("Switching to frame index: {}", index);
        waitUtils.waitAndSwitchToFrame(index);
    }

    // Always call this after finishing work inside an iframe
    protected void switchToDefaultContent() {
        log.debug("Switching back to default content");
        driver.switchTo().defaultContent();
    }

    // ══════════════════════════════════════════════════════════════════════
    // ALERT HANDLING
    // ══════════════════════════════════════════════════════════════════════

    protected void acceptAlert() {
        log.debug("Accepting alert");
        waitUtils.waitForAlert().accept();
    }

    protected void dismissAlert() {
        log.debug("Dismissing alert");
        waitUtils.waitForAlert().dismiss();
    }

    protected String getAlertText() {
        log.debug("Getting alert text");
        return waitUtils.waitForAlert().getText();
    }

    protected void typeInAlert(String text) {
        log.debug("Typing in alert: {}", text);
        Alert alert = waitUtils.waitForAlert();
        alert.sendKeys(text);
        alert.accept();
    }

    // ══════════════════════════════════════════════════════════════════════
    // JAVASCRIPT UTILITIES
    // ══════════════════════════════════════════════════════════════════════

    // Highlights an element with a red border — use during debugging
    // to visually confirm you are targeting the right element
    protected void highlightElement(WebElement element) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript(
                "arguments[0].style.border='3px solid red'", element);
    }

    // Sets the value of an input field directly via JavaScript
    // Use when sendKeys() doesn't work (e.g. read-only fields, date pickers)
    protected void setValueByJS(WebElement element, String value) {
        log.debug("Setting value via JS: {}", value);
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].value=arguments[1];", element, value);
    }

    // Executes any arbitrary JavaScript and returns the result
    protected Object executeJS(String script, Object... args) {
        return ((JavascriptExecutor) driver).executeScript(script, args);
    }

    // ══════════════════════════════════════════════════════════════════════
    // SCREENSHOT
    // ══════════════════════════════════════════════════════════════════════

    // Returns a screenshot as byte array — used in Hooks @After to
    // attach to Cucumber report on failure
    public byte[] takeScreenshot() {
        log.debug("Taking screenshot");
        return ((TakesScreenshot) driver)
                .getScreenshotAs(OutputType.BYTES);
    }

    // ══════════════════════════════════════════════════════════════════════
    // WAIT DELEGATION — convenience shortcuts
    // ══════════════════════════════════════════════════════════════════════

    // These let page objects call waitFor methods without prefixing waitUtils
    // e.g.  waitForVisible(By.id("x"))  instead of  waitUtils.waitForVisible(...)

    protected WebElement waitForVisible(By locator) {
        return waitUtils.waitForVisible(locator);
    }

    protected WebElement waitForVisible(WebElement element) {
        return waitUtils.waitForVisible(element);
    }

    protected WebElement waitForClickable(By locator) {
        return waitUtils.waitForClickable(locator);
    }

    protected WebElement waitForClickable(WebElement element) {
        return waitUtils.waitForClickable(element);
    }

    protected void waitForInvisibility(By locator) {
        waitUtils.waitForInvisibility(locator);
    }

    protected void waitForUrlContains(String fragment) {
        waitUtils.waitForUrlContains(fragment);
    }

    protected void waitForPageLoad() {
        waitUtils.waitForPageLoad();
    }
}