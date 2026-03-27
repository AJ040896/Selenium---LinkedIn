package com.linkedinjobs.utils;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.bonigarcia.wdm.WebDriverManager;



public class DriverFactory {

    private static final ThreadLocal<WebDriver> driverPool = new ThreadLocal<>();

    private static final Logger log = LoggerFactory.getLogger(DriverFactory.class);

    private DriverFactory() {
        // Private constructor to prevent instantiation
    }

    public static void initDriver(String browser) {
        WebDriver driver;

        switch (browser.trim().toLowerCase()) {

            case "chrome":
                WebDriverManager.chromedriver().setup();
                driver = new ChromeDriver(getChromeOptions(false));
                break;

            case "chrome-headless":
                WebDriverManager.chromedriver().setup();
                driver = new ChromeDriver(getChromeOptions(true));
                break;

            case "firefox":
                WebDriverManager.firefoxdriver().setup();
                driver = new FirefoxDriver(getFirefoxOptions(false));
                break;

            case "firefox-headless":
                WebDriverManager.firefoxdriver().setup();
                driver = new FirefoxDriver(getFirefoxOptions(true));
                break;

            case "edge":
                WebDriverManager.edgedriver().setup();
                driver = new EdgeDriver(getEdgeOptions());
                break;

            default:
                throw new IllegalArgumentException(
                    "Unsupported browser: " + browser +
                    ". Use: chrome, firefox, edge, chrome-headless"
                );
        }

        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(0));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));

        driverPool.set(driver);
        log.info("Initialized WebDriver for browser: {} on thread: {}: " , browser, Thread.currentThread().getName());

    }


    public static WebDriver getDriver() {
        WebDriver driver = driverPool.get();
        if (driver == null) {
            throw new IllegalStateException(
                "Driver not initialised for thread: " +
                Thread.currentThread().getName() +
                ". Did you call initDriver() in @Before?"
            );
        }
        return driver;
    }

    public static void quitDriver() {
        WebDriver driver = driverPool.get();
        if (driver != null) {
            try {
                driver.quit();
                log.info("Browser closed on thread: {}",
                    Thread.currentThread().getName());
            } catch (Exception e) {
                log.warn("Error closing browser: {}", e.getMessage());
            } finally {
                driverPool.remove();
            }
        }
    }


    private static ChromeOptions getChromeOptions(boolean headless) {
        ChromeOptions opts = new ChromeOptions();
        if (headless) opts.addArguments("--headless=new");
        opts.addArguments(
            "--no-sandbox",           // needed in Docker/CI
            "--disable-dev-shm-usage", // prevents memory issues in CI
            "--disable-gpu",           // stability in headless
            "--window-size=1920,1080", // consistent viewport
            "--disable-extensions",
            "--disable-notifications"
        );
        opts.setExperimentalOption("excludeSwitches",
            List.of("enable-automation"));  // hides automation banner
        return opts;
    }

    private static FirefoxOptions getFirefoxOptions(boolean headless) {
        FirefoxOptions opts = new FirefoxOptions();
        if (headless) opts.addArguments("--headless");
        opts.addPreference("dom.webnotifications.enabled", false);
        return opts;
    }

    private static EdgeOptions getEdgeOptions() {
        EdgeOptions opts = new EdgeOptions();
        opts.addArguments("--no-sandbox", "--disable-dev-shm-usage");
        return opts;
    }

}


