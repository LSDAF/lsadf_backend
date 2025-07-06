package com.lsadf.workflow.activity.game.inventory;

import com.lsadf.core.domain.game.inventory.item.Item;
import com.lsadf.core.infra.web.request.game.inventory.ItemRequest;
import io.temporal.activity.ActivityInterface;
import io.temporal.activity.ActivityMethod;
import java.util.Set;

@ActivityInterface
public interface InventoryActivity {
  @ActivityMethod
  Item createItemInInventory(String gameSaveId, ItemRequest itemRequest);

  @ActivityMethod
  void removeItemFromInventory(String gameSaveId, String itemClientId);

  @ActivityMethod
  Item updateItemInInventory(String gameSaveId, String itemClientId, ItemRequest itemRequest);

  @ActivityMethod
  Set<Item> getInventoryItems(String gameSaveId);
}
