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

Feature: Admin Cache Controller BDD tests

  Background:
    Given the BDD engine is ready
    And the cache is enabled
    And a clean database
    And the time clock set to the present

    # We assume we have the two following users in keycloak
    # paul.ochon@test.com: ADMIN,USER: toto1234
    # paul.itesse@test.com: USER: toto5678

  Scenario: Check if cache is enabled
    When the user logs in with the following credentials
      | username            | password |
      | paul.ochon@test.com | toto1234 |
    And the user requests the admin endpoint to get the cache status

    Then the response status code should be 200

    And the response should have the following Boolean true

  Scenario: Toggle cache status
    When the user logs in with the following credentials
      | username            | password |
      | paul.ochon@test.com | toto1234 |
    And the user requests the admin endpoint to toggle the cache status

    Then the response status code should be 200
    And the response should have the following Boolean false

  Scenario: Flush the cache
    Given the following game saves
      | id                                   | userEmail           | gold | diamond | emerald | amethyst | currentStage | maxStage | wave | attack | critChance | critDamage | health | resistance |
      | 0530e1fe-3428-4edd-bb32-cb563419d0bd | paul.ochon@test.com | 1000 | 1000    | 1000    | 1000     | 1000         | 1000     | 10   | 1100   | 1200       | 1300       | 1400   | 1500       |
    And the following characteristics entries in cache
      | gameSaveId                           | attack | critChance | critDamage | health | resistance |
      | 0530e1fe-3428-4edd-bb32-cb563419d0bd | 100    | 200        | 300        | 400    | 500        |
    And the following currency entries in cache
      | gameSaveId                           | gold     | diamond  | emerald  | amethyst |
      | 0530e1fe-3428-4edd-bb32-cb563419d0bd | 56302802 | 56302802 | 56302802 | 56302802 |
    And the following stage entries in cache
      | gameSaveId                           | currentStage | maxStage | wave |
      | 0530e1fe-3428-4edd-bb32-cb563419d0bd | 99           | 100      | 5    |

    When the user logs in with the following credentials
      | username            | password |
      | paul.ochon@test.com | toto1234 |
    And the user requests the admin endpoint to flush and clear the cache

    Then the response status code should be 200

    And the zset flush pending cache should be empty
    And the set flush processing cache should be empty