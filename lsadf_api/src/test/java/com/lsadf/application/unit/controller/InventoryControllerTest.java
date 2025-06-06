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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lsadf.application.controller.game.inventory.InventoryController;
import com.lsadf.application.controller.game.inventory.InventoryControllerImpl;
import com.lsadf.core.domain.game.inventory.item.ItemRarity;
import com.lsadf.core.domain.game.inventory.item.ItemStat;
import com.lsadf.core.domain.game.inventory.item.ItemType;
import com.lsadf.core.infra.web.controller.advice.GlobalExceptionHandler;
import com.lsadf.core.infra.web.requests.game.inventory.ItemRequest;
import com.lsadf.core.unit.config.UnitTestConfiguration;
import com.lsadf.core.unit.config.WithMockJwtUser;
import java.util.Collections;
import java.util.function.Supplier;
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

@RunWith(SpringRunner.class)
@WebMvcTest({
  InventoryController.class,
  InventoryControllerImpl.class,
  GlobalExceptionHandler.class
})
@Import(UnitTestConfiguration.class)
@TestMethodOrder(MethodOrderer.MethodName.class)
@ActiveProfiles("test")
class InventoryControllerTest {

  @Autowired private MockMvc mockMvc;

  @Autowired private ObjectMapper objectMapper;

  private Supplier<ItemRequest> itemRequestSupplier =
      () ->
          ItemRequest.builder()
              .itemType(ItemType.CHESTPLATE.getType())
              .level(420)
              .itemRarity(ItemRarity.EPIC.getRarity())
              .blueprintId("blueprint_id")
              .isEquipped(false)
              .clientId("client_id")
              .additionalStats(Collections.emptyList())
              .mainStat(new ItemStat())
              .build();

  @Test
  @SneakyThrows
  void getInventory_Items_should_return_401_when_user_not_authenticated() {
    // when
    mockMvc
        .perform(get("/api/v1/inventory/{gameSaveId}", "36f27c2a-06e8-4bdb-bf59-56999116f5ef"))
        // then
        .andExpect(status().isUnauthorized());
  }

  @Test
  @SneakyThrows
  @WithMockJwtUser(username = "paul.ochon@test.com", name = "Paul OCHON")
  void getInventory_Items_should_return_400_when_non_uuid_gameSaveId() {
    // when
    mockMvc
        .perform(get("/api/v1/inventory/{gameSaveId}", "testtesttest"))
        // then
        .andExpect(status().isBadRequest());
  }

  @Test
  @SneakyThrows
  @WithMockJwtUser(username = "paul.ochon@test.com", name = "Paul OCHON")
  void getInventory_Items_should_return_200_when_authenticated_user_and_valid_uuid() {
    // when
    mockMvc
        .perform(get("/api/v1/inventory/{gameSaveId}", "36f27c2a-06e8-4bdb-bf59-56999116f5ef"))
        // then
        .andExpect(status().isOk());
  }

  @Test
  @SneakyThrows
  void createItemInInventory_should_return_401_when_user_not_authenticated() {
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
  @WithMockJwtUser(username = "paul.ochon@test.com", name = "Paul OCHON")
  void createItemInInventory_should_return_400_when_gameSaveId_is_non_uuid() {
    // when
    mockMvc
        .perform(post("/api/v1/inventory/{gameSaveId}/items", "toto"))
        .andExpect(status().isBadRequest());
  }

  @Test
  @SneakyThrows
  @WithMockJwtUser(username = "paul.ochon@test.com", name = "Paul OCHON")
  void createItemInInventory_should_return_400_when_invalid_object() {
    // when

    ItemRequest itemRequest = new ItemRequest();

    mockMvc
        .perform(
            post("/api/v1/inventory/{gameSaveId}/items", "36f27c2a-06e8-4bdb-bf59-56999116f5ef")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(itemRequest)))
        .andExpect(status().isBadRequest());
  }

  @Test
  @SneakyThrows
  @WithMockJwtUser(username = "paul.ochon@test.com", name = "Paul OCHON")
  void createItemInInventory_should_return_200_when_valid_object() {
    // when

    ItemRequest itemRequest = itemRequestSupplier.get();

    mockMvc
        .perform(
            post("/api/v1/inventory/{gameSaveId}/items", "36f27c2a-06e8-4bdb-bf59-56999116f5ef")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(itemRequest)))
        .andExpect(status().isOk());
  }

  @Test
  @SneakyThrows
  void deleteItemFromInventory_should_return_401_when_unauthenticated() {
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
  @WithMockJwtUser(username = "paul.ochon@test.com", name = "Paul OCHON")
  void deleteItemFromInventory_should_return_400_when_invalid_uuid() {
    mockMvc
        .perform(
            delete(
                "/api/v1/inventory/{gameSaveId}/items/{clientId}",
                "toto",
                "7a22d36c-b2b1-4b70-bce1-a7a8573a557b"))
        .andExpect(status().isBadRequest());
  }

  @Test
  @SneakyThrows
  @WithMockJwtUser(username = "paul.ochon@test.com", name = "Paul OCHON")
  void deleteItemFromInventory_should_return_200_when_valid_uuid_and_item_id() {
    mockMvc
        .perform(
            delete(
                "/api/v1/inventory/{gameSaveId}/items/{clientId}",
                "36f27c2a-06e8-4bdb-bf59-56999116f5ef",
                "7a22d36c-b2b1-4b70-bce1-a7a8573a557b"))
        .andExpect(status().isOk());
  }

  @Test
  @SneakyThrows
  void updateItemInInventory_should_return_401_when_user_not_authenticated() {
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
  @WithMockJwtUser(username = "paul.ochon@test.com", name = "Paul OCHON")
  void updateItemInInventory_should_return_400_when_invalid_uuid() {
    ItemRequest itemRequest = itemRequestSupplier.get();

    mockMvc
        .perform(
            put(
                    "/api/v1/inventory/{gameSaveId}/items/{clientId}",
                    "toto",
                    "7a22d36c-b2b1-4b70-bce1-a7a8573a557b")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(itemRequest)))
        .andExpect(status().isBadRequest());
  }

  @Test
  @SneakyThrows
  @WithMockJwtUser(username = "paul.ochon@test.com", name = "Paul OCHON")
  void updateItemInInventory_should_return_400_when_invalid_object() {
    ItemRequest invalidItemRequest = itemRequestSupplier.get();
    invalidItemRequest.setItemType(null);

    mockMvc
        .perform(
            put(
                    "/api/v1/inventory/{gameSaveId}/items/{clientId}",
                    "36f27c2a-06e8-4bdb-bf59-56999116f5ef",
                    "7a22d36c-b2b1-4b70-bce1-a7a8573a557b")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(invalidItemRequest)))
        .andExpect(status().isBadRequest());
  }

  @Test
  @SneakyThrows
  @WithMockJwtUser(username = "paul.ochon@test.com", name = "Paul OCHON")
  void updateItemInInventory_should_return_200_when_valid_uuid_and_item_id() {
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
        .andExpect(status().isOk());
  }
}
