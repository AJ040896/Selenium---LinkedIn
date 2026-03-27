package com.linkedinjobs.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConfigReader {

    private static final Properties props = new Properties();
    private static final Logger log = LoggerFactory.getLogger(ConfigReader.class);

    static {
        try (InputStream in = ConfigReader.class
                .getClassLoader()
                .getResourceAsStream("config.properties")) {

            if (in == null) {
                throw new RuntimeException(
                    "config.properties not found in src/main/resources"
                );
            }
            props.load(in);
            log.info("config.properties loaded successfully");

        } catch (IOException e) {
            throw new RuntimeException("Failed to load config.properties", e);
        }
    }

    private ConfigReader() {}

    // ── Browser ────────────────────────────────────────────────────
    // System property (-Dbrowser=firefox) takes priority over config file
    // Allows CI pipelines to override without changing any code
    public static String getBrowser() {
        String sysProp = System.getProperty("browser");
        return (sysProp != null && !sysProp.trim().isEmpty())
                ? sysProp.trim()
                : props.getProperty("browser", "chrome");
    }

    // ── URLs ───────────────────────────────────────────────────────
    public static String getBaseUrl() {
        String sysProp = System.getProperty("base.url");
        return (sysProp != null && !sysProp.trim().isEmpty())
                ? sysProp.trim()
                : props.getProperty("base.url");
    }

    // ── Timeouts ───────────────────────────────────────────────────
    public static int getExplicitWait() {
        return Integer.parseInt(
            props.getProperty("explicit.wait", "10")
        );
    }

    public static int getPageLoadTimeout() {
        return Integer.parseInt(
            props.getProperty("page.load.timeout", "30")
        );
    }

    // ── Headless flag ──────────────────────────────────────────────
    public static boolean isHeadless() {
        String sysProp = System.getProperty("headless");
        if (sysProp != null) return Boolean.parseBoolean(sysProp);
        return Boolean.parseBoolean(props.getProperty("headless", "false"));
    }

    // ── Test data credentials ──────────────────────────────────────
    public static String getAdminUsername() {
        return props.getProperty("admin.username");
    }

    public static String getAdminPassword() {
        return props.getProperty("admin.password");
    }

    // ── Generic getter — for any custom property ───────────────────
    public static String getProperty(String key) {
        String value = props.getProperty(key);
        if (value == null) {
            log.warn("Property '{}' not found in config.properties", key);
        }
        return value;
    }

    public static String getProperty(String key, String defaultValue) {
        return props.getProperty(key, defaultValue);
    }
}