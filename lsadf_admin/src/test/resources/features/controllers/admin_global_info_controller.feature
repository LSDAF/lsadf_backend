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

Feature: Admin Global Info Controller BDD tests

  Background:
    Given the BDD engine is ready
    And the cache is enabled
    And a clean database
    And the time clock set to the present

    # We assume we have the two following users in keycloak
    # paul.ochon@test.com: ADMIN,USER: toto1234
    # paul.itesse@test.com: USER: toto5678

  Scenario: Get the global info of the application
    Given the following game saves
      | id                                   | userEmail            | gold | diamond | emerald | amethyst | currentStage | maxStage | wave    | nickname | attack | critChance | critDamage | health | resistance |
      | 0530e1fe-3428-4edd-bb32-cb563419d0bd | paul.ochon@test.com  | 1000 | 1000    | 1000    | 1000     | 1000         | 1000     | 10      | test-3   | 1100   | 1200       | 1300       | 1400   | 1500       |
      | 3dd8b6d8-7aaa-4580-9a8a-a0bb6cc5bb21 | paul.itesse@test.com | 1000 | 1000    | 1000    | 1000     | 1000         | 1000     | 1000    | test-2   | 600    | 700        | 800        | 900    | 1000       |
      | 804af894-931b-4ee6-968f-1703689066fb | paul.ochon@test.com  | 1000 | 1000    | 1000    | 1000     | 1000         | 1000     | 100000  | test-1   | 100    | 200        | 300        | 400    | 500        |
      | 9929ee41-9a7b-4320-90d5-ee963888d876 | paul.itesse@test.com | 1000 | 1000    | 1000    | 1000     | 1000         | 1000     | 1000000 | test-4   | 600    | 700        | 800        | 900    | 1000       |
      | 6a4f12dc-4e83-40f7-992e-8f2e04375d74 | paul.ochon@test.com  | 1000 | 1000    | 1000    | 1000     | 1000         | 1000     | 1000000 | test-5   | 1100   | 1200       | 1300       | 1400   | 1500       |

    And the time clock set to the following value 2020-01-01T00:00:00Z

    When the user logs in with the following credentials
      | username            | password |
      | paul.ochon@test.com | toto1234 |

    And the user requests the admin endpoint to get the global info

    Then the response status code should be 200

    And the response should have the following GlobalInfo
      | userCounter | gameSaveCounter | now                  |
      | 2           | 5               | 2020-01-01T00:00:00Z |