Feature: GameSave Controller BDD tests

  Background:
    Given the BDD engine is ready
    And a clean database
    And the time clock set to the present

    # We assume we have the two following users in keycloak
    # paul.ochon@test.com: ADMIN,USER: toto1234
    # paul.itesse@test.com: USER: toto5678

  Scenario: A User creates a new GameSave
    When the user logs in with the following credentials
      | username            | password |
      | paul.ochon@test.com | toto1234 |

    And the user requests the endpoint to generate a GameSave

    Then the response status code should be 200
    And the response should have the following GameSave
      | userId                               | userEmail           | gold | diamond | emerald | amethyst | healthPoints | attack | currentStage | maxStage |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | paul.ochon@test.com | 0    | 0       | 0       | 0        | 10           | 1      | 1            | 1        |

  Scenario: A user tries to update a GameSave with invalid id
    Given the following game saves
      | id                                   | userEmail           | gold | diamond | emerald | amethyst | healthPoints | attack | currentStage | maxStage |
      | 0530e1fe-3428-4edd-bb32-cb563419d0bd | paul.ochon@test.com | 1000 | 1000    | 1000    | 1000     | 100          | 10     | 1000         | 1000     |

    When the user logs in with the following credentials
      | username            | password |
      | paul.ochon@test.com | toto1234 |

    And the user requests the endpoint to update a GameSave with id d06664b0-5c4c-4d0b-a253-4f742b470bfd with the following GameSaveUpdateNicknameRequest
      | nickname   |
      | Play3r-0n3 |

    Then the response status code should be 404

  Scenario: A user tries to update a non-owned GameSave
    Given the following game saves
      | id                                   | userEmail           | gold | diamond | emerald | amethyst | healthPoints | attack | currentStage | maxStage |
      | 0530e1fe-3428-4edd-bb32-cb563419d0bd | paul.itesse@test.com | 1000 | 1000    | 1000    | 1000     | 100          | 10     | 1000         | 1000     |

    When the user logs in with the following credentials
      | username            | password |
      | paul.ochon@test.com | toto1234 |

    And the user requests the endpoint to update a GameSave with id 0530e1fe-3428-4edd-bb32-cb563419d0bd with the following GameSaveUpdateNicknameRequest
      | nickname   |
      | Play3r-0n3 |

    Then the response status code should be 403

  Scenario: A user updates an owned GameSave with valid custom nickname
    Given the following game saves
      | id                                   | userEmail           | gold | diamond | emerald | amethyst | healthPoints | attack | currentStage | maxStage | nickname |
      | 0530e1fe-3428-4edd-bb32-cb563419d0bd | paul.ochon@test.com | 1000 | 1000    | 1000    | 1000     | 100          | 10     | 1000         | 1000     | player1  |

    When the user logs in with the following credentials
      | username            | password |
      | paul.ochon@test.com | toto1234 |

    And the user requests the endpoint to update a GameSave with id 0530e1fe-3428-4edd-bb32-cb563419d0bd with the following GameSaveUpdateNicknameRequest
      | nickname   |
      | Play3r-0n3 |

    Then the response status code should be 200

  Scenario: A user updates an owned GameSave with valid data but the nickname is already taken

    Given the following game saves
      | id                                   | userEmail           | gold | diamond | emerald | amethyst | healthPoints | attack | nickname | currentStage | maxStage |
      | 0530e1fe-3428-4edd-bb32-cb563419d0bd | paul.ochon@test.com | 1000 | 1000    | 1000    | 1000     | 100          | 10     | player1  | 1000         | 1000     |
      | 0530e1fe-3428-4edd-bb32-cb563419d0be | paul.ochon@test.com | 1000 | 1000    | 1000    | 1000     | 100          | 10     | player2  | 1000         | 1000     |

    When the user logs in with the following credentials
      | username            | password |
      | paul.ochon@test.com | toto1234 |

    And the user requests the endpoint to update a GameSave with id 0530e1fe-3428-4edd-bb32-cb563419d0bd with the following GameSaveUpdateNicknameRequest
      | nickname |
      | player2  |

    Then the response status code should be 400

  Scenario: A user gets its GameSaves without cached data
    Given the following game saves
      | id                                   | userEmail           | gold | diamond | emerald | amethyst | healthPoints | attack | maxStage | currentStage |
      | 0530e1fe-3428-4edd-bb32-cb563419d0bd | paul.ochon@test.com | 10   | 100     | 1000    | 10000    | 10           | 10     | 100      | 99           |
      | 3bb1a064-79cc-4279-920a-fd0760663ca5 | paul.ochon@test.com | 100  | 1000    | 10000   | 100000   | 100          | 100    | 1000     | 999          |
      | cf0f3d45-18c0-41f8-8007-41c5ea6d3e0b | paul.ochon@test.com | 1000 | 10000   | 100000  | 1000000  | 1000         | 1000   | 10000    | 9999         |

    When the user logs in with the following credentials
      | username            | password |
      | paul.ochon@test.com | toto1234 |

    And the user requests the endpoint to get his GameSaves

    Then the response status code should be 200

    And the response should have the following GameSaves
      | id                                   | userEmail           | gold | diamond | emerald | amethyst | healthPoints | attack | maxStage | currentStage |
      | 0530e1fe-3428-4edd-bb32-cb563419d0bd | paul.ochon@test.com | 10   | 100     | 1000    | 10000    | 10           | 10     | 100      | 99           |
      | 3bb1a064-79cc-4279-920a-fd0760663ca5 | paul.ochon@test.com | 100  | 1000    | 10000   | 100000   | 100          | 100    | 1000     | 999          |
      | cf0f3d45-18c0-41f8-8007-41c5ea6d3e0b | paul.ochon@test.com | 1000 | 10000   | 100000  | 1000000  | 1000         | 1000   | 10000    | 9999         |

  Scenario: A user gets its GameSaves with cached data
    Given the following game saves
      | id                                   | userEmail           | gold | diamond | emerald | amethyst | healthPoints | attack | maxStage | currentStage |
      | 0530e1fe-3428-4edd-bb32-cb563419d0bd | paul.ochon@test.com | 10   | 100     | 1000    | 10000    | 10           | 10     | 100      | 99           |
      | 3bb1a064-79cc-4279-920a-fd0760663ca5 | paul.ochon@test.com | 100  | 1000    | 10000   | 100000   | 100          | 100    | 1000     | 999          |
      | cf0f3d45-18c0-41f8-8007-41c5ea6d3e0b | paul.ochon@test.com | 1000 | 10000   | 100000  | 1000000  | 1000         | 1000   | 10000    | 9999         |

    And the following currency entries in cache
      | gameSaveId                           | gold  | diamond | emerald | amethyst |
      | 0530e1fe-3428-4edd-bb32-cb563419d0bd | 10000 | 100000  | 1000000 | 10000000 |

    And the following stage entries in cache
      | gameSaveId                           | currentStage | maxStage |
      | 0530e1fe-3428-4edd-bb32-cb563419d0bd | 100          | 100      |

    When the user logs in with the following credentials
      | username            | password |
      | paul.ochon@test.com | toto1234 |

    And the user requests the endpoint to get his GameSaves

    Then the response status code should be 200

    And the response should have the following GameSaves
      | id                                   | userEmail           | gold  | diamond | emerald | amethyst | healthPoints | attack | maxStage | currentStage |
      | 0530e1fe-3428-4edd-bb32-cb563419d0bd | paul.ochon@test.com | 10000 | 100000  | 1000000 | 10000000 | 10           | 10     | 100      | 100          |
      | 3bb1a064-79cc-4279-920a-fd0760663ca5 | paul.ochon@test.com | 100   | 1000    | 10000   | 100000   | 100          | 100    | 1000     | 999          |
      | cf0f3d45-18c0-41f8-8007-41c5ea6d3e0b | paul.ochon@test.com | 1000  | 10000   | 100000  | 1000000  | 1000         | 1000   | 10000    | 9999         |

