# LinkedIn Automation Framework

Selenium Java + TestNG + Cucumber BDD framework for LinkedIn job search automation.

## Tech Stack
- Java 11
- Selenium WebDriver 4.x
- TestNG 7.x
- Cucumber 7.x
- PicoContainer (DI)
- Maven
- SLF4J + Log4j2

## Setup

1. Clone the repo
```
   git clone https://github.com/your-username/linkedin-automation-framework.git
```

2. Copy the config template and fill in your credentials
```
   cp src/main/resources/config.properties.template src/main/resources/config.properties
```

3. Edit `config.properties` with your LinkedIn credentials

4. Run all tests
```
   mvn test
```

5. Run specific tags
```
   mvn test -Dcucumber.filter.tags="@smoke"
   mvn test -Dbrowser=chrome-headless
```

## Project Structure
```
src/main/java/com/linkedinjobs/
├── pages/          # Page Object classes
├── utils/          # DriverFactory, ConfigReader, WaitUtils, ReportUtils
└── model/          # POJOs (JobListing)

src/test/java/com/linkedinjobs/
├── stepdefinitions/ # Cucumber step definitions + Hooks
└── runner/          # TestRunner

src/test/resources/
├── features/        # Gherkin feature files
└── testdata/        # Test data JSON files
```

## Reports
After a test run, reports are generated in:
- `target/cucumber-reports/report.html` — Cucumber HTML report
- `target/reports/` — Extracted job listings (JSON + CSV)