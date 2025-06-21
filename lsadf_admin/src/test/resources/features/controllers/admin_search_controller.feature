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

Feature: Admin Search Controller BDD tests

  Background:
    Given the BDD engine is ready
    And the cache is enabled
    And a clean database
    And the time clock set to the present

    # We assume we have the two following users in keycloak
    # paul.ochon@test.com: ADMIN,USER: toto1234: ce60ea41-3765-4562-8c96-8673de8f96b0
    # paul.itesse@test.com: USER: toto5678: 71d6755e-1dfd-4f1f-8d7b-bdec9a62c6e8

  # GAME SAVE SEARCH

  Scenario: A user searches for a game save with no search criteria, no ordering
    Given the following game saves
      | id                                   | userEmail            | gold | diamond | emerald | amethyst | currentStage | maxStage | nickname | attack | critChance | critDamage | health | resistance |
      | 0530e1fe-3428-4edd-bb32-cb563419d0bd | paul.ochon@test.com  | 1    | 5       | 2       | 5        | 10           | 10       | test-1   | 1100   | 1200       | 1300       | 1400   | 1500       |
      | 3dd8b6d8-7aaa-4580-9a8a-a0bb6cc5bb21 | paul.itesse@test.com | 2    | 4       | 2       | 15       | 100          | 100      | test-2   | 600    | 700        | 800        | 900    | 1000       |
      | 804af894-931b-4ee6-968f-1703689066fb | paul.ochon@test.com  | 3    | 3       | 2       | 35       | 1000         | 1000     | test-3   | 100    | 200        | 300        | 400    | 500        |
      | 9929ee41-9a7b-4320-90d5-ee963888d876 | paul.itesse@test.com | 4    | 2       | 2       | 45       | 10000        | 10000    | test-4   | 600    | 700        | 800        | 900    | 1000       |
      | 6a4f12dc-4e83-40f7-992e-8f2e04375d74 | paul.ochon@test.com  | 5    | 1       | 2       | 55       | 100000       | 100000   | test-5   | 1100   | 1200       | 1300       | 1400   | 1500       |

    When the user logs in with the following credentials
      | username            | password |
      | paul.ochon@test.com | toto1234 |

    And the user requests the admin endpoint to search game saves ordered by NONE with the following SearchRequest
      | key | value |

    Then the response status code should be 200
    And the response should have the following GameSaveResponses
      | id                                   | userEmail            | gold | diamond | emerald | amethyst | currentStage | maxStage | nickname | attack | critChance | critDamage | health | resistance |
      | 0530e1fe-3428-4edd-bb32-cb563419d0bd | paul.ochon@test.com  | 1    | 5       | 2       | 5        | 10           | 10       | test-1   | 1100   | 1200       | 1300       | 1400   | 1500       |
      | 3dd8b6d8-7aaa-4580-9a8a-a0bb6cc5bb21 | paul.itesse@test.com | 2    | 4       | 2       | 15       | 100          | 100      | test-2   | 600    | 700        | 800        | 900    | 1000       |
      | 804af894-931b-4ee6-968f-1703689066fb | paul.ochon@test.com  | 3    | 3       | 2       | 35       | 1000         | 1000     | test-3   | 100    | 200        | 300        | 400    | 500        |
      | 9929ee41-9a7b-4320-90d5-ee963888d876 | paul.itesse@test.com | 4    | 2       | 2       | 45       | 10000        | 10000    | test-4   | 600    | 700        | 800        | 900    | 1000       |
      | 6a4f12dc-4e83-40f7-992e-8f2e04375d74 | paul.ochon@test.com  | 5    | 1       | 2       | 55       | 100000       | 100000   | test-5   | 1100   | 1200       | 1300       | 1400   | 1500       |


  Scenario: A user searches for a game save with no search criteria, no ordering, and cached values
    Given the following game saves
      | id                                   | userEmail            | gold | diamond | emerald | amethyst | currentStage | maxStage | nickname | attack | critChance | critDamage | health | resistance |
      | 0530e1fe-3428-4edd-bb32-cb563419d0bd | paul.ochon@test.com  | 1    | 5       | 2       | 5        | 10           | 10       | test-1   | 1100   | 1200       | 1300       | 1400   | 1500       |
      | 3dd8b6d8-7aaa-4580-9a8a-a0bb6cc5bb21 | paul.itesse@test.com | 2    | 4       | 2       | 15       | 100          | 100      | test-2   | 600    | 700        | 800        | 900    | 1000       |
      | 804af894-931b-4ee6-968f-1703689066fb | paul.ochon@test.com  | 3    | 3       | 2       | 35       | 1000         | 1000     | test-3   | 100    | 200        | 300        | 400    | 500        |
      | 9929ee41-9a7b-4320-90d5-ee963888d876 | paul.itesse@test.com | 4    | 2       | 2       | 45       | 10000        | 10000    | test-4   | 600    | 700        | 800        | 900    | 1000       |
      | 6a4f12dc-4e83-40f7-992e-8f2e04375d74 | paul.ochon@test.com  | 5    | 1       | 2       | 55       | 100000       | 100000   | test-5   | 1100   | 1200       | 1300       | 1400   | 1500       |

    And the following currency entries in cache
      | gameSaveId                           | gold | diamond | emerald | amethyst |
      | 0530e1fe-3428-4edd-bb32-cb563419d0bd | 1    | 10      | 100     | 1000     |
      | 3dd8b6d8-7aaa-4580-9a8a-a0bb6cc5bb21 | 2    | 20      | 200     | 2000     |
      | 804af894-931b-4ee6-968f-1703689066fb | 3    | 30      | 300     | 3000     |
      | 9929ee41-9a7b-4320-90d5-ee963888d876 | 4    | 40      | 400     | 4000     |
      | 6a4f12dc-4e83-40f7-992e-8f2e04375d74 | 5    | 50      | 500     | 5000     |

    When the user logs in with the following credentials
      | username            | password |
      | paul.ochon@test.com | toto1234 |

    And the user requests the admin endpoint to search game saves ordered by NONE with the following SearchRequest
      | key | value |

    Then the response status code should be 200
    And the response should have the following GameSaveResponses
      | id                                   | userEmail            | gold | diamond | emerald | amethyst | currentStage | maxStage | nickname | attack | critChance | critDamage | health | resistance |
      | 0530e1fe-3428-4edd-bb32-cb563419d0bd | paul.ochon@test.com  | 1    | 10      | 100     | 1000     | 10           | 10       | test-1   | 1100   | 1200       | 1300       | 1400   | 1500       |
      | 3dd8b6d8-7aaa-4580-9a8a-a0bb6cc5bb21 | paul.itesse@test.com | 2    | 20      | 200     | 2000     | 100          | 100      | test-2   | 600    | 700        | 800        | 900    | 1000       |
      | 804af894-931b-4ee6-968f-1703689066fb | paul.ochon@test.com  | 3    | 30      | 300     | 3000     | 1000         | 1000     | test-3   | 100    | 200        | 300        | 400    | 500        |
      | 9929ee41-9a7b-4320-90d5-ee963888d876 | paul.itesse@test.com | 4    | 40      | 400     | 4000     | 10000        | 10000    | test-4   | 600    | 700        | 800        | 900    | 1000       |
      | 6a4f12dc-4e83-40f7-992e-8f2e04375d74 | paul.ochon@test.com  | 5    | 50      | 500     | 5000     | 100000       | 100000   | test-5   | 1100   | 1200       | 1300       | 1400   | 1500       |


  Scenario: A user searches for a game save with one search criteria, no ordering
    Given the following game saves
      | id                                   | userEmail            | gold | diamond | emerald | amethyst | currentStage | maxStage | nickname | attack | critChance | critDamage | health | resistance |
      | 0530e1fe-3428-4edd-bb32-cb563419d0bd | paul.ochon@test.com  | 1    | 5       | 2       | 5        | 10           | 10       | test-1   | 1100   | 1200       | 1300       | 1400   | 1500       |
      | 3dd8b6d8-7aaa-4580-9a8a-a0bb6cc5bb21 | paul.itesse@test.com | 2    | 4       | 2       | 15       | 100          | 100      | test-2   | 600    | 700        | 800        | 900    | 1000       |
      | 804af894-931b-4ee6-968f-1703689066fb | paul.ochon@test.com  | 3    | 3       | 2       | 35       | 1000         | 1000     | test-3   | 100    | 200        | 300        | 400    | 500        |
      | 9929ee41-9a7b-4320-90d5-ee963888d876 | paul.itesse@test.com | 4    | 2       | 2       | 45       | 10000        | 10000    | test-4   | 600    | 700        | 800        | 900    | 1000       |
      | 6a4f12dc-4e83-40f7-992e-8f2e04375d74 | paul.ochon@test.com  | 5    | 1       | 2       | 55       | 100000       | 100000   | test-5   | 1100   | 1200       | 1300       | 1400   | 1500       |

    When the user logs in with the following credentials
      | username            | password |
      | paul.ochon@test.com | toto1234 |

    And the user requests the admin endpoint to search game saves ordered by NONE with the following SearchRequest
      | key        | value                |
      | user_email | paul.itesse@test.com |

    Then the response status code should be 200
    And the response should have the following GameSaveResponses
      | id                                   | userEmail            | gold | diamond | emerald | amethyst | currentStage | maxStage | nickname | attack | critChance | critDamage | health | resistance |
      | 3dd8b6d8-7aaa-4580-9a8a-a0bb6cc5bb21 | paul.itesse@test.com | 2    | 4       | 2       | 15       | 100          | 100      | test-2   | 600    | 700        | 800        | 900   | 1000       |
      | 9929ee41-9a7b-4320-90d5-ee963888d876 | paul.itesse@test.com | 4    | 2       | 2       | 45       | 10000        | 10000    | test-4   | 600    | 700        | 800        | 900    | 1000       |


  Scenario: A user searches for a game save with no search criteria, specific ordering
    Given the following game saves
      | id                                   | userEmail            | gold | diamond | emerald | amethyst | currentStage | maxStage | nickname | attack | critChance | critDamage | health | resistance |
      | 0530e1fe-3428-4edd-bb32-cb563419d0bd | paul.ochon@test.com  | 1    | 5       | 2       | 5        | 10           | 10       | test-1   | 1100   | 1200       | 1300       | 1400   | 1500       |
      | 3dd8b6d8-7aaa-4580-9a8a-a0bb6cc5bb21 | paul.itesse@test.com | 2    | 4       | 2       | 15       | 100          | 100      | test-2   | 600    | 700        | 800        | 900    | 1000       |
      | 804af894-931b-4ee6-968f-1703689066fb | paul.ochon@test.com  | 3    | 3       | 2       | 35       | 1000         | 1000     | test-3   | 100    | 200        | 300        | 400    | 500        |
      | 9929ee41-9a7b-4320-90d5-ee963888d876 | paul.itesse@test.com | 4    | 2       | 2       | 45       | 10000        | 10000    | test-4   | 600    | 700        | 800        | 900    | 1000       |
      | 6a4f12dc-4e83-40f7-992e-8f2e04375d74 | paul.ochon@test.com  | 5    | 1       | 2       | 55       | 100000       | 100000   | test-5   | 1100   | 1200       | 1300       | 1400   | 1500       |

    When the user logs in with the following credentials
      | username            | password |
      | paul.ochon@test.com | toto1234 |

    And the user requests the admin endpoint to search game saves ordered by AMETHYST_DESC with the following SearchRequest
      | key | value |

    Then the response status code should be 200
    And the response should have the following GameSaveResponses in exact order
      | id                                   | userEmail            | gold | diamond | emerald | amethyst | currentStage | maxStage | nickname | attack | critChance | critDamage | health | resistance |
      | 6a4f12dc-4e83-40f7-992e-8f2e04375d74 | paul.ochon@test.com  | 5    | 1       | 2       | 55       | 100000       | 100000   | test-5   | 1100   | 1200       | 1300       | 1400   | 1500       |
      | 9929ee41-9a7b-4320-90d5-ee963888d876 | paul.itesse@test.com | 4    | 2       | 2       | 45       | 10000        | 10000    | test-4   | 600    | 700        | 800        | 900    | 1000       |
      | 804af894-931b-4ee6-968f-1703689066fb | paul.ochon@test.com  | 3    | 3       | 2       | 35       | 1000         | 1000     | test-3   | 100    | 200        | 300        | 400    | 500        |
      | 3dd8b6d8-7aaa-4580-9a8a-a0bb6cc5bb21 | paul.itesse@test.com | 2    | 4       | 2       | 15       | 100          | 100      | test-2   | 600    | 700        | 800        | 900    | 1000       |
      | 0530e1fe-3428-4edd-bb32-cb563419d0bd | paul.ochon@test.com  | 1    | 5       | 2       | 5        | 10           | 10       | test-1   | 1100   | 1200       | 1300       | 1400   | 1500       |


  Scenario: A user searches for a game save with one search criteria and a specific ordering
    Given the following game saves
      | id                                   | userEmail            | gold | diamond | emerald | amethyst | currentStage | maxStage | nickname | attack | critChance | critDamage | health | resistance |
      | 0530e1fe-3428-4edd-bb32-cb563419d0bd | paul.ochon@test.com  | 1    | 5       | 2       | 5        | 10           | 10       | test-1   | 1100   | 1200       | 1300       | 1400   | 1500       |
      | 3dd8b6d8-7aaa-4580-9a8a-a0bb6cc5bb21 | paul.itesse@test.com | 2    | 4       | 2       | 15       | 100          | 100      | test-2   | 600    | 700        | 800        | 900    | 1000       |
      | 804af894-931b-4ee6-968f-1703689066fb | paul.ochon@test.com  | 3    | 3       | 2       | 35       | 1000         | 1000     | test-3   | 100    | 200        | 300        | 400    | 500        |
      | 9929ee41-9a7b-4320-90d5-ee963888d876 | paul.itesse@test.com | 4    | 2       | 2       | 45       | 10000        | 10000    | test-4   | 600    | 700        | 800        | 900    | 1000       |
      | 6a4f12dc-4e83-40f7-992e-8f2e04375d74 | paul.ochon@test.com  | 5    | 1       | 2       | 55       | 100000       | 100000   | test-5   | 1100   | 1200       | 1300       | 1400   | 1500       |

    When the user logs in with the following credentials
      | username            | password |
      | paul.ochon@test.com | toto1234 |

    And the user requests the admin endpoint to search game saves ordered by DIAMOND_DESC with the following SearchRequest
      | key        | value               |
      | user_email | paul.ochon@test.com |

    Then the response status code should be 200
    And the response should have the following GameSaveResponses in exact order
      | id                                   | userEmail           | gold | diamond | emerald | amethyst | currentStage | maxStage | nickname | attack | critChance | critDamage | health | resistance |
      | 0530e1fe-3428-4edd-bb32-cb563419d0bd | paul.ochon@test.com | 1    | 5       | 2       | 5        | 10           | 10       | test-1   | 1100   | 1200       | 1300       | 1400   | 1500       |
      | 804af894-931b-4ee6-968f-1703689066fb | paul.ochon@test.com | 3    | 3       | 2       | 35       | 1000         | 1000     | test-3   | 100    | 200        | 300        | 400    | 500        |
      | 6a4f12dc-4e83-40f7-992e-8f2e04375d74 | paul.ochon@test.com | 5    | 1       | 2       | 55       | 100000       | 100000   | test-5   | 1100   | 1200       | 1300       | 1400   | 1500       |

  Scenario: A user searches for a game save with several search criterias
    Given the following game saves
      | id                                   | userEmail            | gold | diamond | emerald | amethyst | currentStage | maxStage | nickname | attack | critChance | critDamage | health | resistance |
      | 0530e1fe-3428-4edd-bb32-cb563419d0bd | paul.ochon@test.com  | 1    | 5       | 2       | 5        | 10           | 10       | test-1   | 1100   | 1200       | 1300       | 1400   | 1500       |
      | 3dd8b6d8-7aaa-4580-9a8a-a0bb6cc5bb21 | paul.itesse@test.com | 2    | 4       | 2       | 15       | 100          | 100      | test-2   | 600    | 700        | 800        | 900    | 1000       |
      | 804af894-931b-4ee6-968f-1703689066fb | paul.ochon@test.com  | 3    | 3       | 2       | 35       | 1000         | 1000     | test-3   | 100    | 200        | 300        | 400    | 500        |
      | 9929ee41-9a7b-4320-90d5-ee963888d876 | paul.itesse@test.com | 4    | 2       | 2       | 45       | 10000        | 10000    | test-4   | 600    | 700        | 800        | 900    | 1000       |
      | 6a4f12dc-4e83-40f7-992e-8f2e04375d74 | paul.ochon@test.com  | 5    | 1       | 2       | 55       | 100000       | 100000   | test-5   | 1100   | 1200       | 1300       | 1400   | 1500       |

    When the user logs in with the following credentials
      | username            | password |
      | paul.ochon@test.com | toto1234 |

    And the user requests the admin endpoint to search game saves ordered by NONE with the following SearchRequest
      | key        | value               |
      | nickname   | test-1              |
      | user_email | paul.ochon@test.com |

    Then the response status code should be 200
    And the response should have the following GameSaveResponses
      | id                                   | userEmail           | gold | diamond | emerald | amethyst | currentStage | maxStage | nickname | attack | critChance | critDamage | health | resistance |
      | 0530e1fe-3428-4edd-bb32-cb563419d0bd | paul.ochon@test.com | 1    | 5       | 2       | 5        | 10           | 10       | test-1   | 1100   | 1200       | 1300       | 1400   | 1500       |


  Scenario: A user searches for a game save with a search criteria that does not exist
    Given the following game saves
      | id                                   | userEmail            | gold | diamond | emerald | amethyst | currentStage | maxStage | nickname | attack | critChance | critDamage | health | resistance |
      | 0530e1fe-3428-4edd-bb32-cb563419d0bd | paul.ochon@test.com  | 1    | 5       | 2       | 5        | 10           | 10       | test-1   | 1100   | 1200       | 1300       | 1400   | 1500       |
      | 3dd8b6d8-7aaa-4580-9a8a-a0bb6cc5bb21 | paul.itesse@test.com | 2    | 4       | 2       | 15       | 100          | 100      | test-2   | 600    | 700        | 800        | 900    | 1000       |
      | 804af894-931b-4ee6-968f-1703689066fb | paul.ochon@test.com  | 3    | 3       | 2       | 35       | 1000         | 1000     | test-3   | 100    | 200        | 300        | 400    | 500        |
      | 9929ee41-9a7b-4320-90d5-ee963888d876 | paul.itesse@test.com | 4    | 2       | 2       | 45       | 10000        | 10000    | test-4   | 600    | 700        | 800        | 900    | 1000       |
      | 6a4f12dc-4e83-40f7-992e-8f2e04375d74 | paul.ochon@test.com  | 5    | 1       | 2       | 55       | 100000       | 100000   | test-5   | 1100   | 1200       | 1300       | 1400   | 1500       |

    When the user logs in with the following credentials
      | username            | password |
      | paul.ochon@test.com | toto1234 |

    And the user requests the admin endpoint to search game saves ordered by NONE with the following SearchRequest
      | key         | value |
      | invalid_key | ADMIN |

    Then the response status code should be 400

  # USER SEARCH

  Scenario: A user searches for a user with no search criteria, no ordering
    When the user logs in with the following credentials
      | username            | password |
      | paul.ochon@test.com | toto1234 |

    And the user requests the admin endpoint to search users ordered by NONE with the following SearchRequest
      | key | value |

    Then the response status code should be 200
    And the response should have the following Users
      | firstName | lastName | username             | enabled | emailVerified | roles      |
      | Paul      | OCHON    | paul.ochon@test.com  | true    | true          | USER,ADMIN |
      | Paul      | ITESSE   | paul.itesse@test.com | true    | true          | USER       |


  Scenario: A user searches for a user with one search criteria, no ordering
    When the user logs in with the following credentials
      | username            | password |
      | paul.ochon@test.com | toto1234 |

    And the user requests the admin endpoint to search users ordered by NONE with the following SearchRequest
      | key        | value               |
      | user_email | paul.ochon@test.com |

    Then the response status code should be 200
    And the response should have the following Users
      | firstName | lastName | username             | enabled | emailVerified | roles      |
      | Paul      | OCHON    | paul.ochon@test.com  | true    | true          | USER,ADMIN |

  Scenario: A user searches for a user with no search criteria, specific ordering
    When the user logs in with the following credentials
      | username            | password |
      | paul.ochon@test.com | toto1234 |
    And the user requests the admin endpoint to search users ordered by LAST_NAME with the following SearchRequest
      | key | value |

    Then the response status code should be 200
    And the response should have the following Users in exact order
      | firstName | lastName | username             | enabled | emailVerified | roles      |
      | Paul      | ITESSE   | paul.itesse@test.com | true    | true          | USER       |
      | Paul      | OCHON    | paul.ochon@test.com  | true    | true          | USER,ADMIN |

  Scenario: A user searches for a user with one search criteria and a specific ordering
    When the user logs in with the following credentials
      | username            | password |
      | paul.ochon@test.com | toto1234 |
    And the user requests the admin endpoint to search users ordered by LAST_NAME with the following SearchRequest
      | key        | value |
      | first_name | Paul  |

    Then the response status code should be 200
    And the response should have the following Users in exact order
      | firstName | lastName | username             | enabled | emailVerified | roles      |
      | Paul      | ITESSE   | paul.itesse@test.com | true    | true          | USER       |
      | Paul      | OCHON    | paul.ochon@test.com  | true    | true          | USER,ADMIN |

  Scenario: A user searches for a user with several search criterias
    When the user logs in with the following credentials
      | username            | password |
      | paul.ochon@test.com | toto1234 |

    And the user requests the admin endpoint to search users ordered by NONE with the following SearchRequest
      | key        | value |
      | first_name | Paul  |
      | last_name  | OCHON |
      | user_roles | ADMIN |

    Then the response status code should be 200
    And the response should have the following Users
      | firstName | lastName | username             | enabled | emailVerified | roles      |
      | Paul      | OCHON    | paul.ochon@test.com  | true    | true          | USER,ADMIN |

  Scenario: A user searches for a user with a search criteria that does not exist
    When the user logs in with the following credentials
      | username            | password |
      | paul.ochon@test.com | toto1234 |

    And the user requests the admin endpoint to search users ordered by NONE with the following SearchRequest
      | key         | value |
      | invalid_key | Test  |

    Then the response status code should be 400