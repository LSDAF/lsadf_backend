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
      | aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa | paul.ochon@test.com | 1000 | 1000    | 1000    | 1000     | 1000         | 1000     | test-1   | 1100   | 1200       | 1300       | 1400   | 1500       |

    When the user logs in with the following credentials
      | username            | password |
      | paul.ochon@test.com | toto1234 |

    And the user requests the endpoint to get the inventory of the game save with id aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa

    Then the response status code should be 200

    And the response should have the following itemResponses
      | id | type |

    And the inventory of the game save with id aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa should be empty

  Scenario: A user requests its inventory when it is not empty
    Given the following game saves
      | id                                   | userEmail           | gold | diamond | emerald | amethyst | currentStage | maxStage | nickname | attack | critChance | critDamage | health | resistance |
      | aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa | paul.ochon@test.com | 1000 | 1000    | 1000    | 1000     | 1000         | 1000     | test-1   | 1100   | 1200       | 1300       | 1400   | 1500       |

    And the following items to the inventory of the game save with id aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa
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

    And the user requests the endpoint to get the inventory of the game save with id aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa

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
      | aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa | paul.ochon@test.com | 1000 | 1000    | 1000    | 1000     | 1000         | 1000     | test-1   | 1100   | 1200       | 1300       | 1400   | 1500       |

    When the user logs in with the following credentials
      | username            | password |
      | paul.ochon@test.com | toto1234 |

    And the user requests the endpoint to create an item in the inventory of the game save with id aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa with the following ItemCreationRequest
      | clientId                                                                   | itemType | blueprintId | itemRarity | isEquipped | level | mainStatBaseValue | mainStatStatistic | additionalStat1BaseValue | additionalStat1Statistic | additionalStat2BaseValue | additionalStat2Statistic | additionalStat3BaseValue | additionalStat3Statistic |
      | 36f27c2a-06e8-4bdb-bf59-56999116f5ef__11111111-1111-1111-1111-111111111111 | boots    | leg_boo_01  | LEGENDARY  | true       | 20    | 100               | attack_add        | 200                      | attack_mult              | 300                      | attack_mult              | 400                      | attack_mult              |

    Then the response status code should be 200

  Scenario: A user requests to create an item in its inventory with an already existing client id
    Given the following game saves
      | id                                   | userEmail           | gold | diamond | emerald | amethyst | currentStage | maxStage | nickname | attack | critChance | critDamage | health | resistance |
      | aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa | paul.ochon@test.com | 1000 | 1000    | 1000    | 1000     | 1000         | 1000     | test-1   | 1100   | 1200       | 1300       | 1400   | 1500       |

    And the following items to the inventory of the game save with id aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa
      | clientId                                                                   | id                                   | itemType | blueprintId | itemRarity | isEquipped | level | mainStatBaseValue | mainStatStatistic | additionalStat1BaseValue | additionalStat1Statistic | additionalStat2BaseValue | additionalStat2Statistic | additionalStat3BaseValue | additionalStat3Statistic |
      | 36f27c2a-06e8-4bdb-bf59-56999116f5ef__11111111-1111-1111-1111-111111111111 | 11111111-1111-1111-1111-111111111111 | boots    | leg_boo_01  | LEGENDARY  | true       | 20    | 100               | attack_add        | 200                      | attack_mult              | 300                      | attack_mult              | 400                      | attack_mult              |

    When the user logs in with the following credentials
      | username            | password |
      | paul.ochon@test.com | toto1234 |

    And the user requests the endpoint to create an item in the inventory of the game save with id aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa with the following ItemCreationRequest
      | clientId                                                                   | itemType | blueprintId | itemRarity | isEquipped | level | mainStatBaseValue | mainStatStatistic | additionalStat1BaseValue | additionalStat1Statistic | additionalStat2BaseValue | additionalStat2Statistic | additionalStat3BaseValue | additionalStat3Statistic |
      | 36f27c2a-06e8-4bdb-bf59-56999116f5ef__11111111-1111-1111-1111-111111111111 | boots    | leg_boo_01  | LEGENDARY  | true       | 20    | 100               | attack_add        | 200                      | attack_mult              | 300                      | attack_mult              | 400                      | attack_mult              |

    Then the response status code should be 400

  Scenario: A user requests to create an item in the inventory of another user
    Given the following game saves
      | id                                   | userEmail            | gold | diamond | emerald | amethyst | currentStage | maxStage | nickname | attack | critChance | critDamage | health | resistance |
      | aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa | paul.ochon@test.com  | 1000 | 1000    | 1000    | 1000     | 1000         | 1000     | test-1   | 1100   | 1200       | 1300       | 1400   | 1500       |
      | bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb | paul.itesse@test.com | 1000 | 1000    | 1000    | 1000     | 1000         | 1000     | test-2   | 600    | 700        | 800        | 900    | 1000       |

    When the user logs in with the following credentials
      | username            | password |
      | paul.ochon@test.com | toto1234 |

    And the user requests the endpoint to create an item in the inventory of the game save with id bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb with the following ItemCreationRequest
      | clientId                                                                   | id                                   | itemType | blueprintId | itemRarity | isEquipped | level | mainStatBaseValue | mainStatStatistic | additionalStat1BaseValue | additionalStat1Statistic | additionalStat2BaseValue | additionalStat2Statistic | additionalStat3BaseValue | additionalStat3Statistic |
      | 36f27c2a-06e8-4bdb-bf59-56999116f5ef__11111111-1111-1111-1111-111111111111 | 11111111-1111-1111-1111-111111111111 | boots    | leg_boo_01  | LEGENDARY  | true       | 20    | 100               | attack_add        | 200                      | attack_mult              | 300                      | attack_mult              | 400                      | attack_mult              |

    Then the response status code should be 403

  Scenario: A user requests to create an item in an invalid inventory
    Given the following game saves
      | id                                   | userEmail           | gold | diamond | emerald | amethyst | currentStage | maxStage | nickname | attack | critChance | critDamage | health | resistance |
      | aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa | paul.ochon@test.com | 1000 | 1000    | 1000    | 1000     | 1000         | 1000     | test-1   | 1100   | 1200       | 1300       | 1400   | 1500       |

    When the user logs in with the following credentials
      | username            | password |
      | paul.ochon@test.com | toto1234 |

    And the user requests the endpoint to create an item in the inventory of the game save with id invalid_id with the following ItemCreationRequest
      | itemType | blueprintId | itemRarity | isEquipped | level | mainStatBaseValue | mainStatStatistic | additionalStat1BaseValue | additionalStat1Statistic | additionalStat2BaseValue | additionalStat2Statistic | additionalStat3BaseValue | additionalStat3Statistic |
      | boots    | leg_boo_01  | LEGENDARY  | true       | 20    | 100               | attack_add        | 200                      | attack_mult              | 300                      | attack_mult              | 400                      | attack_mult              |

    Then the response status code should be 400

  Scenario: A user requests to create an item in the inventory of a inexistent game save
    Given the following game saves
      | id                                   | userEmail           | gold | diamond | emerald | amethyst | currentStage | maxStage | nickname | attack | critChance | critDamage | health | resistance |
      | aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa | paul.ochon@test.com | 1000 | 1000    | 1000    | 1000     | 1000         | 1000     | test-1   | 1100   | 1200       | 1300       | 1400   | 1500       |

    When the user logs in with the following credentials
      | username            | password |
      | paul.ochon@test.com | toto1234 |

    And the user requests the endpoint to create an item in the inventory of the game save with id bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb with the following ItemCreationRequest
      | clientId                                                                   | id                                   | itemType | blueprintId | itemRarity | isEquipped | level | mainStatBaseValue | mainStatStatistic | additionalStat1BaseValue | additionalStat1Statistic | additionalStat2BaseValue | additionalStat2Statistic | additionalStat3BaseValue | additionalStat3Statistic |
      | 36f27c2a-06e8-4bdb-bf59-56999116f5ef__11111111-1111-1111-1111-111111111111 | 11111111-1111-1111-1111-111111111111 | boots    | leg_boo_01  | LEGENDARY  | true       | 20    | 100               | attack_add        | 200                      | attack_mult              | 300                      | attack_mult              | 400                      | attack_mult              |

    Then the response status code should be 404
#  ---- DELETE ----

  Scenario: A user requests to delete an item in its inventory
    Given the following game saves
      | id                                   | userEmail           | gold | diamond | emerald | amethyst | currentStage | maxStage | nickname | attack | critChance | critDamage | health | resistance |
      | aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa | paul.ochon@test.com | 1000 | 1000    | 1000    | 1000     | 1000         | 1000     | test-1   | 1100   | 1200       | 1300       | 1400   | 1500       |

    And the following items to the inventory of the game save with id aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa
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

    And the user requests the endpoint to delete an item with client id 36f27c2a-06e8-4bdb-bf59-56999116f5ef__11111111-1111-1111-1111-111111111111 in the inventory of the game save with id aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa

    Then the response status code should be 200

  Scenario: A user requests to delete an item in the inventory of another user
    Given the following game saves
      | id                                   | userEmail            | gold | diamond | emerald | amethyst | currentStage | maxStage | nickname | attack | critChance | critDamage | health | resistance |
      | aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa | paul.ochon@test.com  | 1000 | 1000    | 1000    | 1000     | 1000         | 1000     | test-1   | 1100   | 1200       | 1300       | 1400   | 1500       |
      | bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb | paul.itesse@test.com | 1000 | 1000    | 1000    | 1000     | 1000         | 1000     | test-2   | 600    | 700        | 800        | 900    | 1000       |

    And the following items to the inventory of the game save with id aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa
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

    And the user requests the endpoint to delete an item with client id 36f27c2a-06e8-4bdb-bf59-56999116f5ef__11111111-1111-1111-1111-111111111111 in the inventory of the game save with id bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb

    Then the response status code should be 403

  Scenario: A user requests to delete an item from the inventory of another user
    Given the following game saves
      | id                                   | userEmail            | gold | diamond | emerald | amethyst | currentStage | maxStage | nickname | attack | critChance | critDamage | health | resistance |
      | aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa | paul.ochon@test.com  | 1000 | 1000    | 1000    | 1000     | 1000         | 1000     | test-1   | 1100   | 1200       | 1300       | 1400   | 1500       |
      | bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb | paul.itesse@test.com | 1000 | 1000    | 1000    | 1000     | 1000         | 1000     | test-2   | 600    | 700        | 800        | 900    | 1000       |

    And the following items to the inventory of the game save with id bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb
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

    And the user requests the endpoint to delete an item with client id 36f27c2a-06e8-4bdb-bf59-56999116f5ef__11111111-1111-1111-1111-111111111111 in the inventory of the game save with id bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb

    Then the response status code should be 403

  Scenario: A user requests to delete an item from the inventory of inexistent client id
    Given the following game saves
      | id                                   | userEmail           | gold | diamond | emerald | amethyst | currentStage | maxStage | nickname | attack | critChance | critDamage | health | resistance |
      | aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa | paul.ochon@test.com | 1000 | 1000    | 1000    | 1000     | 1000         | 1000     | test-1   | 1100   | 1200       | 1300       | 1400   | 1500       |

    And the following items to the inventory of the game save with id aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa
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

    And the user requests the endpoint to delete an item with client id 11111111-1111-1111-1111-111111111111 in the inventory of the game save with id aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa

    Then the response status code should be 404

  Scenario: A user requests to delete an item in the inventory of a inexistent game save
    Given the following game saves
      | id                                   | userEmail           | gold | diamond | emerald | amethyst | currentStage | maxStage | nickname | attack | critChance | critDamage | health | resistance |
      | aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa | paul.ochon@test.com | 1000 | 1000    | 1000    | 1000     | 1000         | 1000     | test-1   | 1100   | 1200       | 1300       | 1400   | 1500       |

    When the user logs in with the following credentials
      | username            | password |
      | paul.ochon@test.com | toto1234 |

    And the user requests the endpoint to delete an item with client id 36f27c2a-06e8-4bdb-bf59-56999116f5ef__11111111-1111-1111-1111-111111111111 in the inventory of the game save with id bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb

    Then the response status code should be 404

#  ---- UPDATE ----
  Scenario: A user requests to update an item in its inventory
    Given the following game saves
      | id                                   | userEmail           | gold | diamond | emerald | amethyst | currentStage | maxStage | nickname | attack | critChance | critDamage | health | resistance |
      | aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa | paul.ochon@test.com | 1000 | 1000    | 1000    | 1000     | 1000         | 1000     | test-1   | 1100   | 1200       | 1300       | 1400   | 1500       |

    And the following items to the inventory of the game save with id aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa
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

    And the user requests the endpoint to update an item with client id 36f27c2a-06e8-4bdb-bf59-56999116f5ef__11111111-1111-1111-1111-111111111111 in the inventory of the game save with id aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa with the following ItemUpdateRequest
      | clientId                                                                   | id                                   | itemType | blueprintId | itemRarity | isEquipped | level | mainStatBaseValue | mainStatStatistic | additionalStat1BaseValue | additionalStat1Statistic | additionalStat2BaseValue | additionalStat2Statistic | additionalStat3BaseValue | additionalStat3Statistic |
      | 36f27c2a-06e8-4bdb-bf59-56999116f5ef__11111111-1111-1111-1111-111111111111 | 11111111-1111-1111-1111-111111111111 | boots    | leg_boo_01  | LEGENDARY  | true       | 20    | 100               | attack_add        | 200                      | attack_mult              | 300                      | attack_mult              | 400                      | attack_mult              |


    Then the response status code should be 200

  Scenario: A user requests to update an item with an inexistent client id
    Given the following game saves
      | id                                   | userEmail           | gold | diamond | emerald | amethyst | currentStage | maxStage | nickname | attack | critChance | critDamage | health | resistance |
      | aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa | paul.ochon@test.com | 1000 | 1000    | 1000    | 1000     | 1000         | 1000     | test-1   | 1100   | 1200       | 1300       | 1400   | 1500       |

    And the following items to the inventory of the game save with id aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa
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

    And the user requests the endpoint to update an item with client id 0000-0000-0000-0000-000000000000 in the inventory of the game save with id aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa with the following ItemUpdateRequest
      | clientId                                                                   | id                                   | itemType | blueprintId | itemRarity | isEquipped | level | mainStatBaseValue | mainStatStatistic | additionalStat1BaseValue | additionalStat1Statistic | additionalStat2BaseValue | additionalStat2Statistic | additionalStat3BaseValue | additionalStat3Statistic |
      | 36f27c2a-06e8-4bdb-bf59-56999116f5ef__11111111-1111-1111-1111-111111111111 | 11111111-1111-1111-1111-111111111111 | boots    | leg_boo_01  | LEGENDARY  | true       | 20    | 100               | attack_add        | 200                      | attack_mult              | 300                      | attack_mult              | 400                      | attack_mult              |


    Then the response status code should be 404

  Scenario: A user requests to update an item from the inventory of another user
    Given the following game saves
      | id                                   | userEmail            | gold | diamond | emerald | amethyst | currentStage | maxStage | nickname | attack | critChance | critDamage | health | resistance |
      | aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa | paul.ochon@test.com  | 1000 | 1000    | 1000    | 1000     | 1000         | 1000     | test-1   | 1100   | 1200       | 1300       | 1400   | 1500       |
      | bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb | paul.itesse@test.com | 1000 | 1000    | 1000    | 1000     | 1000         | 1000     | test-2   | 600    | 700        | 800        | 900    | 1000       |

    And the following items to the inventory of the game save with id bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb
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

    And the user requests the endpoint to update an item with client id 36f27c2a-06e8-4bdb-bf59-56999116f5ef__11111111-1111-1111-1111-111111111111 in the inventory of the game save with id bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb with the following ItemUpdateRequest
      | clientId                                                                   | id                                   | itemType | blueprintId | itemRarity | isEquipped | level | mainStatBaseValue | mainStatStatistic | additionalStat1BaseValue | additionalStat1Statistic | additionalStat2BaseValue | additionalStat2Statistic | additionalStat3BaseValue | additionalStat3Statistic |
      | 36f27c2a-06e8-4bdb-bf59-56999116f5ef__11111111-1111-1111-1111-111111111111 | 11111111-1111-1111-1111-111111111111 | boots    | leg_boo_01  | LEGENDARY  | true       | 20    | 100               | attack_add        | 200                      | attack_mult              | 300                      | attack_mult              | 400                      | attack_mult              |


    Then the response status code should be 403

