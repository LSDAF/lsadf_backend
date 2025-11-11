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


Feature: Game Mail Template Controller BDD tests

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
      | id                                   | mailTemplateId                       | object                                                                                                                                                                                       | type     |
      | 2099c6fc-9618-4b1d-a0a9-24d75d0d1f32 | be7f65c8-c2eb-4dba-b351-746c293751d3 | {"gold":500,"diamond":1000,"emerald":1500,"amethyst":2000}                                                                                                                                   | CURRENCY |
      | 36dca975-ff13-425f-82cb-9a95e424e102 | be7f65c8-c2eb-4dba-b351-746c293751d3 | {"client_id":null,"blueprint_id":"leg_boo_01","type":"BOOTS","rarity":"RARE","is_equipped":false,"level":50,"main_stat":{"statistic":"ATTACK_ADD","base_value":120.0},"additional_stats":[]} | ITEM     |
      | 7f2b50f4-67c7-458a-93cb-9a47cd82c90c | 38e9b063-724b-4794-ba7e-157d1e25c465 | {"gold":500,"diamond":0,"emerald":0,"amethyst":0}                                                                                                                                            | CURRENCY |

    And the following game emails
      | id                                   | gameSaveId                           | mailTemplateId                       | isRead | isAttachmentClaimed |
      | adbaf507-3987-45e1-bea6-e51525943148 | 0530e1fe-3428-4edd-bb32-cb563419d0bd | be7f65c8-c2eb-4dba-b351-746c293751d3 | false  | false               |
      | 3a766b66-f7fc-417f-9e17-0ff400b74414 | 0530e1fe-3428-4edd-bb32-cb563419d0bd | 38e9b063-724b-4794-ba7e-157d1e25c465 | true   | true                |

  Scenario: An admin user gets all the templates available
    When the user logs in with the following credentials
      | username            | password |
      | paul.ochon@test.com | toto1234 |

    And the user requests the admin endpoint to get all the game mail templates

    Then the response status code should be 200

    And the response should have the following GameMailTemplateResponses
      | id                                   | name             | subject               | body                                                               | expirationDays |
      | be7f65c8-c2eb-4dba-b351-746c293751d3 | Welcome Template | Welcome to LSDAF!     | Hello there, welcome to LSADF!                                     | 30             |
      | 38e9b063-724b-4794-ba7e-157d1e25c465 | LSADF V2.0       | Welcome to LSDAF 2.0! | Hello there, welcome to LSADF 2.0! Have fun with the new updates ! | 30             |

  Scenario: An admin user gets a template with its attachments
    When the user logs in with the following credentials
      | username            | password |
      | paul.ochon@test.com | toto1234 |

    And the user requests the admin endpoint to get the game mail template with id be7f65c8-c2eb-4dba-b351-746c293751d3

    Then the response status code should be 200

    And the response should have the following GameMailTemplateResponse
      | id                                   | name             | subject           | body                           | expirationDays |
      | be7f65c8-c2eb-4dba-b351-746c293751d3 | Welcome Template | Welcome to LSDAF! | Hello there, welcome to LSADF! | 30             |

    And the response should have the following GameMailAttachments
      | attachment                                                                                                                                                                                   | type     |
      | {"amethyst":2000, "diamond":1000, "emerald":1500, "gold":500}                                                                                                                                | CURRENCY |
      | {"client_id":null,"blueprint_id":"leg_boo_01","type":"BOOTS","rarity":"RARE","is_equipped":false,"level":50,"main_stat":{"statistic":"ATTACK_ADD","base_value":120.0},"additional_stats":[]} | ITEM     |


  Scenario: An admin user creates a new game mail template
    When the user logs in with the following credentials
      | username            | password |
      | paul.ochon@test.com | toto1234 |

    And the user requests the admin endpoint to create a new game mail template with the following data
      | name           | subject              | body                                              | expirationDays |
      | New Template 1 | Welcome to LSDAF V3! | Hello there, welcome to LSADF V3! Enjoy the game! | 15             |

    Then the response status code should be 200

    And the response should have the following GameMailTemplateResponse
      | name           | subject              | body                                              | expirationDays |
      | New Template 1 | Welcome to LSDAF V3! | Hello there, welcome to LSADF V3! Enjoy the game! | 15             |

  Scenario: An admin user deletes an existing game mail template
    When the user logs in with the following credentials
      | username            | password |
      | paul.ochon@test.com | toto1234 |

    And the user requests the admin endpoint to delete the game mail template with id be7f65c8-c2eb-4dba-b351-746c293751d3

    Then the response status code should be 200

  Scenario: An admin user adds a new attachment to an existing game mail template
    When the user logs in with the following credentials
      | username            | password |
      | paul.ochon@test.com | toto1234 |

    And the user requests the admin endpoint to add a new attachment to the game mail template with id 38e9b063-724b-4794-ba7e-157d1e25c465 with the following data
      | attachment                                                                                                                                                                                   | type |
      | {"client_id":null,"blueprint_id":"leg_boo_01","type":"BOOTS","rarity":"RARE","is_equipped":false,"level":50,"main_stat":{"statistic":"ATTACK_ADD","base_value":120.0},"additional_stats":[]} | ITEM |

    Then the response status code should be 200

    And the response should have the following GameMailTemplateResponse
      | id                                   | name       | subject               | body                                                               | expirationDays |
      | 38e9b063-724b-4794-ba7e-157d1e25c465 | LSADF V2.0 | Welcome to LSDAF 2.0! | Hello there, welcome to LSADF 2.0! Have fun with the new updates ! | 30             |

    And the response should have the following GameMailAttachments
      | attachment                                                                                                                                                                                   | type     |
      | {"gold":500,"diamond":0,"emerald":0,"amethyst":0}                                                                                                                                            | CURRENCY |
      | {"client_id":null,"blueprint_id":"leg_boo_01","type":"BOOTS","rarity":"RARE","is_equipped":false,"level":50,"main_stat":{"statistic":"ATTACK_ADD","base_value":120.0},"additional_stats":[]} | ITEM     |
