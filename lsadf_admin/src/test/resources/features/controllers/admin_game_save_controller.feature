#
# Copyright © 2024-2026 LSDAF
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

Feature: Admin GameSave Controller BDD tests

  Background:
    Given the BDD engine is ready
    And the cache is enabled
    And a clean database
    And the time clock set to the present
    And the following game saves
      | id                                   | userEmail           | gold | diamond | emerald | amethyst | currentStage | maxStage | wave   | nickname | attack | critChance | critDamage | health | resistance |
      | 0530e1fe-3428-4edd-bb32-cb563419d0bd | paul.ochon@test.com | 1    | 5       | 2       | 5        | 10           | 10       | 10     | test-1   | 100    | 200        | 300        | 400    | 500        |
      | 804af894-931b-4ee6-968f-1703689066fb | paul.ochon@test.com | 3    | 3       | 2       | 15       | 1000         | 1000     | 1000   | test-2   | 600    | 700        | 800        | 900    | 1000       |
      | 6a4f12dc-4e83-40f7-992e-8f2e04375d74 | paul.ochon@test.com | 5    | 1       | 2       | 25       | 100000       | 100000   | 100000 | test-3   | 1100   | 1200       | 1300       | 1400   | 1500       |

    # We assume we have at least the following user in keycloak
    # paul.ochon@test.com: ADMIN,USER: toto1234: ce60ea41-3765-4562-8c96-8673de8f96b0

  Scenario: A user gets all the game saves
    When the user logs in with the following credentials
      | username            | password |
      | paul.ochon@test.com | toto1234 |
    And the user requests the admin endpoint to get all the game saves ordered by NONE

    Then the response status code should be 200
    And the response should have the following GameSaveResponses
      | id                                   | userEmail           | gold | diamond | emerald | amethyst | currentStage | maxStage | wave   | nickname | attack | critChance | critDamage | health | resistance |
      | 0530e1fe-3428-4edd-bb32-cb563419d0bd | paul.ochon@test.com | 1    | 5       | 2       | 5        | 10           | 10       | 10     | test-1   | 100    | 200        | 300        | 400    | 500        |
      | 804af894-931b-4ee6-968f-1703689066fb | paul.ochon@test.com | 3    | 3       | 2       | 15       | 1000         | 1000     | 1000   | test-2   | 600    | 700        | 800        | 900    | 1000       |
      | 6a4f12dc-4e83-40f7-992e-8f2e04375d74 | paul.ochon@test.com | 5    | 1       | 2       | 25       | 100000       | 100000   | 100000 | test-3   | 1100   | 1200       | 1300       | 1400   | 1500       |

  Scenario: A user gets all the game saves in a specific order
    When the user logs in with the following credentials
      | username            | password |
      | paul.ochon@test.com | toto1234 |
    And the user requests the admin endpoint to get all the game saves ordered by MAX_STAGE_DESC

    Then the response status code should be 200
    And the response should have the following GameSaveResponses in exact order
      | id                                   | userEmail           | gold | diamond | emerald | amethyst | currentStage | maxStage | wave   | nickname | attack | critChance | critDamage | health | resistance |
      | 6a4f12dc-4e83-40f7-992e-8f2e04375d74 | paul.ochon@test.com | 5    | 1       | 2       | 25       | 100000       | 100000   | 100000 | test-3   | 1100   | 1200       | 1300       | 1400   | 1500       |
      | 804af894-931b-4ee6-968f-1703689066fb | paul.ochon@test.com | 3    | 3       | 2       | 15       | 1000         | 1000     | 1000   | test-2   | 600    | 700        | 800        | 900    | 1000       |
      | 0530e1fe-3428-4edd-bb32-cb563419d0bd | paul.ochon@test.com | 1    | 5       | 2       | 5        | 10           | 10       | 10     | test-1   | 100    | 200        | 300        | 400    | 500        |


  Scenario: A user gets a game save by its id
    When the user logs in with the following credentials
      | username            | password |
      | paul.ochon@test.com | toto1234 |

    And the user requests the admin endpoint to get a game save with the following id 6a4f12dc-4e83-40f7-992e-8f2e04375d74

    Then the response status code should be 200

    And the response should have the following GameSaveResponse
      | id                                   | userEmail           | gold | diamond | emerald | amethyst | currentStage | maxStage | wave   | nickname | attack | critChance | critDamage | health | resistance |
      | 6a4f12dc-4e83-40f7-992e-8f2e04375d74 | paul.ochon@test.com | 5    | 1       | 2       | 25       | 100000       | 100000   | 100000 | test-3   | 1100   | 1200       | 1300       | 1400   | 1500       |


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
    And the response should have the following GameSaveResponses
      | id                                   | userEmail           | gold | diamond | emerald | amethyst | currentStage | maxStage | wave   | nickname | attack | critChance | critDamage | health | resistance |
      | 0530e1fe-3428-4edd-bb32-cb563419d0bd | paul.ochon@test.com | 1    | 5       | 2       | 5        | 10           | 10       | 10     | test-1   | 100    | 200        | 300        | 400    | 500        |
      | 804af894-931b-4ee6-968f-1703689066fb | paul.ochon@test.com | 3    | 3       | 2       | 15       | 1000         | 1000     | 1000   | test-2   | 600    | 700        | 800        | 900    | 1000       |
      | 6a4f12dc-4e83-40f7-992e-8f2e04375d74 | paul.ochon@test.com | 5    | 1       | 2       | 25       | 100000       | 100000   | 100000 | test-3   | 1100   | 1200       | 1300       | 1400   | 1500       |


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
      | id                                   | userEmail           | gold | diamond | emerald | amethyst | currentStage | maxStage | wave | nickname | attack | critChance | critDamage | health | resistance |
      | 45c2736f-6c3b-4955-9b26-ce515091810b | paul.ochon@test.com | 1    | 2       | 3       | 4        | 7            | 8        | 10   | newgame  | 100    | 200        | 300        | 400    | 500        |

    Then the response status code should be 200
    And the response should have the following GameSaveResponse
      | id                                   | userEmail           | gold | diamond | emerald | amethyst | currentStage | maxStage | wave | nickname | attack | critChance | critDamage | health | resistance |
      | 45c2736f-6c3b-4955-9b26-ce515091810b | paul.ochon@test.com | 1    | 2       | 3       | 4        | 7            | 8        | 10   | newgame  | 100    | 200        | 300        | 400    | 500        |

  Scenario: A user generates a new game save with an existing id
    When the user logs in with the following credentials
      | username            | password |
      | paul.ochon@test.com | toto1234 |
    And the user requests the admin endpoint to create a new game save with the following AdminGameSaveCreationRequest
      | id                                   | userEmail           | gold | diamond | emerald | amethyst | currentStage | maxStage | wave | nickname | attack | critChance | critDamage | health | resistance |
      | 0530e1fe-3428-4edd-bb32-cb563419d0bd | paul.ochon@test.com | 1    | 2       | 3       | 4        | 7            | 8        | 10   | newgame  | 100    | 200        | 300        | 400    | 500        |

    Then the response status code should be 400

  Scenario: A user generates a new game save with a non-existing username
    When the user logs in with the following credentials
      | username            | password |
      | paul.ochon@test.com | toto1234 |
    And the user requests the admin endpoint to create a new game save with the following AdminGameSaveCreationRequest
      | id                                   | userEmail                 | gold | diamond | emerald | amethyst | currentStage | maxStage | wave | nickname | attack | critChance | critDamage | health | resistance |
      | f0347e5c-6add-4b5c-a73a-74c4cff2e72a | paul.kalkbrenner@test.com | 1    | 2       | 3       | 4        | 7            | 8        | 10   | newgame  | 100    | 200        | 300        | 400    | 500        |

    Then the response status code should be 404


  Scenario: A user updates a game save
    When the user logs in with the following credentials
      | username            | password |
      | paul.ochon@test.com | toto1234 |
    And the user requests the admin endpoint to update the game save with id 6a4f12dc-4e83-40f7-992e-8f2e04375d74 with the following GameSaveUpdateRequest
      | healthPoints | attack | nickname |
      | 100000       | 599    | test-ZZZ |

    Then the response status code should be 200
    And the response should have the following GameSaveResponse
      | id                                   | userEmail           | gold | diamond | emerald | amethyst | currentStage | maxStage | wave   | nickname | attack | critChance | critDamage | health | resistance |
      | 6a4f12dc-4e83-40f7-992e-8f2e04375d74 | paul.ochon@test.com | 5    | 1       | 2       | 25       | 100000       | 100000   | 100000 | test-ZZZ | 1100   | 1200       | 1300       | 1400   | 1500       |

  Scenario: A user updates a non-existing game save
    When the user logs in with the following credentials
      | username            | password |
      | paul.ochon@test.com | toto1234 |
    And the user requests the admin endpoint to update the game save with id 88df00e0-1529-44a1-97d6-c11218bd6003 with the following GameSaveUpdateRequest
      | nickname | attack | critChance | critDamage | health | resistance |
      | test-ZZZ | 100    | 200        | 300        | 400    | 500        |

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
    And the user requests the admin endpoint to update the game save with id 0530e1fe-3428-4edd-bb32-cb563419d0bd with the following GameSaveUpdateRequest
      | attack | critChance | critDamage | health | resistance |
      | 100    | 100        | 100        | 100    | 100        |

    Then the response status code should be 200

  Scenario: A user updates the characteristics of a non-existing game save
    When the user logs in with the following credentials
      | username            | password |
      | paul.ochon@test.com | toto1234 |
    And the user requests the admin endpoint to update the game save with id 861e3a80-eade-402c-8598-3820e870c252 with the following GameSaveUpdateRequest
      | attack | critChance | critDamage | health | resistance |
      | 100    | 100        | 100        | 100    | 100        |
    Then the response status code should be 404

  Scenario: A user updates the currencies of a game save
    When the user logs in with the following credentials
      | username            | password |
      | paul.ochon@test.com | toto1234 |
    And the user requests the admin endpoint to update the game save with id 0530e1fe-3428-4edd-bb32-cb563419d0bd with the following GameSaveUpdateRequest
      | gold | diamond | emerald | amethyst |
      | 100  |         | 300     |          |

    Then the response status code should be 200

  Scenario: A user updates the currencies of a non-existing game save
    When the user logs in with the following credentials
      | username            | password |
      | paul.ochon@test.com | toto1234 |
    And the user requests the admin endpoint to update the game save with id 861e3a80-eade-402c-8598-3820e870c252 with the following GameSaveUpdateRequest
      | gold | diamond | emerald | amethyst |
      | 100  |         | 300     |          |
    Then the response status code should be 404

  Scenario: A user updates the stages of a game save
    When the user logs in with the following credentials
      | username            | password |
      | paul.ochon@test.com | toto1234 |
    And the user requests the admin endpoint to update the game save with id 0530e1fe-3428-4edd-bb32-cb563419d0bd with the following GameSaveUpdateRequest
      | currentStage | maxStage | wave |
      | 1000         | 2000     | 10   |
    Then the response status code should be 200

  Scenario: A user updates the stages of a non-existing game save
    When the user logs in with the following credentials
      | username            | password |
      | paul.ochon@test.com | toto1234 |
    And the user requests the admin endpoint to update the game save with id e97e0aa2-f380-4833-9e9e-79284379000b with the following GameSaveUpdateRequest
      | currentStage | maxStage |
      | 1000         | 1000     |

    Then the response status code should be 404