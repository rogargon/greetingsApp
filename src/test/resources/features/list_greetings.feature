@https://www.pivotaltracker.com/story/show/104103006
Feature: List Greetings
    In order to be aware of current greetings
    As an API user
    I want to get a list of existing greetings

  Scenario: list greetings
    Given the greetings repository has the following greetings:
        | message      | email             | date       |
        | Hello World! | test1@example.org | 11/30/2014 |
        | Bye          | test2@example.org | 12/01/2014 |
    When the client request the list of greetings
    Then the response is a list containing 2 greetings
    And one greeting has id 1 and content "Hello World!"
    And one greeting has id 2 and content "Bye"

  Scenario: list empty greetings
    Given the greetings repository has no greetings
    When the client request the list of greetings
    Then the response has no greetings
