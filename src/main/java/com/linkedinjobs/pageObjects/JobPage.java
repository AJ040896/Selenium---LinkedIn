package com.linkedinjobs.pageObjects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JobPage extends BasePage{
    
    private static final Logger log = LoggerFactory.getLogger(HomePage.class);

    @FindBy(xpath="(//*[local-name() ='svg' and @id = 'arrow-right-small']/following-sibling::span)[1]")
    private WebElement ShowAllOption;

    @FindBy(css="input[placeholder = 'Describe the job you want']")
    private WebElement SearchJobField;

    public JobPage(WebDriver driver) {
        super(driver);
        log.info("HomePage initialized");
    }

    public JobPage clickShowAllOption() {
        click(ShowAllOption);
        return this;
    }

    public JobPage enterSearchJob(String jobTitle) {
        type(SearchJobField, jobTitle);
        return this;
    }
    
}
