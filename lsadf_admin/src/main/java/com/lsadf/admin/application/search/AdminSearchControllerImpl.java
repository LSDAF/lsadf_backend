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
package com.lsadf.admin.application.search;

import static com.lsadf.core.infra.web.response.ResponseUtils.generateResponse;

import com.lsadf.core.application.search.SearchService;
import com.lsadf.core.domain.game.GameSave;
import com.lsadf.core.domain.user.User;
import com.lsadf.core.infra.web.controller.BaseController;
import com.lsadf.core.infra.web.request.game.game_save.GameSaveSortingParameter;
import com.lsadf.core.infra.web.request.search.SearchRequest;
import com.lsadf.core.infra.web.request.user.UserSortingParameter;
import com.lsadf.core.infra.web.response.ApiResponse;
import com.lsadf.core.infra.web.response.game.game_save.GameSaveResponse;
import com.lsadf.core.infra.web.response.game.game_save.GameSaveResponseMapper;
import com.lsadf.core.infra.web.response.user.UserResponse;
import com.lsadf.core.infra.web.response.user.UserResponseMapper;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.RestController;

/** The implementation of the AdminSearchController */
@Slf4j
@RestController
public class AdminSearchControllerImpl extends BaseController implements AdminSearchController {

  private final SearchService searchService;

  private final GameSaveResponseMapper gameSaveResponseMapper;
  private final UserResponseMapper userResponseMapper;

  @Autowired
  public AdminSearchControllerImpl(
      SearchService searchService,
      GameSaveResponseMapper gameSaveResponseMapper,
      UserResponseMapper userResponseMapper) {
    this.searchService = searchService;
    this.gameSaveResponseMapper = gameSaveResponseMapper;
    this.userResponseMapper = userResponseMapper;
  }

  /** {@inheritDoc} */
  @Override
  public Logger getLogger() {
    return log;
  }

  /**
   * {@inheritDoc}
   *
   * @return
   */
  @Override
  public ResponseEntity<ApiResponse<List<UserResponse>>> searchUsers(
      Jwt jwt, SearchRequest searchRequest, List<String> orderBy) {
    List<UserSortingParameter> sortingParameterList =
        Collections.singletonList(UserSortingParameter.NONE);
    if (orderBy != null && !orderBy.isEmpty()) {
      sortingParameterList = orderBy.stream().map(UserSortingParameter::fromString).toList();
    }
    validateUser(jwt);
    try (Stream<User> userStream = searchService.searchUsers(searchRequest, sortingParameterList)) {
      List<UserResponse> users = userStream.map(userResponseMapper::map).toList();
      return generateResponse(HttpStatus.OK, users);
    }
  }

  /**
   * {@inheritDoc}
   *
   * @return
   */
  @Override
  public ResponseEntity<ApiResponse<List<GameSaveResponse>>> searchGameSaves(
      Jwt jwt, SearchRequest searchRequest, List<String> orderBy) {
    List<GameSaveSortingParameter> gameSaveOrderBy =
        Collections.singletonList(GameSaveSortingParameter.NONE);
    if (orderBy != null && !orderBy.isEmpty()) {
      gameSaveOrderBy = orderBy.stream().map(GameSaveSortingParameter::valueOf).toList();
    }
    validateUser(jwt);
    try (Stream<GameSave> gameSaveStream =
        searchService.searchGameSaves(searchRequest, gameSaveOrderBy)) {
      List<GameSaveResponse> gameSaves = gameSaveStream.map(gameSaveResponseMapper::map).toList();
      return generateResponse(HttpStatus.OK, gameSaves);
    }
  }
}
