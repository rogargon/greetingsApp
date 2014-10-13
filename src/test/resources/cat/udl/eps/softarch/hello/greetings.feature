Feature: GreetingsJSONAPI
  JSON API version of the Greetings application

  Scenario: list greetings
    Given the greetings repository has the following greetings:
      | Hello World! |
      | Bye          |
    When the client request the list of greetings
    Then the response is a list containing 2 greetings
    And one greeting has id 1 and content "Hello World!"
    And one greeting has id 2 and content "Bye"

  Scenario: retrieve an existing greeting
    Given the greetings repository has the following greetings:
      | Hello World! |
      | Bye          |
    When the client requests greeting with id 2
    Then the response is a greeting with id 2 and content "Bye"