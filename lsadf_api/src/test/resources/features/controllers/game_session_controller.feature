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


Feature: Game Session Controller BDD tests

  Background:
    Given the BDD engine is ready
    And the cache is enabled
    And a clean database
    And the time clock set to the present
    And the following game saves
      | id                                   | userEmail            | gold | diamond | emerald | amethyst | currentStage | maxStage | attack | critChance | critDamage | health | resistance |
      | 0530e1fe-3428-4edd-bb32-cb563419d0bd | paul.ochon@test.com  | 1000 | 1000    | 1000    | 1000     | 1000         | 1000     | 1100   | 1200       | 1300       | 1400   | 1500       |
      | cbe75715-80ea-4296-b045-27c3e78d95bc | paul.itesse@test.com | 1000 | 1000    | 1000    | 1000     | 1000         | 1000     | 1100   | 1200       | 1300       | 1400   | 1500       |

  Scenario: A user generates a new game session for his game save
    When the user logs in with the following credentials
      | username            | password |
      | paul.ochon@test.com | toto1234 |

    And the user requests the endpoint to generate a new session for the game save with id 0530e1fe-3428-4edd-bb32-cb563419d0bd

    Then the response status code should be 200

  Scenario: A user generates a new game session for an invalid game save
    When the user logs in with the following credentials
      | username            | password |
      | paul.ochon@test.com | toto1234 |

    And the user requests the endpoint to generate a new session for the game save with id 5aa93649-0124-4a17-9633-de21d107e56f

    Then the response status code should be 404

  Scenario: A user generates a new game session for a non-owned game save
    When the user logs in with the following credentials
      | username            | password |
      | paul.ochon@test.com | toto1234 |

    And the user requests the endpoint to generate a new session for the game save with id cbe75715-80ea-4296-b045-27c3e78d95bc

    Then the response status code should be 403

  Scenario: A user refreshes his gameSession for his game save
    Given the following game sessions
      | id                                   | gameSaveId                           | cancelled | version |
      | 6025d3de-49ee-4ca1-98c0-28cb49f85e87 | 0530e1fe-3428-4edd-bb32-cb563419d0bd | false     | 1       |
    When the user logs in with the following credentials
      | username            | password |
      | paul.ochon@test.com | toto1234 |

    And the user requests the endpoint to refresh his current game session with id 6025d3de-49ee-4ca1-98c0-28cb49f85e87

    Then the response status code should be 200

  Scenario: A user refreshes his gameSession for a non-existing game save
    When the user logs in with the following credentials
      | username            | password |
      | paul.ochon@test.com | toto1234 |

    And the user requests the endpoint to refresh his current game session with id 5aa93649-0124-4a17-9633-de21d107e56f

    Then the response status code should be 404

  Scenario: A user refreshes his gameSession for a non-owned game save
    Given the following game sessions
      | id                                   | gameSaveId                           | cancelled | version |
      | fcb07fb7-3e67-4b0f-b014-d8c285fa0879 | cbe75715-80ea-4296-b045-27c3e78d95bc | false     | 1       |
    When the user logs in with the following credentials
      | username            | password |
      | paul.ochon@test.com | toto1234 |

    And the user requests the endpoint to refresh his current game session with id fcb07fb7-3e67-4b0f-b014-d8c285fa0879

    Then the response status code should be 403

  Scenario: A user refreshes an already cancelled game session
    Given the following game sessions
      | id                                   | gameSaveId                           | cancelled | version |
      | 6025d3de-49ee-4ca1-98c0-28cb49f85e87 | 0530e1fe-3428-4edd-bb32-cb563419d0bd | true      | 1       |
    When the user logs in with the following credentials
      | username            | password |
      | paul.ochon@test.com | toto1234 |

    And the user requests the endpoint to refresh his current game session with id 6025d3de-49ee-4ca1-98c0-28cb49f85e87

    Then the response status code should be 403

  Scenario: A user refreshed an expired game session
    Given the following game sessions
      | id                                   | gameSaveId                           | cancelled | version | endTime                 |
      | 6025d3de-49ee-4ca1-98c0-28cb49f85e87 | 0530e1fe-3428-4edd-bb32-cb563419d0bd | false     | 1       | 2020-01-01T00:00:00.00Z |
    When the user logs in with the following credentials
      | username            | password |
      | paul.ochon@test.com | toto1234 |

    And the user requests the endpoint to refresh his current game session with id 6025d3de-49ee-4ca1-98c0-28cb49f85e87

    Then the response status code should be 403