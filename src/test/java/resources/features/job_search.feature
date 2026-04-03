@smoke @job-search
Feature: LinkedIn job search and extraction

  Background:
    Given the user is logged into LinkedIn

  @positive @remote-jobs
  Scenario Outline: Search and extract remote job listings
    Given the user navigates to the LinkedIn Jobs page
    When  the user searches for "<role>" in "<location>"
    And   the user applies the following filters
      | filter      | value          |
      | Work type   | Remote         |
      | Date posted | Past 24 hours  |
      | Experience  | Mid-Senior     |
    And   the user scrolls through all job results
    Then  the job listings should be extracted and saved to a report
    And   the report should contain at least <min_count> jobs
    And   each job should have title, company, location and apply link

    Examples:
      | role                   | location | min_count |
      | QA Automation Engineer | India    | 5         |
      | SDET                   | India    | 3         |
      | Software Test Engineer | Remote   | 5         |

  @easy-apply
  Scenario: Identify Easy Apply jobs from search results
    Given the user navigates to the LinkedIn Jobs page
    When  the user searches for "QA Automation Engineer" in "India"
    And   the user filters by Easy Apply only
    And   the user scrolls through all job results
    Then  all extracted jobs should have Easy Apply badge
    And   the report should be saved as "easy_apply_jobs.json"

  @smoke
  Scenario: Job search returns results for a valid role
    Given the user navigates to the LinkedIn Jobs page
    When  the user searches for "Selenium Java" in "India"
    Then  the job results panel should be displayed
    And   at least 1 job should be visible in the results