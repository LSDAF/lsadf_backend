#
# Copyright © 2024-2025 LSDAF
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#     http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#

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
    And the response should have the following GameSaveResponse
      | userId                               | userEmail           | gold | diamond | emerald | amethyst | currentStage | maxStage | attack | critChance | critDamage | health | resistance |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | paul.ochon@test.com | 0    | 0       | 0       | 0        | 1            | 1        | 1      | 1          | 1          | 1      | 1          |

  @ignore
  Scenario: A user tries to update a GameSave with invalid id
    Given the following game saves
      | id                                   | userEmail           | gold | diamond | emerald | amethyst | currentStage | maxStage | attack | critChance | critDamage | health | resistance |
      | 0530e1fe-3428-4edd-bb32-cb563419d0bd | paul.ochon@test.com | 1000 | 1000    | 1000    | 1000     | 1000         | 1000     | 1100   | 1200       | 1300       | 1400   | 1500       |

    When the user logs in with the following credentials
      | username            | password |
      | paul.ochon@test.com | toto1234 |

    And the user requests the endpoint to update a GameSave with id d06664b0-5c4c-4d0b-a253-4f742b470bfd with the following GameSaveNicknameUpdateRequest
      | nickname   |
      | Play3r-0n3 |

    Then the response status code should be 404

  Scenario: A user tries to update a non-owned GameSave
    Given the following game saves
      | id                                   | userEmail            | gold | diamond | emerald | amethyst | currentStage | maxStage | attack | critChance | critDamage | health | resistance |
      | 0530e1fe-3428-4edd-bb32-cb563419d0bd | paul.itesse@test.com | 1000 | 1000    | 1000    | 1000     | 1000         | 1000     | 1100   | 1200       | 1300       | 1400   | 1500       |

    When the user logs in with the following credentials
      | username            | password |
      | paul.ochon@test.com | toto1234 |

    And the user requests the endpoint to update a GameSave with id 0530e1fe-3428-4edd-bb32-cb563419d0bd with the following GameSaveNicknameUpdateRequest
      | nickname   |
      | Play3r-0n3 |

    Then the response status code should be 403

  Scenario: A user updates an owned GameSave with valid custom nickname
    Given the following game saves
      | id                                   | userEmail           | gold | diamond | emerald | amethyst | currentStage | maxStage | nickname | attack | critChance | critDamage | health | resistance |
      | 0530e1fe-3428-4edd-bb32-cb563419d0bd | paul.ochon@test.com | 1000 | 1000    | 1000    | 1000     | 1000         | 1000     | player1  | 1100   | 1200       | 1300       | 1400   | 1500       |

    When the user logs in with the following credentials
      | username            | password |
      | paul.ochon@test.com | toto1234 |

    And the user requests the endpoint to update a GameSave with id 0530e1fe-3428-4edd-bb32-cb563419d0bd with the following GameSaveNicknameUpdateRequest
      | nickname   |
      | Play3r-0n3 |

    Then the response status code should be 200

  Scenario: A user updates an owned GameSave with valid data but the nickname is already taken

    Given the following game saves
      | id                                   | userEmail           | gold | diamond | emerald | amethyst | nickname | currentStage | maxStage | attack | critChance | critDamage | health | resistance |
      | 0530e1fe-3428-4edd-bb32-cb563419d0bd | paul.ochon@test.com | 1000 | 1000    | 1000    | 1000     | player1  | 1000         | 1000     | 1100   | 1200       | 1300       | 1400   | 1500       |
      | 0530e1fe-3428-4edd-bb32-cb563419d0be | paul.ochon@test.com | 1000 | 1000    | 1000    | 1000     | player2  | 1000         | 1000     | 400    | 500        | 600        | 700    | 800        |

    When the user logs in with the following credentials
      | username            | password |
      | paul.ochon@test.com | toto1234 |

    And the user requests the endpoint to update a GameSave with id 0530e1fe-3428-4edd-bb32-cb563419d0bd with the following GameSaveNicknameUpdateRequest
      | nickname |
      | player2  |

    Then the response status code should be 400

  Scenario: A user gets its GameSaves without cached data
    Given the following game saves
      | id                                   | userEmail           | gold | diamond | emerald | amethyst | maxStage | currentStage | attack | critChance | critDamage | health | resistance |
      | 0530e1fe-3428-4edd-bb32-cb563419d0bd | paul.ochon@test.com | 10   | 100     | 1000    | 10000    | 100      | 99           | 1100   | 1200       | 1300       | 1400   | 1500       |
      | 3bb1a064-79cc-4279-920a-fd0760663ca5 | paul.ochon@test.com | 100  | 1000    | 10000   | 100000   | 1000     | 999          | 400    | 500        | 600        | 700    | 800        |
      | cf0f3d45-18c0-41f8-8007-41c5ea6d3e0b | paul.ochon@test.com | 1000 | 10000   | 100000  | 1000000  | 10000    | 9999         | 1111   | 1222       | 1333       | 1444   | 1555       |

    When the user logs in with the following credentials
      | username            | password |
      | paul.ochon@test.com | toto1234 |

    And the user requests the endpoint to get his GameSaves

    Then the response status code should be 200

    And the response should have the following GameSaveResponses
      | id                                   | userEmail           | gold | diamond | emerald | amethyst | maxStage | currentStage | attack | critChance | critDamage | health | resistance |
      | 0530e1fe-3428-4edd-bb32-cb563419d0bd | paul.ochon@test.com | 10   | 100     | 1000    | 10000    | 100      | 99           | 1100   | 1200       | 1300       | 1400   | 1500       |
      | 3bb1a064-79cc-4279-920a-fd0760663ca5 | paul.ochon@test.com | 100  | 1000    | 10000   | 100000   | 1000     | 999          | 400    | 500        | 600        | 700    | 800        |
      | cf0f3d45-18c0-41f8-8007-41c5ea6d3e0b | paul.ochon@test.com | 1000 | 10000   | 100000  | 1000000  | 10000    | 9999         | 1111   | 1222       | 1333       | 1444   | 1555       |

  Scenario: A user gets its GameSaves with cached data
    Given the following game saves
      | id                                   | userEmail           | gold | diamond | emerald | amethyst | maxStage | currentStage | attack | critChance | critDamage | health | resistance |
      | 0530e1fe-3428-4edd-bb32-cb563419d0bd | paul.ochon@test.com | 10   | 100     | 1000    | 10000    | 100      | 99           | 1100   | 1200       | 1300       | 1400   | 1500       |
      | 3bb1a064-79cc-4279-920a-fd0760663ca5 | paul.ochon@test.com | 100  | 1000    | 10000   | 100000   | 1000     | 999          | 400    | 500        | 600        | 700    | 800        |
      | cf0f3d45-18c0-41f8-8007-41c5ea6d3e0b | paul.ochon@test.com | 1000 | 10000   | 100000  | 1000000  | 10000    | 9999         | 1111   | 1222       | 1333       | 1444   | 1555       |

    And the following characteristics entries in cache
      | gameSaveId                           | attack | critChance | critDamage | health | resistance |
      | 0530e1fe-3428-4edd-bb32-cb563419d0bd | 9999   | 9999       | 9999       | 9999   | 9999       |

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

    And the response should have the following GameSaveResponses
      | id                                   | userEmail           | gold  | diamond | emerald | amethyst | maxStage | currentStage | attack | critChance | critDamage | health | resistance |
      | 0530e1fe-3428-4edd-bb32-cb563419d0bd | paul.ochon@test.com | 10000 | 100000  | 1000000 | 10000000 | 100      | 100          | 9999   | 9999       | 9999       | 9999   | 9999       |
      | 3bb1a064-79cc-4279-920a-fd0760663ca5 | paul.ochon@test.com | 100   | 1000    | 10000   | 100000   | 1000     | 999          | 400    | 500        | 600        | 700    | 800        |
      | cf0f3d45-18c0-41f8-8007-41c5ea6d3e0b | paul.ochon@test.com | 1000  | 10000   | 100000  | 1000000  | 10000    | 9999         | 1111   | 1222       | 1333       | 1444   | 1555       |

