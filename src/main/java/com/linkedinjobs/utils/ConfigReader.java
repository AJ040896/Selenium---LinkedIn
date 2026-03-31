package com.linkedinjobs.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.cdimascio.dotenv.Dotenv;

public class ConfigReader {

    private static final Logger log = LoggerFactory.getLogger(ConfigReader.class);
    private static final Dotenv dotenv;

    static {
        // Load .env file from project root
        // ignoreIfMissing() — no crash in CI where env vars
        // are injected directly without a .env file
        dotenv = Dotenv.configure()
                .ignoreIfMissing()
                .filename(".env")
                .load();
        log.info("ConfigReader initialised");
    }

    private ConfigReader() {}

    // ══════════════════════════════════════════════════════════════
    // CORE RESOLVER
    // ══════════════════════════════════════════════════════════════

    // Priority chain (highest to lowest):
    // 1. System property  — -Dkey=value from CLI (mvn test -DBROWSER=firefox)
    // 2. .env file        — local developer machine
    // 3. System env var   — CI/CD injected secrets (GitHub Actions, Jenkins)
    // 4. Hardcoded default — last resort, non-sensitive values only
    private static String resolve(String key, String defaultValue) {

        // 1. System property (-D flag) — highest priority
        // key.toLowerCase() because -D flags use lowercase by convention
        String sysProp = System.getProperty(key.toLowerCase());
        if (isSet(sysProp)) {
            log.debug("'{}' resolved from system property (-D flag)", key);
            return sysProp.trim();
        }

        // 2. .env file
        String dotenvVal = dotenv.get(key, null);
        if (isSet(dotenvVal)) {
            log.debug("'{}' resolved from .env file", key);
            return dotenvVal.trim();
        }

        // 3. System environment variable (CI/CD)
        String envVar = System.getenv(key);
        if (isSet(envVar)) {
            log.debug("'{}' resolved from environment variable", key);
            return envVar.trim();
        }

        // 4. Hardcoded default
        log.debug("'{}' not found anywhere — using default: {}", key, defaultValue);
        return defaultValue;
    }

    private static boolean isSet(String value) {
        return value != null && !value.trim().isEmpty();
    }

    // ══════════════════════════════════════════════════════════════
    // BROWSER
    // ══════════════════════════════════════════════════════════════

    public static String getBrowser() {
        return resolve("BROWSER", "chrome");
    }

    public static boolean isHeadless() {
        return Boolean.parseBoolean(resolve("HEADLESS", "false"));
    }

    // ══════════════════════════════════════════════════════════════
    // APPLICATION
    // ══════════════════════════════════════════════════════════════

    public static String getBaseUrl() {
        return resolve("BASE_URL", "https://www.linkedin.com");
    }

    // ══════════════════════════════════════════════════════════════
    // TIMEOUTS
    // ══════════════════════════════════════════════════════════════

    public static int getExplicitWait() {
        return Integer.parseInt(resolve("EXPLICIT_WAIT", "10"));
    }

    public static int getPageLoadTimeout() {
        return Integer.parseInt(resolve("PAGE_LOAD_TIMEOUT", "30"));
    }

    // ══════════════════════════════════════════════════════════════
    // CREDENTIALS
    // ══════════════════════════════════════════════════════════════

    public static String getAdminUsername() {
        String username = resolve("LINKEDIN_USERNAME", "");
        if (!isSet(username)) {
            throw new RuntimeException(
                "LinkedIn username not configured.\n" +
                "Add LINKEDIN_USERNAME to your .env file."
            );
        }
        return username;
    }

    public static String getAdminPassword() {
        String password = resolve("LINKEDIN_PASSWORD", "");
        if (!isSet(password)) {
            throw new RuntimeException(
                "LinkedIn password not configured.\n" +
                "Add LINKEDIN_PASSWORD to your .env file."
            );
        }
        return password;
    }
}