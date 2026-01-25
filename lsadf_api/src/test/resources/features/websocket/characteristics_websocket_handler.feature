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

Feature: Characteristics WebSocket Handler BDD tests

  Background:
    Given the BDD engine is ready
    And the cache is enabled
    And a clean database
    And the time clock set to the present

    # We assume we have the two following users in keycloak
    # paul.ochon@test.com: ADMIN,USER: toto1234
    # paul.itesse@test.com: USER: toto5678

  Scenario: A user updates characteristics through WebSocket with cache
    Given the following game saves
      | id                                   | userEmail           | gold    | diamond | emerald | amethyst | maxStage | currentStage | wave | attack | critChance | critDamage | health | resistance |
      | f81b710d-3e02-4871-a86f-390377798dd1 | paul.ochon@test.com | 5630280 | 5630280 | 5630280 | 5630280  | 10       | 10           | 2    | 1100   | 1200       | 1300       | 1400   | 1500       |
    And the following characteristics entries in cache
      | gameSaveId                           | attack | critChance | critDamage | health | resistance |
      | f81b710d-3e02-4871-a86f-390377798dd1 | 100    | 200        | 300        | 400    | 500        |
    And the following game sessions
      | id                                   | gameSaveId                           | cancelled | version |
      | 6025d3de-49ee-4ca1-98c0-28cb49f85e87 | f81b710d-3e02-4871-a86f-390377798dd1 | false     | 1       |

    When the user logs in with the following credentials
      | username            | password |
      | paul.ochon@test.com | toto1234 |

    And the user connects to the WebSocket endpoint with session id 6025d3de-49ee-4ca1-98c0-28cb49f85e87
    And the user sends a characteristics update event through WebSocket
      | attack | critChance | critDamage | health | resistance |
      | 2000   | 3000       | 4000       | 5000   | 6000       |

    Then the WebSocket connection should receive an ACK message
    And the following characteristics entries in cache
      | gameSaveId                           | attack | critChance | critDamage | health | resistance |
      | f81b710d-3e02-4871-a86f-390377798dd1 | 2000   | 3000       | 4000       | 5000   | 6000       |

  Scenario: A user updates characteristics through WebSocket without cache
    Given the following game saves
      | id                                   | userEmail           | gold    | diamond | emerald | amethyst | maxStage | currentStage | wave | attack | critChance | critDamage | health | resistance |
      | f81b710d-3e02-4871-a86f-390377798dd1 | paul.ochon@test.com | 5630280 | 5630280 | 5630280 | 5630280  | 10       | 10           | 2    | 1100   | 1200       | 1300       | 1400   | 1500       |
    And the following game sessions
      | id                                   | gameSaveId                           | cancelled | version |
      | 6025d3de-49ee-4ca1-98c0-28cb49f85e87 | f81b710d-3e02-4871-a86f-390377798dd1 | false     | 1       |

    And the cache is disabled

    When the user logs in with the following credentials
      | username            | password |
      | paul.ochon@test.com | toto1234 |

    And the user connects to the WebSocket endpoint with session id 6025d3de-49ee-4ca1-98c0-28cb49f85e87
    And the user sends a characteristics update event through WebSocket
      | attack | critChance | critDamage | health | resistance |
      | 2500   | 3500       | 4500       | 5500   | 6500       |

    Then the WebSocket connection should receive an ACK message

  Scenario: A user tries to update characteristics with invalid session id
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

  Scenario: A user updates characteristics multiple times through WebSocket
    Given the following game saves
      | id                                   | userEmail           | gold | diamond | emerald | amethyst | maxStage | currentStage | wave | attack | critChance | critDamage | health | resistance |
      | f81b710d-3e02-4871-a86f-390377798dd1 | paul.ochon@test.com | 1000 | 2000    | 3000    | 4000     | 10       | 10           | 2    | 100    | 200        | 300        | 400    | 500        |
    And the following characteristics entries in cache
      | gameSaveId                           | attack | critChance | critDamage | health | resistance |
      | f81b710d-3e02-4871-a86f-390377798dd1 | 100    | 200        | 300        | 400    | 500        |
    And the following game sessions
      | id                                   | gameSaveId                           | cancelled | version |
      | 6025d3de-49ee-4ca1-98c0-28cb49f85e87 | f81b710d-3e02-4871-a86f-390377798dd1 | false     | 1       |

    When the user logs in with the following credentials
      | username            | password |
      | paul.ochon@test.com | toto1234 |

    And the user connects to the WebSocket endpoint with session id 6025d3de-49ee-4ca1-98c0-28cb49f85e87
    And the user sends a characteristics update event through WebSocket
      | attack | critChance | critDamage | health | resistance |
      | 1000   | 2000       | 3000       | 4000   | 5000       |
    And the user sends a characteristics update event through WebSocket
      | attack | critChance | critDamage | health | resistance |
      | 1500   | 2500       | 3500       | 4500   | 5500       |
    And the user sends a characteristics update event through WebSocket
      | attack | critChance | critDamage | health | resistance |
      | 2000   | 3000       | 4000       | 5000   | 6000       |

    Then the WebSocket connection should have received 3 ACK messages

  Scenario: A user updates characteristics with invalid data through WebSocket
    Given the following game saves
      | id                                   | userEmail           | gold    | diamond | emerald | amethyst | maxStage | currentStage | wave | attack | critChance | critDamage | health | resistance |
      | f81b710d-3e02-4871-a86f-390377798dd1 | paul.ochon@test.com | 5630280 | 5630280 | 5630280 | 5630280  | 10       | 10           | 2    | 1100   | 1200       | 1300       | 1400   | 1500       |
    And the following characteristics entries in cache
      | gameSaveId                           | attack | critChance | critDamage | health | resistance |
      | f81b710d-3e02-4871-a86f-390377798dd1 | 100    | 200        | 300        | 400    | 500        |
    And the following game sessions
      | id                                   | gameSaveId                           | cancelled | version |
      | 6025d3de-49ee-4ca1-98c0-28cb49f85e87 | f81b710d-3e02-4871-a86f-390377798dd1 | false     | 1       |

    When the user logs in with the following credentials
      | username            | password |
      | paul.ochon@test.com | toto1234 |

    And the user connects to the WebSocket endpoint with session id 6025d3de-49ee-4ca1-98c0-28cb49f85e87
    And the user sends a characteristics update event through WebSocket
      | attack | critChance | critDamage | health | resistance |
      | -1000  | 2000       | 3000       | 4000   | 5000       |

    Then the WebSocket connection should receive an ERROR message
