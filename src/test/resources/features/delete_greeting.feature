@https://www.pivotaltracker.com/story/show/104103484
Feature: Delete Greeting
    In order to be able to change my mind about greeting others
    As an API user
    I want to delete greetings

    Scenario: delete existing greeting
      Given the users repository has the following users:
        | username          | email            |
        | testuser          | test@example.org |
      Given the greetings repository has the following greetings:
        | message      | email             | date       |
        | Hello World! | test1@example.org | 11/30/2014 |
        | Bye          | test2@example.org | 12/01/2014 |
      When the client deletes greeting with id 1
      Then the response is status code 204
      And the response is empty
      When the client requests greeting with id 1
      Then the response is status code 404

    Scenario: delete non-existing greeting
      Given the users repository has the following users:
        | username          | email            |
        | testuser          | test@example.org |
      Given the greetings repository has the following greetings:
        | message      | email             | date       |
        | Hello World! | test1@example.org | 11/30/2014 |
        | Bye          | test2@example.org | 12/01/2014 |
      When the client deletes greeting with id 999
      Then the response is status code 404
