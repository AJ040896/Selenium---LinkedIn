package com.linkedinjobs.pageObjects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoginPage extends BasePage{
    private static final Logger log = LoggerFactory.getLogger(LoginPage.class);
    

    @FindBy(id = "username")
    private WebElement usernameField;



    public LoginPage(WebDriver driver) {
        super(driver);
        log.info("LoginPage initialized");
    }
}
