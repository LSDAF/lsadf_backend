/*
 * Copyright © 2024-2026 LSDAF
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
package com.lsadf.admin.application.unit.controller;

import static com.lsadf.core.unit.config.MockAuthenticationFactory.createMockJwt;

import com.lsadf.admin.application.game.inventory.AdminInventoryController;
import com.lsadf.admin.application.game.inventory.AdminInventoryControllerImpl;
import com.lsadf.core.application.game.inventory.InventoryRepositoryPort;
import com.lsadf.core.application.game.inventory.InventoryService;
import com.lsadf.core.application.game.mail.GameMailRepositoryPort;
import com.lsadf.core.application.game.mail.GameMailTemplateRepositoryPort;
import com.lsadf.core.application.game.save.GameSaveRepositoryPort;
import com.lsadf.core.application.game.save.GameSaveService;
import com.lsadf.core.application.game.save.characteristics.CharacteristicsCachePort;
import com.lsadf.core.application.game.save.characteristics.CharacteristicsEventPublisherPort;
import com.lsadf.core.application.game.save.characteristics.CharacteristicsRepositoryPort;
import com.lsadf.core.application.game.save.currency.CurrencyCachePort;
import com.lsadf.core.application.game.save.currency.CurrencyRepositoryPort;
import com.lsadf.core.application.game.save.metadata.GameMetadataCachePort;
import com.lsadf.core.application.game.save.metadata.GameMetadataRepositoryPort;
import com.lsadf.core.application.game.save.stage.StageCachePort;
import com.lsadf.core.application.game.save.stage.StageRepositoryPort;
import com.lsadf.core.application.game.session.GameSessionCachePort;
import com.lsadf.core.application.game.session.GameSessionQueryService;
import com.lsadf.core.application.game.session.GameSessionRepositoryPort;
import com.lsadf.core.domain.game.inventory.ItemStatistic;
import com.lsadf.core.infra.web.controller.advice.GlobalExceptionHandler;
import com.lsadf.core.infra.web.dto.common.game.inventory.ItemStatDto;
import com.lsadf.core.infra.web.dto.request.game.inventory.ItemRequest;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import lombok.SneakyThrows;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.Answers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import tools.jackson.databind.ObjectMapper;

@WebMvcTest(
    value = {
      GlobalExceptionHandler.class,
      AdminInventoryController.class,
      AdminInventoryControllerImpl.class
    })
@TestMethodOrder(MethodOrderer.MethodName.class)
@ActiveProfiles("test")
@MockitoBean(
    types = {
      JwtDecoder.class,
      GameSaveService.class,
      GameSessionRepositoryPort.class,
      GameMetadataRepositoryPort.class,
      CharacteristicsRepositoryPort.class,
      CurrencyRepositoryPort.class,
      StageRepositoryPort.class,
      GameMailTemplateRepositoryPort.class,
      GameMailRepositoryPort.class,
      GameSaveRepositoryPort.class,
      InventoryRepositoryPort.class,
      GameSessionCachePort.class,
      GameMetadataCachePort.class,
      CurrencyCachePort.class,
      StageCachePort.class,
      CharacteristicsCachePort.class,
      CharacteristicsEventPublisherPort.class,
      GameSessionQueryService.class,
    })
class AdminInventoryControllerTests {

  @Autowired private MockMvc mockMvc;

  @Autowired private ObjectMapper objectMapper;

  @MockitoBean(answers = Answers.RETURNS_DEEP_STUBS)
  private InventoryService inventoryService;

  private static final String GAME_SAVE_ID = "550e8400-e29b-41d4-a716-446655440000";
  private static final String ITEM_CLIENT_ID = UUID.randomUUID() + "__" + UUID.randomUUID();

  private static final SecurityMockMvcRequestPostProcessors.JwtRequestPostProcessor MOCK_JWT_USER =
      createMockJwt("paul.ochon@test.com", List.of("USER"), "Paul OCHON");

  private static final SecurityMockMvcRequestPostProcessors.JwtRequestPostProcessor MOCK_JWT_ADMIN =
      createMockJwt("paul.ochon@test.com", List.of("USER", "ADMIN"), "Paul OCHON");

  private ItemRequest createTestItemRequest() {
    ItemStatDto mainStat = new ItemStatDto(ItemStatistic.ATTACK_ADD, 100.0f);
    return ItemRequest.builder()
        .clientId(ITEM_CLIENT_ID)
        .type("weapon")
        .blueprintId("test_blueprint")
        .rarity("COMMON")
        .isEquipped(false)
        .level(1)
        .mainStat(mainStat)
        .additionalStats(Collections.emptyList())
        .build();
  }

  // Tests for getInventory
  @Test
  @SneakyThrows
  void test_getInventory_returns401_when_userNotAuthenticated() {
    // when
    mockMvc
        .perform(
            MockMvcRequestBuilders.get("/api/v1/admin/inventory/" + GAME_SAVE_ID)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE))
        // then
        .andExpect(MockMvcResultMatchers.status().isUnauthorized());
  }

  @Test
  @SneakyThrows
  void test_getInventory_returns403_when_userNotAdmin() {
    // when
    mockMvc
        .perform(
            MockMvcRequestBuilders.get("/api/v1/admin/inventory/" + GAME_SAVE_ID)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .with(MOCK_JWT_USER))
        // then
        .andExpect(MockMvcResultMatchers.status().isForbidden());
  }

  @Test
  @SneakyThrows
  void test_getInventory_returns200_when_authenticatedUserIsAdmin() {
    // when
    mockMvc
        .perform(
            MockMvcRequestBuilders.get("/api/v1/admin/inventory/" + GAME_SAVE_ID)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .with(MOCK_JWT_ADMIN))
        // then
        .andExpect(MockMvcResultMatchers.status().isOk());
  }

  // Tests for createItemInInventory
  @Test
  @SneakyThrows
  void test_createItemInInventory_returns401_when_userNotAuthenticated() {
    // given
    ItemRequest itemRequest = createTestItemRequest();

    // when
    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/api/v1/admin/inventory/" + GAME_SAVE_ID + "/item")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(itemRequest)))
        // then
        .andExpect(MockMvcResultMatchers.status().isUnauthorized());
  }

  @Test
  @SneakyThrows
  void test_createItemInInventory_returns403_when_userNotAdmin() {
    // given
    ItemRequest itemRequest = createTestItemRequest();

    // when
    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/api/v1/admin/inventory/" + GAME_SAVE_ID + "/item")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(itemRequest))
                .with(MOCK_JWT_USER))
        // then
        .andExpect(MockMvcResultMatchers.status().isForbidden());
  }

  @Test
  @SneakyThrows
  void test_createItemInInventory_returns200_when_authenticatedUserIsAdmin() {
    // given
    ItemRequest itemRequest = createTestItemRequest();

    // when
    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/api/v1/admin/inventory/" + GAME_SAVE_ID + "/item")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(itemRequest))
                .with(MOCK_JWT_ADMIN))
        // then
        .andExpect(MockMvcResultMatchers.status().isOk());
  }

  // Tests for deleteItemFromInventory
  @Test
  @SneakyThrows
  void test_deleteItemFromInventory_returns401_when_userNotAuthenticated() {
    // when
    mockMvc
        .perform(
            MockMvcRequestBuilders.delete(
                    "/api/v1/admin/inventory/" + GAME_SAVE_ID + "/item/" + ITEM_CLIENT_ID)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE))
        // then
        .andExpect(MockMvcResultMatchers.status().isUnauthorized());
  }

  @Test
  @SneakyThrows
  void test_deleteItemFromInventory_returns403_when_userNotAdmin() {
    // when
    mockMvc
        .perform(
            MockMvcRequestBuilders.delete(
                    "/api/v1/admin/inventory/" + GAME_SAVE_ID + "/item/" + ITEM_CLIENT_ID)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .with(MOCK_JWT_USER))
        // then
        .andExpect(MockMvcResultMatchers.status().isForbidden());
  }

  @Test
  @SneakyThrows
  void test_deleteItemFromInventory_returns200_when_authenticatedUserIsAdmin() {
    // when
    mockMvc
        .perform(
            MockMvcRequestBuilders.delete(
                    "/api/v1/admin/inventory/" + GAME_SAVE_ID + "/item/" + ITEM_CLIENT_ID)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .with(MOCK_JWT_ADMIN))
        // then
        .andExpect(MockMvcResultMatchers.status().isOk());
  }

  // Tests for updateItemInInventory
  @Test
  @SneakyThrows
  void test_updateItemInInventory_returns401_when_userNotAuthenticated() {
    // given
    ItemRequest itemRequest = createTestItemRequest();

    // when
    mockMvc
        .perform(
            MockMvcRequestBuilders.put(
                    "/api/v1/admin/inventory/" + GAME_SAVE_ID + "/item/" + ITEM_CLIENT_ID)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(itemRequest)))
        // then
        .andExpect(MockMvcResultMatchers.status().isUnauthorized());
  }

  @Test
  @SneakyThrows
  void test_updateItemInInventory_returns403_when_userNotAdmin() {
    // given
    ItemRequest itemRequest = createTestItemRequest();

    // when
    mockMvc
        .perform(
            MockMvcRequestBuilders.put(
                    "/api/v1/admin/inventory/" + GAME_SAVE_ID + "/item/" + ITEM_CLIENT_ID)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(itemRequest))
                .with(MOCK_JWT_USER))
        // then
        .andExpect(MockMvcResultMatchers.status().isForbidden());
  }

  @Test
  @SneakyThrows
  void test_updateItemInInventory_returns200_when_authenticatedUserIsAdmin() {
    // given
    ItemRequest itemRequest = createTestItemRequest();

    // when
    mockMvc
        .perform(
            MockMvcRequestBuilders.put(
                    "/api/v1/admin/inventory/" + GAME_SAVE_ID + "/item/" + ITEM_CLIENT_ID)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(itemRequest))
                .with(MOCK_JWT_ADMIN))
        // then
        .andExpect(MockMvcResultMatchers.status().isOk());
  }

  // Tests for clearInventoryItems
  @Test
  @SneakyThrows
  void test_clearInventoryItems_returns401_when_userNotAuthenticated() {
    // when
    mockMvc
        .perform(
            MockMvcRequestBuilders.delete("/api/v1/admin/inventory/" + GAME_SAVE_ID)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE))
        // then
        .andExpect(MockMvcResultMatchers.status().isUnauthorized());
  }

  @Test
  @SneakyThrows
  void test_clearInventoryItems_returns403_when_userNotAdmin() {
    // when
    mockMvc
        .perform(
            MockMvcRequestBuilders.delete("/api/v1/admin/inventory/" + GAME_SAVE_ID)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .with(MOCK_JWT_USER))
        // then
        .andExpect(MockMvcResultMatchers.status().isForbidden());
  }

  @Test
  @SneakyThrows
  void test_clearInventoryItems_returns200_when_authenticatedUserIsAdmin() {
    // when
    mockMvc
        .perform(
            MockMvcRequestBuilders.delete("/api/v1/admin/inventory/" + GAME_SAVE_ID)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .with(MOCK_JWT_ADMIN))
        // then
        .andExpect(MockMvcResultMatchers.status().isOk());
  }
}
