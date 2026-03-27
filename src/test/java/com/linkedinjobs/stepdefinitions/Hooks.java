package com.linkedinjobs.stepdefinitions;



import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkedinjobs.utils.ConfigReader;
import com.linkedinjobs.utils.DriverFactory;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;



public class Hooks {

    private final TextContext context;
    private static final Logger log = LoggerFactory.getLogger(Hooks.class);

    public Hooks(TextContext context) {
        this.context = context;
    }

    @Before
    public void setUp(Scenario scenario) {
        log.info("Setting up the test environment...", scenario.getName());

        DriverFactory.initDriver(ConfigReader.getBrowser());
        context.setDriver(DriverFactory.getDriver());
        context.getDriver().get(ConfigReader.getBaseUrl());

        log.info("Test environment setup complete.", ConfigReader.getBrowser());
    }

    @After
    public void tearDown(Scenario scenario) {
        

        if(scenario.isFailed()) {
            log.warn("Scenario failed: Taking Screenshot" + scenario.getName());
            byte[] screenshot = ((TakesScreenshot) context.getDriver()).getScreenshotAs(OutputType.BYTES);
            scenario.attach(screenshot, "image/png", "failure-screenshot");
        }

        DriverFactory.quitDriver();
        log.info("Test environment teardown complete.", scenario.getName());
    }
}
