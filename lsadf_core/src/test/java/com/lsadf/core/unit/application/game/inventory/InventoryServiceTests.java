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

package com.lsadf.core.unit.application.game.inventory;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.lsadf.core.application.game.inventory.InventoryRepositoryPort;
import com.lsadf.core.application.game.inventory.impl.InventoryServiceImpl;
import com.lsadf.core.application.game.save.metadata.GameMetadataService;
import com.lsadf.core.domain.game.inventory.*;
import com.lsadf.core.exception.AlreadyExistingItemClientIdException;
import com.lsadf.core.exception.http.NotFoundException;
import com.lsadf.core.infra.web.dto.common.game.inventory.ItemStatDto;
import com.lsadf.core.infra.web.dto.request.game.inventory.ItemRequest;
import java.util.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class InventoryServiceTests {

  @Mock private InventoryRepositoryPort inventoryRepositoryPort;

  @Mock private GameMetadataService gameMetadataService;

  @InjectMocks private InventoryServiceImpl inventoryService;

  private UUID gameSaveId;
  private String itemClientId;
  private ItemRequest itemRequest;
  private Item mockItem;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    gameSaveId = UUID.randomUUID();
    itemClientId = "client-123";
    itemRequest =
        ItemRequest.builder()
            .clientId(itemClientId)
            .type(ItemType.SWORD.getType())
            .blueprintId("blueprint-123")
            .additionalStats(new ArrayList<>())
            .isEquipped(false)
            .mainStat(new ItemStatDto(ItemStatistic.ATTACK_ADD, 125.4f))
            .rarity(ItemRarity.MYTHIC.getRarity())
            .level(5)
            .build();
    mockItem =
        Item.builder()
            .level(5)
            .clientId(itemClientId)
            .blueprintId("blueprint-123")
            .id(UUID.randomUUID())
            .isEquipped(false)
            .mainStat(new ItemStat(ItemStatistic.CRIT_CHANCE, 0.1f))
            .build();
  }

  @Test
  void test_getInventoryItems_throwsNotFoundException_when_gameSaveIdNotExists() {
    // Arrange
    when(gameMetadataService.existsById(gameSaveId)).thenReturn(false);

    // Act & Assert
    assertThrows(NotFoundException.class, () -> inventoryService.getInventoryItems(gameSaveId));
    verify(gameMetadataService).existsById(gameSaveId);
  }

  @Test
  void test_getInventoryItems_when_success() {
    // Arrange
    Set<Item> expectedItems = Set.of(mockItem);
    when(gameMetadataService.existsById(gameSaveId)).thenReturn(true);
    when(inventoryRepositoryPort.findAllItemsByGameSaveId(gameSaveId)).thenReturn(expectedItems);

    // Act
    Set<Item> result = inventoryService.getInventoryItems(gameSaveId);

    // Assert
    assertEquals(expectedItems, result);
    verify(gameMetadataService).existsById(gameSaveId);
    verify(inventoryRepositoryPort).findAllItemsByGameSaveId(gameSaveId);
  }

  @Test
  void test_createItemInInventory_throwsNotFoundException_when_gameSaveIdNotExists() {
    // Arrange
    when(gameMetadataService.existsById(gameSaveId)).thenReturn(false);

    // Act & Assert
    assertThrows(
        NotFoundException.class,
        () -> inventoryService.createItemInInventory(gameSaveId, itemRequest));
    verify(gameMetadataService).existsById(gameSaveId);
  }

  @Test
  void
      test_createItemInInventory_throwsAlreadyExistingItemClientIdException_when_calledWithExistingClientId() {
    // Arrange
    when(gameMetadataService.existsById(gameSaveId)).thenReturn(true);
    when(inventoryRepositoryPort.existsByClientId(itemClientId)).thenReturn(true);

    // Act & Assert
    assertThrows(
        AlreadyExistingItemClientIdException.class,
        () -> inventoryService.createItemInInventory(gameSaveId, itemRequest));
    verify(gameMetadataService).existsById(gameSaveId);
    verify(inventoryRepositoryPort).existsByClientId(itemClientId);
  }

  @Test
  void test_createItemInInventory_when_success() {
    // Arrange
    when(gameMetadataService.existsById(gameSaveId)).thenReturn(true);
    when(inventoryRepositoryPort.existsByClientId(itemClientId)).thenReturn(false);
    when(inventoryRepositoryPort.createItem(eq(gameSaveId), any(Item.class))).thenReturn(mockItem);

    // Act
    Item result = inventoryService.createItemInInventory(gameSaveId, itemRequest);

    // Assert
    assertEquals(mockItem, result);
    verify(gameMetadataService).existsById(gameSaveId);
    verify(inventoryRepositoryPort).existsByClientId(itemClientId);
    verify(inventoryRepositoryPort).createItem(eq(gameSaveId), any(Item.class));
  }

  @Test
  void test_deleteItemFromInventory_throwsNotFoundException_when_gameSaveIdNotExists() {
    // Arrange
    when(gameMetadataService.existsById(gameSaveId)).thenReturn(false);

    // Act & Assert
    assertThrows(
        NotFoundException.class,
        () -> inventoryService.deleteItemFromInventory(gameSaveId, itemClientId));
    verify(gameMetadataService).existsById(gameSaveId);
  }

  @Test
  void test_deleteItemFromInventory_throwsNotFoundException_when_itemClientIdNotExists() {
    // Arrange
    when(gameMetadataService.existsById(gameSaveId)).thenReturn(true);
    when(inventoryRepositoryPort.existsByClientId(itemClientId)).thenReturn(false);

    // Act & Assert
    assertThrows(
        NotFoundException.class,
        () -> inventoryService.deleteItemFromInventory(gameSaveId, itemClientId));
    verify(gameMetadataService).existsById(gameSaveId);
  }

  @Test
  void test_deleteItemFromInventory_when_success() {
    // Arrange
    when(gameMetadataService.existsById(gameSaveId)).thenReturn(true);
    when(inventoryRepositoryPort.findItemByClientId(itemClientId))
        .thenReturn(Optional.of(mockItem));

    // Act
    inventoryService.deleteItemFromInventory(gameSaveId, itemClientId);

    // Assert
    verify(gameMetadataService).existsById(gameSaveId);
    verify(inventoryRepositoryPort).findItemByClientId(itemClientId);
    verify(inventoryRepositoryPort).deleteItemByClientId(itemClientId);
  }

  @Test
  void test_updateItemInInventory_throwsNotFoundException_when_gameSaveIdNotExists() {
    // Arrange
    when(gameMetadataService.existsById(gameSaveId)).thenReturn(false);

    // Act & Assert
    assertThrows(
        NotFoundException.class,
        () -> inventoryService.updateItemInInventory(gameSaveId, itemClientId, itemRequest));
    verify(gameMetadataService).existsById(gameSaveId);
  }

  @Test
  void test_updateItemInInventory_throwsNotFoundException_when_itemClientIdNotExists() {
    // Arrange
    when(gameMetadataService.existsById(gameSaveId)).thenReturn(true);
    when(inventoryRepositoryPort.existsByClientId(itemClientId)).thenReturn(false);

    // Act & Assert
    assertThrows(
        NotFoundException.class,
        () -> inventoryService.updateItemInInventory(gameSaveId, itemClientId, itemRequest));
    verify(gameMetadataService).existsById(gameSaveId);
  }

  @Test
  void test_updateItemInInventory_when_success() {
    // Arrange
    when(gameMetadataService.existsById(gameSaveId)).thenReturn(true);
    when(inventoryRepositoryPort.findItemByClientId(itemClientId))
        .thenReturn(Optional.of(mockItem));
    when(inventoryRepositoryPort.updateItem(eq(gameSaveId), any(Item.class))).thenReturn(mockItem);

    // Act
    Item result = inventoryService.updateItemInInventory(gameSaveId, itemClientId, itemRequest);

    // Assert
    assertEquals(mockItem, result);
    verify(gameMetadataService).existsById(gameSaveId);
    verify(inventoryRepositoryPort).findItemByClientId(itemClientId);
    verify(inventoryRepositoryPort).updateItem(eq(gameSaveId), any(Item.class));
  }

  @Test
  void test_clearInventory_when_success() {
    // Arrange
    when(gameMetadataService.existsById(gameSaveId)).thenReturn(true);

    // Act
    inventoryService.clearInventory(gameSaveId);

    // Assert
    verify(gameMetadataService).existsById(gameSaveId);
    verify(inventoryRepositoryPort).deleteAllItemsByGameSaveId(gameSaveId);
  }

  @Test
  void test_clearInventory_throwsNotFoundException_when_gameSaveIdNotExists() {
    // Arrange
    when(gameMetadataService.existsById(gameSaveId)).thenReturn(false);

    // Act & Assert
    assertThrows(NotFoundException.class, () -> inventoryService.clearInventory(gameSaveId));
    verify(gameMetadataService).existsById(gameSaveId);
  }
}
