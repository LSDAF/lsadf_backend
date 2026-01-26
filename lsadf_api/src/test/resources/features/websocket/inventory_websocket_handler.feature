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

Feature: Inventory WebSocket Handler BDD tests

  Background:
    Given the BDD engine is ready
    And a clean database
    And the time clock set to the present

    # We assume we have the two following users in keycloak
    # paul.ochon@test.com: ADMIN,USER: toto1234
    # paul.itesse@test.com: USER: toto5678

  Scenario: A user creates an inventory item through WebSocket
    Given the following game saves
      | id                                   | userEmail           | gold    | diamond | emerald | amethyst | maxStage | currentStage | wave | attack | critChance | critDamage | health | resistance |
      | f81b710d-3e02-4871-a86f-390377798dd1 | paul.ochon@test.com | 5630280 | 5630280 | 5630280 | 5630280  | 10       | 10           | 2    | 1100   | 1200       | 1300       | 1400   | 1500       |
    And the following game sessions
      | id                                   | gameSaveId                           | cancelled | version |
      | 6025d3de-49ee-4ca1-98c0-28cb49f85e87 | f81b710d-3e02-4871-a86f-390377798dd1 | false     | 1       |

    When the user logs in with the following credentials
      | username            | password |
      | paul.ochon@test.com | toto1234 |

    And the user connects to the WebSocket endpoint with session id 6025d3de-49ee-4ca1-98c0-28cb49f85e87
    And the user sends an inventory item create event through WebSocket
      | clientId | blueprintId | itemType | itemRarity | isEquipped | level | mainStatStatistic | mainStatBaseValue | additionalStat1Statistic | additionalStat1BaseValue | additionalStat2Statistic | additionalStat2BaseValue |
      | item-001 | sword-bp-1  | SWORD    | NORMAL     | false      | 1     | ATTACK_ADD        | 100               | CRIT_CHANCE              | 10                       | CRIT_DAMAGE              | 20                       |

    Then the WebSocket connection should receive an ACK message
    And the inventory item with clientId item-001 should exist in database for game save f81b710d-3e02-4871-a86f-390377798dd1

  Scenario: A user updates an inventory item through WebSocket
    Given the following game saves
      | id                                   | userEmail           | gold    | diamond | emerald | amethyst | maxStage | currentStage | wave | attack | critChance | critDamage | health | resistance |
      | f81b710d-3e02-4871-a86f-390377798dd1 | paul.ochon@test.com | 5630280 | 5630280 | 5630280 | 5630280  | 10       | 10           | 2    | 1100   | 1200       | 1300       | 1400   | 1500       |
    And the following items to the inventory of the game save with id f81b710d-3e02-4871-a86f-390377798dd1
      | id                                   | clientId | blueprintId | itemType | itemRarity | isEquipped | level | mainStatStatistic | mainStatBaseValue | additionalStat1Statistic | additionalStat1BaseValue |
      | cb1d555f-3970-4886-b1a5-82106c2ae676 | item-002 | armor-bp-1  | BOOTS    | NORMAL     | true       | 5     | ATTACK_MULT       | 500               | RESISTANCE_ADD           | 50                       |
    And the following game sessions
      | id                                   | gameSaveId                           | cancelled | version |
      | 6025d3de-49ee-4ca1-98c0-28cb49f85e87 | f81b710d-3e02-4871-a86f-390377798dd1 | false     | 1       |

    When the user logs in with the following credentials
      | username            | password |
      | paul.ochon@test.com | toto1234 |

    And the user connects to the WebSocket endpoint with session id 6025d3de-49ee-4ca1-98c0-28cb49f85e87
    And the user sends an inventory item update event through WebSocket
      | clientId | blueprintId | itemType | itemRarity | isEquipped | level | mainStatStatistic | mainStatBaseValue | additionalStat1Statistic | additionalStat1BaseValue |
      | item-002 | shield-bp-2 | SWORD    | EPIC       | false      | 10    | ATTACK_MULT       | 1000              | RESISTANCE_ADD           | 100                      |

    Then the WebSocket connection should receive an ACK message
    And the inventory item with clientId item-002 should have level 10 for game save f81b710d-3e02-4871-a86f-390377798dd1

  Scenario: A user deletes an inventory item through WebSocket
    Given the following game saves
      | id                                   | userEmail           | gold    | diamond | emerald | amethyst | maxStage | currentStage | wave | attack | critChance | critDamage | health | resistance |
      | f81b710d-3e02-4871-a86f-390377798dd1 | paul.ochon@test.com | 5630280 | 5630280 | 5630280 | 5630280  | 10       | 10           | 2    | 1100   | 1200       | 1300       | 1400   | 1500       |
    And the following items to the inventory of the game save with id f81b710d-3e02-4871-a86f-390377798dd1
      | id                                   | clientId | blueprintId | itemType | itemRarity | isEquipped | level | mainStatStatistic | mainStatBaseValue |
      | cb1d555f-3970-4886-b1a5-82106c2ae676 | item-003 | helm-bp-1   | HELMET   | EPIC       | true       | 3     | HEALTH_ADD        | 300               |
    And the following game sessions
      | id                                   | gameSaveId                           | cancelled | version |
      | 6025d3de-49ee-4ca1-98c0-28cb49f85e87 | f81b710d-3e02-4871-a86f-390377798dd1 | false     | 1       |

    When the user logs in with the following credentials
      | username            | password |
      | paul.ochon@test.com | toto1234 |

    And the user connects to the WebSocket endpoint with session id 6025d3de-49ee-4ca1-98c0-28cb49f85e87
    And the user sends an inventory item delete event through WebSocket
      | clientId |
      | item-003 |

    Then the WebSocket connection should receive an ACK message
    And the inventory item with clientId item-003 should not exist in database for game save f81b710d-3e02-4871-a86f-390377798dd1

  Scenario: A user tries to create an inventory item with invalid session id
    Given the following game saves
      | id                                   | userEmail           | gold    | diamond | emerald | amethyst | maxStage | currentStage | wave | attack | critChance | critDamage | health | resistance |
      | f81b710d-3e02-4871-a86f-390377798dd1 | paul.ochon@test.com | 5630280 | 5630280 | 5630280 | 5630280  | 10       | 10           | 2    | 1100   | 1200       | 1300       | 1400   | 1500       |
    And the following game sessions
      | id                                   | gameSaveId                           | cancelled | version |
      | 6025d3de-49ee-4ca1-98c0-28cb49f85e87 | f81b710d-3e02-4871-a86f-390377798dd1 | false     | 1       |

    When the user logs in with the following credentials
      | username            | password |
      | paul.ochon@test.com | toto1234 |

    And the user connects to the WebSocket endpoint with session id 42f94d0a-c28b-4363-8cb6-de11aa8836cf

    Then a ExecutionException should be thrown

  Scenario: A user creates multiple inventory items through WebSocket
    Given the following game saves
      | id                                   | userEmail           | gold | diamond | emerald | amethyst | maxStage | currentStage | wave | attack | critChance | critDamage | health | resistance |
      | f81b710d-3e02-4871-a86f-390377798dd1 | paul.ochon@test.com | 1000 | 2000    | 3000    | 4000     | 10       | 10           | 2    | 1100   | 1200       | 1300       | 1400   | 1500       |
    And the following game sessions
      | id                                   | gameSaveId                           | cancelled | version |
      | 6025d3de-49ee-4ca1-98c0-28cb49f85e87 | f81b710d-3e02-4871-a86f-390377798dd1 | false     | 1       |

    When the user logs in with the following credentials
      | username            | password |
      | paul.ochon@test.com | toto1234 |

    And the user connects to the WebSocket endpoint with session id 6025d3de-49ee-4ca1-98c0-28cb49f85e87
    And the user sends an inventory item create event through WebSocket
      | clientId | blueprintId | itemType | itemRarity | isEquipped | level | mainStatStatistic | mainStatBaseValue |
      | item-101 | sword-bp-1  | SWORD    | NORMAL     | false      | 1     | ATTACK_ADD        | 50                |
    And the user sends an inventory item create event through WebSocket
      | clientId | blueprintId | itemType | itemRarity | isEquipped | level | mainStatStatistic | mainStatBaseValue |
      | item-102 | shield-bp-1 | SHIELD   | RARE       | true       | 2     | HEALTH_ADD        | 200               |
    And the user sends an inventory item create event through WebSocket
      | clientId | blueprintId | itemType | itemRarity | isEquipped | level | mainStatStatistic | mainStatBaseValue |
      | item-103 | helm-bp-1   | HELMET   | RARE       | false      | 3     | HEALTH_ADD        | 300               |

    Then the WebSocket connection should have received 3 ACK messages
    And the inventory item with clientId item-101 should exist in database for game save f81b710d-3e02-4871-a86f-390377798dd1
    And the inventory item with clientId item-102 should exist in database for game save f81b710d-3e02-4871-a86f-390377798dd1
    And the inventory item with clientId item-103 should exist in database for game save f81b710d-3e02-4871-a86f-390377798dd1

  Scenario: A user tries to create an inventory item with missing required fields
    Given the following game saves
      | id                                   | userEmail           | gold    | diamond | emerald | amethyst | maxStage | currentStage | wave | attack | critChance | critDamage | health | resistance |
      | f81b710d-3e02-4871-a86f-390377798dd1 | paul.ochon@test.com | 5630280 | 5630280 | 5630280 | 5630280  | 10       | 10           | 2    | 1100   | 1200       | 1300       | 1400   | 1500       |
    And the following game sessions
      | id                                   | gameSaveId                           | cancelled | version |
      | 6025d3de-49ee-4ca1-98c0-28cb49f85e87 | f81b710d-3e02-4871-a86f-390377798dd1 | false     | 1       |

    When the user logs in with the following credentials
      | username            | password |
      | paul.ochon@test.com | toto1234 |

    And the user connects to the WebSocket endpoint with session id 6025d3de-49ee-4ca1-98c0-28cb49f85e87
    And the user sends an inventory item create event through WebSocket with missing fields
      | blueprintId | type   | rarity |
      | sword-bp-1  | WEAPON | COMMON |

    Then the WebSocket connection should receive an ERROR message

  Scenario: A user tries to update a non-existent inventory item through WebSocket
    Given the following game saves
      | id                                   | userEmail           | gold    | diamond | emerald | amethyst | maxStage | currentStage | wave | attack | critChance | critDamage | health | resistance |
      | f81b710d-3e02-4871-a86f-390377798dd1 | paul.ochon@test.com | 5630280 | 5630280 | 5630280 | 5630280  | 10       | 10           | 2    | 1100   | 1200       | 1300       | 1400   | 1500       |
    And the following game sessions
      | id                                   | gameSaveId                           | cancelled | version |
      | 6025d3de-49ee-4ca1-98c0-28cb49f85e87 | f81b710d-3e02-4871-a86f-390377798dd1 | false     | 1       |

    When the user logs in with the following credentials
      | username            | password |
      | paul.ochon@test.com | toto1234 |

    And the user connects to the WebSocket endpoint with session id 6025d3de-49ee-4ca1-98c0-28cb49f85e87
    And the user sends an inventory item update event through WebSocket
      | clientId        | blueprintId | itemType | itemRarity | isEquipped | level | mainStatStatistic | mainStatBaseValue |
      | non-existent-id | sword-bp-1  | SWORD    | NORMAL     | false      | 1     | ATTACK_ADD        | 100               |

    Then the WebSocket connection should receive an ERROR message

  Scenario: A user tries to delete a non-existent inventory item through WebSocket
    Given the following game saves
      | id                                   | userEmail           | gold    | diamond | emerald | amethyst | maxStage | currentStage | wave | attack | critChance | critDamage | health | resistance |
      | f81b710d-3e02-4871-a86f-390377798dd1 | paul.ochon@test.com | 5630280 | 5630280 | 5630280 | 5630280  | 10       | 10           | 2    | 1100   | 1200       | 1300       | 1400   | 1500       |
    And the following game sessions
      | id                                   | gameSaveId                           | cancelled | version |
      | 6025d3de-49ee-4ca1-98c0-28cb49f85e87 | f81b710d-3e02-4871-a86f-390377798dd1 | false     | 1       |

    When the user logs in with the following credentials
      | username            | password |
      | paul.ochon@test.com | toto1234 |

    And the user connects to the WebSocket endpoint with session id 6025d3de-49ee-4ca1-98c0-28cb49f85e87
    And the user sends an inventory item delete event through WebSocket
      | clientId        |
      | non-existent-id |

    Then the WebSocket connection should receive an ERROR message
