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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import com.lsadf.core.application.game.inventory.InventoryService;
import com.lsadf.core.application.game.inventory.InventoryServiceImpl;
import com.lsadf.core.domain.game.inventory.item.*;
import com.lsadf.core.infra.exceptions.http.NotFoundException;
import com.lsadf.core.infra.persistence.game.game_save.GameSaveEntity;
import com.lsadf.core.infra.persistence.game.inventory.InventoryEntity;
import com.lsadf.core.infra.persistence.game.inventory.InventoryRepository;
import com.lsadf.core.infra.persistence.game.inventory.items.ItemEntity;
import com.lsadf.core.infra.persistence.game.inventory.items.ItemEntityMapper;
import com.lsadf.core.infra.persistence.game.inventory.items.ItemRepository;
import com.lsadf.core.infra.web.requests.game.inventory.ItemRequest;
import java.util.*;
import java.util.stream.Collectors;
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

  private ItemEntityMapper itemEntityMapper = new ItemEntityMapper();

  @BeforeEach
  void init() {
    // Create all mocks and inject them into the service
    MockitoAnnotations.openMocks(this);

    inventoryService =
        new InventoryServiceImpl(inventoryRepository, itemRepository, itemEntityMapper);
  }

  /* GET */
  @Test
  void getInventory_Items_on_null_gamesave_id() {
    // Act & Assert
    assertThrows(IllegalArgumentException.class, () -> inventoryService.getInventoryItems(null));
  }

  @Test
  void getInventory_Items_on_non_existing_gamesave_id() {
    // Arrange
    when(inventoryRepository.findById(anyString())).thenReturn(Optional.empty());

    // Assert
    assertThrows(NotFoundException.class, () -> inventoryService.getInventoryItems("1"));
  }

  @Test
  void getInventory_on_existing_gamesave_id_with_empty_inventoryItems() {
    // Arrange
    InventoryEntity inventoryEntity = InventoryEntity.builder().items(new HashSet<>()).build();

    when(inventoryRepository.findById(anyString())).thenReturn(Optional.of(inventoryEntity));

    // Act
    Set<Item> result = inventoryService.getInventoryItems("1");

    // Assert
    assertThat(result).isEmpty();
  }

  @Test
  void getInventory_Items_on_existing_gamesave_id_with_items() {
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

    var list = List.of(itemEntity, itemEntity2);
    var expecteditems = list.stream().map(itemEntityMapper::mapToModel).collect(Collectors.toSet());

    InventoryEntity inventoryEntity = InventoryEntity.builder().items(new HashSet<>(list)).build();

    when(inventoryRepository.findById(anyString())).thenReturn(Optional.of(inventoryEntity));

    // Act
    Set<Item> result = inventoryService.getInventoryItems("1");

    // Assert
    assertThat(result).containsExactly(expecteditems.toArray(new Item[0]));
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
    when(inventoryRepository.save(any())).thenReturn(inventoryEntity);
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
    reset(inventoryRepository, itemRepository);
    InventoryEntity inventoryEntity = InventoryEntity.builder().items(new HashSet<>()).build();

    var now = new Date();
    ItemEntity presentItemEntity =
        ItemEntity.builder()
            .id(UUID.randomUUID().toString())
            .inventoryEntity(inventoryEntity)
            .createdAt(now)
            .updatedAt(now)
            .clientId(UUID.randomUUID().toString())
            .itemType(ItemType.CHESTPLATE)
            .itemRarity(ItemRarity.RARE)
            .isEquipped(false)
            .level(10)
            .mainStat(new ItemStat(ItemStatistic.HEALTH_MULT, 100f))
            .additionalStats(List.of(new ItemStat(ItemStatistic.HEALTH_ADD, 100f)))
            .build();

    inventoryEntity.getItems().add(presentItemEntity);

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
    when(inventoryRepository.save(any())).thenReturn(inventoryEntity);

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
    reset(inventoryRepository, itemRepository);
    GameSaveEntity gameSaveEntity =
        GameSaveEntity.builder().id("1").userEmail("test@test.com").build();
    ItemEntity itemEntity =
        ItemEntity.builder()
            .id("2")
            .clientId("6f27c2a-06e8-4bdb-bf59-56999116f5ef__22222222-2222-2222-2222-222222222222")
            .blueprintId("old_blueprint")
            .itemType(ItemType.BOOTS)
            .itemRarity(ItemRarity.RARE)
            .isEquipped(false)
            .level(10)
            .mainStat(new ItemStat(ItemStatistic.HEALTH_MULT, 100f))
            .additionalStats(List.of(new ItemStat(ItemStatistic.HEALTH_ADD, 100f)))
            .build();

    InventoryEntity inventoryEntity =
        InventoryEntity.builder()
            .items(new HashSet<>(List.of(itemEntity)))
            .gameSave(gameSaveEntity)
            .build();

    itemEntity.setInventoryEntity(inventoryEntity);

    ItemRequest updatedItemRequest =
        new ItemRequest(
            "6f27c2a-06e8-4bdb-bf59-56999116f5ef__22222222-2222-2222-2222-222222222222",
            ItemType.SWORD.getType(),
            "new_blueprint",
            ItemRarity.LEGENDARY.getRarity(),
            true,
            20,
            new ItemStat(ItemStatistic.ATTACK_ADD, 200f),
            List.of(new ItemStat(ItemStatistic.ATTACK_MULT, 3f)));

    when(inventoryRepository.findById(anyString())).thenReturn(Optional.of(inventoryEntity));
    when(itemRepository.findItemEntityByClientId(anyString())).thenReturn(Optional.of(itemEntity));
    when(inventoryRepository.save(any())).thenReturn(inventoryEntity);

    // Act
    inventoryService.updateItemInInventory(
        "1",
        "6f27c2a-06e8-4bdb-bf59-56999116f5ef__22222222-2222-2222-2222-222222222222",
        updatedItemRequest);

    // Assert
    ArgumentCaptor<InventoryEntity> inventoryEntityCaptor =
        ArgumentCaptor.forClass(InventoryEntity.class);
    verify(inventoryRepository).save(inventoryEntityCaptor.capture());

    InventoryEntity capturedInventory = inventoryEntityCaptor.getValue();
    ItemEntity updatedItem =
        capturedInventory.getItems().stream()
            .filter(
                item ->
                    item.getClientId()
                        .equals(
                            "6f27c2a-06e8-4bdb-bf59-56999116f5ef__22222222-2222-2222-2222-222222222222"))
            .findFirst()
            .orElseThrow();

    assertThat(updatedItem.getBlueprintId()).isEqualTo("new_blueprint");
    assertThat(updatedItem.getItemType()).isEqualTo(ItemType.SWORD);
    assertThat(updatedItem.getItemRarity()).isEqualTo(ItemRarity.LEGENDARY);
    assertThat(updatedItem.getIsEquipped()).isTrue();
    assertThat(updatedItem.getLevel()).isEqualTo(20);
    assertThat(updatedItem.getMainStat()).isEqualTo(new ItemStat(ItemStatistic.ATTACK_ADD, 200f));
    assertThat(updatedItem.getAdditionalStats())
        .containsExactly(new ItemStat(ItemStatistic.ATTACK_MULT, 3f));
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
