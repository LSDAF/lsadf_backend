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

Feature: Auth Controller BDD tests

  Background:
    Given the BDD engine is ready
    And a clean database
    And the time clock set to the present

    # We assume we have the two following users in keycloak
    # paul.ochon@test.com: ADMIN,USER: toto1234
    # paul.itesse@test.com: USER: toto5678

  Scenario: A user logs in with valid credentials
    When the user logs in with the following credentials
      | username            | password |
      | paul.ochon@test.com | toto1234 |

    Then the response status code should be 200

  Scenario: A user logs in with valid refresh token
    When the user logs in with the following credentials
      | username            | password |
      | paul.ochon@test.com | toto1234 |

    And the user uses the previously generated refresh token to log in

    Then the response status code should be 200

  Scenario: A user logs in with invalid refresh token
    When the user logs in with the following credentials
      | username            | password |
      | paul.ochon@test.com | toto1234 |

    And the user logs in with the following refresh token invalid_token

    Then the response status code should be 400

  Scenario: A user logs in with invalid credentials -> invalid username/password
    When the user logs in with the following credentials
      | username            | password |
      | paul.ochon@test.com | toto5678 |

    Then the response status code should be 401