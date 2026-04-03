package com.linkedinjobs.pageObjects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HomePage extends BasePage{
    
    private static final Logger log = LoggerFactory.getLogger(HomePage.class);

    @FindBy(xpath="//button[contains(@aria-label, 'Jobs')]")
    private WebElement JobsIcon;

    public HomePage(WebDriver driver) {
        super(driver);
        log.info("HomePage initialized");
    }

    public HomePage clickJobsIcon() {
        click(JobsIcon);
        return this;
    }
    
}
