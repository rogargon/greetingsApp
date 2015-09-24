Feature: Retrieve Greeting
  In order to be aware of current greetings
  As an API user
  I want to get the details of particular greetings

  Scenario: retrieve an existing greeting
    Given the greetings repository has the following greetings:
      | message      | email             | date       |
      | Hello World! | test1@example.org | 11/30/2014 |
      | Bye          | test2@example.org | 12/01/2014 |
    When the client requests greeting with id 2
    Then the response is a greeting with id 2 and content "Bye"

  Scenario: retrieve a non-existing greeting
    Given greeting with id 999 doesn't exist
    When the client requests greeting with id 999
    Then the response is status code 404
