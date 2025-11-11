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


Feature: Game Mail Controller BDD tests

  Background:
    Given the BDD engine is ready
    And the cache is enabled
    And a clean database
    And the time clock set to the present
    And the following game saves
      | id                                   | userEmail           | gold | diamond | emerald | amethyst | currentStage | wave | maxStage | attack | critChance | critDamage | health | resistance |
      | 0530e1fe-3428-4edd-bb32-cb563419d0bd | paul.ochon@test.com | 1000 | 1000    | 1000    | 1000     | 1000         | 10   | 1000     | 1100   | 1200       | 1300       | 1400   | 1500       |
      | 9d68fe00-7449-4d90-9fa9-d3f77193be9f | paul.ochon@test.com | 1000 | 1000    | 1000    | 1000     | 1000         | 10   | 1000     | 1100   | 1200       | 1300       | 1400   | 1500       |

    And the following game email templates
      | id                                   | name             | subject               | body                                                               | expirationDays |
      | be7f65c8-c2eb-4dba-b351-746c293751d3 | Welcome Template | Welcome to LSDAF!     | Hello there, welcome to LSADF!                                     | 30             |
      | 38e9b063-724b-4794-ba7e-157d1e25c465 | LSADF V2.0       | Welcome to LSDAF 2.0! | Hello there, welcome to LSADF 2.0! Have fun with the new updates ! | 30             |

    And the following game email template attachments
      | id                                   | mailTemplateId                       | object                                                                                                                                                                                                                                 | type     |
      | 2099c6fc-9618-4b1d-a0a9-24d75d0d1f32 | be7f65c8-c2eb-4dba-b351-746c293751d3 | {"gold":500,"diamond":1000,"emerald":1500,"amethyst":2000}                                                                                                                                                                             | CURRENCY |
      | 36dca975-ff13-425f-82cb-9a95e424e102 | be7f65c8-c2eb-4dba-b351-746c293751d3 | {"id":"1de67a24-f15c-41aa-88fb-3208eb860abf","gameSaveId":"8443ecaf-c92e-49db-93c8-ce868ccc4e0c","blueprintId":"leg_boo_01","itemType":"BOOTS","itemRarity":"RARE","level":50,"mainStat":{"statistic":"ATTACK_ADD","baseValue":120.0}} | ITEM     |
      | 7f2b50f4-67c7-458a-93cb-9a47cd82c90c | 38e9b063-724b-4794-ba7e-157d1e25c465 | {"gold":5000}                                                                                                                                                                                                                          | CURRENCY |

  Scenario: An admin user sends a game mail to a specific game save
    When the user logs in with the following credentials
      | username            | password |
      | paul.ochon@test.com | toto1234 |
    And the user requests the admin endpoint to send game mail to game save "0530e1fe-3428-4edd-bb32-cb563419d0bd" with template "be7f65c8-c2eb-4dba-b351-746c293751d3"
    Then the response status code should be 200

  Scenario: An admin user sends a game mail to all game saves
    When the user logs in with the following credentials
      | username            | password |
      | paul.ochon@test.com | toto1234 |
    And the user requests the admin endpoint to send game mail to all game saves with template "be7f65c8-c2eb-4dba-b351-746c293751d3"
    Then the response status code should be 200

  Scenario: An admin user sends a game mail to a specific game save with a non-existing template
    When the user logs in with the following credentials
      | username            | password |
      | paul.ochon@test.com | toto1234 |
    And the user requests the admin endpoint to send game mail to game save "0530e1fe-3428-4edd-bb32-cb563419d0bd" with template "00000000-0000-0000-0000-000000000000"
    Then the response status code should be 404

  Scenario: An admin user sends a game mail to all game saves with a non-existing template
    When the user logs in with the following credentials
      | username            | password |
      | paul.ochon@test.com | toto1234 |
    And the user requests the admin endpoint to send game mail to all game saves with template "00000000-0000-0000-0000-000000000000"
    Then the response status code should be 404



