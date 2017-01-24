Feature: The client can be authenticated
  Scenario Outline: Client creates an application
    Given The client use the name <name> and the password <pwd>
    When The client calls /applications
    Then The client should get a response with HTTP status code <status>
    And The response should contain the application id <id>
    Examples:
      | name | pwd | status |
      | app  | app | 201    |