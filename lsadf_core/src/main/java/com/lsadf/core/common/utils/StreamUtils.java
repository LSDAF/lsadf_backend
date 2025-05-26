/*
 * Copyright 2024-2025 LSDAF
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package com.lsadf.core.common.utils;

import com.lsadf.core.domain.game.GameSave;
import com.lsadf.core.domain.user.User;
import com.lsadf.core.infra.web.requests.game.game_save.GameSaveSortingParameter;
import com.lsadf.core.infra.web.requests.user.UserSortingParameter;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;
import lombok.experimental.UtilityClass;

@UtilityClass
public class StreamUtils {

  /**
   * Apply filters to a stream
   *
   * @param userStream the user stream
   * @param sortingParameters the sorting parameters
   * @return the filtered stream
   */
  public static Stream<User> sortUsers(
      Stream<User> userStream, List<UserSortingParameter> sortingParameters) {
    if (sortingParameters == null || sortingParameters.isEmpty()) {
      return userStream;
    }
    // Combine all comparators into a single comparator
    Comparator<User> finalComparator = null;
    boolean initialized = false;
    for (UserSortingParameter orderBy : sortingParameters) {
      Comparator<User> comparator = orderBy.getComparator();
      if (!initialized) {
        finalComparator = comparator;
        initialized = true;
      } else {
        finalComparator = finalComparator.thenComparing(comparator);
      }
    }

    if (finalComparator == null) {
      return userStream;
    }

    return userStream.sorted(finalComparator);
  }

  /**
   * Apply sorting filters to a stream
   *
   * @param gameSaveStream the game save stream
   * @param sortingParameters the sorting parameters
   * @return the sorted stream
   */
  public static Stream<GameSave> sortGameSaves(
      Stream<GameSave> gameSaveStream, List<GameSaveSortingParameter> sortingParameters) {
    if (sortingParameters == null || sortingParameters.isEmpty()) {
      return gameSaveStream;
    }
    // Combine all comparators into a single comparator
    Comparator<GameSave> finalComparator = null;
    boolean initialized = false;
    for (GameSaveSortingParameter orderBy : sortingParameters) {
      Comparator<GameSave> comparator = orderBy.getComparator();
      if (!initialized) {
        finalComparator = comparator;
        initialized = true;
      } else {
        finalComparator = finalComparator.thenComparing(comparator);
      }
    }

    if (finalComparator == null) {
      return gameSaveStream;
    }

    return gameSaveStream.sorted(finalComparator);
  }
}
