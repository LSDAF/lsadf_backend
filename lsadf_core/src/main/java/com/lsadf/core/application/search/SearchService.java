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
package com.lsadf.core.application.search;

import com.lsadf.core.domain.game.game_save.GameSave;
import com.lsadf.core.domain.user.User;
import com.lsadf.core.infra.web.request.game.game_save.GameSaveSortingParameter;
import com.lsadf.core.infra.web.request.search.SearchRequest;
import com.lsadf.core.infra.web.request.user.UserSortingParameter;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

public interface SearchService {
  /**
   * Search for users based on the given search request
   *
   * @param searchRequest The search request
   * @param orderBy The order by
   * @return A stream of users
   */
  Stream<User> searchUsers(SearchRequest searchRequest, List<UserSortingParameter> orderBy);

  default Stream<User> searchUsers(SearchRequest searchRequest) {
    return searchUsers(searchRequest, Collections.singletonList(UserSortingParameter.NONE));
  }

  /**
   * Search for game saves based on the given search request
   *
   * @param searchRequest The search request
   * @param orderBy The order by
   * @return A stream of game saves
   */
  Stream<GameSave> searchGameSaves(
      SearchRequest searchRequest, List<GameSaveSortingParameter> orderBy);

  default Stream<GameSave> searchGameSaves(SearchRequest searchRequest) {
    return searchGameSaves(searchRequest, Collections.singletonList(GameSaveSortingParameter.NONE));
  }
}
