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

Feature: Inventory Controller BDD tests

  Background:
    Given the BDD engine is ready
    And a clean database
    And the time clock set to the present

    # We assume we have the two following users in keycloak
    # paul.ochon@test.com: ADMIN,USER: toto1234
    # paul.itesse@test.com: USER: toto5678

#  ---- GET ----
  Scenario: A user requests its inventory when it is empty
    Given the following game saves
      | id                                   | userEmail           | gold | diamond | emerald | amethyst | currentStage | maxStage | nickname | attack | critChance | critDamage | health | resistance |
      | bce12af4-9f70-47d7-b357-e6ea2b8d7bb7 | paul.ochon@test.com | 1000 | 1000    | 1000    | 1000     | 1000         | 1000     | test-1   | 1100   | 1200       | 1300       | 1400   | 1500       |

    When the user logs in with the following credentials
      | username            | password |
      | paul.ochon@test.com | toto1234 |

    And the user requests the endpoint to get the inventory of the game save with id bce12af4-9f70-47d7-b357-e6ea2b8d7bb7

    Then the response status code should be 200

    And the response should have the following itemResponses
      | id | type |

    And the inventory of the game save with id bce12af4-9f70-47d7-b357-e6ea2b8d7bb7 should be empty

  Scenario: A user requests its inventory when it is not empty
    Given the following game saves
      | id                                   | userEmail           | gold | diamond | emerald | amethyst | currentStage | maxStage | nickname | attack | critChance | critDamage | health | resistance |
      | bce12af4-9f70-47d7-b357-e6ea2b8d7bb7 | paul.ochon@test.com | 1000 | 1000    | 1000    | 1000     | 1000         | 1000     | test-1   | 1100   | 1200       | 1300       | 1400   | 1500       |

    And the following items to the inventory of the game save with id bce12af4-9f70-47d7-b357-e6ea2b8d7bb7
      | clientId                                                                   | id                                   | itemType   | blueprintId | itemRarity | isEquipped | level | mainStatBaseValue | mainStatStatistic | additionalStat1BaseValue | additionalStat1Statistic | additionalStat2BaseValue | additionalStat2Statistic | additionalStat3BaseValue | additionalStat3Statistic |
      | 36f27c2a-06e8-4bdb-bf59-56999116f5ef__11111111-1111-1111-1111-111111111111 | 11111111-1111-1111-1111-111111111111 | boots      | leg_boo_01  | LEGENDARY  | true       | 20    | 100               | attack_add        | 200                      | attack_mult              | 300                      | attack_mult              | 400                      | attack_mult              |
      | 36f27c2a-06e8-4bdb-bf59-56999116f5ef__22222222-2222-2222-2222-222222222222 | 22222222-2222-2222-2222-222222222222 | chestplate | leg_che_01  | LEGENDARY  | true       | 20    | 100               | attack_add        | 200                      | attack_mult              | 300                      | attack_mult              | 400                      | attack_mult              |
      | 36f27c2a-06e8-4bdb-bf59-56999116f5ef__33333333-3333-3333-3333-333333333333 | 33333333-3333-3333-3333-333333333333 | gloves     | leg_glo_01  | LEGENDARY  | true       | 20    | 100               | attack_add        | 200                      | attack_mult              | 300                      | attack_mult              | 400                      | attack_mult              |
      | 36f27c2a-06e8-4bdb-bf59-56999116f5ef__44444444-4444-4444-4444-444444444444 | 44444444-4444-4444-4444-444444444444 | helmet     | leg_hel_01  | LEGENDARY  | true       | 20    | 100               | attack_add        | 200                      | attack_mult              | 300                      | attack_mult              | 400                      | attack_mult              |
      | 36f27c2a-06e8-4bdb-bf59-56999116f5ef__55555555-5555-5555-5555-555555555555 | 55555555-5555-5555-5555-555555555555 | shield     | leg_she_01  | LEGENDARY  | true       | 20    | 100               | attack_add        | 200                      | attack_mult              | 300                      | attack_mult              | 400                      | attack_mult              |
      | 36f27c2a-06e8-4bdb-bf59-56999116f5ef__66666666-6666-6666-6666-666666666666 | 66666666-6666-6666-6666-666666666666 | sword      | leg_swo_01  | LEGENDARY  | true       | 20    | 100               | attack_add        | 200                      | attack_mult              | 300                      | attack_mult              | 400                      | attack_mult              |


    When the user logs in with the following credentials
      | username            | password |
      | paul.ochon@test.com | toto1234 |

    And the user requests the endpoint to get the inventory of the game save with id bce12af4-9f70-47d7-b357-e6ea2b8d7bb7

    Then the response status code should be 200

    And the response should have the following itemResponses
      | clientId                                                                   | id                                   | itemType   | blueprintId | itemRarity | isEquipped | level | mainStatBaseValue | mainStatStatistic | additionalStat1BaseValue | additionalStat1Statistic | additionalStat2BaseValue | additionalStat2Statistic | additionalStat3BaseValue | additionalStat3Statistic |
      | 36f27c2a-06e8-4bdb-bf59-56999116f5ef__11111111-1111-1111-1111-111111111111 | 11111111-1111-1111-1111-111111111111 | boots      | leg_boo_01  | LEGENDARY  | true       | 20    | 100               | attack_add        | 200                      | attack_mult              | 300                      | attack_mult              | 400                      | attack_mult              |
      | 36f27c2a-06e8-4bdb-bf59-56999116f5ef__22222222-2222-2222-2222-222222222222 | 22222222-2222-2222-2222-222222222222 | chestplate | leg_che_01  | LEGENDARY  | true       | 20    | 100               | attack_add        | 200                      | attack_mult              | 300                      | attack_mult              | 400                      | attack_mult              |
      | 36f27c2a-06e8-4bdb-bf59-56999116f5ef__33333333-3333-3333-3333-333333333333 | 33333333-3333-3333-3333-333333333333 | gloves     | leg_glo_01  | LEGENDARY  | true       | 20    | 100               | attack_add        | 200                      | attack_mult              | 300                      | attack_mult              | 400                      | attack_mult              |
      | 36f27c2a-06e8-4bdb-bf59-56999116f5ef__44444444-4444-4444-4444-444444444444 | 44444444-4444-4444-4444-444444444444 | helmet     | leg_hel_01  | LEGENDARY  | true       | 20    | 100               | attack_add        | 200                      | attack_mult              | 300                      | attack_mult              | 400                      | attack_mult              |
      | 36f27c2a-06e8-4bdb-bf59-56999116f5ef__55555555-5555-5555-5555-555555555555 | 55555555-5555-5555-5555-555555555555 | shield     | leg_she_01  | LEGENDARY  | true       | 20    | 100               | attack_add        | 200                      | attack_mult              | 300                      | attack_mult              | 400                      | attack_mult              |
      | 36f27c2a-06e8-4bdb-bf59-56999116f5ef__66666666-6666-6666-6666-666666666666 | 66666666-6666-6666-6666-666666666666 | sword      | leg_swo_01  | LEGENDARY  | true       | 20    | 100               | attack_add        | 200                      | attack_mult              | 300                      | attack_mult              | 400                      | attack_mult              |

#  ---- CREATE ----
  Scenario: A user requests to create an item in its inventory
    Given the following game saves
      | id                                   | userEmail           | gold | diamond | emerald | amethyst | currentStage | maxStage | nickname | attack | critChance | critDamage | health | resistance |
      | bce12af4-9f70-47d7-b357-e6ea2b8d7bb7 | paul.ochon@test.com | 1000 | 1000    | 1000    | 1000     | 1000         | 1000     | test-1   | 1100   | 1200       | 1300       | 1400   | 1500       |

    And the following game sessions
      | id                                   | gameSaveId                           | cancelled | version |
      | 6025d3de-49ee-4ca1-98c0-28cb49f85e87 | bce12af4-9f70-47d7-b357-e6ea2b8d7bb7 | false     | 1       |

    When the user logs in with the following credentials
      | username            | password |
      | paul.ochon@test.com | toto1234 |

    And the user requests the endpoint to create an item in the inventory of the game save with id bce12af4-9f70-47d7-b357-e6ea2b8d7bb7 with session id 6025d3de-49ee-4ca1-98c0-28cb49f85e87 and the following ItemCreationRequest
      | clientId                                                                   | itemType | blueprintId | itemRarity | isEquipped | level | mainStatBaseValue | mainStatStatistic | additionalStat1BaseValue | additionalStat1Statistic | additionalStat2BaseValue | additionalStat2Statistic | additionalStat3BaseValue | additionalStat3Statistic |
      | 36f27c2a-06e8-4bdb-bf59-56999116f5ef__11111111-1111-1111-1111-111111111111 | boots    | leg_boo_01  | LEGENDARY  | true       | 20    | 100               | attack_add        | 200                      | attack_mult              | 300                      | attack_mult              | 400                      | attack_mult              |

    Then the response status code should be 200

  Scenario: A user requests to create an item in its inventory with an already existing client id
    Given the following game saves
      | id                                   | userEmail           | gold | diamond | emerald | amethyst | currentStage | maxStage | nickname | attack | critChance | critDamage | health | resistance |
      | bce12af4-9f70-47d7-b357-e6ea2b8d7bb7 | paul.ochon@test.com | 1000 | 1000    | 1000    | 1000     | 1000         | 1000     | test-1   | 1100   | 1200       | 1300       | 1400   | 1500       |

    And the following items to the inventory of the game save with id bce12af4-9f70-47d7-b357-e6ea2b8d7bb7
      | clientId                                                                   | id                                   | itemType | blueprintId | itemRarity | isEquipped | level | mainStatBaseValue | mainStatStatistic | additionalStat1BaseValue | additionalStat1Statistic | additionalStat2BaseValue | additionalStat2Statistic | additionalStat3BaseValue | additionalStat3Statistic |
      | 36f27c2a-06e8-4bdb-bf59-56999116f5ef__11111111-1111-1111-1111-111111111111 | 11111111-1111-1111-1111-111111111111 | boots    | leg_boo_01  | LEGENDARY  | true       | 20    | 100               | attack_add        | 200                      | attack_mult              | 300                      | attack_mult              | 400                      | attack_mult              |

    And the following game sessions
      | id                                   | gameSaveId                           | cancelled | version |
      | 6025d3de-49ee-4ca1-98c0-28cb49f85e87 | bce12af4-9f70-47d7-b357-e6ea2b8d7bb7 | false     | 1       |

    When the user logs in with the following credentials
      | username            | password |
      | paul.ochon@test.com | toto1234 |

    And the user requests the endpoint to create an item in the inventory of the game save with id bce12af4-9f70-47d7-b357-e6ea2b8d7bb7 with session id 6025d3de-49ee-4ca1-98c0-28cb49f85e87 and the following ItemCreationRequest
      | clientId                                                                   | itemType | blueprintId | itemRarity | isEquipped | level | mainStatBaseValue | mainStatStatistic | additionalStat1BaseValue | additionalStat1Statistic | additionalStat2BaseValue | additionalStat2Statistic | additionalStat3BaseValue | additionalStat3Statistic |
      | 36f27c2a-06e8-4bdb-bf59-56999116f5ef__11111111-1111-1111-1111-111111111111 | boots    | leg_boo_01  | LEGENDARY  | true       | 20    | 100               | attack_add        | 200                      | attack_mult              | 300                      | attack_mult              | 400                      | attack_mult              |

    Then the response status code should be 400

  Scenario: A user requests to create an item in the inventory of another user
    Given the following game saves
      | id                                   | userEmail            | gold | diamond | emerald | amethyst | currentStage | maxStage | nickname | attack | critChance | critDamage | health | resistance |
      | bce12af4-9f70-47d7-b357-e6ea2b8d7bb7 | paul.ochon@test.com  | 1000 | 1000    | 1000    | 1000     | 1000         | 1000     | test-1   | 1100   | 1200       | 1300       | 1400   | 1500       |
      | 13a1ce35-e2de-4c35-9d62-d5aa47eeab98 | paul.itesse@test.com | 1000 | 1000    | 1000    | 1000     | 1000         | 1000     | test-2   | 600    | 700        | 800        | 900    | 1000       |

    And the following game sessions
      | id                                   | gameSaveId                           | cancelled | version |
      | 6025d3de-49ee-4ca1-98c0-28cb49f85e87 | bce12af4-9f70-47d7-b357-e6ea2b8d7bb7 | false     | 1       |

    When the user logs in with the following credentials
      | username            | password |
      | paul.ochon@test.com | toto1234 |

    And the user requests the endpoint to create an item in the inventory of the game save with id 13a1ce35-e2de-4c35-9d62-d5aa47eeab98 with session id 6025d3de-49ee-4ca1-98c0-28cb49f85e87 and the following ItemCreationRequest
      | clientId                                                                   | id                                   | itemType | blueprintId | itemRarity | isEquipped | level | mainStatBaseValue | mainStatStatistic | additionalStat1BaseValue | additionalStat1Statistic | additionalStat2BaseValue | additionalStat2Statistic | additionalStat3BaseValue | additionalStat3Statistic |
      | 36f27c2a-06e8-4bdb-bf59-56999116f5ef__11111111-1111-1111-1111-111111111111 | 11111111-1111-1111-1111-111111111111 | boots    | leg_boo_01  | LEGENDARY  | true       | 20    | 100               | attack_add        | 200                      | attack_mult              | 300                      | attack_mult              | 400                      | attack_mult              |

    Then the response status code should be 403

  Scenario: A user requests to create an item in an invalid inventory
    Given the following game saves
      | id                                   | userEmail           | gold | diamond | emerald | amethyst | currentStage | maxStage | nickname | attack | critChance | critDamage | health | resistance |
      | bce12af4-9f70-47d7-b357-e6ea2b8d7bb7 | paul.ochon@test.com | 1000 | 1000    | 1000    | 1000     | 1000         | 1000     | test-1   | 1100   | 1200       | 1300       | 1400   | 1500       |

    And the following game sessions
      | id                                   | gameSaveId                           | cancelled | version |
      | 6025d3de-49ee-4ca1-98c0-28cb49f85e87 | bce12af4-9f70-47d7-b357-e6ea2b8d7bb7 | false     | 1       |

    When the user logs in with the following credentials
      | username            | password |
      | paul.ochon@test.com | toto1234 |

    And the user requests the endpoint to create an item in the inventory of the game save with id invalid_id with session id 6025d3de-49ee-4ca1-98c0-28cb49f85e87 and the following ItemCreationRequest
      | itemType | blueprintId | itemRarity | isEquipped | level | mainStatBaseValue | mainStatStatistic | additionalStat1BaseValue | additionalStat1Statistic | additionalStat2BaseValue | additionalStat2Statistic | additionalStat3BaseValue | additionalStat3Statistic |
      | boots    | leg_boo_01  | LEGENDARY  | true       | 20    | 100               | attack_add        | 200                      | attack_mult              | 300                      | attack_mult              | 400                      | attack_mult              |

    Then the response status code should be 400

  Scenario: A user requests to create an item in the inventory of a inexistent game save
    Given the following game saves
      | id                                   | userEmail           | gold | diamond | emerald | amethyst | currentStage | maxStage | nickname | attack | critChance | critDamage | health | resistance |
      | bce12af4-9f70-47d7-b357-e6ea2b8d7bb7 | paul.ochon@test.com | 1000 | 1000    | 1000    | 1000     | 1000         | 1000     | test-1   | 1100   | 1200       | 1300       | 1400   | 1500       |

    And the following game sessions
      | id                                   | gameSaveId                           | cancelled | version |
      | 6025d3de-49ee-4ca1-98c0-28cb49f85e87 | bce12af4-9f70-47d7-b357-e6ea2b8d7bb7 | false     | 1       |

    When the user logs in with the following credentials
      | username            | password |
      | paul.ochon@test.com | toto1234 |

    And the user requests the endpoint to create an item in the inventory of the game save with id 13a1ce35-e2de-4c35-9d62-d5aa47eeab98 with session id 6025d3de-49ee-4ca1-98c0-28cb49f85e87 and the following ItemCreationRequest
      | clientId                                                                   | id                                   | itemType | blueprintId | itemRarity | isEquipped | level | mainStatBaseValue | mainStatStatistic | additionalStat1BaseValue | additionalStat1Statistic | additionalStat2BaseValue | additionalStat2Statistic | additionalStat3BaseValue | additionalStat3Statistic |
      | 36f27c2a-06e8-4bdb-bf59-56999116f5ef__11111111-1111-1111-1111-111111111111 | 11111111-1111-1111-1111-111111111111 | boots    | leg_boo_01  | LEGENDARY  | true       | 20    | 100               | attack_add        | 200                      | attack_mult              | 300                      | attack_mult              | 400                      | attack_mult              |

    Then the response status code should be 404
#  ---- DELETE ----

  Scenario: A user requests to delete an item in its inventory
    Given the following game saves
      | id                                   | userEmail           | gold | diamond | emerald | amethyst | currentStage | maxStage | nickname | attack | critChance | critDamage | health | resistance |
      | bce12af4-9f70-47d7-b357-e6ea2b8d7bb7 | paul.ochon@test.com | 1000 | 1000    | 1000    | 1000     | 1000         | 1000     | test-1   | 1100   | 1200       | 1300       | 1400   | 1500       |

    And the following game sessions
      | id                                   | gameSaveId                           | cancelled | version |
      | 6025d3de-49ee-4ca1-98c0-28cb49f85e87 | bce12af4-9f70-47d7-b357-e6ea2b8d7bb7 | false     | 1       |

    And the following items to the inventory of the game save with id bce12af4-9f70-47d7-b357-e6ea2b8d7bb7
      | clientId                                                                   | id                                   | itemType   | blueprintId | itemRarity | isEquipped | level | mainStatBaseValue | mainStatStatistic | additionalStat1BaseValue | additionalStat1Statistic | additionalStat2BaseValue | additionalStat2Statistic | additionalStat3BaseValue | additionalStat3Statistic |
      | 36f27c2a-06e8-4bdb-bf59-56999116f5ef__11111111-1111-1111-1111-111111111111 | 11111111-1111-1111-1111-111111111111 | boots      | leg_boo_01  | LEGENDARY  | true       | 20    | 100               | attack_add        | 200                      | attack_mult              | 300                      | attack_mult              | 400                      | attack_mult              |
      | 36f27c2a-06e8-4bdb-bf59-56999116f5ef__22222222-2222-2222-2222-222222222222 | 22222222-2222-2222-2222-222222222222 | chestplate | leg_che_01  | LEGENDARY  | true       | 20    | 100               | attack_add        | 200                      | attack_mult              | 300                      | attack_mult              | 400                      | attack_mult              |
      | 36f27c2a-06e8-4bdb-bf59-56999116f5ef__33333333-3333-3333-3333-333333333333 | 33333333-3333-3333-3333-333333333333 | gloves     | leg_glo_01  | LEGENDARY  | true       | 20    | 100               | attack_add        | 200                      | attack_mult              | 300                      | attack_mult              | 400                      | attack_mult              |
      | 36f27c2a-06e8-4bdb-bf59-56999116f5ef__44444444-4444-4444-4444-444444444444 | 44444444-4444-4444-4444-444444444444 | helmet     | leg_hel_01  | LEGENDARY  | true       | 20    | 100               | attack_add        | 200                      | attack_mult              | 300                      | attack_mult              | 400                      | attack_mult              |
      | 36f27c2a-06e8-4bdb-bf59-56999116f5ef__55555555-5555-5555-5555-555555555555 | 55555555-5555-5555-5555-555555555555 | shield     | leg_she_01  | LEGENDARY  | true       | 20    | 100               | attack_add        | 200                      | attack_mult              | 300                      | attack_mult              | 400                      | attack_mult              |
      | 36f27c2a-06e8-4bdb-bf59-56999116f5ef__66666666-6666-6666-6666-666666666666 | 66666666-6666-6666-6666-666666666666 | sword      | leg_swo_01  | LEGENDARY  | true       | 20    | 100               | attack_add        | 200                      | attack_mult              | 300                      | attack_mult              | 400                      | attack_mult              |

    When the user logs in with the following credentials
      | username            | password |
      | paul.ochon@test.com | toto1234 |

    And the user requests the endpoint to delete an item with client id 36f27c2a-06e8-4bdb-bf59-56999116f5ef__11111111-1111-1111-1111-111111111111 in the inventory of the game save with id bce12af4-9f70-47d7-b357-e6ea2b8d7bb7 and session id 6025d3de-49ee-4ca1-98c0-28cb49f85e87

    Then the response status code should be 200

  Scenario: A user requests to delete an item in the inventory of another user
    Given the following game saves
      | id                                   | userEmail            | gold | diamond | emerald | amethyst | currentStage | maxStage | nickname | attack | critChance | critDamage | health | resistance |
      | bce12af4-9f70-47d7-b357-e6ea2b8d7bb7 | paul.ochon@test.com  | 1000 | 1000    | 1000    | 1000     | 1000         | 1000     | test-1   | 1100   | 1200       | 1300       | 1400   | 1500       |
      | 13a1ce35-e2de-4c35-9d62-d5aa47eeab98 | paul.itesse@test.com | 1000 | 1000    | 1000    | 1000     | 1000         | 1000     | test-2   | 600    | 700        | 800        | 900    | 1000       |

    And the following game sessions
      | id                                   | gameSaveId                           | cancelled | version |
      | 6025d3de-49ee-4ca1-98c0-28cb49f85e87 | bce12af4-9f70-47d7-b357-e6ea2b8d7bb7 | false     | 1       |

    And the following items to the inventory of the game save with id bce12af4-9f70-47d7-b357-e6ea2b8d7bb7
      | clientId                                                                   | id                                   | itemType   | blueprintId | itemRarity | isEquipped | level | mainStatBaseValue | mainStatStatistic | additionalStat1BaseValue | additionalStat1Statistic | additionalStat2BaseValue | additionalStat2Statistic | additionalStat3BaseValue | additionalStat3Statistic |
      | 36f27c2a-06e8-4bdb-bf59-56999116f5ef__11111111-1111-1111-1111-111111111111 | 11111111-1111-1111-1111-111111111111 | boots      | leg_boo_01  | LEGENDARY  | true       | 20    | 100               | attack_add        | 200                      | attack_mult              | 300                      | attack_mult              | 400                      | attack_mult              |
      | 36f27c2a-06e8-4bdb-bf59-56999116f5ef__22222222-2222-2222-2222-222222222222 | 22222222-2222-2222-2222-222222222222 | chestplate | leg_che_01  | LEGENDARY  | true       | 20    | 100               | attack_add        | 200                      | attack_mult              | 300                      | attack_mult              | 400                      | attack_mult              |
      | 36f27c2a-06e8-4bdb-bf59-56999116f5ef__33333333-3333-3333-3333-333333333333 | 33333333-3333-3333-3333-333333333333 | gloves     | leg_glo_01  | LEGENDARY  | true       | 20    | 100               | attack_add        | 200                      | attack_mult              | 300                      | attack_mult              | 400                      | attack_mult              |
      | 36f27c2a-06e8-4bdb-bf59-56999116f5ef__44444444-4444-4444-4444-444444444444 | 44444444-4444-4444-4444-444444444444 | helmet     | leg_hel_01  | LEGENDARY  | true       | 20    | 100               | attack_add        | 200                      | attack_mult              | 300                      | attack_mult              | 400                      | attack_mult              |
      | 36f27c2a-06e8-4bdb-bf59-56999116f5ef__55555555-5555-5555-5555-555555555555 | 55555555-5555-5555-5555-555555555555 | shield     | leg_she_01  | LEGENDARY  | true       | 20    | 100               | attack_add        | 200                      | attack_mult              | 300                      | attack_mult              | 400                      | attack_mult              |
      | 36f27c2a-06e8-4bdb-bf59-56999116f5ef__66666666-6666-6666-6666-666666666666 | 66666666-6666-6666-6666-666666666666 | sword      | leg_swo_01  | LEGENDARY  | true       | 20    | 100               | attack_add        | 200                      | attack_mult              | 300                      | attack_mult              | 400                      | attack_mult              |

    When the user logs in with the following credentials
      | username            | password |
      | paul.ochon@test.com | toto1234 |

    And the user requests the endpoint to delete an item with client id 36f27c2a-06e8-4bdb-bf59-56999116f5ef__11111111-1111-1111-1111-111111111111 in the inventory of the game save with id 13a1ce35-e2de-4c35-9d62-d5aa47eeab98 and session id 6025d3de-49ee-4ca1-98c0-28cb49f85e87

    Then the response status code should be 403

  Scenario: A user requests to delete an item from the inventory of another user
    Given the following game saves
      | id                                   | userEmail            | gold | diamond | emerald | amethyst | currentStage | maxStage | nickname | attack | critChance | critDamage | health | resistance |
      | bce12af4-9f70-47d7-b357-e6ea2b8d7bb7 | paul.ochon@test.com  | 1000 | 1000    | 1000    | 1000     | 1000         | 1000     | test-1   | 1100   | 1200       | 1300       | 1400   | 1500       |
      | 13a1ce35-e2de-4c35-9d62-d5aa47eeab98 | paul.itesse@test.com | 1000 | 1000    | 1000    | 1000     | 1000         | 1000     | test-2   | 600    | 700        | 800        | 900    | 1000       |

    And the following game sessions
      | id                                   | gameSaveId                           | cancelled | version |
      | 6025d3de-49ee-4ca1-98c0-28cb49f85e87 | bce12af4-9f70-47d7-b357-e6ea2b8d7bb7 | false     | 1       |

    And the following items to the inventory of the game save with id 13a1ce35-e2de-4c35-9d62-d5aa47eeab98
      | clientId                                                                   | id                                   | itemType   | blueprintId | itemRarity | isEquipped | level | mainStatBaseValue | mainStatStatistic | additionalStat1BaseValue | additionalStat1Statistic | additionalStat2BaseValue | additionalStat2Statistic | additionalStat3BaseValue | additionalStat3Statistic |
      | 36f27c2a-06e8-4bdb-bf59-56999116f5ef__11111111-1111-1111-1111-111111111111 | 11111111-1111-1111-1111-111111111111 | boots      | leg_boo_01  | LEGENDARY  | true       | 20    | 100               | attack_add        | 200                      | attack_mult              | 300                      | attack_mult              | 400                      | attack_mult              |
      | 36f27c2a-06e8-4bdb-bf59-56999116f5ef__22222222-2222-2222-2222-222222222222 | 22222222-2222-2222-2222-222222222222 | chestplate | leg_che_01  | LEGENDARY  | true       | 20    | 100               | attack_add        | 200                      | attack_mult              | 300                      | attack_mult              | 400                      | attack_mult              |
      | 36f27c2a-06e8-4bdb-bf59-56999116f5ef__33333333-3333-3333-3333-333333333333 | 33333333-3333-3333-3333-333333333333 | gloves     | leg_glo_01  | LEGENDARY  | true       | 20    | 100               | attack_add        | 200                      | attack_mult              | 300                      | attack_mult              | 400                      | attack_mult              |
      | 36f27c2a-06e8-4bdb-bf59-56999116f5ef__44444444-4444-4444-4444-444444444444 | 44444444-4444-4444-4444-444444444444 | helmet     | leg_hel_01  | LEGENDARY  | true       | 20    | 100               | attack_add        | 200                      | attack_mult              | 300                      | attack_mult              | 400                      | attack_mult              |
      | 36f27c2a-06e8-4bdb-bf59-56999116f5ef__55555555-5555-5555-5555-555555555555 | 55555555-5555-5555-5555-555555555555 | shield     | leg_she_01  | LEGENDARY  | true       | 20    | 100               | attack_add        | 200                      | attack_mult              | 300                      | attack_mult              | 400                      | attack_mult              |
      | 36f27c2a-06e8-4bdb-bf59-56999116f5ef__66666666-6666-6666-6666-666666666666 | 66666666-6666-6666-6666-666666666666 | sword      | leg_swo_01  | LEGENDARY  | true       | 20    | 100               | attack_add        | 200                      | attack_mult              | 300                      | attack_mult              | 400                      | attack_mult              |

    When the user logs in with the following credentials
      | username            | password |
      | paul.ochon@test.com | toto1234 |

    And the user requests the endpoint to delete an item with client id 36f27c2a-06e8-4bdb-bf59-56999116f5ef__11111111-1111-1111-1111-111111111111 in the inventory of the game save with id 13a1ce35-e2de-4c35-9d62-d5aa47eeab98 and session id 6025d3de-49ee-4ca1-98c0-28cb49f85e87

    Then the response status code should be 403

  Scenario: A user requests to delete an item from the inventory of inexistent client id
    Given the following game saves
      | id                                   | userEmail           | gold | diamond | emerald | amethyst | currentStage | maxStage | nickname | attack | critChance | critDamage | health | resistance |
      | bce12af4-9f70-47d7-b357-e6ea2b8d7bb7 | paul.ochon@test.com | 1000 | 1000    | 1000    | 1000     | 1000         | 1000     | test-1   | 1100   | 1200       | 1300       | 1400   | 1500       |

    And the following game sessions
      | id                                   | gameSaveId                           | cancelled | version |
      | 6025d3de-49ee-4ca1-98c0-28cb49f85e87 | bce12af4-9f70-47d7-b357-e6ea2b8d7bb7 | false     | 1       |

    And the following items to the inventory of the game save with id bce12af4-9f70-47d7-b357-e6ea2b8d7bb7
      | clientId                                                                   | id                                   | itemType   | blueprintId | itemRarity | isEquipped | level | mainStatBaseValue | mainStatStatistic | additionalStat1BaseValue | additionalStat1Statistic | additionalStat2BaseValue | additionalStat2Statistic | additionalStat3BaseValue | additionalStat3Statistic |
      | 36f27c2a-06e8-4bdb-bf59-56999116f5ef__11111111-1111-1111-1111-111111111111 | 11111111-1111-1111-1111-111111111111 | boots      | leg_boo_01  | LEGENDARY  | true       | 20    | 100               | attack_add        | 200                      | attack_mult              | 300                      | attack_mult              | 400                      | attack_mult              |
      | 36f27c2a-06e8-4bdb-bf59-56999116f5ef__22222222-2222-2222-2222-222222222222 | 22222222-2222-2222-2222-222222222222 | chestplate | leg_che_01  | LEGENDARY  | true       | 20    | 100               | attack_add        | 200                      | attack_mult              | 300                      | attack_mult              | 400                      | attack_mult              |
      | 36f27c2a-06e8-4bdb-bf59-56999116f5ef__33333333-3333-3333-3333-333333333333 | 33333333-3333-3333-3333-333333333333 | gloves     | leg_glo_01  | LEGENDARY  | true       | 20    | 100               | attack_add        | 200                      | attack_mult              | 300                      | attack_mult              | 400                      | attack_mult              |
      | 36f27c2a-06e8-4bdb-bf59-56999116f5ef__44444444-4444-4444-4444-444444444444 | 44444444-4444-4444-4444-444444444444 | helmet     | leg_hel_01  | LEGENDARY  | true       | 20    | 100               | attack_add        | 200                      | attack_mult              | 300                      | attack_mult              | 400                      | attack_mult              |
      | 36f27c2a-06e8-4bdb-bf59-56999116f5ef__55555555-5555-5555-5555-555555555555 | 55555555-5555-5555-5555-555555555555 | shield     | leg_she_01  | LEGENDARY  | true       | 20    | 100               | attack_add        | 200                      | attack_mult              | 300                      | attack_mult              | 400                      | attack_mult              |
      | 36f27c2a-06e8-4bdb-bf59-56999116f5ef__66666666-6666-6666-6666-666666666666 | 66666666-6666-6666-6666-666666666666 | sword      | leg_swo_01  | LEGENDARY  | true       | 20    | 100               | attack_add        | 200                      | attack_mult              | 300                      | attack_mult              | 400                      | attack_mult              |

    When the user logs in with the following credentials
      | username            | password |
      | paul.ochon@test.com | toto1234 |

    And the user requests the endpoint to delete an item with client id 11111111-1111-1111-1111-111111111111 in the inventory of the game save with id bce12af4-9f70-47d7-b357-e6ea2b8d7bb7 and session id 6025d3de-49ee-4ca1-98c0-28cb49f85e87

    Then the response status code should be 404

  Scenario: A user requests to delete an item in the inventory of a inexistent game save
    Given the following game saves
      | id                                   | userEmail           | gold | diamond | emerald | amethyst | currentStage | maxStage | nickname | attack | critChance | critDamage | health | resistance |
      | bce12af4-9f70-47d7-b357-e6ea2b8d7bb7 | paul.ochon@test.com | 1000 | 1000    | 1000    | 1000     | 1000         | 1000     | test-1   | 1100   | 1200       | 1300       | 1400   | 1500       |

    And the following game sessions
      | id                                   | gameSaveId                           | cancelled | version |
      | 6025d3de-49ee-4ca1-98c0-28cb49f85e87 | bce12af4-9f70-47d7-b357-e6ea2b8d7bb7 | false     | 1       |

    When the user logs in with the following credentials
      | username            | password |
      | paul.ochon@test.com | toto1234 |

    And the user requests the endpoint to delete an item with client id 36f27c2a-06e8-4bdb-bf59-56999116f5ef__11111111-1111-1111-1111-111111111111 in the inventory of the game save with id 13a1ce35-e2de-4c35-9d62-d5aa47eeab98 and session id 6025d3de-49ee-4ca1-98c0-28cb49f85e87

    Then the response status code should be 404

#  ---- UPDATE ----
  Scenario: A user requests to update an item in its inventory
    Given the following game saves
      | id                                   | userEmail           | gold | diamond | emerald | amethyst | currentStage | maxStage | nickname | attack | critChance | critDamage | health | resistance |
      | bce12af4-9f70-47d7-b357-e6ea2b8d7bb7 | paul.ochon@test.com | 1000 | 1000    | 1000    | 1000     | 1000         | 1000     | test-1   | 1100   | 1200       | 1300       | 1400   | 1500       |

    And the following game sessions
      | id                                   | gameSaveId                           | cancelled | version |
      | 6025d3de-49ee-4ca1-98c0-28cb49f85e87 | bce12af4-9f70-47d7-b357-e6ea2b8d7bb7 | false     | 1       |

    And the following items to the inventory of the game save with id bce12af4-9f70-47d7-b357-e6ea2b8d7bb7
      | clientId                                                                   | id                                   | itemType   | blueprintId | itemRarity | isEquipped | level | mainStatBaseValue | mainStatStatistic | additionalStat1BaseValue | additionalStat1Statistic | additionalStat2BaseValue | additionalStat2Statistic | additionalStat3BaseValue | additionalStat3Statistic |
      | 36f27c2a-06e8-4bdb-bf59-56999116f5ef__11111111-1111-1111-1111-111111111111 | 11111111-1111-1111-1111-111111111111 | boots      | leg_boo_01  | LEGENDARY  | true       | 20    | 100               | attack_add        | 200                      | attack_mult              | 300                      | attack_mult              | 400                      | attack_mult              |
      | 36f27c2a-06e8-4bdb-bf59-56999116f5ef__22222222-2222-2222-2222-222222222222 | 22222222-2222-2222-2222-222222222222 | chestplate | leg_che_01  | LEGENDARY  | true       | 20    | 100               | attack_add        | 200                      | attack_mult              | 300                      | attack_mult              | 400                      | attack_mult              |
      | 36f27c2a-06e8-4bdb-bf59-56999116f5ef__33333333-3333-3333-3333-333333333333 | 33333333-3333-3333-3333-333333333333 | gloves     | leg_glo_01  | LEGENDARY  | true       | 20    | 100               | attack_add        | 200                      | attack_mult              | 300                      | attack_mult              | 400                      | attack_mult              |
      | 36f27c2a-06e8-4bdb-bf59-56999116f5ef__44444444-4444-4444-4444-444444444444 | 44444444-4444-4444-4444-444444444444 | helmet     | leg_hel_01  | LEGENDARY  | true       | 20    | 100               | attack_add        | 200                      | attack_mult              | 300                      | attack_mult              | 400                      | attack_mult              |
      | 36f27c2a-06e8-4bdb-bf59-56999116f5ef__55555555-5555-5555-5555-555555555555 | 55555555-5555-5555-5555-555555555555 | shield     | leg_she_01  | LEGENDARY  | true       | 20    | 100               | attack_add        | 200                      | attack_mult              | 300                      | attack_mult              | 400                      | attack_mult              |
      | 36f27c2a-06e8-4bdb-bf59-56999116f5ef__66666666-6666-6666-6666-666666666666 | 66666666-6666-6666-6666-666666666666 | sword      | leg_swo_01  | LEGENDARY  | true       | 20    | 100               | attack_add        | 200                      | attack_mult              | 300                      | attack_mult              | 400                      | attack_mult              |

    When the user logs in with the following credentials
      | username            | password |
      | paul.ochon@test.com | toto1234 |

    And the user requests the endpoint to update an item with client id 36f27c2a-06e8-4bdb-bf59-56999116f5ef__11111111-1111-1111-1111-111111111111 in the inventory of the game save with id bce12af4-9f70-47d7-b357-e6ea2b8d7bb7 with session id 6025d3de-49ee-4ca1-98c0-28cb49f85e87 and the following ItemUpdateRequest
      | clientId                                                                   | id                                   | itemType | blueprintId | itemRarity | isEquipped | level | mainStatBaseValue | mainStatStatistic | additionalStat1BaseValue | additionalStat1Statistic | additionalStat2BaseValue | additionalStat2Statistic | additionalStat3BaseValue | additionalStat3Statistic |
      | 36f27c2a-06e8-4bdb-bf59-56999116f5ef__11111111-1111-1111-1111-111111111111 | 11111111-1111-1111-1111-111111111111 | boots    | leg_boo_01  | LEGENDARY  | true       | 20    | 100               | attack_add        | 200                      | attack_mult              | 300                      | attack_mult              | 400                      | attack_mult              |


    Then the response status code should be 200

  Scenario: A user requests to update an item with an inexistent client id
    Given the following game saves
      | id                                   | userEmail           | gold | diamond | emerald | amethyst | currentStage | maxStage | nickname | attack | critChance | critDamage | health | resistance |
      | bce12af4-9f70-47d7-b357-e6ea2b8d7bb7 | paul.ochon@test.com | 1000 | 1000    | 1000    | 1000     | 1000         | 1000     | test-1   | 1100   | 1200       | 1300       | 1400   | 1500       |

    And the following game sessions
      | id                                   | gameSaveId                           | cancelled | version |
      | 6025d3de-49ee-4ca1-98c0-28cb49f85e87 | bce12af4-9f70-47d7-b357-e6ea2b8d7bb7 | false     | 1       |

    And the following items to the inventory of the game save with id bce12af4-9f70-47d7-b357-e6ea2b8d7bb7
      | clientId                                                                   | id                                   | itemType   | blueprintId | itemRarity | isEquipped | level | mainStatBaseValue | mainStatStatistic | additionalStat1BaseValue | additionalStat1Statistic | additionalStat2BaseValue | additionalStat2Statistic | additionalStat3BaseValue | additionalStat3Statistic |
      | 36f27c2a-06e8-4bdb-bf59-56999116f5ef__11111111-1111-1111-1111-111111111111 | 11111111-1111-1111-1111-111111111111 | boots      | leg_boo_01  | LEGENDARY  | true       | 20    | 100               | attack_add        | 200                      | attack_mult              | 300                      | attack_mult              | 400                      | attack_mult              |
      | 36f27c2a-06e8-4bdb-bf59-56999116f5ef__22222222-2222-2222-2222-222222222222 | 22222222-2222-2222-2222-222222222222 | chestplate | leg_che_01  | LEGENDARY  | true       | 20    | 100               | attack_add        | 200                      | attack_mult              | 300                      | attack_mult              | 400                      | attack_mult              |
      | 36f27c2a-06e8-4bdb-bf59-56999116f5ef__33333333-3333-3333-3333-333333333333 | 33333333-3333-3333-3333-333333333333 | gloves     | leg_glo_01  | LEGENDARY  | true       | 20    | 100               | attack_add        | 200                      | attack_mult              | 300                      | attack_mult              | 400                      | attack_mult              |
      | 36f27c2a-06e8-4bdb-bf59-56999116f5ef__44444444-4444-4444-4444-444444444444 | 44444444-4444-4444-4444-444444444444 | helmet     | leg_hel_01  | LEGENDARY  | true       | 20    | 100               | attack_add        | 200                      | attack_mult              | 300                      | attack_mult              | 400                      | attack_mult              |
      | 36f27c2a-06e8-4bdb-bf59-56999116f5ef__55555555-5555-5555-5555-555555555555 | 55555555-5555-5555-5555-555555555555 | shield     | leg_she_01  | LEGENDARY  | true       | 20    | 100               | attack_add        | 200                      | attack_mult              | 300                      | attack_mult              | 400                      | attack_mult              |
      | 36f27c2a-06e8-4bdb-bf59-56999116f5ef__66666666-6666-6666-6666-666666666666 | 66666666-6666-6666-6666-666666666666 | sword      | leg_swo_01  | LEGENDARY  | true       | 20    | 100               | attack_add        | 200                      | attack_mult              | 300                      | attack_mult              | 400                      | attack_mult              |

    When the user logs in with the following credentials
      | username            | password |
      | paul.ochon@test.com | toto1234 |

    And the user requests the endpoint to update an item with client id 0000-0000-0000-0000-000000000000 in the inventory of the game save with id bce12af4-9f70-47d7-b357-e6ea2b8d7bb7 with session id 6025d3de-49ee-4ca1-98c0-28cb49f85e87 and the following ItemUpdateRequest
      | clientId                                                                   | id                                   | itemType | blueprintId | itemRarity | isEquipped | level | mainStatBaseValue | mainStatStatistic | additionalStat1BaseValue | additionalStat1Statistic | additionalStat2BaseValue | additionalStat2Statistic | additionalStat3BaseValue | additionalStat3Statistic |
      | 36f27c2a-06e8-4bdb-bf59-56999116f5ef__11111111-1111-1111-1111-111111111111 | 11111111-1111-1111-1111-111111111111 | boots    | leg_boo_01  | LEGENDARY  | true       | 20    | 100               | attack_add        | 200                      | attack_mult              | 300                      | attack_mult              | 400                      | attack_mult              |


    Then the response status code should be 404

  Scenario: A user requests to update an item from the inventory of another user
    Given the following game saves
      | id                                   | userEmail            | gold | diamond | emerald | amethyst | currentStage | maxStage | nickname | attack | critChance | critDamage | health | resistance |
      | bce12af4-9f70-47d7-b357-e6ea2b8d7bb7 | paul.ochon@test.com  | 1000 | 1000    | 1000    | 1000     | 1000         | 1000     | test-1   | 1100   | 1200       | 1300       | 1400   | 1500       |
      | 13a1ce35-e2de-4c35-9d62-d5aa47eeab98 | paul.itesse@test.com | 1000 | 1000    | 1000    | 1000     | 1000         | 1000     | test-2   | 600    | 700        | 800        | 900    | 1000       |

    And the following game sessions
      | id                                   | gameSaveId                           | cancelled | version |
      | 6025d3de-49ee-4ca1-98c0-28cb49f85e87 | bce12af4-9f70-47d7-b357-e6ea2b8d7bb7 | false     | 1       |

    And the following items to the inventory of the game save with id 13a1ce35-e2de-4c35-9d62-d5aa47eeab98
      | clientId                                                                   | id                                   | itemType   | blueprintId | itemRarity | isEquipped | level | mainStatBaseValue | mainStatStatistic | additionalStat1BaseValue | additionalStat1Statistic | additionalStat2BaseValue | additionalStat2Statistic | additionalStat3BaseValue | additionalStat3Statistic |
      | 36f27c2a-06e8-4bdb-bf59-56999116f5ef__11111111-1111-1111-1111-111111111111 | 11111111-1111-1111-1111-111111111111 | boots      | leg_boo_01  | LEGENDARY  | true       | 20    | 100               | attack_add        | 200                      | attack_mult              | 300                      | attack_mult              | 400                      | attack_mult              |
      | 36f27c2a-06e8-4bdb-bf59-56999116f5ef__22222222-2222-2222-2222-222222222222 | 22222222-2222-2222-2222-222222222222 | chestplate | leg_che_01  | LEGENDARY  | true       | 20    | 100               | attack_add        | 200                      | attack_mult              | 300                      | attack_mult              | 400                      | attack_mult              |
      | 36f27c2a-06e8-4bdb-bf59-56999116f5ef__33333333-3333-3333-3333-333333333333 | 33333333-3333-3333-3333-333333333333 | gloves     | leg_glo_01  | LEGENDARY  | true       | 20    | 100               | attack_add        | 200                      | attack_mult              | 300                      | attack_mult              | 400                      | attack_mult              |
      | 36f27c2a-06e8-4bdb-bf59-56999116f5ef__44444444-4444-4444-4444-444444444444 | 44444444-4444-4444-4444-444444444444 | helmet     | leg_hel_01  | LEGENDARY  | true       | 20    | 100               | attack_add        | 200                      | attack_mult              | 300                      | attack_mult              | 400                      | attack_mult              |
      | 36f27c2a-06e8-4bdb-bf59-56999116f5ef__55555555-5555-5555-5555-555555555555 | 55555555-5555-5555-5555-555555555555 | shield     | leg_she_01  | LEGENDARY  | true       | 20    | 100               | attack_add        | 200                      | attack_mult              | 300                      | attack_mult              | 400                      | attack_mult              |
      | 36f27c2a-06e8-4bdb-bf59-56999116f5ef__66666666-6666-6666-6666-666666666666 | 66666666-6666-6666-6666-666666666666 | sword      | leg_swo_01  | LEGENDARY  | true       | 20    | 100               | attack_add        | 200                      | attack_mult              | 300                      | attack_mult              | 400                      | attack_mult              |

    When the user logs in with the following credentials
      | username            | password |
      | paul.ochon@test.com | toto1234 |

    And the user requests the endpoint to update an item with client id 36f27c2a-06e8-4bdb-bf59-56999116f5ef__11111111-1111-1111-1111-111111111111 in the inventory of the game save with id 13a1ce35-e2de-4c35-9d62-d5aa47eeab98 with session id 6025d3de-49ee-4ca1-98c0-28cb49f85e87 and the following ItemUpdateRequest
      | clientId                                                                   | id                                   | itemType | blueprintId | itemRarity | isEquipped | level | mainStatBaseValue | mainStatStatistic | additionalStat1BaseValue | additionalStat1Statistic | additionalStat2BaseValue | additionalStat2Statistic | additionalStat3BaseValue | additionalStat3Statistic |
      | 36f27c2a-06e8-4bdb-bf59-56999116f5ef__11111111-1111-1111-1111-111111111111 | 11111111-1111-1111-1111-111111111111 | boots    | leg_boo_01  | LEGENDARY  | true       | 20    | 100               | attack_add        | 200                      | attack_mult              | 300                      | attack_mult              | 400                      | attack_mult              |


    Then the response status code should be 403

