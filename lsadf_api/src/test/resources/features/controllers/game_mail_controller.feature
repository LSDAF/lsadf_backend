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

    And the following game sessions
      | id                                   | gameSaveId                           | cancelled | version |
      | 6025d3de-49ee-4ca1-98c0-28cb49f85e87 | 0530e1fe-3428-4edd-bb32-cb563419d0bd | false     | 1       |
      | e2f9b827-6c3a-4c9f-8e5b-3c4d7f1a9d2b | 9d68fe00-7449-4d90-9fa9-d3f77193be9f | false     | 1       |

    And the following game email templates
      | id                                   | name             | subject               | body                                                               | expirationDays |
      | be7f65c8-c2eb-4dba-b351-746c293751d3 | Welcome Template | Welcome to LSDAF!     | Hello there, welcome to LSADF!                                     | 30             |
      | 38e9b063-724b-4794-ba7e-157d1e25c465 | LSADF V2.0       | Welcome to LSDAF 2.0! | Hello there, welcome to LSADF 2.0! Have fun with the new updates ! | 30             |
      | 806809ab-c241-46b6-803d-2ba5d6bdcc29 | LSADF V3.0       | Welcome to LSDAF 3.0! | Hello there, welcome to LSADF 3.0! Have fun with the new updates ! | 30             |

    And the following game email template attachments
      | id                                   | mailTemplateId                       | object                                                                                                                                                                                                                                 | type     |
      | 2099c6fc-9618-4b1d-a0a9-24d75d0d1f32 | be7f65c8-c2eb-4dba-b351-746c293751d3 | {"gold":500,"diamond":1000,"emerald":1500,"amethyst":2000}                                                                                                                                                                             | CURRENCY |
      | 36dca975-ff13-425f-82cb-9a95e424e102 | be7f65c8-c2eb-4dba-b351-746c293751d3 | {"id":"1de67a24-f15c-41aa-88fb-3208eb860abf","gameSaveId":"8443ecaf-c92e-49db-93c8-ce868ccc4e0c","blueprintId":"leg_boo_01","itemType":"BOOTS","itemRarity":"RARE","level":50,"mainStat":{"statistic":"ATTACK_ADD","baseValue":120.0}} | ITEM     |
      | 7f2b50f4-67c7-458a-93cb-9a47cd82c90c | 38e9b063-724b-4794-ba7e-157d1e25c465 | {"gold":5000}                                                                                                                                                                                                                          | CURRENCY |

    And the following game emails
      | id                                   | gameSaveId                           | mailTemplateId                       | isRead | isAttachmentClaimed |
      | adbaf507-3987-45e1-bea6-e51525943148 | 0530e1fe-3428-4edd-bb32-cb563419d0bd | be7f65c8-c2eb-4dba-b351-746c293751d3 | false  | false               |
      | 3a766b66-f7fc-417f-9e17-0ff400b74414 | 0530e1fe-3428-4edd-bb32-cb563419d0bd | 38e9b063-724b-4794-ba7e-157d1e25c465 | true   | true                |
      | 49d6d5cb-16a4-4665-b432-2cf29c995fad | 0530e1fe-3428-4edd-bb32-cb563419d0bd | 806809ab-c241-46b6-803d-2ba5d6bdcc29 | true   | false               |

  Scenario: A user gets his mailbox for his game save
    When the user logs in with the following credentials
      | username            | password |
      | paul.ochon@test.com | toto1234 |

    And the user gets the mails of his game save with id 0530e1fe-3428-4edd-bb32-cb563419d0bd

    Then the response status code should be 200

    And the response should have the following GameMailResponses
      | id                                   | gameSaveId                           | subject               | body                                                               | read  | attachmentsClaimed |
      | adbaf507-3987-45e1-bea6-e51525943148 | 0530e1fe-3428-4edd-bb32-cb563419d0bd | Welcome to LSDAF!     | Hello there, welcome to LSADF!                                     | false | false              |
      | 3a766b66-f7fc-417f-9e17-0ff400b74414 | 0530e1fe-3428-4edd-bb32-cb563419d0bd | Welcome to LSDAF 2.0! | Hello there, welcome to LSADF 2.0! Have fun with the new updates ! | true  | true               |


  Scenario: A user reads a specific email with attachments from his mailbox
    When the user logs in with the following credentials
      | username            | password |
      | paul.ochon@test.com | toto1234 |

    And the user gets the content of the game mail with id adbaf507-3987-45e1-bea6-e51525943148

    Then the response status code should be 200

    And the response should have the following GameMailResponse
      | id                                   | gameSaveId                           | subject           | body                           | read | attachmentsClaimed |
      | adbaf507-3987-45e1-bea6-e51525943148 | 0530e1fe-3428-4edd-bb32-cb563419d0bd | Welcome to LSDAF! | Hello there, welcome to LSADF! | true | false              |

    And the response should have the following GameMailAttachments
      | type     | attachment                                                                                                                                                                                                                                                                                      |
      | CURRENCY | {"gold":500,"diamond":1000,"emerald":1500,"amethyst":2000}                                                                                                                                                                                                                                      |
      | ITEM     | {"id":"1de67a24-f15c-41aa-88fb-3208eb860abf","gameSaveId":"8443ecaf-c92e-49db-93c8-ce868ccc4e0c","clientId":null,"blueprintId":"leg_boo_01","itemType":"BOOTS","itemRarity":"RARE","isEquipped":null,"level":50,"mainStat":{"statistic":"ATTACK_ADD","baseValue":120.0},"additionalStats":null} |

  Scenario: A user reads a specific email without attachments from his mailbox
    When the user logs in with the following credentials
      | username            | password |
      | paul.ochon@test.com | toto1234 |

    And the user gets the content of the game mail with id 49d6d5cb-16a4-4665-b432-2cf29c995fad

    Then the response status code should be 200

    And the response should have the following GameMailResponse
      | id                                   | gameSaveId                           | subject               | body                                                               | read | attachmentsClaimed |
      | 49d6d5cb-16a4-4665-b432-2cf29c995fad | 0530e1fe-3428-4edd-bb32-cb563419d0bd | Welcome to LSDAF 3.0! | Hello there, welcome to LSADF 3.0! Have fun with the new updates ! | true | false              |

    And the response should not contain any attachments


  Scenario: A user claims an email attachment from an email in his mailbox
    When the user logs in with the following credentials
      | username            | password |
      | paul.ochon@test.com | toto1234 |

    And the user claims the attachments of the game mail with id adbaf507-3987-45e1-bea6-e51525943148 and session id 6025d3de-49ee-4ca1-98c0-28cb49f85e87

    Then the response status code should be 200

  Scenario: A user claims an email attachment from an email containing no attachment in his mailbox
    When the user logs in with the following credentials
      | username            | password |
      | paul.ochon@test.com | toto1234 |

    And the user claims the attachments of the game mail with id 49d6d5cb-16a4-4665-b432-2cf29c995fad and session id 6025d3de-49ee-4ca1-98c0-28cb49f85e87

    Then the response status code should be 403

  Scenario: A user tries to claim an already claimed email attachment from an email in his mailbox
    When the user logs in with the following credentials
      | username            | password |
      | paul.ochon@test.com | toto1234 |

    And the user claims the attachments of the game mail with id 3a766b66-f7fc-417f-9e17-0ff400b74414 and session id 6025d3de-49ee-4ca1-98c0-28cb49f85e87

    Then the response status code should be 403

  Scenario: A user tries to get an invalid email
    When the user logs in with the following credentials
      | username            | password |
      | paul.ochon@test.com | toto1234 |

    And the user gets the content of the game mail with id 9d1e3f62-4c70-4b9a-9b15-2a7f3c6c8e7a

    Then the response status code should be 404

  Scenario: A user tries to read an email not owned by his game save
    When the user logs in with the following credentials
      | username            | password |
      | paul.ochon@test.com | toto1234 |

    And the user claims the attachments of the game mail with id 3a766b66-f7fc-417f-9e17-0ff400b74414 and session id e2f9b827-6c3a-4c9f-8e5b-3c4d7f1a9d2b

    Then the response status code should be 403

  Scenario: A user deletes his read game saves
    When the user logs in with the following credentials
      | username            | password |
      | paul.ochon@test.com | toto1234 |

    And the user deletes his game mails for the game save with id 0530e1fe-3428-4edd-bb32-cb563419d0bd and session id 6025d3de-49ee-4ca1-98c0-28cb49f85e87

    Then the response status code should be 200