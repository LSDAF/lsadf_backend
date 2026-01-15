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
package com.lsadf.application.unit.controller;

import static com.lsadf.core.infra.web.controller.ParameterConstants.X_GAME_SESSION_ID;
import static com.lsadf.core.unit.config.MockAuthenticationFactory.createMockJwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lsadf.application.controller.game.inventory.InventoryController;
import com.lsadf.application.controller.game.inventory.InventoryControllerImpl;
import com.lsadf.core.application.game.inventory.InventoryEventPublisherPort;
import com.lsadf.core.application.game.inventory.InventoryRepositoryPort;
import com.lsadf.core.application.game.inventory.InventoryService;
import com.lsadf.core.application.game.mail.GameMailRepositoryPort;
import com.lsadf.core.application.game.mail.GameMailTemplateRepositoryPort;
import com.lsadf.core.application.game.save.GameSaveRepositoryPort;
import com.lsadf.core.application.game.save.GameSaveService;
import com.lsadf.core.application.game.save.characteristics.CharacteristicsCachePort;
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
import com.lsadf.core.domain.game.inventory.ItemRarity;
import com.lsadf.core.domain.game.inventory.ItemStatistic;
import com.lsadf.core.domain.game.inventory.ItemType;
import com.lsadf.core.infra.web.controller.advice.GlobalExceptionHandler;
import com.lsadf.core.infra.web.dto.common.game.inventory.ItemStatDto;
import com.lsadf.core.infra.web.dto.request.game.inventory.ItemRequest;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;
import lombok.SneakyThrows;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.Answers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest({
  InventoryController.class,
  InventoryControllerImpl.class,
  GlobalExceptionHandler.class
})
@TestMethodOrder(MethodOrderer.MethodName.class)
@ActiveProfiles("test")
@MockitoBean(
    types = {
      GameSaveService.class,
      GameSessionRepositoryPort.class,
      GameMetadataRepositoryPort.class,
      CharacteristicsRepositoryPort.class,
      CurrencyRepositoryPort.class,
      InventoryEventPublisherPort.class,
      GameMailTemplateRepositoryPort.class,
      GameMailRepositoryPort.class,
      StageRepositoryPort.class,
      GameSaveRepositoryPort.class,
      InventoryRepositoryPort.class,
      GameSessionCachePort.class,
      GameMetadataCachePort.class,
      CurrencyCachePort.class,
      StageCachePort.class,
      CharacteristicsCachePort.class,
      GameSessionQueryService.class,
    })
class InventoryControllerTests {

  @Autowired private MockMvc mockMvc;

  @Autowired private ObjectMapper objectMapper;

  @MockitoBean(answers = Answers.RETURNS_DEEP_STUBS)
  private InventoryService inventoryService;

  private static final String UUID = java.util.UUID.randomUUID().toString();

  private static final SecurityMockMvcRequestPostProcessors.JwtRequestPostProcessor MOCK_JWT_USER =
      createMockJwt("paul.ochon@test.com", List.of("USER"), "Paul OCHON");

  private Supplier<ItemRequest> itemRequestSupplier =
      () ->
          ItemRequest.builder()
              .type(ItemType.CHESTPLATE.getType())
              .level(420)
              .rarity(ItemRarity.EPIC.getRarity())
              .blueprintId("blueprint_id")
              .isEquipped(false)
              .clientId(UUID)
              .additionalStats(Collections.emptyList())
              .mainStat(new ItemStatDto(ItemStatistic.ATTACK_ADD, 500f))
              .build();

  @Test
  @SneakyThrows
  void test_getInventoryItems_returns401_when_userNotAuthenticated() {
    // when
    mockMvc
        .perform(get("/api/v1/inventory/{gameSaveId}", "36f27c2a-06e8-4bdb-bf59-56999116f5ef"))
        // then
        .andExpect(status().isUnauthorized());
  }

  @Test
  @SneakyThrows
  void test_getInventoryItems_returns400_when_nonUuidGameSaveId() {
    // when
    mockMvc
        .perform(get("/api/v1/inventory/{gameSaveId}", "testtesttest").with(MOCK_JWT_USER))
        // then
        .andExpect(status().isBadRequest());
  }

  @Test
  @SneakyThrows
  void test_getInventoryItems_returns200_when_authenticatedUserAndValidUuid() {
    // when
    mockMvc
        .perform(
            get("/api/v1/inventory/{gameSaveId}", "36f27c2a-06e8-4bdb-bf59-56999116f5ef")
                .with(MOCK_JWT_USER))
        // then
        .andExpect(status().isOk());
  }

  @Test
  @SneakyThrows
  void test_createItemInInventory_returns401_when_userNotAuthenticated() {
    // when
    ItemRequest itemRequest = itemRequestSupplier.get();
    mockMvc
        .perform(
            post("/api/v1/inventory/{gameSaveId}/items", "36f27c2a-06e8-4bdb-bf59-56999116f5ef")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(itemRequest)))
        .andExpect(status().isUnauthorized());
  }

  @Test
  @SneakyThrows
  void test_createItemInInventory_returns400_when_gameSaveIdIsNonUuid() {
    // when
    mockMvc
        .perform(post("/api/v1/inventory/{gameSaveId}/items", "toto").with(MOCK_JWT_USER))
        .andExpect(status().isBadRequest());
  }

  @Test
  @SneakyThrows
  void test_createItemInInventory_returns400_when_null_payload() {
    // when
    mockMvc
        .perform(
            post("/api/v1/inventory/{gameSaveId}/items", "36f27c2a-06e8-4bdb-bf59-56999116f5ef")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .with(MOCK_JWT_USER))
        .andExpect(status().isBadRequest());
  }

  @Test
  @SneakyThrows
  void test_createItemInInventory_returns400_when_invalidObject() {
    // when
    ItemRequest invalidItemRequest =
        ItemRequest.builder()
            .isEquipped(null)
            .type(null)
            .mainStat(null)
            .blueprintId(null)
            .additionalStats(null)
            .level(-12)
            .build();

    mockMvc
        .perform(
            post("/api/v1/inventory/{gameSaveId}/items", "36f27c2a-06e8-4bdb-bf59-56999116f5ef")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidItemRequest))
                .with(MOCK_JWT_USER))
        .andExpect(status().isBadRequest());
  }

  @Test
  @SneakyThrows
  void test_createItemInInventory_returns200_when_validObject() {
    // when

    ItemRequest itemRequest = itemRequestSupplier.get();

    mockMvc
        .perform(
            post("/api/v1/inventory/{gameSaveId}/items", "36f27c2a-06e8-4bdb-bf59-56999116f5ef")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(itemRequest))
                .header(X_GAME_SESSION_ID, java.util.UUID.randomUUID().toString())
                .with(MOCK_JWT_USER))
        .andExpect(status().isOk());
  }

  @Test
  @SneakyThrows
  void test_deleteItemFromInventory_returns401_when_unauthenticated() {
    mockMvc
        .perform(
            delete(
                "/api/v1/inventory/{gameSaveId}/items/{clientId}",
                "36f27c2a-06e8-4bdb-bf59-56999116f5ef",
                "7a22d36c-b2b1-4b70-bce1-a7a8573a557b"))
        .andExpect(status().isUnauthorized());
  }

  @Test
  @SneakyThrows
  void test_deleteItemFromInventory_returns400_when_invalidUuid() {
    mockMvc
        .perform(
            delete(
                    "/api/v1/inventory/{gameSaveId}/items/{clientId}",
                    "toto",
                    "7a22d36c-b2b1-4b70-bce1-a7a8573a557b")
                .with(MOCK_JWT_USER))
        .andExpect(status().isBadRequest());
  }

  @Test
  @SneakyThrows
  void test_deleteItemFromInventory_returns200_when_validUuidAndItemId() {
    mockMvc
        .perform(
            delete(
                    "/api/v1/inventory/{gameSaveId}/items/{clientId}",
                    "36f27c2a-06e8-4bdb-bf59-56999116f5ef",
                    "7a22d36c-b2b1-4b70-bce1-a7a8573a557b")
                .header(X_GAME_SESSION_ID, java.util.UUID.randomUUID().toString())
                .with(MOCK_JWT_USER))
        .andExpect(status().isOk());
  }

  @Test
  @SneakyThrows
  void test_updateItemInInventory_returns401_when_userNotAuthenticated() {
    ItemRequest itemRequest = itemRequestSupplier.get();

    mockMvc
        .perform(
            put(
                    "/api/v1/inventory/{gameSaveId}/items/{clientId}",
                    "36f27c2a-06e8-4bdb-bf59-56999116f5ef",
                    "7a22d36c-b2b1-4b70-bce1-a7a8573a557b")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(itemRequest)))
        .andExpect(status().isUnauthorized());
  }

  @Test
  @SneakyThrows
  void test_updateItemInInventory_returns400_when_invalidUuid() {
    ItemRequest itemRequest = itemRequestSupplier.get();

    mockMvc
        .perform(
            put(
                    "/api/v1/inventory/{gameSaveId}/items/{clientId}",
                    "toto",
                    "7a22d36c-b2b1-4b70-bce1-a7a8573a557b")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(itemRequest))
                .with(MOCK_JWT_USER))
        .andExpect(status().isBadRequest());
  }

  @Test
  @SneakyThrows
  void test_updateItemInInventory_returns400_when_invalidObject() {
    ItemRequest invalidItemRequest =
        ItemRequest.builder()
            .isEquipped(null)
            .type(null)
            .mainStat(null)
            .blueprintId(null)
            .additionalStats(null)
            .level(-12)
            .build();
    mockMvc
        .perform(
            put(
                    "/api/v1/inventory/{gameSaveId}/items/{clientId}",
                    "36f27c2a-06e8-4bdb-bf59-56999116f5ef",
                    "7a22d36c-b2b1-4b70-bce1-a7a8573a557b")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(invalidItemRequest))
                .with(MOCK_JWT_USER))
        .andExpect(status().isBadRequest());
  }

  @Test
  @SneakyThrows
  void test_updateItemInInventory_returns400_when_null_object() {
    mockMvc
        .perform(
            put(
                    "/api/v1/inventory/{gameSaveId}/items/{clientId}",
                    "36f27c2a-06e8-4bdb-bf59-56999116f5ef",
                    "7a22d36c-b2b1-4b70-bce1-a7a8573a557b")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .with(MOCK_JWT_USER))
        .andExpect(status().isBadRequest());
  }

  @Test
  @SneakyThrows
  void test_updateItemInInventory_returns200_when_validUuidAndItemId() {
    ItemRequest itemRequest = itemRequestSupplier.get();

    mockMvc
        .perform(
            put(
                    "/api/v1/inventory/{gameSaveId}/items/{clientId}",
                    "36f27c2a-06e8-4bdb-bf59-56999116f5ef",
                    "7a22d36c-b2b1-4b70-bce1-a7a8573a557b")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(itemRequest))
                .header(X_GAME_SESSION_ID, java.util.UUID.randomUUID().toString())
                .with(MOCK_JWT_USER))
        .andExpect(status().isOk());
  }
}
