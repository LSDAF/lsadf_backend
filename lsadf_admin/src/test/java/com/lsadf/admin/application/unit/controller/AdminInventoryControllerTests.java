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
package com.lsadf.admin.application.unit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lsadf.admin.application.game.inventory.AdminInventoryController;
import com.lsadf.admin.application.game.inventory.AdminInventoryControllerImpl;
import com.lsadf.core.domain.game.inventory.item.ItemStat;
import com.lsadf.core.domain.game.inventory.item.ItemStatistic;
import com.lsadf.core.infra.web.controller.advice.GlobalExceptionHandler;
import com.lsadf.core.infra.web.request.game.inventory.ItemRequest;
import com.lsadf.core.unit.config.UnitTestConfiguration;
import com.lsadf.core.unit.config.WithMockJwtUser;
import java.util.Collections;
import java.util.UUID;
import lombok.SneakyThrows;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@RunWith(SpringRunner.class)
@WebMvcTest(
    value = {
      GlobalExceptionHandler.class,
      AdminInventoryController.class,
      AdminInventoryControllerImpl.class
    })
@Import(UnitTestConfiguration.class)
@TestMethodOrder(MethodOrderer.MethodName.class)
@ActiveProfiles("test")
class AdminInventoryControllerTests {

  @Autowired private MockMvc mockMvc;

  @Autowired private ObjectMapper objectMapper;

  private static final String GAME_SAVE_ID = "550e8400-e29b-41d4-a716-446655440000";
  private static final String ITEM_CLIENT_ID = UUID.randomUUID() + "__" + UUID.randomUUID();

  private ItemRequest createTestItemRequest() {
    ItemStat mainStat = new ItemStat(ItemStatistic.ATTACK_ADD, 100.0f);
    return ItemRequest.builder()
        .clientId(ITEM_CLIENT_ID)
        .itemType("weapon")
        .blueprintId("test_blueprint")
        .itemRarity("COMMON")
        .isEquipped(false)
        .level(1)
        .mainStat(mainStat)
        .additionalStats(Collections.emptyList())
        .build();
  }

  // Tests for getInventory
  @Test
  @SneakyThrows
  void getInventory_should_return_401_when_user_not_authenticated() {
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
  @WithMockJwtUser(username = "paul.ochon@test.com", name = "Paul OCHON")
  void getInventory_should_return_403_when_user_not_admin() {
    // when
    mockMvc
        .perform(
            MockMvcRequestBuilders.get("/api/v1/admin/inventory/" + GAME_SAVE_ID)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE))
        // then
        .andExpect(MockMvcResultMatchers.status().isForbidden());
  }

  @Test
  @SneakyThrows
  @WithMockJwtUser(
      username = "paul.ochon@test.com",
      name = "Paul OCHON",
      roles = {"ADMIN"})
  void getInventory_should_return_200_when_authenticated_user_is_admin() {
    // when
    mockMvc
        .perform(
            MockMvcRequestBuilders.get("/api/v1/admin/inventory/" + GAME_SAVE_ID)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE))
        // then
        .andExpect(MockMvcResultMatchers.status().isOk());
  }

  // Tests for createItemInInventory
  @Test
  @SneakyThrows
  void createItemInInventory_should_return_401_when_user_not_authenticated() {
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
  @WithMockJwtUser(username = "paul.ochon@test.com", name = "Paul OCHON")
  void createItemInInventory_should_return_403_when_user_not_admin() {
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
        .andExpect(MockMvcResultMatchers.status().isForbidden());
  }

  @Test
  @SneakyThrows
  @WithMockJwtUser(
      username = "paul.ochon@test.com",
      name = "Paul OCHON",
      roles = {"ADMIN"})
  void createItemInInventory_should_return_200_when_authenticated_user_is_admin() {
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
        .andExpect(MockMvcResultMatchers.status().isOk());
  }

  // Tests for deleteItemFromInventory
  @Test
  @SneakyThrows
  void deleteItemFromInventory_should_return_401_when_user_not_authenticated() {
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
  @WithMockJwtUser(username = "paul.ochon@test.com", name = "Paul OCHON")
  void deleteItemFromInventory_should_return_403_when_user_not_admin() {
    // when
    mockMvc
        .perform(
            MockMvcRequestBuilders.delete(
                    "/api/v1/admin/inventory/" + GAME_SAVE_ID + "/item/" + ITEM_CLIENT_ID)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE))
        // then
        .andExpect(MockMvcResultMatchers.status().isForbidden());
  }

  @Test
  @SneakyThrows
  @WithMockJwtUser(
      username = "paul.ochon@test.com",
      name = "Paul OCHON",
      roles = {"ADMIN"})
  void deleteItemFromInventory_should_return_200_when_authenticated_user_is_admin() {
    // when
    mockMvc
        .perform(
            MockMvcRequestBuilders.delete(
                    "/api/v1/admin/inventory/" + GAME_SAVE_ID + "/item/" + ITEM_CLIENT_ID)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE))
        // then
        .andExpect(MockMvcResultMatchers.status().isOk());
  }

  // Tests for updateItemInInventory
  @Test
  @SneakyThrows
  void updateItemInInventory_should_return_401_when_user_not_authenticated() {
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
  @WithMockJwtUser(username = "paul.ochon@test.com", name = "Paul OCHON")
  void updateItemInInventory_should_return_403_when_user_not_admin() {
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
        .andExpect(MockMvcResultMatchers.status().isForbidden());
  }

  @Test
  @SneakyThrows
  @WithMockJwtUser(
      username = "paul.ochon@test.com",
      name = "Paul OCHON",
      roles = {"ADMIN"})
  void updateItemInInventory_should_return_200_when_authenticated_user_is_admin() {
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
        .andExpect(MockMvcResultMatchers.status().isOk());
  }

  // Tests for clearInventoryItems
  @Test
  @SneakyThrows
  void clearInventoryItems_should_return_401_when_user_not_authenticated() {
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
  @WithMockJwtUser(username = "paul.ochon@test.com", name = "Paul OCHON")
  void clearInventoryItems_should_return_403_when_user_not_admin() {
    // when
    mockMvc
        .perform(
            MockMvcRequestBuilders.delete("/api/v1/admin/inventory/" + GAME_SAVE_ID)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE))
        // then
        .andExpect(MockMvcResultMatchers.status().isForbidden());
  }

  @Test
  @SneakyThrows
  @WithMockJwtUser(
      username = "paul.ochon@test.com",
      name = "Paul OCHON",
      roles = {"ADMIN"})
  void clearInventoryItems_should_return_200_when_authenticated_user_is_admin() {
    // when
    mockMvc
        .perform(
            MockMvcRequestBuilders.delete("/api/v1/admin/inventory/" + GAME_SAVE_ID)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE))
        // then
        .andExpect(MockMvcResultMatchers.status().isOk());
  }
}
