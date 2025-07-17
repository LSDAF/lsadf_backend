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

import static com.lsadf.core.infra.web.JsonAttributes.*;

import com.lsadf.core.application.game.game_save.GameSaveService;
import com.lsadf.core.application.user.UserService;
import com.lsadf.core.domain.game.GameSave;
import com.lsadf.core.domain.user.User;
import com.lsadf.core.infra.util.StreamUtils;
import com.lsadf.core.infra.web.request.common.Filter;
import com.lsadf.core.infra.web.request.game.game_save.GameSaveSortingParameter;
import com.lsadf.core.infra.web.request.search.SearchRequest;
import com.lsadf.core.infra.web.request.user.UserSortingParameter;
import java.util.List;
import java.util.stream.Stream;
import org.springframework.transaction.annotation.Transactional;

/** Implementation of SearchService */
public class SearchServiceImpl implements SearchService {

  private final UserService userService;
  private final GameSaveService gameSaveService;

  public SearchServiceImpl(UserService userService, GameSaveService gameSaveService) {
    this.userService = userService;
    this.gameSaveService = gameSaveService;
  }

  /** {@inheritDoc} */
  @Override
  public Stream<User> searchUsers(SearchRequest searchRequest, List<UserSortingParameter> orderBy) {
    Stream<User> userStream = userService.getUsers();
    List<Filter> filters = searchRequest.getFilters();
    for (Filter filter : filters) {
      switch (filter.type()) {
        case FIRST_NAME ->
            userStream = userStream.filter(user -> user.getFirstName().equals(filter.value()));
        case LAST_NAME ->
            userStream = userStream.filter(user -> user.getLastName().equals(filter.value()));
        case USERNAME, USER_EMAIL ->
            userStream = userStream.filter(user -> user.getUsername().equals(filter.value()));
        case ID -> userStream = userStream.filter(user -> user.getId().equals(filter.value()));
        case USER_ROLES ->
            userStream =
                userStream.filter(
                    user ->
                        user.getUserRoles().stream().anyMatch(role -> role.equals(filter.value())));
        default -> throw new IllegalArgumentException("Invalid filter type");
      }
    }

    return StreamUtils.sortUsers(userStream, orderBy);
  }

  /** {@inheritDoc} */
  @Override
  @Transactional(readOnly = true)
  public Stream<GameSave> searchGameSaves(
      SearchRequest searchRequest, List<GameSaveSortingParameter> orderBy) {
    Stream<GameSave> gameSaveStream = gameSaveService.getGameSaves();
    List<Filter> filters = searchRequest.getFilters();
    for (Filter filter : filters) {
      switch (filter.type()) {
        case ID ->
            gameSaveStream =
                gameSaveStream.filter(gameSave -> gameSave.getId().equals(filter.value()));
        case USER_EMAIL ->
            gameSaveStream =
                gameSaveStream.filter(gameSave -> gameSave.getUserEmail().equals(filter.value()));
        case NICKNAME ->
            gameSaveStream =
                gameSaveStream.filter(gameSave -> gameSave.getNickname().equals(filter.value()));
        default -> throw new IllegalArgumentException("Invalid filter type");
      }
    }

    return StreamUtils.sortGameSaves(gameSaveStream, orderBy);
  }
}
