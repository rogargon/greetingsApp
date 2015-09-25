@https://www.pivotaltracker.com/story/show/104103434
Feature: Create Greeting
    In order to greet others
    As an API user
    I want to create greetings to be shared

    Scenario: create new greeting
      Given the users repository has the following users:
        | username          | email            |
        | testuser          | test@example.org |
      When the client creates a greeting with content "Bye bye!", email "test@example.org" and date "11/30/2014"
      Then the response is status code 201
      And header "Location" points to a greeting with content "Bye bye!"

    Scenario: create new greeting with empty content
      Given the users repository has the following users:
        | username          | email            |
        | testuser          | test@example.org |
      When the client creates a greeting with content "", email "test@example.org" and date "11/30/2014"
      Then the response is status code 422
      And error message contains "Content cannot be blank"

    Scenario: create new greeting with content longer than 256 characters
      Given the users repository has the following users:
        | username          | email            |
        | testuser          | test@example.org |
      When the client creates a greeting with content "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Phasellus tincidunt, dui sed efficitur pellentesque, lorem velit varius ligula, id malesuada felis purus vitae tellus. Donec at vestibulum purus, eget hendrerit nisl. Duis aliquam leo ac magna mollis malesuada.", email "test@example.org" and date "11/30/2014"
      Then the response is status code 422
      And error message contains "Content maximum length is 256 characters long"

    Scenario: unexisting user creates a new greeting
      Given the users repository has the following users:
        | username          | email            |
        | testuser          | test@example.org |
      When the client creates a greeting with content "Me too!", email "othertest@example.org" and date "11/30/2014"
      Then the response is status code 404
      And error message contains "Greeting author othertest@example.org not found"
