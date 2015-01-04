Feature: GreetingsAPI
  API version of the Greetings application

  Scenario: list greetings
    Given the greetings repository has the following greetings:
      | content      | email             | date       |
      | Hello World! | test1@example.org | 11/30/2014 |
      | Bye          | test2@example.org | 12/01/2014 |
    When the client request the list of greetings
    Then the response is a list containing 2 greetings
    And one greeting has id 1 and content "Hello World!"
    And one greeting has id 2 and content "Bye"

  Scenario: retrieve an existing greeting
    Given the greetings repository has the following greetings:
      | content      | email             | date       |
      | Hello World! | test1@example.org | 11/30/2014 |
      | Bye          | test2@example.org | 12/01/2014 |
    When the client requests greeting with id 2
    Then the response is a greeting with id 2 and content "Bye"

  Scenario: retrieve a non-existing greeting
    Given greeting with id 999 doesn't exist
    When the client requests greeting with id 999
    Then the response is status code 404
    And error message contains "Greeting with id 999 not found"
    And error url is "/greetings/999"

  Scenario: create new greeting
    When the client creates a greeting with content "Bye bye!", email "test@example.org" and date "11/30/2014"
    Then the response is status code 201
    And header "Location" points to a greeting with content "Bye bye!"

  Scenario: create new greeting with empty content
    When the client creates a greeting with content "", email "test@example.org" and date "11/30/2014"
    Then the response is status code 400
    And error message contains "Content cannot be blank"
    And error url is "/greetings"

  Scenario: create new greeting with content longer than 256 characters
    When the client creates a greeting with content "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Phasellus tincidunt, dui sed efficitur pellentesque, lorem velit varius ligula, id malesuada felis purus vitae tellus. Donec at vestibulum purus, eget hendrerit nisl. Duis aliquam leo ac magna mollis malesuada.", email "test@example.org" and date "11/30/2014"
    Then the response is status code 400
    And error message contains "Content maximum length is 256 characters long"
    And error url is "/greetings"

  Scenario: update existing greeting
    Given the greetings repository has the following greetings:
      | content      | email             | date       |
      | Hello World! | test1@example.org | 11/30/2014 |
      | Bye          | test2@example.org | 12/01/2014 |
    When the client updates greeting with id 1 with content "Just hello!", email "test1@example.org" and date "11/30/2014"
    Then the response is status code 200
    And the response is a greeting with id 1 and content "Just hello!"

  Scenario: update non-existing greeting
    Given the greetings repository has the following greetings:
      | content      | email             | date       |
      | Hello World! | test1@example.org | 11/30/2014 |
      | Bye          | test2@example.org | 12/01/2014 |
    When the client updates greeting with id 999 with content "Hello Again!", email "test@example.org" and date "11/30/2014"
    Then the response is status code 404
    And error message contains "Greeting with id 999 not found"
    And error url is "/greetings/999"

  Scenario: update existing greeting with empty content
    Given the greetings repository has the following greetings:
      | content      | email             | date       |
      | Hello World! | test1@example.org | 11/30/2014 |
      | Bye          | test2@example.org | 12/01/2014 |
    When the client updates greeting with id 1 with content "", email "test@example.org" and date "11/30/2014"
    Then the response is status code 400
    And error message contains "Content cannot be blank"
    And error url is "/greetings/1"

  Scenario: update existing greeting with content longer than 256 characters
    When the client updates greeting with id 1 with content "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Phasellus tincidunt, dui sed efficitur pellentesque, lorem velit varius ligula, id malesuada felis purus vitae tellus. Donec at vestibulum purus, eget hendrerit nisl. Duis aliquam leo ac magna mollis malesuada.", email "test@example.org" and date "11/30/2014"
    Then the response is status code 400
    And error message contains "Content maximum length is 256 characters long"
    And error url is "/greetings/1"

  Scenario: delete existing greeting
    Given the greetings repository has the following greetings:
      | content      | email             | date       |
      | Hello World! | test1@example.org | 11/30/2014 |
      | Bye          | test2@example.org | 12/01/2014 |
    When the client deletes greeting with id 1
    Then the response is status code 200
    And the response is empty

  Scenario: delete non-existing greeting
    Given the greetings repository has the following greetings:
      | content      | email             | date       |
      | Hello World! | test1@example.org | 11/30/2014 |
      | Bye          | test2@example.org | 12/01/2014 |
    When the client deletes greeting with id 999
    Then the response is status code 404
    And error message contains "Greeting with id 999 not found"
    And error url is "/greetings/999"
