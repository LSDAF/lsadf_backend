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
package com.lsadf.core.application.search.impl;

import static com.lsadf.core.infra.web.JsonAttributes.*;

import com.lsadf.core.application.game.save.GameSaveService;
import com.lsadf.core.application.search.SearchService;
import com.lsadf.core.application.user.UserService;
import com.lsadf.core.domain.game.save.GameSave;
import com.lsadf.core.domain.user.User;
import com.lsadf.core.infra.util.StreamUtils;
import com.lsadf.core.infra.web.dto.request.common.Filter;
import com.lsadf.core.infra.web.dto.request.game.save.GameSaveSortingParameter;
import com.lsadf.core.infra.web.dto.request.search.SearchRequest;
import com.lsadf.core.infra.web.dto.request.user.UserSortingParameter;
import java.util.List;
import java.util.stream.Stream;

/** Implementation of SearchService */
public class SearchServiceImpl implements SearchService {

  private final UserService userService;
  private final GameSaveService gameSaveService;

  public SearchServiceImpl(UserService userService, GameSaveService gameSaveService) {
    this.userService = userService;
    this.gameSaveService = gameSaveService;
  }

  @Override
  public Stream<User> searchUsers(SearchRequest searchRequest, List<UserSortingParameter> orderBy) {
    Stream<User> userStream = userService.getUsers();
    List<Filter> filters = searchRequest.filters();
    for (Filter filter : filters) {
      var value = filter.value();
      var regexValue = valueToRegex(value);
      switch (filter.type()) {
        case FIRST_NAME, FIRST_NAME_CAMEL_CASE ->
            userStream = userStream.filter(user -> user.getFirstName().matches(regexValue));
        case LAST_NAME, LAST_NAME_CAMEL_CASE ->
            userStream = userStream.filter(user -> user.getLastName().matches(regexValue));
        case USERNAME, USER_EMAIL, USER_EMAIL_CAMEL_CASE ->
            userStream = userStream.filter(user -> user.getUsername().matches(regexValue));
        case ID ->
            userStream = userStream.filter(user -> user.getId().toString().matches(regexValue));
        case USER_ROLES ->
            userStream =
                userStream.filter(
                    user ->
                        user.getUserRoles().stream().anyMatch(role -> role.matches(regexValue)));
        default -> throw new IllegalArgumentException("Invalid filter type");
      }
    }

    return StreamUtils.sortUsers(userStream, orderBy);
  }

  @Override
  public Stream<GameSave> searchGameSaves(
      SearchRequest searchRequest, List<GameSaveSortingParameter> orderBy) {
    Stream<GameSave> gameSaveStream = gameSaveService.getGameSaves().stream();
    List<Filter> filters = searchRequest.filters();
    for (Filter filter : filters) {
      var value = filter.value();
      var regexValue = valueToRegex(value);
      switch (filter.type()) {
        case ID ->
            gameSaveStream =
                gameSaveStream.filter(
                    gameSave -> gameSave.getMetadata().id().toString().matches(regexValue));
        case USER_EMAIL, USER_EMAIL_CAMEL_CASE ->
            gameSaveStream =
                gameSaveStream.filter(
                    gameSave -> gameSave.getMetadata().userEmail().matches(regexValue));
        case NICKNAME ->
            gameSaveStream =
                gameSaveStream.filter(
                    gameSave -> gameSave.getMetadata().nickname().matches(regexValue));
        default -> throw new IllegalArgumentException("Invalid filter type");
      }
    }

    return StreamUtils.sortGameSaves(gameSaveStream, orderBy);
  }

  /**
   * Convert a SQL-like pattern to a regex pattern.
   *
   * @param valuePattern The SQL-like pattern (e.g. San%)
   * @return The regex pattern (e.g. ^San.*$)
   */
  private static String valueToRegex(String valuePattern) {
    // Convert SQL-like pattern (e.g. San%) into regex (e.g. ^San.*$)
    String regex =
        valuePattern
            .replace(".", "\\.") // escape literal dots
            .replace("%", ".*") // convert SQL wildcard
            .replace("_", "."); // convert SQL single-char wildcard
    regex = "^" + regex + "$";
    return regex;
  }
}
