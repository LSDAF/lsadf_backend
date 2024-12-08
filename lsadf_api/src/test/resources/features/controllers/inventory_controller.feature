Feature: Inventory Controller BDD tests

  Background:
    Given the BDD engine is ready
    And a clean database
    And the time clock set to the present

    # We assume we have the two following users in keycloak
    # paul.ochon@test.com: ADMIN,USER: toto1234
    # paul.itesse@test.com: USER: toto5678

#  ---- GET ----
  Scenario: A user requests its inventory when it is empty
    Given the following game saves
      | id                                   | userEmail           | gold | diamond | emerald | amethyst | currentStage | maxStage | nickname | attack | critChance | critDamage | health | resistance |
      | aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa | paul.ochon@test.com | 1000 | 1000    | 1000    | 1000     | 1000         | 1000     | test-1   | 1100   | 1200       | 1300       | 1400   | 1500       |

    And the inventory of the game save with id aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa is set to empty

    When the user logs in with the following credentials
      | username            | password |
      | paul.ochon@test.com | toto1234 |

    And the user requests the endpoint to get the inventory of the game save with id aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa

    Then the response status code should be 200

    And the response should have the following items in the inventory
      | id                                   | type   |

    And the inventory of the game save with id aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa should be empty

  Scenario: A user requests its inventory when it is not empty
    Given the following game saves
      | id                                   | userEmail           | gold | diamond | emerald | amethyst | currentStage | maxStage | nickname | attack | critChance | critDamage | health | resistance |
      | aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa | paul.ochon@test.com | 1000 | 1000    | 1000    | 1000     | 1000         | 1000     | test-1   | 1100   | 1200       | 1300       | 1400   | 1500       |

    And the following items to the inventory of the game save with id aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa
      | clientId                                                                   | id                                   | type       | blueprintId | rarity    | isEquipped | level | mainStatBaseValue | mainStatStatistic | additionalStat1BaseValue | additionalStat1Statistic | additionalStat2BaseValue | additionalStat2Statistic | additionalStat3BaseValue | additionalStat3Statistic |
      | 36f27c2a-06e8-4bdb-bf59-56999116f5ef__11111111-1111-1111-1111-111111111111 | 11111111-1111-1111-1111-111111111111 | boots      | leg_boo_01  | LEGENDARY | true       | 20    | 100               | attack_add        | 200                      | attack_mult              | 300                      | attack_mult              | 400                      | attack_mult              |
      | 36f27c2a-06e8-4bdb-bf59-56999116f5ef__22222222-2222-2222-2222-222222222222 | 22222222-2222-2222-2222-222222222222 | chestplate | leg_che_01  | LEGENDARY | true       | 20    | 100               | attack_add        | 200                      | attack_mult              | 300                      | attack_mult              | 400                      | attack_mult              |
      | 36f27c2a-06e8-4bdb-bf59-56999116f5ef__33333333-3333-3333-3333-333333333333 | 33333333-3333-3333-3333-333333333333 | gloves     | leg_glo_01  | LEGENDARY | true       | 20    | 100               | attack_add        | 200                      | attack_mult              | 300                      | attack_mult              | 400                      | attack_mult              |
      | 36f27c2a-06e8-4bdb-bf59-56999116f5ef__44444444-4444-4444-4444-444444444444 | 44444444-4444-4444-4444-444444444444 | helmet     | leg_hel_01  | LEGENDARY | true       | 20    | 100               | attack_add        | 200                      | attack_mult              | 300                      | attack_mult              | 400                      | attack_mult              |
      | 36f27c2a-06e8-4bdb-bf59-56999116f5ef__55555555-5555-5555-5555-555555555555 | 55555555-5555-5555-5555-555555555555 | shield     | leg_she_01  | LEGENDARY | true       | 20    | 100               | attack_add        | 200                      | attack_mult              | 300                      | attack_mult              | 400                      | attack_mult              |
      | 36f27c2a-06e8-4bdb-bf59-56999116f5ef__66666666-6666-6666-6666-666666666666 | 66666666-6666-6666-6666-666666666666 | sword      | leg_swo_01  | LEGENDARY | true       | 20    | 100               | attack_add        | 200                      | attack_mult              | 300                      | attack_mult              | 400                      | attack_mult              |

    When the user logs in with the following credentials
      | username            | password |
      | paul.ochon@test.com | toto1234 |

    And the user requests the endpoint to get the inventory of the game save with id aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa

    Then the response status code should be 200

    And the response should have the following items in the inventory
      | clientId                                                                   | id                                   | type       | blueprintId | rarity    | isEquipped | level | mainStatBaseValue | mainStatStatistic | additionalStat1BaseValue | additionalStat1Statistic | additionalStat2BaseValue | additionalStat2Statistic | additionalStat3BaseValue | additionalStat3Statistic |
      | 36f27c2a-06e8-4bdb-bf59-56999116f5ef__11111111-1111-1111-1111-111111111111 | 11111111-1111-1111-1111-111111111111 | boots      | leg_boo_01  | LEGENDARY | true       | 20    | 100               | attack_add        | 200                      | attack_mult              | 300                      | attack_mult              | 400                      | attack_mult              |
      | 36f27c2a-06e8-4bdb-bf59-56999116f5ef__22222222-2222-2222-2222-222222222222 | 22222222-2222-2222-2222-222222222222 | chestplate | leg_che_01  | LEGENDARY | true       | 20    | 100               | attack_add        | 200                      | attack_mult              | 300                      | attack_mult              | 400                      | attack_mult              |
      | 36f27c2a-06e8-4bdb-bf59-56999116f5ef__33333333-3333-3333-3333-333333333333 | 33333333-3333-3333-3333-333333333333 | gloves     | leg_glo_01  | LEGENDARY | true       | 20    | 100               | attack_add        | 200                      | attack_mult              | 300                      | attack_mult              | 400                      | attack_mult              |
      | 36f27c2a-06e8-4bdb-bf59-56999116f5ef__44444444-4444-4444-4444-444444444444 | 44444444-4444-4444-4444-444444444444 | helmet     | leg_hel_01  | LEGENDARY | true       | 20    | 100               | attack_add        | 200                      | attack_mult              | 300                      | attack_mult              | 400                      | attack_mult              |
      | 36f27c2a-06e8-4bdb-bf59-56999116f5ef__55555555-5555-5555-5555-555555555555 | 55555555-5555-5555-5555-555555555555 | shield     | leg_she_01  | LEGENDARY | true       | 20    | 100               | attack_add        | 200                      | attack_mult              | 300                      | attack_mult              | 400                      | attack_mult              |
      | 36f27c2a-06e8-4bdb-bf59-56999116f5ef__66666666-6666-6666-6666-666666666666 | 66666666-6666-6666-6666-666666666666 | sword      | leg_swo_01  | LEGENDARY | true       | 20    | 100               | attack_add        | 200                      | attack_mult              | 300                      | attack_mult              | 400                      | attack_mult              |

#  ---- CREATE ----
  Scenario: A user requests to create an item in its inventory
    Given the following game saves
      | id                                   | userEmail           | gold | diamond | emerald | amethyst | currentStage | maxStage | nickname | attack | critChance | critDamage | health | resistance |
      | aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa | paul.ochon@test.com | 1000 | 1000    | 1000    | 1000     | 1000         | 1000     | test-1   | 1100   | 1200       | 1300       | 1400   | 1500       |

    And the inventory of the game save with id aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa is set to empty

    When the user logs in with the following credentials
      | username            | password |
      | paul.ochon@test.com | toto1234 |

    And the user requests the endpoint to create an item in the inventory of the game save with id aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa with the following ItemCreationRequest
      | clientId                                                                   | type   | blueprintId | rarity      | isEquipped | level | mainStatBaseValue | mainStatStatistic | additionalStat1BaseValue | additionalStat1Statistic | additionalStat2BaseValue | additionalStat2Statistic | additionalStat3BaseValue | additionalStat3Statistic |
      | 36f27c2a-06e8-4bdb-bf59-56999116f5ef__11111111-1111-1111-1111-111111111111 | boots  | leg_boo_01  | LEGENDARY   | true       | 20    | 100               | attack_add        | 200                      | attack_mult              | 300                      | attack_mult              | 400                      | attack_mult              |

    Then the response status code should be 200

  Scenario: A user requests to create an item in its inventory with an already existing client id
    Given the following game saves
      | id                                   | userEmail           | gold | diamond | emerald | amethyst | currentStage | maxStage | nickname | attack | critChance | critDamage | health | resistance |
      | aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa | paul.ochon@test.com | 1000 | 1000    | 1000    | 1000     | 1000         | 1000     | test-1   | 1100   | 1200       | 1300       | 1400   | 1500       |

    And the following items to the inventory of the game save with id aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa
      | clientId                                                                   | id                                   | type       | blueprintId | rarity     | isEquipped | level | mainStatBaseValue | mainStatStatistic | additionalStat1BaseValue | additionalStat1Statistic | additionalStat2BaseValue | additionalStat2Statistic | additionalStat3BaseValue | additionalStat3Statistic |
      | 36f27c2a-06e8-4bdb-bf59-56999116f5ef__11111111-1111-1111-1111-111111111111 | 11111111-1111-1111-1111-111111111111 | boots      | leg_boo_01  | LEGENDARY  | true       | 20    | 100               | attack_add        | 200                      | attack_mult              | 300                      | attack_mult              | 400                      | attack_mult              |

    When the user logs in with the following credentials
      | username            | password |
      | paul.ochon@test.com | toto1234 |

    And the user requests the endpoint to create an item in the inventory of the game save with id aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa with the following ItemCreationRequest
      | clientId                                                                   | type   | blueprintId | rarity      | isEquipped | level | mainStatBaseValue | mainStatStatistic | additionalStat1BaseValue | additionalStat1Statistic | additionalStat2BaseValue | additionalStat2Statistic | additionalStat3BaseValue | additionalStat3Statistic |
      | 36f27c2a-06e8-4bdb-bf59-56999116f5ef__11111111-1111-1111-1111-111111111111 | boots  | leg_boo_01  | LEGENDARY   | true       | 20    | 100               | attack_add        | 200                      | attack_mult              | 300                      | attack_mult              | 400                      | attack_mult              |

    Then the response status code should be 400

  Scenario: A user requests to create an item in the inventory of another user
    Given the following game saves
      | id                                   | userEmail            | gold | diamond | emerald | amethyst | currentStage | maxStage | nickname | attack | critChance | critDamage | health | resistance |
      | aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa | paul.ochon@test.com  | 1000 | 1000    | 1000    | 1000     | 1000         | 1000     | test-1   | 1100   | 1200       | 1300       | 1400   | 1500       |
      | bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb | paul.itesse@test.com | 1000 | 1000    | 1000    | 1000     | 1000         | 1000     | test-2   | 600    | 700        | 800        | 900    | 1000       |

    And the inventory of the game save with id aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa is set to empty
    And the inventory of the game save with id bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb is set to empty

    When the user logs in with the following credentials
      | username            | password |
      | paul.ochon@test.com | toto1234 |

    And the user requests the endpoint to create an item in the inventory of the game save with id bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb with the following ItemCreationRequest
      | clientId                                                                   | id                                   | type       | blueprintId | rarity    | isEquipped | level | mainStatBaseValue | mainStatStatistic | additionalStat1BaseValue | additionalStat1Statistic | additionalStat2BaseValue | additionalStat2Statistic | additionalStat3BaseValue | additionalStat3Statistic |
      | 36f27c2a-06e8-4bdb-bf59-56999116f5ef__11111111-1111-1111-1111-111111111111 | 11111111-1111-1111-1111-111111111111 | boots      | leg_boo_01  | LEGENDARY | true       | 20    | 100               | attack_add        | 200                      | attack_mult              | 300                      | attack_mult              | 400                      | attack_mult              |

    Then the response status code should be 403

  Scenario: A user requests to create an item in an invalid inventory
    Given the following game saves
      | id                                   | userEmail           | gold | diamond | emerald | amethyst | currentStage | maxStage | nickname | attack | critChance | critDamage | health | resistance |
      | aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa | paul.ochon@test.com | 1000 | 1000    | 1000    | 1000     | 1000         | 1000     | test-1   | 1100   | 1200       | 1300       | 1400   | 1500       |

    And the inventory of the game save with id aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa is set to empty

    When the user logs in with the following credentials
      | username            | password |
      | paul.ochon@test.com | toto1234 |

    And the user requests the endpoint to create an item in the inventory of the game save with id invalid_id with the following ItemCreationRequest
      | type  | blueprintId | rarity     | isEquipped | level | mainStatBaseValue | mainStatStatistic | additionalStat1BaseValue | additionalStat1Statistic | additionalStat2BaseValue | additionalStat2Statistic | additionalStat3BaseValue | additionalStat3Statistic |
      | boots | leg_boo_01  | LEGENDARY  | true       | 20    | 100               | attack_add        | 200                      | attack_mult              | 300                      | attack_mult              | 400                      | attack_mult              |

    Then the response status code should be 400

  Scenario: A user requests to create an item in the inventory of a inexistent game save
    Given the following game saves
      | id                                   | userEmail           | gold | diamond | emerald | amethyst | currentStage | maxStage | nickname | attack | critChance | critDamage | health | resistance |
      | aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa | paul.ochon@test.com | 1000 | 1000    | 1000    | 1000     | 1000         | 1000     | test-1   | 1100   | 1200       | 1300       | 1400   | 1500       |

    And the inventory of the game save with id aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa is set to empty

    When the user logs in with the following credentials
      | username            | password |
      | paul.ochon@test.com | toto1234 |

    And the user requests the endpoint to create an item in the inventory of the game save with id bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb with the following ItemCreationRequest
      | clientId                                                                   | id                                   | type       | blueprintId | rarity    | isEquipped | level | mainStatBaseValue | mainStatStatistic | additionalStat1BaseValue | additionalStat1Statistic | additionalStat2BaseValue | additionalStat2Statistic | additionalStat3BaseValue | additionalStat3Statistic |
      | 36f27c2a-06e8-4bdb-bf59-56999116f5ef__11111111-1111-1111-1111-111111111111 | 11111111-1111-1111-1111-111111111111 | boots      | leg_boo_01  | LEGENDARY | true       | 20    | 100               | attack_add        | 200                      | attack_mult              | 300                      | attack_mult              | 400                      | attack_mult              |

    Then the response status code should be 404
#  ---- DELETE ----

  Scenario: A user requests to delete an item in its inventory
    Given the following game saves
      | id                                   | userEmail           | gold | diamond | emerald | amethyst | currentStage | maxStage | nickname | attack | critChance | critDamage | health | resistance |
      | aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa | paul.ochon@test.com | 1000 | 1000    | 1000    | 1000     | 1000         | 1000     | test-1   | 1100   | 1200       | 1300       | 1400   | 1500       |

    And the following items to the inventory of the game save with id aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa
      | clientId                                                                   | id                                   | type       | blueprintId | rarity    | isEquipped | level | mainStatBaseValue | mainStatStatistic | additionalStat1BaseValue | additionalStat1Statistic | additionalStat2BaseValue | additionalStat2Statistic | additionalStat3BaseValue | additionalStat3Statistic |
      | 36f27c2a-06e8-4bdb-bf59-56999116f5ef__11111111-1111-1111-1111-111111111111 | 11111111-1111-1111-1111-111111111111 | boots      | leg_boo_01  | LEGENDARY | true       | 20    | 100               | attack_add        | 200                      | attack_mult              | 300                      | attack_mult              | 400                      | attack_mult              |
      | 36f27c2a-06e8-4bdb-bf59-56999116f5ef__22222222-2222-2222-2222-222222222222 | 22222222-2222-2222-2222-222222222222 | chestplate | leg_che_01  | LEGENDARY | true       | 20    | 100               | attack_add        | 200                      | attack_mult              | 300                      | attack_mult              | 400                      | attack_mult              |
      | 36f27c2a-06e8-4bdb-bf59-56999116f5ef__33333333-3333-3333-3333-333333333333 | 33333333-3333-3333-3333-333333333333 | gloves     | leg_glo_01  | LEGENDARY | true       | 20    | 100               | attack_add        | 200                      | attack_mult              | 300                      | attack_mult              | 400                      | attack_mult              |
      | 36f27c2a-06e8-4bdb-bf59-56999116f5ef__44444444-4444-4444-4444-444444444444 | 44444444-4444-4444-4444-444444444444 | helmet     | leg_hel_01  | LEGENDARY | true       | 20    | 100               | attack_add        | 200                      | attack_mult              | 300                      | attack_mult              | 400                      | attack_mult              |
      | 36f27c2a-06e8-4bdb-bf59-56999116f5ef__55555555-5555-5555-5555-555555555555 | 55555555-5555-5555-5555-555555555555 | shield     | leg_she_01  | LEGENDARY | true       | 20    | 100               | attack_add        | 200                      | attack_mult              | 300                      | attack_mult              | 400                      | attack_mult              |
      | 36f27c2a-06e8-4bdb-bf59-56999116f5ef__66666666-6666-6666-6666-666666666666 | 66666666-6666-6666-6666-666666666666 | sword      | leg_swo_01  | LEGENDARY | true       | 20    | 100               | attack_add        | 200                      | attack_mult              | 300                      | attack_mult              | 400                      | attack_mult              |

    When the user logs in with the following credentials
      | username            | password |
      | paul.ochon@test.com | toto1234 |

    And the user requests the endpoint to delete an item with id 11111111-1111-1111-1111-111111111111 in the inventory of the game save with id aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa

    Then the response status code should be 200

  Scenario: A user requests to delete an item in the inventory of another user
    Given the following game saves
      | id                                   | userEmail            | gold | diamond | emerald | amethyst | currentStage | maxStage | nickname | attack | critChance | critDamage | health | resistance |
      | aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa | paul.ochon@test.com  | 1000 | 1000    | 1000    | 1000     | 1000         | 1000     | test-1   | 1100   | 1200       | 1300       | 1400   | 1500       |
      | bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb | paul.itesse@test.com | 1000 | 1000    | 1000    | 1000     | 1000         | 1000     | test-2   | 600    | 700        | 800        | 900    | 1000       |

    And the following items to the inventory of the game save with id aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa
      | clientId                                                                   | id                                   | type       | blueprintId | rarity    | isEquipped | level | mainStatBaseValue | mainStatStatistic | additionalStat1BaseValue | additionalStat1Statistic | additionalStat2BaseValue | additionalStat2Statistic | additionalStat3BaseValue | additionalStat3Statistic |
      | 36f27c2a-06e8-4bdb-bf59-56999116f5ef__11111111-1111-1111-1111-111111111111 | 11111111-1111-1111-1111-111111111111 | boots      | leg_boo_01  | LEGENDARY | true       | 20    | 100               | attack_add        | 200                      | attack_mult              | 300                      | attack_mult              | 400                      | attack_mult              |
      | 36f27c2a-06e8-4bdb-bf59-56999116f5ef__22222222-2222-2222-2222-222222222222 | 22222222-2222-2222-2222-222222222222 | chestplate | leg_che_01  | LEGENDARY | true       | 20    | 100               | attack_add        | 200                      | attack_mult              | 300                      | attack_mult              | 400                      | attack_mult              |
      | 36f27c2a-06e8-4bdb-bf59-56999116f5ef__33333333-3333-3333-3333-333333333333 | 33333333-3333-3333-3333-333333333333 | gloves     | leg_glo_01  | LEGENDARY | true       | 20    | 100               | attack_add        | 200                      | attack_mult              | 300                      | attack_mult              | 400                      | attack_mult              |
      | 36f27c2a-06e8-4bdb-bf59-56999116f5ef__44444444-4444-4444-4444-444444444444 | 44444444-4444-4444-4444-444444444444 | helmet     | leg_hel_01  | LEGENDARY | true       | 20    | 100               | attack_add        | 200                      | attack_mult              | 300                      | attack_mult              | 400                      | attack_mult              |
      | 36f27c2a-06e8-4bdb-bf59-56999116f5ef__55555555-5555-5555-5555-555555555555 | 55555555-5555-5555-5555-555555555555 | shield     | leg_she_01  | LEGENDARY | true       | 20    | 100               | attack_add        | 200                      | attack_mult              | 300                      | attack_mult              | 400                      | attack_mult              |
      | 36f27c2a-06e8-4bdb-bf59-56999116f5ef__66666666-6666-6666-6666-666666666666 | 66666666-6666-6666-6666-666666666666 | sword      | leg_swo_01  | LEGENDARY | true       | 20    | 100               | attack_add        | 200                      | attack_mult              | 300                      | attack_mult              | 400                      | attack_mult              |

    When the user logs in with the following credentials
      | username            | password |
      | paul.ochon@test.com | toto1234 |

    And the user requests the endpoint to delete an item with id 11111111-1111-1111-1111-111111111111 in the inventory of the game save with id bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb

    Then the response status code should be 403

  Scenario: A user requests to delete an item in an invalid inventory
    Given the following game saves
      | id                                   | userEmail           | gold | diamond | emerald | amethyst | currentStage | maxStage | nickname | attack | critChance | critDamage | health | resistance |
      | aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa | paul.ochon@test.com | 1000 | 1000    | 1000    | 1000     | 1000         | 1000     | test-1   | 1100   | 1200       | 1300       | 1400   | 1500       |

    And the following items to the inventory of the game save with id aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa
      | clientId                                                                   | id                                   | type       | blueprintId | rarity    | isEquipped | level | mainStatBaseValue | mainStatStatistic | additionalStat1BaseValue | additionalStat1Statistic | additionalStat2BaseValue | additionalStat2Statistic | additionalStat3BaseValue | additionalStat3Statistic |
      | 36f27c2a-06e8-4bdb-bf59-56999116f5ef__11111111-1111-1111-1111-111111111111 | 11111111-1111-1111-1111-111111111111 | boots      | leg_boo_01  | LEGENDARY | true       | 20    | 100               | attack_add        | 200                      | attack_mult              | 300                      | attack_mult              | 400                      | attack_mult              |
      | 36f27c2a-06e8-4bdb-bf59-56999116f5ef__22222222-2222-2222-2222-222222222222 | 22222222-2222-2222-2222-222222222222 | chestplate | leg_che_01  | LEGENDARY | true       | 20    | 100               | attack_add        | 200                      | attack_mult              | 300                      | attack_mult              | 400                      | attack_mult              |
      | 36f27c2a-06e8-4bdb-bf59-56999116f5ef__33333333-3333-3333-3333-333333333333 | 33333333-3333-3333-3333-333333333333 | gloves     | leg_glo_01  | LEGENDARY | true       | 20    | 100               | attack_add        | 200                      | attack_mult              | 300                      | attack_mult              | 400                      | attack_mult              |
      | 36f27c2a-06e8-4bdb-bf59-56999116f5ef__44444444-4444-4444-4444-444444444444 | 44444444-4444-4444-4444-444444444444 | helmet     | leg_hel_01  | LEGENDARY | true       | 20    | 100               | attack_add        | 200                      | attack_mult              | 300                      | attack_mult              | 400                      | attack_mult              |
      | 36f27c2a-06e8-4bdb-bf59-56999116f5ef__55555555-5555-5555-5555-555555555555 | 55555555-5555-5555-5555-555555555555 | shield     | leg_she_01  | LEGENDARY | true       | 20    | 100               | attack_add        | 200                      | attack_mult              | 300                      | attack_mult              | 400                      | attack_mult              |
      | 36f27c2a-06e8-4bdb-bf59-56999116f5ef__66666666-6666-6666-6666-666666666666 | 66666666-6666-6666-6666-666666666666 | sword      | leg_swo_01  | LEGENDARY | true       | 20    | 100               | attack_add        | 200                      | attack_mult              | 300                      | attack_mult              | 400                      | attack_mult              |

    When the user logs in with the following credentials
      | username            | password |
      | paul.ochon@test.com | toto1234 |

    And the user requests the endpoint to delete an item with id invalid_item_id in the inventory of the game save with id aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa

    Then the response status code should be 400

  Scenario: A user requests to delete an item in the inventory of a inexistent game save
    Given the following game saves
      | id                                   | userEmail           | gold | diamond | emerald | amethyst | currentStage | maxStage | nickname | attack | critChance | critDamage | health | resistance |
      | aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa | paul.ochon@test.com | 1000 | 1000    | 1000    | 1000     | 1000         | 1000     | test-1   | 1100   | 1200       | 1300       | 1400   | 1500       |

    And the inventory of the game save with id aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa is set to empty

    When the user logs in with the following credentials
      | username            | password |
      | paul.ochon@test.com | toto1234 |

    And the user requests the endpoint to delete an item with id 11111111-1111-1111-1111-111111111111 in the inventory of the game save with id bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb

    Then the response status code should be 404

#  ---- UPDATE ----
  Scenario: A user requests to update an item in its inventory
    Given the following game saves
      | id                                   | userEmail           | gold | diamond | emerald | amethyst | currentStage | maxStage | nickname | attack | critChance | critDamage | health | resistance |
      | aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa | paul.ochon@test.com | 1000 | 1000    | 1000    | 1000     | 1000         | 1000     | test-1   | 1100   | 1200       | 1300       | 1400   | 1500       |

    And the following items to the inventory of the game save with id aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa
      | clientId                                                                   | id                                   | type       | blueprintId | rarity    | isEquipped | level | mainStatBaseValue | mainStatStatistic | additionalStat1BaseValue | additionalStat1Statistic | additionalStat2BaseValue | additionalStat2Statistic | additionalStat3BaseValue | additionalStat3Statistic |
      | 36f27c2a-06e8-4bdb-bf59-56999116f5ef__11111111-1111-1111-1111-111111111111 | 11111111-1111-1111-1111-111111111111 | boots      | leg_boo_01  | LEGENDARY | true       | 20    | 100               | attack_add        | 200                      | attack_mult              | 300                      | attack_mult              | 400                      | attack_mult              |
      | 36f27c2a-06e8-4bdb-bf59-56999116f5ef__22222222-2222-2222-2222-222222222222 | 22222222-2222-2222-2222-222222222222 | chestplate | leg_che_01  | LEGENDARY | true       | 20    | 100               | attack_add        | 200                      | attack_mult              | 300                      | attack_mult              | 400                      | attack_mult              |
      | 36f27c2a-06e8-4bdb-bf59-56999116f5ef__33333333-3333-3333-3333-333333333333 | 33333333-3333-3333-3333-333333333333 | gloves     | leg_glo_01  | LEGENDARY | true       | 20    | 100               | attack_add        | 200                      | attack_mult              | 300                      | attack_mult              | 400                      | attack_mult              |
      | 36f27c2a-06e8-4bdb-bf59-56999116f5ef__44444444-4444-4444-4444-444444444444 | 44444444-4444-4444-4444-444444444444 | helmet     | leg_hel_01  | LEGENDARY | true       | 20    | 100               | attack_add        | 200                      | attack_mult              | 300                      | attack_mult              | 400                      | attack_mult              |
      | 36f27c2a-06e8-4bdb-bf59-56999116f5ef__55555555-5555-5555-5555-555555555555 | 55555555-5555-5555-5555-555555555555 | shield     | leg_she_01  | LEGENDARY | true       | 20    | 100               | attack_add        | 200                      | attack_mult              | 300                      | attack_mult              | 400                      | attack_mult              |
      | 36f27c2a-06e8-4bdb-bf59-56999116f5ef__66666666-6666-6666-6666-666666666666 | 66666666-6666-6666-6666-666666666666 | sword      | leg_swo_01  | LEGENDARY | true       | 20    | 100               | attack_add        | 200                      | attack_mult              | 300                      | attack_mult              | 400                      | attack_mult              |

    When the user logs in with the following credentials
      | username            | password |
      | paul.ochon@test.com | toto1234 |

    And the user requests the endpoint to update an item with id 11111111-1111-1111-1111-111111111111 in the inventory of the game save with id aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa with the following ItemUpdateRequest
      | clientId                                                                   | id                                   | type       | blueprintId | rarity    | isEquipped | level | mainStatBaseValue | mainStatStatistic | additionalStat1BaseValue | additionalStat1Statistic | additionalStat2BaseValue | additionalStat2Statistic | additionalStat3BaseValue | additionalStat3Statistic |
      | 36f27c2a-06e8-4bdb-bf59-56999116f5ef__11111111-1111-1111-1111-111111111111 | 11111111-1111-1111-1111-111111111111 | boots      | leg_boo_01  | LEGENDARY | true       | 20    | 100               | attack_add        | 200                      | attack_mult              | 300                      | attack_mult              | 400                      | attack_mult              |


    Then the response status code should be 200

  Scenario: A user requests to update an item in its inventory with invalid item id
    Given the following game saves
      | id                                   | userEmail           | gold | diamond | emerald | amethyst | currentStage | maxStage | nickname | attack | critChance | critDamage | health | resistance |
      | aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa | paul.ochon@test.com | 1000 | 1000    | 1000    | 1000     | 1000         | 1000     | test-1   | 1100   | 1200       | 1300       | 1400   | 1500       |

    And the following items to the inventory of the game save with id aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa
      | clientId                                                                   | id                                   | type       | blueprintId | rarity    | isEquipped | level | mainStatBaseValue | mainStatStatistic | additionalStat1BaseValue | additionalStat1Statistic | additionalStat2BaseValue | additionalStat2Statistic | additionalStat3BaseValue | additionalStat3Statistic |
      | 36f27c2a-06e8-4bdb-bf59-56999116f5ef__11111111-1111-1111-1111-111111111111 | 11111111-1111-1111-1111-111111111111 | boots      | leg_boo_01  | LEGENDARY | true       | 20    | 100               | attack_add        | 200                      | attack_mult              | 300                      | attack_mult              | 400                      | attack_mult              |
      | 36f27c2a-06e8-4bdb-bf59-56999116f5ef__22222222-2222-2222-2222-222222222222 | 22222222-2222-2222-2222-222222222222 | chestplate | leg_che_01  | LEGENDARY | true       | 20    | 100               | attack_add        | 200                      | attack_mult              | 300                      | attack_mult              | 400                      | attack_mult              |
      | 36f27c2a-06e8-4bdb-bf59-56999116f5ef__33333333-3333-3333-3333-333333333333 | 33333333-3333-3333-3333-333333333333 | gloves     | leg_glo_01  | LEGENDARY | true       | 20    | 100               | attack_add        | 200                      | attack_mult              | 300                      | attack_mult              | 400                      | attack_mult              |
      | 36f27c2a-06e8-4bdb-bf59-56999116f5ef__44444444-4444-4444-4444-444444444444 | 44444444-4444-4444-4444-444444444444 | helmet     | leg_hel_01  | LEGENDARY | true       | 20    | 100               | attack_add        | 200                      | attack_mult              | 300                      | attack_mult              | 400                      | attack_mult              |
      | 36f27c2a-06e8-4bdb-bf59-56999116f5ef__55555555-5555-5555-5555-555555555555 | 55555555-5555-5555-5555-555555555555 | shield     | leg_she_01  | LEGENDARY | true       | 20    | 100               | attack_add        | 200                      | attack_mult              | 300                      | attack_mult              | 400                      | attack_mult              |
      | 36f27c2a-06e8-4bdb-bf59-56999116f5ef__66666666-6666-6666-6666-666666666666 | 66666666-6666-6666-6666-666666666666 | sword      | leg_swo_01  | LEGENDARY | true       | 20    | 100               | attack_add        | 200                      | attack_mult              | 300                      | attack_mult              | 400                      | attack_mult              |

    When the user logs in with the following credentials
      | username            | password |
      | paul.ochon@test.com | toto1234 |

    And the user requests the endpoint to update an item with id invalid_item_id in the inventory of the game save with id aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa with the following ItemUpdateRequest
      | clientId                                                                   | id                                   | type       | blueprintId | rarity    | isEquipped | level | mainStatBaseValue | mainStatStatistic | additionalStat1BaseValue | additionalStat1Statistic | additionalStat2BaseValue | additionalStat2Statistic | additionalStat3BaseValue | additionalStat3Statistic |
      | 36f27c2a-06e8-4bdb-bf59-56999116f5ef__11111111-1111-1111-1111-111111111111 | 11111111-1111-1111-1111-111111111111 | boots      | leg_boo_01  | LEGENDARY | true       | 20    | 100               | attack_add        | 200                      | attack_mult              | 300                      | attack_mult              | 400                      | attack_mult              |

    Then the response status code should be 400

  Scenario: A user requests to update an item from the inventory of another user
    Given the following game saves
      | id                                   | userEmail            | gold | diamond | emerald | amethyst | currentStage | maxStage | nickname | attack | critChance | critDamage | health | resistance |
      | aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa | paul.ochon@test.com  | 1000 | 1000    | 1000    | 1000     | 1000         | 1000     | test-1   | 1100   | 1200       | 1300       | 1400   | 1500       |
      | bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb | paul.itesse@test.com | 1000 | 1000    | 1000    | 1000     | 1000         | 1000     | test-2   | 600    | 700        | 800        | 900    | 1000       |

    And the following items to the inventory of the game save with id bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb
      | clientId                                                                   | id                                   | type       | blueprintId | rarity    | isEquipped | level | mainStatBaseValue | mainStatStatistic | additionalStat1BaseValue | additionalStat1Statistic | additionalStat2BaseValue | additionalStat2Statistic | additionalStat3BaseValue | additionalStat3Statistic |
      | 36f27c2a-06e8-4bdb-bf59-56999116f5ef__11111111-1111-1111-1111-111111111111 | 11111111-1111-1111-1111-111111111111 | boots      | leg_boo_01  | LEGENDARY | true       | 20    | 100               | attack_add        | 200                      | attack_mult              | 300                      | attack_mult              | 400                      | attack_mult              |
      | 36f27c2a-06e8-4bdb-bf59-56999116f5ef__22222222-2222-2222-2222-222222222222 | 22222222-2222-2222-2222-222222222222 | chestplate | leg_che_01  | LEGENDARY | true       | 20    | 100               | attack_add        | 200                      | attack_mult              | 300                      | attack_mult              | 400                      | attack_mult              |
      | 36f27c2a-06e8-4bdb-bf59-56999116f5ef__33333333-3333-3333-3333-333333333333 | 33333333-3333-3333-3333-333333333333 | gloves     | leg_glo_01  | LEGENDARY | true       | 20    | 100               | attack_add        | 200                      | attack_mult              | 300                      | attack_mult              | 400                      | attack_mult              |
      | 36f27c2a-06e8-4bdb-bf59-56999116f5ef__44444444-4444-4444-4444-444444444444 | 44444444-4444-4444-4444-444444444444 | helmet     | leg_hel_01  | LEGENDARY | true       | 20    | 100               | attack_add        | 200                      | attack_mult              | 300                      | attack_mult              | 400                      | attack_mult              |
      | 36f27c2a-06e8-4bdb-bf59-56999116f5ef__55555555-5555-5555-5555-555555555555 | 55555555-5555-5555-5555-555555555555 | shield     | leg_she_01  | LEGENDARY | true       | 20    | 100               | attack_add        | 200                      | attack_mult              | 300                      | attack_mult              | 400                      | attack_mult              |
      | 36f27c2a-06e8-4bdb-bf59-56999116f5ef__66666666-6666-6666-6666-666666666666 | 66666666-6666-6666-6666-666666666666 | sword      | leg_swo_01  | LEGENDARY | true       | 20    | 100               | attack_add        | 200                      | attack_mult              | 300                      | attack_mult              | 400                      | attack_mult              |

    When the user logs in with the following credentials
      | username            | password |
      | paul.ochon@test.com | toto1234 |

    And the user requests the endpoint to update an item with id 11111111-1111-1111-1111-111111111111 in the inventory of the game save with id bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb with the following ItemUpdateRequest
      | clientId                                                                   | id                                   | type       | blueprintId | rarity    | isEquipped | level | mainStatBaseValue | mainStatStatistic | additionalStat1BaseValue | additionalStat1Statistic | additionalStat2BaseValue | additionalStat2Statistic | additionalStat3BaseValue | additionalStat3Statistic |
      | 36f27c2a-06e8-4bdb-bf59-56999116f5ef__11111111-1111-1111-1111-111111111111 | 11111111-1111-1111-1111-111111111111 | boots      | leg_boo_01  | LEGENDARY | true       | 20    | 100               | attack_add        | 200                      | attack_mult              | 300                      | attack_mult              | 400                      | attack_mult              |


    Then the response status code should be 403
