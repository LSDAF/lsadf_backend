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

Feature: Characteristics Controller BDD tests

  Background:
    Given the BDD engine is ready
    And the cache is enabled
    And a clean database
    And the time clock set to the present

    # We assume we have the two following users in keycloak
    # paul.ochon@test.com: ADMIN,USER: toto1234
    # paul.itesse@test.com: USER: toto5678

  Scenario: A user gets the characteristics of one of his game saves with cache
    Given the following game saves
      | id                                   | userEmail           | gold | diamond | emerald | amethyst | maxStage | currentStage | attack | critChance | critDamage | health | resistance |
      | f81b710d-3e02-4871-a86f-390377798dd1 | paul.ochon@test.com | 100  | 100     | 100     | 100      | 10       | 10           | 1100   | 1200       | 1300       | 1400   | 1500       |
    And the following characteristics entries in cache
      | gameSaveId                           | attack | critChance | critDamage | health | resistance |
      | f81b710d-3e02-4871-a86f-390377798dd1 | 1111   | 2222       | 3333       | 4444   | 5555       |

    When the user logs in with the following credentials
      | username            | password |
      | paul.ochon@test.com | toto1234 |

    And the user requests the endpoint to get the characteristics of the game save with id f81b710d-3e02-4871-a86f-390377798dd1

    Then the response status code should be 200

    And the response should have the following Characteristics
      | attack | critChance | critDamage | health | resistance |
      | 1111   | 2222       | 3333       | 4444   | 5555       |

  Scenario: A user gets the characteristics of one of his game saves without cache
    Given the following game saves
      | id                                   | userEmail           | gold | diamond | emerald | amethyst | maxStage | currentStage | attack | critChance | critDamage | health | resistance |
      | f81b710d-3e02-4871-a86f-390377798dd1 | paul.ochon@test.com | 100  | 200     | 300     | 400      | 10       | 10           | 1100   | 1200       | 1300       | 1400   | 1500       |

    When the user logs in with the following credentials
      | username            | password |
      | paul.ochon@test.com | toto1234 |

    And the user requests the endpoint to get the characteristics of the game save with id f81b710d-3e02-4871-a86f-390377798dd1

    Then the response status code should be 200

    And the response should have the following Characteristics
      | attack | critChance | critDamage | health | resistance |
      | 1100   | 1200       | 1300       | 1400   | 1500       |

  Scenario: A user gets the characteristics of a game save that does not exist
    Given the following game saves
      | id                                   | userEmail           | gold    | maxStage | currentStage | diamond | emerald | amethyst | attack | critChance | critDamage | health | resistance |
      | f81b710d-3e02-4871-a86f-390377798dd1 | paul.ochon@test.com | 5630280 | 10       | 10           | 10      | 10      | 10       | 1100   | 1200       | 1300       | 1400   | 1500       |

    When the user logs in with the following credentials
      | username            | password |
      | paul.ochon@test.com | toto1234 |

    And the user requests the endpoint to get the characteristics of the game save with id 9d96507e-56e8-447d-aa0f-b2248ae59454

    Then the response status code should be 404

  Scenario: A user gets the characteristics of a non-owned game save
    Given the following game saves
      | id                                   | userEmail            | gold    | maxStage | currentStage | diamond | emerald | amethyst | attack | critChance | critDamage | health | resistance |
      | f81b710d-3e02-4871-a86f-390377798dd1 | paul.itesse@test.com | 5630280 | 10       | 10           | 10      | 10      | 10       | 1100   | 1200       | 1300       | 1400   | 1500       |

    When the user logs in with the following credentials
      | username            | password |
      | paul.ochon@test.com | toto1234 |

    And the user requests the endpoint to get the characteristics of the game save with id f81b710d-3e02-4871-a86f-390377798dd1

    Then the response status code should be 403

  Scenario: A user sets the characteristics of one of his game saves with cache
    Given the following game saves
      | id                                   | userEmail           | gold    | diamond | emerald | amethyst | maxStage | currentStage | attack | critChance | critDamage | health | resistance |
      | f81b710d-3e02-4871-a86f-390377798dd1 | paul.ochon@test.com | 5630280 | 5630280 | 5630280 | 5630280  | 10       | 10           | 1100   | 1200       | 1300       | 1400   | 1500       |
    And the following characteristics entries in cache
      | gameSaveId                           | gold |
      | f81b710d-3e02-4871-a86f-390377798dd1 | 666  |

    And the following game sessions
      | id                                   | gameSaveId                           | cancelled | version |
      | 6025d3de-49ee-4ca1-98c0-28cb49f85e87 | f81b710d-3e02-4871-a86f-390377798dd1 | false     | 1       |

    When the user logs in with the following credentials
      | username            | password |
      | paul.ochon@test.com | toto1234 |

    And the user requests the endpoint to set the characteristics with the following CharacteristicsRequest for the game save with id f81b710d-3e02-4871-a86f-390377798dd1 and session id 6025d3de-49ee-4ca1-98c0-28cb49f85e87
      | attack | critChance | critDamage | health | resistance |
      | 1000   | 1200       | 1300       | 1400   | 1500       |

    Then the response status code should be 200

    And the following characteristics entries in cache
      | gameSaveId                           | attack | critChance | critDamage | health | resistance |
      | f81b710d-3e02-4871-a86f-390377798dd1 | 1000   | 1200       | 1300       | 1400   | 1500       |


  Scenario: A user sets the characteristics of one of his game saves without cache
    Given the following game saves
      | id                                   | userEmail           | gold    | maxStage | currentStage | diamond | emerald | amethyst | attack | critChance | critDamage | health | resistance |
      | f81b710d-3e02-4871-a86f-390377798dd1 | paul.ochon@test.com | 5630280 | 10       | 10           | 10      | 10      | 10       | 1100   | 1200       | 1300       | 1400   | 1500       |

    And the following game sessions
      | id                                   | gameSaveId                           | cancelled | version |
      | 6025d3de-49ee-4ca1-98c0-28cb49f85e87 | f81b710d-3e02-4871-a86f-390377798dd1 | false     | 1       |

    And the cache is disabled

    When the user logs in with the following credentials
      | username            | password |
      | paul.ochon@test.com | toto1234 |

    And the user requests the endpoint to set the characteristics with the following CharacteristicsRequest for the game save with id f81b710d-3e02-4871-a86f-390377798dd1 and session id 6025d3de-49ee-4ca1-98c0-28cb49f85e87
      | gameSaveId                           | attack | critChance | critDamage | health | resistance |
      | f81b710d-3e02-4871-a86f-390377798dd1 | 100    | 200        | 300        | 400    | 500        |

    Then the response status code should be 200

  Scenario: A user sets the characteristics of a non-owned game save
    Given the following game saves
      | id                                   | userEmail            | gold    | maxStage | currentStage | diamond | emerald | amethyst | attack | critChance | critDamage | health | resistance |
      | f81b710d-3e02-4871-a86f-390377798dd1 | paul.itesse@test.com | 5630280 | 10       | 10           | 10      | 10      | 10       | 1100   | 1200       | 1300       | 1400   | 1500       |

    And the following game sessions
      | id                                   | gameSaveId                           | cancelled | version |
      | 6025d3de-49ee-4ca1-98c0-28cb49f85e87 | f81b710d-3e02-4871-a86f-390377798dd1 | false     | 1       |

    When the user logs in with the following credentials
      | username            | password |
      | paul.ochon@test.com | toto1234 |

    And the user requests the endpoint to set the characteristics with the following CharacteristicsRequest for the game save with id f81b710d-3e02-4871-a86f-390377798dd1 and session id 6025d3de-49ee-4ca1-98c0-28cb49f85e87
      | gameSaveId                           | attack | critChance | critDamage | health | resistance |
      | f81b710d-3e02-4871-a86f-390377798dd1 | 1      | 2          | 3          | 4      | 5          |

    Then the response status code should be 403

  Scenario: A user sets the characteristics of a non-existing game save
    Given the following game saves
      | id                                   | userEmail           | gold    | maxStage | currentStage | diamond | emerald | amethyst | attack | critChance | critDamage | health | resistance |
      | f81b710d-3e02-4871-a86f-390377798dd1 | paul.ochon@test.com | 5630280 | 10       | 10           | 10      | 10      | 10       | 1100   | 1200       | 1300       | 1400   | 1500       |

    And the following game sessions
      | id                                   | gameSaveId                           | cancelled | version |
      | 6025d3de-49ee-4ca1-98c0-28cb49f85e87 | f81b710d-3e02-4871-a86f-390377798dd1 | false     | 1       |

    When the user logs in with the following credentials
      | username            | password |
      | paul.ochon@test.com | toto1234 |

    And the user requests the endpoint to set the characteristics with the following CharacteristicsRequest for the game save with id 7545eed0-237c-4182-849f-f9d4e1d112b5 and session id 6025d3de-49ee-4ca1-98c0-28cb49f85e87
      | gameSaveId                           | attack | critChance | critDamage | health | resistance |
      | 7545eed0-237c-4182-849f-f9d4e1d112b5 | 1      | 2          | 3          | 4      | 5          |

    Then the response status code should be 404
