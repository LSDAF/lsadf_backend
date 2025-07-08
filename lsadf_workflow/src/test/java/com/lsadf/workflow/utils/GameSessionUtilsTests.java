/*
 * Copyright © 2024-2025 LSDAF
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.lsadf.workflow.utils;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class GameSessionUtilsTests {

  // input
  private static final String TEST_USER_ID = "b28f426e-1c95-4dc9-a5d5-71072a917c3c";
  private static final String TEST_GAME_SAVE_ID = "37d1c911-0e22-4252-baeb-43edcbc8e6b3";

  // output
  private static final String TEST_SESSION_ID = TEST_USER_ID + "_" + TEST_GAME_SAVE_ID;

  @Test
  void buildSessionId_should_build_session_id_correctly() {
    // When
    String result = GameSessionUtils.buildSessionId(TEST_USER_ID, TEST_GAME_SAVE_ID);

    // Then
    assertEquals(TEST_SESSION_ID, result);
  }

  @Test
  void extractUserIdFromSessionId_should_return_valid_value() {
    // When
    String result = GameSessionUtils.extractUserIdFromSessionId(TEST_SESSION_ID);

    // Then
    assertEquals(TEST_USER_ID, result);
  }

  @Test
  void extractGameSaveIdFromSessionId_should_return_valid_value() {
    // When
    String result = GameSessionUtils.extractGameSaveIdFromSessionId(TEST_SESSION_ID);

    // Then
    assertEquals(TEST_GAME_SAVE_ID, result);
  }
}
