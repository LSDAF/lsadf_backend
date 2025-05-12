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

Feature: Admin User Controller BDD tests

  Background:
    Given the BDD engine is ready
    And the cache is enabled
    And a clean database
    And the time clock set to the present

    # We assume we have the two following users in keycloak
    # paul.ochon@test.com: ADMIN,USER: toto1234: ce60ea41-3765-4562-8c96-8673de8f96b0
    # paul.itesse@test.com: USER: toto5678: 71d6755e-1dfd-4f1f-8d7b-bdec9a62c6e8

  Scenario: A user gets all the users
    When the user logs in with the following credentials
      | username            | password |
      | paul.ochon@test.com | toto1234 |
    And the user requests the admin endpoint to get all the users ordered by NONE

    Then the response status code should be 200
    And the response should have the following Users
      | id                                   | firstName | lastName | username            | enabled | emailVerified | roles      |
      | ce60ea41-3765-4562-8c96-8673de8f96b0 | Paul      | OCHON    | paul.ochon@test.com | true    | true          | USER,ADMIN |

  Scenario: A user gets all the users in a specific order
    When the user logs in with the following credentials
      | username            | password |
      | paul.ochon@test.com | toto1234 |
    And the user requests the admin endpoint to get all the users ordered by USERNAME_DESC

    Then the response status code should be 200
    And the response should have the following Users in exact order
      | id                                   | firstName | lastName | username             | enabled | emailVerified | roles      |
      | ce60ea41-3765-4562-8c96-8673de8f96b0 | Paul      | OCHON    | paul.ochon@test.com  | true    | true          | USER,ADMIN |
      | 71d6755e-1dfd-4f1f-8d7b-bdec9a62c6e8 | Paul      | ITESSE   | paul.itesse@test.com | true    | true          | USER       |


  Scenario: A user gets a user by its id
    When the user logs in with the following credentials
      | username            | password |
      | paul.ochon@test.com | toto1234 |
    And the user requests the admin endpoint to get the user with the following id 71d6755e-1dfd-4f1f-8d7b-bdec9a62c6e8

    Then the response status code should be 200
    And the response should have the following User
      | id                                   | firstName | lastName | username             | enabled | emailVerified | roles |
      | 71d6755e-1dfd-4f1f-8d7b-bdec9a62c6e8 | Paul      | ITESSE   | paul.itesse@test.com | true    | true          | USER  |

  Scenario: A user gets a non-existing user by its id
    When the user logs in with the following credentials
      | username            | password |
      | paul.ochon@test.com | toto1234 |
    And the user requests the admin endpoint to get the user with the following id d44a00e3-a46c-4936-bbc4-9ca6b90c1e30

    Then the response status code should be 404

  Scenario: A user gets a user by its username
    When the user logs in with the following credentials
      | username            | password |
      | paul.ochon@test.com | toto1234 |
    And the user requests the admin endpoint to get the user with the following username paul.itesse@test.com

    Then the response status code should be 200
    And the response should have the following User
      | id                                   | firstName | lastName | username             | enabled | emailVerified | roles |
      | 71d6755e-1dfd-4f1f-8d7b-bdec9a62c6e8 | Paul      | ITESSE   | paul.itesse@test.com | true    | true          | USER  |

  Scenario: A user gets a non-existing user by its username
    When the user logs in with the following credentials
      | username            | password |
      | paul.ochon@test.com | toto1234 |
    And the user requests the admin endpoint to get the user with the following username paul.emploi@test.com

    Then the response status code should be 404

  Scenario: A user creates a new user
    When the user logs in with the following credentials
      | username            | password |
      | paul.ochon@test.com | toto1234 |

    And the user requests the admin endpoint to create a new user with the following AdminUserCreationRequest
      | firstName | lastName | username            | enabled | emailVerified | userRoles |
      | Paul      | ISSON    | paul.isson@test.com | true    | true          | USER      |

    Then the response status code should be 200
    And the number of users should be 3

    And the response should have the following User
      | firstName | lastName | username            | enabled | emailVerified | roles |
      | Paul      | ISSON    | paul.isson@test.com | true    | true          | USER  |

  Scenario: A user creates a new user with a non-existing role
    When the user logs in with the following credentials
      | username            | password |
      | paul.ochon@test.com | toto1234 |

    And the user requests the admin endpoint to create a new user with the following AdminUserCreationRequest
      | firstName | lastName | username             | enabled | emailVerified | userRoles       |
      | Paul      | AROIDE   | paul.aroide@test.com | true    | true          | INEXISTING_ROLE |

    Then the response status code should be 400
    And the number of users should be 3

  Scenario: A user tries to create a new user with an existing username
    When the user logs in with the following credentials
      | username            | password |
      | paul.ochon@test.com | toto1234 |

    And the user requests the admin endpoint to create a new user with the following AdminUserCreationRequest
      | firstName | lastName | username             | enabled | emailVerified | roles |
      | Paul      | ISSON    | paul.isson@test.com | true    | true          | USER  |

    Then the response status code should be 400
    And the number of users should be 3

  Scenario: A user updates a user
    When the user logs in with the following credentials
      | username            | password |
      | paul.ochon@test.com | toto1234 |

    And the user requests the admin endpoint to update the user with id 71d6755e-1dfd-4f1f-8d7b-bdec9a62c6e8 with the following AdminUserUpdateRequest
      | firstName | lastName | enabled | emailVerified | userRoles  |
      | Jean      | DUJARDIN | false   | false         | USER,ADMIN |

    Then the response status code should be 200
    And the response should have the following User
      | id                                   | firstName | lastName | username             | enabled | emailVerified | roles      |
      | 71d6755e-1dfd-4f1f-8d7b-bdec9a62c6e8 | Jean      | DUJARDIN | paul.itesse@test.com | false   | false         | USER,ADMIN |

    And the number of users should be 3

  Scenario: A user updates a non-existing user
    When the user logs in with the following credentials
      | username            | password |
      | paul.ochon@test.com | toto1234 |

    And the user requests the admin endpoint to update the user with id 84baae13-abb2-4698-9999-a18aa9dc098e with the following AdminUserUpdateRequest
      | firstName | lastName | enabled | emailVerified | userRoles  |
      | Jean      | DUJARDIN | false   | false         | USER,ADMIN |

    Then the response status code should be 404
    And the number of users should be 3

  Scenario: A user updates a user with a non-existing role
    When the user logs in with the following credentials
      | username            | password |
      | paul.ochon@test.com | toto1234 |

    And the user requests the admin endpoint to update the user with id 71d6755e-1dfd-4f1f-8d7b-bdec9a62c6e8 with the following AdminUserUpdateRequest
      | firstName | lastName | enabled | emailVerified | userRoles            |
      | Jean      | DUJARDIN | false   | false         | USER,INEXISTING_ROLE |


    Then the response status code should be 400
    And the number of users should be 3

  Scenario: A user deletes a user
    When the user logs in with the following credentials
      | username            | password |
      | paul.ochon@test.com | toto1234 |

    And the user requests the admin endpoint to delete the user with id 71d6755e-1dfd-4f1f-8d7b-bdec9a62c6e8

    Then the response status code should be 200
    And the number of users should be 2

  Scenario: A user deletes a non-existing user
    When the user logs in with the following credentials
      | username            | password |
      | paul.ochon@test.com | toto1234 |

    And the user requests the admin endpoint to delete the user with id 09390bad-84a8-4206-82bd-fd41411ee4f3

    Then the response status code should be 404
    And the number of users should be 2