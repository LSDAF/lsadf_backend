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
package com.lsadf.core.unit.services;

import static com.lsadf.core.infra.web.JsonAttributes.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import com.lsadf.core.application.game.game_save.GameSaveService;
import com.lsadf.core.application.search.SearchService;
import com.lsadf.core.application.search.SearchServiceImpl;
import com.lsadf.core.application.user.UserService;
import com.lsadf.core.domain.game.GameSave;
import com.lsadf.core.domain.user.User;
import com.lsadf.core.infra.persistence.game.characteristics.CharacteristicsEntity;
import com.lsadf.core.infra.persistence.game.characteristics.CharacteristicsEntityMapper;
import com.lsadf.core.infra.persistence.game.currency.CurrencyEntity;
import com.lsadf.core.infra.persistence.game.currency.CurrencyEntityMapper;
import com.lsadf.core.infra.persistence.game.game_save.GameSaveEntity;
import com.lsadf.core.infra.persistence.game.game_save.GameSaveEntityMapper;
import com.lsadf.core.infra.persistence.game.stage.StageEntity;
import com.lsadf.core.infra.persistence.game.stage.StageEntityMapper;
import com.lsadf.core.infra.web.request.common.Filter;
import com.lsadf.core.infra.web.request.search.SearchRequest;
import com.lsadf.core.infra.web.request.user.UserSortingParameter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

@TestMethodOrder(MethodOrderer.MethodName.class)
class SearchServiceTests {
  @Mock UserService userService;

  @Mock GameSaveService gameSaveService;

  static GameSaveEntityMapper mapper =
      new GameSaveEntityMapper(
          new CharacteristicsEntityMapper(), new StageEntityMapper(), new CurrencyEntityMapper());

  SearchService searchService;

  // private elements
  private static final User USER1 =
      new User(
          "x", "Toto", "Toto", "toto@toto.com", true, true, List.of("USER", "ADMIN"), new Date());
  private static final User USER2 =
      new User("y", "Tata", "Tata", "tata@tata.com", true, true, List.of("USER"), new Date());
  private static final User USER3 =
      new User("z", "Toto", "Tutu", "tototutu@tutu.com", true, true, List.of("USER"), new Date());

  private static final StageEntity STAGE1 = new StageEntity("x", null, USER1.getUsername(), 1L, 2L);
  private static final StageEntity STAGE2 = new StageEntity("y", null, USER2.getUsername(), 3L, 4L);
  private static final StageEntity STAGE3 = new StageEntity("z", null, USER3.getUsername(), 5L, 6L);

  private static final CharacteristicsEntity CHARACTERISTICS1 =
      new CharacteristicsEntity("x", null, 1L, 2L, 3L, 4L, 5L);
  private static final CharacteristicsEntity CHARACTERISTICS2 =
      new CharacteristicsEntity("y", null, 5L, 6L, 7L, 8L, 9L);
  private static final CharacteristicsEntity CHARACTERISTICS3 =
      new CharacteristicsEntity("z", null, 9L, 10L, 11L, 12L, 13L);

  private static final CurrencyEntity CURRENCY1 =
      new CurrencyEntity("x", null, USER1.getUsername(), 1L, 2L, 3L, 4L);
  private static final CurrencyEntity CURRENCY2 =
      new CurrencyEntity("y", null, USER2.getUsername(), 5L, 6L, 7L, 8L);
  private static final CurrencyEntity CURRENCY3 =
      new CurrencyEntity("z", null, USER3.getUsername(), 9L, 10L, 11L, 12L);

  private static final GameSaveEntity GAME_SAVE_ENTITY_1 =
      GameSaveEntity.builder()
          .id("x")
          .characteristicsEntity(CHARACTERISTICS1)
          .currencyEntity(CURRENCY1)
          .stageEntity(STAGE1)
          .userEmail(USER1.getUsername())
          .nickname("x")
          .createdAt(new Date())
          .updatedAt(new Date())
          .build();

  private static final GameSave GAME_SAVE_1 = mapper.map(GAME_SAVE_ENTITY_1);

  private static final GameSaveEntity GAME_SAVE_ENTITY_2 =
      GameSaveEntity.builder()
          .id("y")
          .characteristicsEntity(CHARACTERISTICS2)
          .currencyEntity(CURRENCY2)
          .stageEntity(STAGE2)
          .userEmail(USER2.getUsername())
          .nickname("y")
          .createdAt(new Date())
          .updatedAt(new Date())
          .build();

  private static final GameSave GAME_SAVE_2 = mapper.map(GAME_SAVE_ENTITY_2);

  private static final GameSaveEntity GAME_SAVE_ENTITY_3 =
      GameSaveEntity.builder()
          .id("z")
          .characteristicsEntity(CHARACTERISTICS3)
          .currencyEntity(CURRENCY3)
          .stageEntity(STAGE3)
          .userEmail(USER3.getUsername())
          .nickname("z")
          .createdAt(new Date())
          .updatedAt(new Date())
          .build();

  private static final GameSave GAME_SAVE_3 = mapper.map(GAME_SAVE_ENTITY_3);

  @BeforeEach
  void init() {
    // Create all mocks and inject them into the service
    MockitoAnnotations.openMocks(this);
    this.searchService = new SearchServiceImpl(userService, gameSaveService);
    Stream<User> users = Stream.of(USER1, USER2, USER3);
    when(userService.getUsers()).thenReturn(users);

    Stream<GameSave> gameSaves = Stream.of(GAME_SAVE_1, GAME_SAVE_2, GAME_SAVE_3);
    when(gameSaveService.getGameSaves()).thenReturn(gameSaves);
  }

  @Test
  void filter_on_user_id() {
    Filter filter = new Filter(ID, "x");
    SearchRequest request = new SearchRequest(List.of(filter));

    List<User> result = searchService.searchUsers(request).toList();

    assertThat(result).hasSize(1);

    var user = result.get(0);

    assertThat(user)
        .usingRecursiveComparison()
        .ignoringFields("creationTimestamp")
        .isEqualTo(USER1);
  }

  @Test
  void filter_on_user_first_name() {
    Filter filter = new Filter(FIRST_NAME, "Toto");
    SearchRequest request = new SearchRequest(List.of(filter));

    List<User> result = searchService.searchUsers(request).toList();

    assertThat(result).hasSize(2);

    var user = result.get(0);
    assertThat(user)
        .usingRecursiveComparison()
        .ignoringFields("creationTimestamp")
        .isEqualTo(USER1);

    user = result.get(1);
    assertThat(user)
        .usingRecursiveComparison()
        .ignoringFields("creationTimestamp")
        .isEqualTo(USER3);
  }

  @Test
  void filter_on_user_last_name() {
    Filter filter = new Filter(LAST_NAME, "Tata");
    SearchRequest request = new SearchRequest(List.of(filter));

    List<User> result = searchService.searchUsers(request).toList();
    assertThat(result).hasSize(1);

    var user = result.get(0);
    assertThat(user)
        .usingRecursiveComparison()
        .ignoringFields("creationTimestamp")
        .isEqualTo(USER2);
  }

  @Test
  void filter_on_user_username() {
    Filter filter = new Filter(USERNAME, "toto@toto.com");
    SearchRequest request = new SearchRequest(List.of(filter));

    List<User> result = searchService.searchUsers(request).toList();
    assertThat(result).hasSize(1);

    var user = result.get(0);
    assertThat(user)
        .usingRecursiveComparison()
        .ignoringFields("creationTimestamp")
        .isEqualTo(USER1);
  }

  @Test
  void filter_on_user_roles() {

    Filter filter = new Filter(USER_ROLES, "ADMIN");
    SearchRequest request = new SearchRequest(List.of(filter));

    var result = searchService.searchUsers(request).toList();
    assertThat(result).hasSize(1);
  }

  @Test
  void filter_on_invalid_user_filter_type() {
    Filter filter = new Filter("INVALID", "x");
    SearchRequest request = new SearchRequest(List.of(filter));

    assertThatThrownBy(() -> searchService.searchUsers(request))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Invalid filter type");
  }

  @Test
  void filter_on_game_save_id() {
    Filter filter = new Filter(ID, "x");
    SearchRequest request = new SearchRequest(List.of(filter));

    List<GameSave> result = searchService.searchGameSaves(request).toList();
    assertThat(result).hasSize(1);

    var gameSave = result.get(0);
    assertThat(gameSave)
        .usingRecursiveComparison()
        .ignoringFields("createdAt", "updatedAt")
        .isEqualTo(GAME_SAVE_1);
  }

  @Test
  void filter_on_invalid_game_save_filter_type() {
    Filter filter = new Filter("INVALID", "x");
    SearchRequest request = new SearchRequest(List.of(filter));

    assertThatThrownBy(() -> searchService.searchGameSaves(request))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Invalid filter type");
  }

  @Test
  void order_by_user_id() {
    SearchRequest request = new SearchRequest(new ArrayList<>());

    List<User> result =
        searchService
            .searchUsers(request, Collections.singletonList(UserSortingParameter.ID))
            .toList();
    assertThat(result).hasSize(3);

    assertThat(result.get(0))
        .usingRecursiveComparison()
        .ignoringFields("creationTimestamp")
        .isEqualTo(USER1);
    assertThat(result.get(1))
        .usingRecursiveComparison()
        .ignoringFields("creationTimestamp")
        .isEqualTo(USER2);
    assertThat(result.get(2))
        .usingRecursiveComparison()
        .ignoringFields("creationTimestamp")
        .isEqualTo(USER3);
  }

  @Test
  void order_by_user_id_desc() {
    SearchRequest request = new SearchRequest(new ArrayList<>());

    List<User> result =
        searchService
            .searchUsers(request, Collections.singletonList(UserSortingParameter.ID_DESC))
            .toList();
    assertThat(result).hasSize(3);

    assertThat(result.get(0))
        .usingRecursiveComparison()
        .ignoringFields("creationTimestamp")
        .isEqualTo(USER3);
    assertThat(result.get(1))
        .usingRecursiveComparison()
        .ignoringFields("creationTimestamp")
        .isEqualTo(USER2);
    assertThat(result.get(2))
        .usingRecursiveComparison()
        .ignoringFields("creationTimestamp")
        .isEqualTo(USER1);
  }
}
