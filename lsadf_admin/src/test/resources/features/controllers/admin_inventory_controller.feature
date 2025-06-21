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

Feature: Admin Inventory Controller BDD tests

  Background:
    Given the BDD engine is ready
    And the cache is enabled
    And a clean database
    And the time clock set to the present
    And the following game saves
      | id                                   | userEmail           | gold | diamond | emerald | amethyst | currentStage | maxStage | nickname | attack | critChance | critDamage | health | resistance |
      | 0530e1fe-3428-4edd-bb32-cb563419d0bd | paul.ochon@test.com | 1    | 5       | 2       | 5        | 10           | 10       | test-1   | 100    | 200        | 300        | 400    | 500        |

    And the following items to the inventory of the game save with id 0530e1fe-3428-4edd-bb32-cb563419d0bd
      | clientId                               | id                                   | itemType   | blueprintId | itemRarity | isEquipped | level | mainStatBaseValue | mainStatStatistic | additionalStat1BaseValue | additionalStat1Statistic | additionalStat2BaseValue | additionalStat2Statistic | additionalStat3BaseValue | additionalStat3Statistic |
      | 668192b4-6057-47e5-bad0-fe795c8dbee6-1 | fc8640e1-382e-4c80-a17c-5a6d196f3932 | boots      | leg_boo_01  | LEGENDARY  | true       | 20    | 100               | attack_add        | 200                      | attack_mult              | 300                      | attack_mult              | 400                      | attack_mult              |
      | 668192b4-6057-47e5-bad0-fe795c8dbee6-2 | 1d4fbe96-cc9b-4c47-b140-545bb77c5a42 | chestplate | leg_che_01  | EPIC       | true       | 20    | 100               | attack_add        | 200                      | attack_mult              | 300                      | attack_mult              | 400                      | attack_mult              |

    # We assume we have at least the following user in keycloak
    # paul.ochon@test.com: ADMIN,USER: toto1234: ce60ea41-3765-4562-8c96-8673de8f96b0

  Scenario: A user gets a game inventory
    When the user logs in with the following credentials
      | username            | password |
      | paul.ochon@test.com | toto1234 |
    And the user requests the admin endpoint to get the inventory for game save with id 0530e1fe-3428-4edd-bb32-cb563419d0bd

    Then the response status code should be 200

    And the response should have the following itemResponses
      | clientId                               | id                                   | itemType   | blueprintId | itemRarity | isEquipped | level | mainStatBaseValue | mainStatStatistic | additionalStat1BaseValue | additionalStat1Statistic | additionalStat2BaseValue | additionalStat2Statistic | additionalStat3BaseValue | additionalStat3Statistic |
      | 668192b4-6057-47e5-bad0-fe795c8dbee6-1 | fc8640e1-382e-4c80-a17c-5a6d196f3932 | boots      | leg_boo_01  | LEGENDARY  | true       | 20    | 100               | attack_add        | 200                      | attack_mult              | 300                      | attack_mult              | 400                      | attack_mult              |
      | 668192b4-6057-47e5-bad0-fe795c8dbee6-2 | 1d4fbe96-cc9b-4c47-b140-545bb77c5a42 | chestplate | leg_che_01  | EPIC       | true       | 20    | 100               | attack_add        | 200                      | attack_mult              | 300                      | attack_mult              | 400                      | attack_mult              |

  Scenario: A user gets a non-existing game inventory
    When the user logs in with the following credentials
      | username            | password |
      | paul.ochon@test.com | toto1234 |

    And the user requests the admin endpoint to get the inventory for game save with id c9f74ebe-7a92-405f-9b38-624e40a2ae4b

    Then the response status code should be 404

  Scenario: A user adds a new item in a game inventory
    When the user logs in with the following credentials
      | username            | password |
      | paul.ochon@test.com | toto1234 |

    And the user requests the admin endpoint to create a new item in the inventory for game save with id 0530e1fe-3428-4edd-bb32-cb563419d0bd with the following ItemRequest
      | clientId                                | id                                   | itemType | blueprintId | itemRarity | isEquipped | level | mainStatBaseValue | mainStatStatistic | additionalStat1BaseValue | additionalStat1Statistic | additionalStat2BaseValue | additionalStat2Statistic | additionalStat3BaseValue | additionalStat3Statistic |
      | 3668192b4-6057-47e5-bad0-fe795c8dbee6-3 | 668192b4-6057-47e5-bad0-fe795c8dbee6 | boots    | leg_boo_01  | LEGENDARY  | true       | 20    | 100               | attack_add        | 200                      | attack_mult              | 300                      | attack_mult              | 400                      | attack_mult              |

    Then the response status code should be 200

    And the response should have the following itemResponses
      | clientId                                | itemType | blueprintId | itemRarity | isEquipped | level | mainStatBaseValue | mainStatStatistic | additionalStat1BaseValue | additionalStat1Statistic | additionalStat2BaseValue | additionalStat2Statistic | additionalStat3BaseValue | additionalStat3Statistic |
      | 3668192b4-6057-47e5-bad0-fe795c8dbee6-3 | boots    | leg_boo_01  | LEGENDARY  | true       | 20    | 100               | attack_add        | 200                      | attack_mult              | 300                      | attack_mult              | 400                      | attack_mult              |


  Scenario: A user adds a new item in a non-existing inventory
    When the user logs in with the following credentials
      | username            | password |
      | paul.ochon@test.com | toto1234 |

    And the user requests the admin endpoint to create a new item in the inventory for game save with id ac142d95-682a-47b6-a81c-49ccdda6c41b with the following ItemRequest
      | clientId                                | itemType | blueprintId | itemRarity | isEquipped | level | mainStatBaseValue | mainStatStatistic | additionalStat1BaseValue | additionalStat1Statistic | additionalStat2BaseValue | additionalStat2Statistic | additionalStat3BaseValue | additionalStat3Statistic |
      | 3668192b4-6057-47e5-bad0-fe795c8dbee6-3 | boots    | leg_boo_01  | LEGENDARY  | true       | 20    | 100               | attack_add        | 200                      | attack_mult              | 300                      | attack_mult              | 400                      | attack_mult              |

    Then the response status code should be 404

  Scenario: A user clears an exisiting game inventory
    When the user logs in with the following credentials
      | username            | password |
      | paul.ochon@test.com | toto1234 |

    And the user requests the admin endpoint to clear the inventory for game save with id 0530e1fe-3428-4edd-bb32-cb563419d0bd

    Then the response status code should be 200

    And the inventory of the game save with id 0530e1fe-3428-4edd-bb32-cb563419d0bd should be empty

  Scenario: A user clears a non-exisiting game inventory
    When the user logs in with the following credentials
      | username            | password |
      | paul.ochon@test.com | toto1234 |

    And the user requests the admin endpoint to clear the inventory for game save with id 6efff741-125d-48c8-823d-33e3f5137ec0

    Then the response status code should be 404

  Scenario: A user updates an existing item in a game inventory
    When the user logs in with the following credentials
      | username            | password |
      | paul.ochon@test.com | toto1234 |

    And the user requests the admin endpoint to update an item with clientId 668192b4-6057-47e5-bad0-fe795c8dbee6-1 in the inventory for game save with id 0530e1fe-3428-4edd-bb32-cb563419d0bd with the following ItemRequest
      | clientId                               | id                                   | itemType | blueprintId | itemRarity | isEquipped | level | mainStatBaseValue | mainStatStatistic | additionalStat1BaseValue | additionalStat1Statistic | additionalStat2BaseValue | additionalStat2Statistic | additionalStat3BaseValue | additionalStat3Statistic |
      | 668192b4-6057-47e5-bad0-fe795c8dbee6-1 | fc8640e1-382e-4c80-a17c-5a6d196f3932 | boots    | leg_boo_01  | LEGENDARY  | true       | 50    | 200               | attack_add        | 400                      | attack_mult              | 600                      | attack_mult              | 800                      | attack_mult              |

    Then the response status code should be 200

    And the response should have the following itemResponses
      | clientId                               | id                                   | itemType | blueprintId | itemRarity | isEquipped | level | mainStatBaseValue | mainStatStatistic | additionalStat1BaseValue | additionalStat1Statistic | additionalStat2BaseValue | additionalStat2Statistic | additionalStat3BaseValue | additionalStat3Statistic |
      | 668192b4-6057-47e5-bad0-fe795c8dbee6-1 | fc8640e1-382e-4c80-a17c-5a6d196f3932 | boots    | leg_boo_01  | LEGENDARY  | true       | 50    | 200               | attack_add        | 400                      | attack_mult              | 600                      | attack_mult              | 800                      | attack_mult              |


  Scenario: A user updates a non-existing item in a game inventory
    When the user logs in with the following credentials
      | username            | password |
      | paul.ochon@test.com | toto1234 |

    And the user requests the admin endpoint to update an item with clientId 6265080a-af5b-4111-82bb-5f80b80e3dab in the inventory for game save with id 0530e1fe-3428-4edd-bb32-cb563419d0bd with the following ItemRequest
      | clientId                               | id                                   | itemType | blueprintId | itemRarity | isEquipped | level | mainStatBaseValue | mainStatStatistic | additionalStat1BaseValue | additionalStat1Statistic | additionalStat2BaseValue | additionalStat2Statistic | additionalStat3BaseValue | additionalStat3Statistic |
      | 668192b4-6057-47e5-bad0-fe795c8dbee6-1 | fc8640e1-382e-4c80-a17c-5a6d196f3932 | boots    | leg_boo_01  | LEGENDARY  | true       | 50    | 200               | attack_add        | 400                      | attack_mult              | 600                      | attack_mult              | 800                      | attack_mult              |

    Then the response status code should be 404

  Scenario: A user updates an existing item in a non-existing game inventory
    When the user logs in with the following credentials
      | username            | password |
      | paul.ochon@test.com | toto1234 |

    And the user requests the admin endpoint to update an item with clientId 668192b4-6057-47e5-bad0-fe795c8dbee6-1 in the inventory for game save with id 47e692aa-cad2-4aa3-ad35-ce6f3329bc92 with the following ItemRequest
      | clientId                               | id                                   | itemType | blueprintId | itemRarity | isEquipped | level | mainStatBaseValue | mainStatStatistic | additionalStat1BaseValue | additionalStat1Statistic | additionalStat2BaseValue | additionalStat2Statistic | additionalStat3BaseValue | additionalStat3Statistic |
      | 668192b4-6057-47e5-bad0-fe795c8dbee6-1 | fc8640e1-382e-4c80-a17c-5a6d196f3932 | boots    | leg_boo_01  | LEGENDARY  | true       | 50    | 200               | attack_add        | 400                      | attack_mult              | 600                      | attack_mult              | 800                      | attack_mult              |

    Then the response status code should be 404