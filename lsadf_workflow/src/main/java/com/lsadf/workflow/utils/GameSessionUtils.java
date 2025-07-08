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

import lombok.experimental.UtilityClass;

/**
 * Utility class providing methods for handling operations related to game session identifiers. This
 * class includes methods for constructing, extracting, and managing session identifiers based on
 * user ID and game save ID.
 */
@UtilityClass
public class GameSessionUtils {
  /**
   * Constructs a session ID by concatenating the user ID and game save ID with an underscore
   * separator.
   *
   * @param userId the unique identifier representing the user
   * @param gameSaveId the unique identifier representing the game save
   * @return a string representing the combined session ID
   */
  public static String buildSessionId(String userId, String gameSaveId) {
    return userId + "_" + gameSaveId;
  }

  /**
   * Extracts the user ID from the given session ID. The session ID is expected to be a string
   * formatted as "userId_gameSaveId", where the user ID appears before the underscore separator.
   *
   * @param sessionId the session ID string containing the user ID and game save ID separated by an
   *     underscore
   * @return the extracted user ID from the provided session ID
   */
  public static String extractUserIdFromSessionId(String sessionId) {
    return sessionId.split("_")[0];
  }

  /**
   * Extracts the game save ID from the given session ID. The session ID is expected to be a string
   * formatted as "userId_gameSaveId", where the game save ID appears after the underscore
   * separator.
   *
   * @param sessionId the session ID string containing the user ID and game save ID separated by an
   *     underscore
   * @return the extracted game save ID from the provided session ID
   */
  public static String extractGameSaveIdFromSessionId(String sessionId) {
    return sessionId.split("_")[1];
  }
}
