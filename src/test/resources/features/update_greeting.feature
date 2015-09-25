@https://www.pivotaltracker.com/story/show/104103658
Feature: Update Greeting
    In order to be able to change my mind about greeting others
    As an API user
    I want to update greetings

    Scenario: update existing greeting
      Given the users repository has the following users:
        | username          | email            |
        | testuser          | test@example.org |
      Given the greetings repository has the following greetings:
        | message      | email             | date       |
        | Hello World! | test@example.org | 11/30/2014 |
        | Bye          | test@example.org | 12/01/2014 |
      When the client updates greeting with id 1 with content "Just hello!", email "test@example.org" and date "11/30/2014"
      Then the response is status code 200
      And the response is a greeting with id 1 and content "Just hello!"
      And header "Location" points to a greeting with content "Just hello!"

    Scenario: update non-existing greeting
      Given the users repository has the following users:
        | username          | email            |
        | testuser          | test@example.org |
      Given the greetings repository has the following greetings:
        | message      | email             | date       |
        | Hello World! | test@example.org | 11/30/2014 |
        | Bye          | test@example.org | 12/01/2014 |
      When the client updates greeting with id 999 with content "Hello Again!", email "test@example.org" and date "11/30/2014"
      Then the response is status code 201
      And header "Location" points to a greeting with content "Hello Again!"

    Scenario: update existing greeting with empty content
      Given the users repository has the following users:
        | username          | email            |
        | testuser          | test@example.org |
      Given the greetings repository has the following greetings:
        | message      | email             | date       |
        | Hello World! | test@example.org | 11/30/2014 |
        | Bye          | test@example.org | 12/01/2014 |
      When the client updates greeting with id 1 with content "", email "test@example.org" and date "11/30/2014"
      Then the response is status code 422
      And error message contains "Content cannot be blank"

    Scenario: update existing greeting with content longer than 256 characters
      Given the users repository has the following users:
        | username          | email            |
        | testuser          | test@example.org |
      When the client updates greeting with id 1 with content "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Phasellus tincidunt, dui sed efficitur pellentesque, lorem velit varius ligula, id malesuada felis purus vitae tellus. Donec at vestibulum purus, eget hendrerit nisl. Duis aliquam leo ac magna mollis malesuada.", email "test@example.org" and date "11/30/2014"
      Then the response is status code 422
      And error message contains "Content maximum length is 256 characters long"
