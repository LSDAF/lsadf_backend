package com.lsadf.workflow.activity.game.inventory;

import com.lsadf.core.application.game.inventory.InventoryService;
import com.lsadf.core.domain.game.inventory.item.Item;
import com.lsadf.core.infra.web.request.game.inventory.ItemRequest;
import java.util.Set;

public class InventoryActivityImpl implements InventoryActivity {

  private InventoryService inventoryService;

  public InventoryActivityImpl(InventoryService inventoryService) {
    this.inventoryService = inventoryService;
  }

  @Override
  public Item createItemInInventory(String gameSaveId, ItemRequest itemRequest) {
    return inventoryService.createItemInInventory(gameSaveId, itemRequest);
  }

  @Override
  public void removeItemFromInventory(String gameSaveId, String itemClientId) {
    inventoryService.deleteItemFromInventory(gameSaveId, itemClientId);
  }

  @Override
  public Item updateItemInInventory(
      String gameSaveId, String itemClientId, ItemRequest itemRequest) {
    return inventoryService.updateItemInInventory(gameSaveId, itemClientId, itemRequest);
  }

  @Override
  public Set<Item> getInventoryItems(String gameSaveId) {
    return inventoryService.getInventoryItems(gameSaveId);
  }
}
