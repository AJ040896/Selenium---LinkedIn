package com.linkedinjobs.pageObjects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JobDetailsPage extends BasePage {
    private static final Logger log = LoggerFactory.getLogger(JobDetailsPage.class);
    

    @FindBy(xpath="//div[@componentkey = 'JobsSearchFilters']/descendant::label[text() = 'Remote']")
    private WebElement RemoteFilter;

    @FindBy(xpath = "//div[@componentkey = 'JobsSearchFilters']/descendant::label[text() = 'Date posted']")
    private WebElement DatePostedFilter;

    @FindBy(xpath = "//div[@componentkey = 'JobsSearchFilters']/descendant::label[text() = 'Experience level']")
    private WebElement ExperienceLevelFilter;

    @FindBy(xpath = "//div[@data-component-type = 'LazyColumn']/descendant::p[text() = 'Past 24 hours']")
    private WebElement Past24HoursOption;

    @FindBy(xpath = "//span[text() = 'Show results']")
    private WebElement ShowResultsButton;

    @FindBy(xpath = "//div[@data-component-type = 'LazyColumn']/descendant::p[text() = 'Senior']")
    private WebElement SeniorExperienceOption;

    @FindBy(xpath = "//div[@data-component-type = 'LazyColumn']/descendant::p[text() = 'Entry-level']")
    private WebElement EntryLevelExperienceOption;

    @FindBy(xpath="//div[contains(@aria-label, 'Company')]/child::p/a")
    private WebElement CompanyName;

    @FindBy(xpath="(//div[@data-component-type='LazyColumn']/descendant::span[@class = '_2fc75a15'])[1]")
    private WebElement Location;

    @FindBy(xpath="//div[@class = '_1d08a0b1']/child::p/a")
    private WebElement JobTitle;

    @FindBy(xpath="//span[contains(text(), 'Apply')]/ancestor::a")
    private WebElement ApplyLink;

    @FindBy(xpath="")
    private WebElement ;

    @FindBy(xpath="")
    private WebElement ;

    @FindBy(xpath="")
    private WebElement ;






    public JobDetailsPage(WebDriver driver) {
        super(driver);
        log.info("JobDetailsPage initialized");
    }
    



}
