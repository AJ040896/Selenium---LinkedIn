package com.linkedinjobs.pageObjects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoginPage extends BasePage{
    private static final Logger log = LoggerFactory.getLogger(LoginPage.class);
    

    @FindBy(css="a[data-tracking-control-name='guest_homepage-basic_nav-header-signin']")
    private WebElement navSignInButton;

    @FindBy(id = "username")
    private WebElement emailField;

    @FindBy(id = "password")
    private WebElement passwordField;

    @FindBy(css=".btn__primary--large[type='submit']")
    private WebElement signInButton;


    public LoginPage(WebDriver driver) {
        super(driver);
        log.info("LoginPage initialized");
    }

    public LoginPage clickNavSignIn() {
        click(navSignInButton);
        return this;
    }

    public LoginPage enterEmail(String email) {
        type(emailField, email);
        return this;
    }

    public LoginPage enterPassword(String password) {
        type(passwordField, password);
        return this;
    }

    public LoginPage clickSignIn() {
        click(signInButton);
        return this;
    }

    public LoginPage loginIntoLinkedIn(String email, String password){
        log.info("Logging in with email: {}", email);
        clickNavSignIn();
        enterEmail(email);
        enterPassword(password);
        clickSignIn();
        return this;
    }
}
