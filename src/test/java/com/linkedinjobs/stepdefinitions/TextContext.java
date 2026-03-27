package com.linkedinjobs.stepdefinitions;

import org.openqa.selenium.WebDriver;

import com.linkedinjobs.pageObjects.LoginPage;

public class TextContext {

    private WebDriver driver;

    public TextContext() {
    }

    public void setDriver(WebDriver driver) {
        this.driver = driver;
    }

    public WebDriver getDriver() {
        return driver;
    }

    // Objects of all the Pages

    public LoginPage getLoginPage() {
        return new LoginPage(driver);
    }

}
