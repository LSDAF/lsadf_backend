Feature: Admin GameSave Controller BDD tests

  Background:
    Given the BDD engine is ready
    And the cache is enabled
    And a clean database
    And the time clock set to the present
    And the following game saves
      | id                                   | userEmail           | gold | diamond | emerald | amethyst | healthPoints | attack | currentStage | maxStage | nickname |
      | 0530e1fe-3428-4edd-bb32-cb563419d0bd | paul.ochon@test.com | 1    | 5       | 2       | 5        | 100          | 10     | 10           | 10       | test-1   |
      | 804af894-931b-4ee6-968f-1703689066fb | paul.ochon@test.com | 3    | 3       | 2       | 15       | 100          | 10     | 1000         | 1000     | test-2   |
      | 6a4f12dc-4e83-40f7-992e-8f2e04375d74 | paul.ochon@test.com | 5    | 1       | 2       | 25       | 100          | 10     | 100000       | 100000   | test-3   |

    # We assume we have at least the following user in keycloak
    # paul.ochon@test.com: ADMIN,USER: toto1234: ce60ea41-3765-4562-8c96-8673de8f96b0

  Scenario: A user gets all the game saves
    When the user logs in with the following credentials
      | username            | password |
      | paul.ochon@test.com | toto1234 |
    And the user requests the admin endpoint to get all the game saves ordered by NONE

    Then the response status code should be 200
    And the response should have the following GameSaves
      | id                                   | userEmail           | gold | diamond | emerald | amethyst | healthPoints | attack | currentStage | maxStage | nickname |
      | 0530e1fe-3428-4edd-bb32-cb563419d0bd | paul.ochon@test.com | 1    | 5       | 2       | 5        | 100          | 10     | 10           | 10       | test-1   |
      | 804af894-931b-4ee6-968f-1703689066fb | paul.ochon@test.com | 3    | 3       | 2       | 15       | 100          | 10     | 1000         | 1000     | test-2   |
      | 6a4f12dc-4e83-40f7-992e-8f2e04375d74 | paul.ochon@test.com | 5    | 1       | 2       | 25       | 100          | 10     | 100000       | 100000   | test-3   |

  Scenario: A user gets all the game saves in a specific order
    When the user logs in with the following credentials
      | username            | password |
      | paul.ochon@test.com | toto1234 |
    And the user requests the admin endpoint to get all the game saves ordered by MAX_STAGE_DESC

    Then the response status code should be 200
    And the response should have the following GameSaves in exact order
      | id                                   | userEmail           | gold | diamond | emerald | amethyst | healthPoints | attack | currentStage | maxStage | nickname |
      | 6a4f12dc-4e83-40f7-992e-8f2e04375d74 | paul.ochon@test.com | 5    | 1       | 2       | 25       | 100          | 10     | 100000       | 100000   | test-3   |
      | 804af894-931b-4ee6-968f-1703689066fb | paul.ochon@test.com | 3    | 3       | 2       | 15       | 100          | 10     | 1000         | 1000     | test-2   |
      | 0530e1fe-3428-4edd-bb32-cb563419d0bd | paul.ochon@test.com | 1    | 5       | 2       | 5        | 100          | 10     | 10           | 10       | test-1   |


  Scenario: A user gets a game save by its id
    When the user logs in with the following credentials
      | username            | password |
      | paul.ochon@test.com | toto1234 |

    And the user requests the admin endpoint to get a game save with the following id 6a4f12dc-4e83-40f7-992e-8f2e04375d74

    Then the response status code should be 200

    And the response should have the following GameSave
      | id                                   | userEmail           | gold | diamond | emerald | amethyst | healthPoints | attack | currentStage | maxStage | nickname |
      | 6a4f12dc-4e83-40f7-992e-8f2e04375d74 | paul.ochon@test.com | 5    | 1       | 2       | 25       | 100          | 10     | 100000       | 100000   | test-3   |


  Scenario: A user gets a non-existing game save by its id
    When the user logs in with the following credentials
      | username            | password |
      | paul.ochon@test.com | toto1234 |
    And the user requests the admin endpoint to get a game save with the following id 41c2ed05-4281-45c1-9232-e63a224f3b04

    Then the response status code should be 404

  Scenario: A user gets all the games saves of a user
    When the user logs in with the following credentials
      | username            | password |
      | paul.ochon@test.com | toto1234 |
    And the user requests the admin endpoint to get all the game saves of the user with the following username paul.ochon@test.com

    Then the response status code should be 200
    And the response should have the following GameSaves
      | id                                   | userEmail           | gold | diamond | emerald | amethyst | healthPoints | attack | currentStage | maxStage | nickname |
      | 0530e1fe-3428-4edd-bb32-cb563419d0bd | paul.ochon@test.com | 1    | 5       | 2       | 5        | 100          | 10     | 10           | 10       | test-1   |
      | 804af894-931b-4ee6-968f-1703689066fb | paul.ochon@test.com | 3    | 3       | 2       | 15       | 100          | 10     | 1000         | 1000     | test-2   |
      | 6a4f12dc-4e83-40f7-992e-8f2e04375d74 | paul.ochon@test.com | 5    | 1       | 2       | 25       | 100          | 10     | 100000       | 100000   | test-3   |


  Scenario: A user gets all the games saves of a non-existing user
    When the user logs in with the following credentials
      | username            | password |
      | paul.ochon@test.com | toto1234 |
    And the user requests the admin endpoint to get all the game saves of the user with the following username paul.onais@test.com

    Then the response status code should be 404

  Scenario: A user generates a new game save
    When the user logs in with the following credentials
      | username            | password |
      | paul.ochon@test.com | toto1234 |
    And the user requests the admin endpoint to create a new game save with the following AdminGameSaveCreationRequest
      | id                                   | userEmail           | gold | diamond | emerald | amethyst | healthPoints | attack | currentStage | maxStage | nickname |
      | 45c2736f-6c3b-4955-9b26-ce515091810b | paul.ochon@test.com | 1    | 2       | 3       | 4        | 5            | 6      | 7            | 8        | newgame  |

    Then the response status code should be 200
    And the response should have the following GameSave
      | id                                   | userEmail           | gold | diamond | emerald | amethyst | healthPoints | attack | currentStage | maxStage | nickname |
      | 45c2736f-6c3b-4955-9b26-ce515091810b | paul.ochon@test.com | 1    | 2       | 3       | 4        | 5            | 6      | 7            | 8        | newgame  |

  Scenario: A user generates a new game save with an existing id
    When the user logs in with the following credentials
      | username            | password |
      | paul.ochon@test.com | toto1234 |
    And the user requests the admin endpoint to create a new game save with the following AdminGameSaveCreationRequest
      | id                                   | userEmail           | gold | diamond | emerald | amethyst | healthPoints | attack | currentStage | maxStage | nickname |
      | 0530e1fe-3428-4edd-bb32-cb563419d0bd | paul.ochon@test.com | 1    | 2       | 3       | 4        | 5            | 6      | 7            | 8        | newgame  |

    Then the response status code should be 400

  Scenario: A user generates a new game save with a non-existing username
    When the user logs in with the following credentials
      | username            | password |
      | paul.ochon@test.com | toto1234 |
    And the user requests the admin endpoint to create a new game save with the following AdminGameSaveCreationRequest
      | id                                   | userEmail                 | gold | diamond | emerald | amethyst | healthPoints | attack | currentStage | maxStage | nickname |
      | 0530e1fe-3428-4edd-bb32-cb563419d0bd | paul.kalkbrenner@test.com | 1    | 2       | 3       | 4        | 5            | 6      | 7            | 8        | newgame  |

    Then the response status code should be 404


  Scenario: A user updates a game save
    When the user logs in with the following credentials
      | username            | password |
      | paul.ochon@test.com | toto1234 |
    And the user requests the admin endpoint to update the game save with id 6a4f12dc-4e83-40f7-992e-8f2e04375d74 with the following AdminGameSaveUpdateRequest
      | healthPoints | attack | nickname |
      | 100000       | 599    | test-ZZZ |

    Then the response status code should be 200
    And the response should have the following GameSave
      | id                                   | userEmail           | gold | diamond | emerald | amethyst | healthPoints | attack | currentStage | maxStage | nickname |
      | 6a4f12dc-4e83-40f7-992e-8f2e04375d74 | paul.ochon@test.com | 5    | 1       | 2       | 25       | 100000       | 599    | 100000       | 100000   | test-ZZZ |

  Scenario: A user updates a non-existing game save
    When the user logs in with the following credentials
      | username            | password |
      | paul.ochon@test.com | toto1234 |
    And the user requests the admin endpoint to update the game save with id 88df00e0-1529-44a1-97d6-c11218bd6003 with the following AdminGameSaveUpdateRequest
      | healthPoints | attack | nickname |
      | 100000       | 599    | test-ZZZ |

    Then the response status code should be 404

  Scenario: A user deletes a game save
    When the user logs in with the following credentials
      | username            | password |
      | paul.ochon@test.com | toto1234 |
    And the user requests the admin endpoint to delete the game save with id 6a4f12dc-4e83-40f7-992e-8f2e04375d74

    Then the response status code should be 200
    And the number of game saves should be 2

  Scenario: A user deletes a non-existing game save
    When the user logs in with the following credentials
      | username            | password |
      | paul.ochon@test.com | toto1234 |
    And the user requests the admin endpoint to delete the game save with id c666cddc-806c-45ad-b531-54e554820ce4

    Then the response status code should be 404
    And the number of game saves should be 3

  Scenario: A user updates the characteristics of a game save
    When the user logs in with the following credentials
      | username            | password |
      | paul.ochon@test.com | toto1234 |
    And the user requests the admin endpoint to update the characteristics of the game save with id 0530e1fe-3428-4edd-bb32-cb563419d0bd with the following CharacteristicsRequest
      | attack | crit_chance | crit_damage | health | resistance |
      | 100    | 100         | 100         | 100    | 100         |

    Then the response status code should be 200

  Scenario: A user updates the characteristics of a non-existing game save
    When the user logs in with the following credentials
      | username            | password |
      | paul.ochon@test.com | toto1234 |
    And the user requests the admin endpoint to update the characteristics of the game save with id 861e3a80-eade-402c-8598-3820e870c252 with the following CharacteristicsRequest
      | attack | crit_chance | crit_damage | health | resistance |
      | 100    | 100         | 100         | 100    | 100         |
    Then the response status code should be 404

  Scenario: A user updates the currencies of a game save
    When the user logs in with the following credentials
      | username            | password |
      | paul.ochon@test.com | toto1234 |
    And the user requests the admin endpoint to update the currencies of the game save with id 0530e1fe-3428-4edd-bb32-cb563419d0bd with the following CurrencyRequest
      | gold | diamond | emerald | amethyst |
      | 100  |         | 300     |          |

    Then the response status code should be 200

  Scenario: A user updates the currencies of a non-existing game save
    When the user logs in with the following credentials
      | username            | password |
      | paul.ochon@test.com | toto1234 |
    And the user requests the admin endpoint to update the currencies of the game save with id 861e3a80-eade-402c-8598-3820e870c252 with the following CurrencyRequest
      | gold | diamond | emerald | amethyst |
      | 100  |         | 300     |          |
    Then the response status code should be 404

  Scenario: A user updates the stages of a game save
    When the user logs in with the following credentials
      | username            | password |
      | paul.ochon@test.com | toto1234 |
    And the user requests the admin endpoint to update the stages of the game save with id 0530e1fe-3428-4edd-bb32-cb563419d0bd with the following StageRequest
      | currentStage | maxStage |
      | 1000         | 2000     |
    Then the response status code should be 200

  Scenario: A user updates the stages of a non-existing game save
    When the user logs in with the following credentials
      | username            | password |
      | paul.ochon@test.com | toto1234 |
    And the user requests the admin endpoint to update the stages of the game save with id e97e0aa2-f380-4833-9e9e-79284379000b with the following StageRequest
      | currentStage | maxStage |
      | 1000         | 1000     |

    Then the response status code should be 404