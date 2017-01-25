Feature: The client can create an application
  Scenario Outline: Client creates an application
    Given The client use the name <name> and the password <pwd>
    When The client calls /applications
    Then The client should get a response with HTTP status code 201
    And The response should contain the application id <id>
    Examples:
      | name     | pwd     |
      | pollcat  | pollcat |

  Scenario Outline: The client creates a level
    Given The client use the name <name> and the threshold <threshold>
    When The client calls /levels
    Then The client should get a response with HTTP status code 201
    Examples:
      | name     | threshold |
      | Best     | 0         |

  Scenario Outline: The client creates a badge
    Given The client create the badge <name>, <image>, <points>, <repeatable>
    When The client calls /badges
    Then The client should get a response with HTTP status code 201
    Examples:
      | name      | image     | points | repeatable |
      | mockBadge | une image | 12     | true       |

  Scenario Outline: The client creates a rule
    Given The client use the name <name>, the eventT type <eventType> and the expression <expr>
    When The client calls /rules
    Then The client should get a response with HTTP status code 201
    Examples:
      | name      | eventType     | expr                                            |
      | bestRule  | mockEventType | if(payload["item"] == 0){award("mockBadge", 1);}|

  Scenario Outline: The client creates a new event
    Given The client use the type <type>, the user id <userId> and the payload <paylod>
    When The client calls /events
    Then The client should get a response with HTTP status code 200
    And The user should have 1 badge(s)
    Examples:
      | type          | userId | paylod     |
      | mockEventType | john   | {"item":0} |

  Scenario Outline: The client delete the application
    When The client calls DELETE on /applications
    Then The client should get a response with HTTP status code 204
    Examples:
      |_|
      |_|

