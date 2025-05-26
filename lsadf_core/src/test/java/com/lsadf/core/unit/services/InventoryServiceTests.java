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

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.lsadf.core.application.game.inventory.InventoryService;
import com.lsadf.core.application.game.inventory.InventoryServiceImpl;
import com.lsadf.core.domain.game.inventory.item.ItemRarity;
import com.lsadf.core.domain.game.inventory.item.ItemStat;
import com.lsadf.core.domain.game.inventory.item.ItemStatistic;
import com.lsadf.core.domain.game.inventory.item.ItemType;
import com.lsadf.core.infra.exceptions.http.NotFoundException;
import com.lsadf.core.infra.persistence.game.GameSaveEntity;
import com.lsadf.core.infra.persistence.game.InventoryEntity;
import com.lsadf.core.infra.persistence.game.InventoryRepository;
import com.lsadf.core.infra.persistence.game.ItemEntity;
import com.lsadf.core.infra.persistence.game.ItemRepository;
import com.lsadf.core.infra.web.requests.game.inventory.item.ItemRequest;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

@TestMethodOrder(MethodOrderer.MethodName.class)
class InventoryServiceTests {
  private InventoryService inventoryService;

  @Mock private InventoryRepository inventoryRepository;

  @Mock private ItemRepository itemRepository;

  @BeforeEach
  void init() {
    // Create all mocks and inject them into the service
    MockitoAnnotations.openMocks(this);

    inventoryService = new InventoryServiceImpl(inventoryRepository, itemRepository);
  }

  /* GET */
  @Test
  void getInventory_on_null_gamesave_id() {
    // Act & Assert
    assertThrows(IllegalArgumentException.class, () -> inventoryService.getInventory(null));
  }

  @Test
  void getInventory_on_non_existing_gamesave_id() {
    // Arrange
    when(inventoryRepository.findById(anyString())).thenReturn(Optional.empty());

    // Assert
    assertThrows(NotFoundException.class, () -> inventoryService.getInventory("1"));
  }

  @Test
  void getInventory_on_existing_gamesave_id_with_empty_inventory() {
    // Arrange
    InventoryEntity inventoryEntity = InventoryEntity.builder().items(new HashSet<>()).build();

    when(inventoryRepository.findById(anyString())).thenReturn(Optional.of(inventoryEntity));

    // Act
    InventoryEntity result = inventoryService.getInventory("1");

    // Assert
    assertThat(result).isEqualTo(inventoryEntity);
  }

  @Test
  void getInventory_on_existing_gamesave_id_with_items() {
    // Arrange
    ItemEntity itemEntity =
        ItemEntity.builder()
            .id("1")
            .clientId("36f27c2a-06e8-4bdb-bf59-56999116f5ef__11111111-1111-1111-1111-111111111111")
            .blueprintId("aze")
            .itemType(ItemType.BOOTS)
            .itemRarity(ItemRarity.LEGENDARY)
            .isEquipped(true)
            .level(20)
            .mainStat(new ItemStat(ItemStatistic.ATTACK_MULT, 100f))
            .additionalStats(List.of(new ItemStat(ItemStatistic.HEALTH_ADD, 200f)))
            .build();

    ItemEntity itemEntity2 =
        ItemEntity.builder()
            .id("2")
            .clientId("36f27c2a-06e8-4bdb-bf59-56999116f5ef__22222222-2222-2222-2222-222222222222")
            .blueprintId("rty")
            .itemType(ItemType.SWORD)
            .itemRarity(ItemRarity.RARE)
            .isEquipped(false)
            .level(25)
            .mainStat(new ItemStat(ItemStatistic.ATTACK_ADD, 150f))
            .additionalStats(List.of(new ItemStat(ItemStatistic.HEALTH_MULT, 2f)))
            .build();

    InventoryEntity inventoryEntity =
        InventoryEntity.builder().items(new HashSet<>(List.of(itemEntity, itemEntity2))).build();

    when(inventoryRepository.findById(anyString())).thenReturn(Optional.of(inventoryEntity));

    // Act
    InventoryEntity result = inventoryService.getInventory("1");

    // Assert
    assertThat(result).isEqualTo(inventoryEntity);
  }

  /* CREATE */
  @Test
  void createItemInInventory_on_null_gamesave_id() {
    // Act & Assert
    assertThrows(
        IllegalArgumentException.class,
        () -> inventoryService.createItemInInventory(null, new ItemRequest()));
  }

  @Test
  void createItemInInventory_on_non_existing_gamesave_id() {
    // Arrange
    when(inventoryRepository.findById(anyString())).thenReturn(Optional.empty());

    // Assert
    assertThrows(
        NotFoundException.class,
        () -> inventoryService.createItemInInventory("1", new ItemRequest()));
  }

  @Test
  void createItemInInventory_on_existing_gamesave_id_with_empty_inventory() {
    // Arrange
    InventoryEntity inventoryEntity = InventoryEntity.builder().items(new HashSet<>()).build();

    ItemRequest itemRequest =
        new ItemRequest(
            "36f27c2a-06e8-4bdb-bf59-56999116f5ef__11111111-1111-1111-1111-111111111111",
            ItemType.BOOTS.getType(),
            "blueprint_id",
            ItemRarity.LEGENDARY.getRarity(),
            true,
            20,
            new ItemStat(ItemStatistic.ATTACK_ADD, 100f),
            List.of(new ItemStat(ItemStatistic.ATTACK_MULT, 2f)));

    when(inventoryRepository.findById(anyString())).thenReturn(Optional.of(inventoryEntity));

    // Act
    inventoryService.createItemInInventory("1", itemRequest);

    // Assert
    ArgumentCaptor<InventoryEntity> inventoryEntityCaptor =
        ArgumentCaptor.forClass(InventoryEntity.class);
    verify(inventoryRepository).save(inventoryEntityCaptor.capture());

    InventoryEntity capturedInventory = inventoryEntityCaptor.getValue();
    assertThat(capturedInventory.getItems()).hasSize(1);
  }

  @Test
  void createItemInInventory_on_existing_gamesave_id_with_non_empty_inventory() {
    // Arrange
    ItemEntity itemEntity = ItemEntity.builder().build();

    InventoryEntity inventoryEntity =
        InventoryEntity.builder().items(new HashSet<>(List.of(itemEntity))).build();

    ItemRequest itemRequest =
        new ItemRequest(
            "36f27c2a-06e8-4bdb-bf59-56999116f5ef__11111111-1111-1111-1111-111111111111",
            ItemType.SWORD.getType(),
            "blueprint_id",
            ItemRarity.LEGENDARY.getRarity(),
            true,
            20,
            new ItemStat(ItemStatistic.ATTACK_ADD, 100f),
            List.of(new ItemStat(ItemStatistic.ATTACK_MULT, 2f)));

    when(inventoryRepository.findById(anyString())).thenReturn(Optional.of(inventoryEntity));

    // Act
    inventoryService.createItemInInventory("1", itemRequest);

    // Assert
    ArgumentCaptor<InventoryEntity> inventoryEntityCaptor =
        ArgumentCaptor.forClass(InventoryEntity.class);
    verify(inventoryRepository).save(inventoryEntityCaptor.capture());

    InventoryEntity capturedInventory = inventoryEntityCaptor.getValue();
    assertThat(capturedInventory.getItems()).hasSize(2);
  }

  /* DELETE */
  @Test
  void deleteItemFromInventory_on_null_gamesave_id() {
    // Act & Assert
    assertThrows(
        IllegalArgumentException.class, () -> inventoryService.deleteItemFromInventory(null, "1"));
  }

  @Test
  void deleteItemFromInventory_on_null_item_id() {
    // Act & Assert
    assertThrows(
        IllegalArgumentException.class, () -> inventoryService.deleteItemFromInventory("1", null));
  }

  @Test
  void deleteItemFromInventory_on_non_existing_gamesave_id() {
    // Arrange
    when(inventoryRepository.findById(anyString())).thenReturn(Optional.empty());

    // Assert
    assertThrows(NotFoundException.class, () -> inventoryService.deleteItemFromInventory("1", "2"));
  }

  @Test
  void deleteItemFromInventory_on_non_existing_item_id() {
    // Arrange
    when(itemRepository.findById(anyString())).thenReturn(Optional.empty());

    // Assert
    assertThrows(NotFoundException.class, () -> inventoryService.deleteItemFromInventory("1", "2"));
  }

  @Test
  void deleteItemInInventory_on_existing_gamesave_id_with_one_item_inventory() {
    // Arrange
    GameSaveEntity gameSaveEntity =
        GameSaveEntity.builder().id("1").userEmail("test@test.com").build();
    ItemEntity itemEntity =
        ItemEntity.builder()
            .id("2")
            .clientId("6f27c2a-06e8-4bdb-bf59-56999116f5ef__22222222-2222-2222-2222-222222222222")
            .build();

    InventoryEntity inventoryEntity =
        InventoryEntity.builder()
            .items(new HashSet<>(List.of(itemEntity)))
            .gameSave(gameSaveEntity)
            .build();

    itemEntity.setInventoryEntity(inventoryEntity);

    when(inventoryRepository.findById(anyString())).thenReturn(Optional.of(inventoryEntity));
    when(itemRepository.findItemEntityByClientId(anyString())).thenReturn(Optional.of(itemEntity));

    // Act
    inventoryService.deleteItemFromInventory(
        "1", "6f27c2a-06e8-4bdb-bf59-56999116f5ef__22222222-2222-2222-2222-222222222222");

    // Assert
    ArgumentCaptor<InventoryEntity> inventoryEntityCaptor =
        ArgumentCaptor.forClass(InventoryEntity.class);
    verify(inventoryRepository).save(inventoryEntityCaptor.capture());

    InventoryEntity capturedInventory = inventoryEntityCaptor.getValue();
    assertThat(capturedInventory.getItems()).isEmpty();
  }

  @Test
  void deleteItemInInventory_on_existing_gamesave_id_with_two_items_inventory() {
    // Arrange
    GameSaveEntity gameSaveEntity =
        GameSaveEntity.builder().id("1").userEmail("test@test.com").build();
    ItemEntity itemEntity =
        ItemEntity.builder()
            .id("1")
            .clientId("6f27c2a-06e8-4bdb-bf59-56999116f5ef__11111111-1111-1111-1111-111111111111")
            .build();
    ItemEntity itemEntity2 =
        ItemEntity.builder()
            .id("2")
            .clientId("6f27c2a-06e8-4bdb-bf59-56999116f5ef__22222222-2222-2222-2222-222222222222")
            .build();

    InventoryEntity inventoryEntity =
        InventoryEntity.builder()
            .items(new HashSet<>(List.of(itemEntity, itemEntity2)))
            .gameSave(gameSaveEntity)
            .build();

    itemEntity.setInventoryEntity(inventoryEntity);
    itemEntity2.setInventoryEntity(inventoryEntity);

    when(inventoryRepository.findById(anyString())).thenReturn(Optional.of(inventoryEntity));
    when(itemRepository.findItemEntityByClientId(anyString())).thenReturn(Optional.of(itemEntity2));

    // Act
    inventoryService.deleteItemFromInventory(
        "1", "6f27c2a-06e8-4bdb-bf59-56999116f5ef__22222222-2222-2222-2222-222222222222");

    // Assert
    ArgumentCaptor<InventoryEntity> inventoryEntityCaptor =
        ArgumentCaptor.forClass(InventoryEntity.class);
    verify(inventoryRepository).save(inventoryEntityCaptor.capture());

    InventoryEntity capturedInventory = inventoryEntityCaptor.getValue();
    assertThat(capturedInventory.getItems()).hasSize(1);
  }

  /* UPDATE */
  @Test
  void updateItemInInventory_on_null_gamesave_id() {
    // Act & Assert
    assertThrows(
        IllegalArgumentException.class,
        () -> inventoryService.updateItemInInventory(null, "1", new ItemRequest()));
  }

  @Test
  void updateItemInInventory_on_null_client_id() {
    // Act & Assert
    assertThrows(
        IllegalArgumentException.class,
        () -> inventoryService.updateItemInInventory("1", null, new ItemRequest()));
  }

  @Test
  void updateItemInInventory_on_null_item_request() {
    // Act & Assert
    assertThrows(
        IllegalArgumentException.class,
        () -> inventoryService.updateItemInInventory("1", "1", null));
  }

  @Test
  void updateItemInInventory_on_non_existing_gamesave_id() {
    // Arrange
    when(inventoryRepository.findById(anyString())).thenReturn(Optional.empty());

    // Assert
    assertThrows(
        NotFoundException.class,
        () -> inventoryService.updateItemInInventory("1", "2", new ItemRequest()));
  }

  @Test
  void updateItemInInventory_on_non_existing_client_id() {
    // Arrange
    when(itemRepository.findById(anyString())).thenReturn(Optional.empty());

    // Assert
    assertThrows(
        NotFoundException.class,
        () -> inventoryService.updateItemInInventory("1", "2", new ItemRequest()));
  }

  @Test
  void updateItemInInventory_on_existing_gamesave_id_with_one_item_inventory() {
    // Arrange
    GameSaveEntity gameSaveEntity =
        GameSaveEntity.builder().id("1").userEmail("test@test.com").build();
    ItemEntity itemEntity =
        ItemEntity.builder()
            .id("2")
            .clientId("6f27c2a-06e8-4bdb-bf59-56999116f5ef__11111111-1111-1111-1111-111111111111")
            .itemType(ItemType.BOOTS)
            .build();

    InventoryEntity inventoryEntity =
        InventoryEntity.builder()
            .items(new HashSet<>(List.of(itemEntity)))
            .gameSave(gameSaveEntity)
            .build();

    itemEntity.setInventoryEntity(inventoryEntity);

    ItemRequest itemRequest =
        new ItemRequest(
            "36f27c2a-06e8-4bdb-bf59-56999116f5ef__11111111-1111-1111-1111-111111111111",
            ItemType.SWORD.getType(),
            "blueprint_id",
            ItemRarity.LEGENDARY.getRarity(),
            true,
            20,
            new ItemStat(ItemStatistic.ATTACK_ADD, 100f),
            List.of(new ItemStat(ItemStatistic.ATTACK_MULT, 2f)));

    when(inventoryRepository.findById(anyString())).thenReturn(Optional.of(inventoryEntity));
    when(itemRepository.findItemEntityByClientId(anyString())).thenReturn(Optional.of(itemEntity));

    // Act
    inventoryService.updateItemInInventory("1", "2", itemRequest);

    // Assert
    ArgumentCaptor<ItemEntity> itemEntityCaptor = ArgumentCaptor.forClass(ItemEntity.class);
    verify(itemRepository).save(itemEntityCaptor.capture());

    ItemEntity capturedItem = itemEntityCaptor.getValue();
    assertThat(capturedItem.getItemType()).isEqualTo(ItemType.SWORD);
  }

  /* CLEAR */
  @Test
  void clearInventory_on_null_gamesave_id() {
    // Act & Assert
    assertThrows(NotFoundException.class, () -> inventoryService.clearInventory(null));
  }

  @Test
  void clearInventory_on_non_existing_gamesave_id() {
    // Arrange
    when(inventoryRepository.findById(anyString())).thenReturn(Optional.empty());

    // Assert
    assertThrows(NotFoundException.class, () -> inventoryService.clearInventory("1"));
  }

  @Test
  void clearInventory_on_existing_gamesave_id_with_empty_inventory() {
    // Arrange
    InventoryEntity inventoryEntity = InventoryEntity.builder().items(new HashSet<>()).build();

    when(inventoryRepository.findById(anyString())).thenReturn(Optional.of(inventoryEntity));

    // Act
    inventoryService.clearInventory("1");

    // Assert
    ArgumentCaptor<InventoryEntity> inventoryEntityCaptor =
        ArgumentCaptor.forClass(InventoryEntity.class);
    verify(inventoryRepository).save(inventoryEntityCaptor.capture());

    InventoryEntity capturedInventory = inventoryEntityCaptor.getValue();
    assertThat(capturedInventory.getItems()).isEmpty();
  }

  @Test
  void clearInventory_on_existing_gamesave_id_with_items() {
    // Arrange
    ItemEntity itemEntity = ItemEntity.builder().build();
    ItemEntity itemEntity2 = ItemEntity.builder().build();

    InventoryEntity inventoryEntity =
        InventoryEntity.builder().items(new HashSet<>(List.of(itemEntity, itemEntity2))).build();

    when(inventoryRepository.findById(anyString())).thenReturn(Optional.of(inventoryEntity));

    // Act
    inventoryService.clearInventory("1");

    // Assert
    ArgumentCaptor<InventoryEntity> inventoryEntityCaptor =
        ArgumentCaptor.forClass(InventoryEntity.class);
    verify(inventoryRepository).save(inventoryEntityCaptor.capture());

    InventoryEntity capturedInventory = inventoryEntityCaptor.getValue();
    assertThat(capturedInventory.getItems()).isEmpty();
  }
}
