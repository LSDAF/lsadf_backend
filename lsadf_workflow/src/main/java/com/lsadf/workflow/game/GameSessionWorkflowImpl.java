package com.lsadf.workflow.game;

import com.lsadf.core.domain.game.characteristics.Characteristics;
import com.lsadf.core.domain.game.currency.Currency;
import com.lsadf.core.domain.game.inventory.item.Item;
import com.lsadf.core.domain.game.stage.Stage;
import com.lsadf.core.infra.web.request.game.inventory.ItemRequest;
import com.lsadf.workflow.activity.game.characteristics.CharacteristicsActivity;
import com.lsadf.workflow.activity.game.currency.CurrencyActivity;
import com.lsadf.workflow.activity.game.game_save.GameSaveActivity;
import com.lsadf.workflow.activity.game.inventory.InventoryActivity;
import com.lsadf.workflow.activity.game.stage.StageActivity;
import java.util.Set;

/** The GameSessionWorkflowImpl class provides an implementation for the GameSessionWorkflow */
public class GameSessionWorkflowImpl implements GameSessionWorkflow {

  private final String sessionId;
  private final String gameSaveId;

  private final CurrencyActivity currencyActivity;
  private final InventoryActivity inventoryActivity;
  private final StageActivity stageActivity;
  private final CharacteristicsActivity characteristicsActivity;
  private final GameSaveActivity gameSaveActivity;

  public GameSessionWorkflowImpl(
      String sessionId,
      String gameSaveId,
      CurrencyActivity currencyActivity,
      InventoryActivity inventoryActivity,
      StageActivity stageActivity,
      CharacteristicsActivity characteristicsActivity,
      GameSaveActivity gameSaveActivity) {
    this.sessionId = sessionId;
    this.gameSaveId = gameSaveId;
    this.currencyActivity = currencyActivity;
    this.inventoryActivity = inventoryActivity;
    this.stageActivity = stageActivity;
    this.characteristicsActivity = characteristicsActivity;
    this.gameSaveActivity = gameSaveActivity;
  }

  /** {@inheritDoc} */
  @Override
  public String getSessionId() {
    return sessionId;
  }

  /** {@inheritDoc} */
  @Override
  public void updateCurrency(Currency currency) {
    currencyActivity.updateCurrency(gameSaveId, currency);
  }

  /** {@inheritDoc} */
  @Override
  public void updateCharacteristics(Characteristics characteristics) {
    characteristicsActivity.updateCharacteristics(gameSaveId, characteristics);
  }

  /** {@inheritDoc} */
  @Override
  public void updateStage(Stage stage) {
    stageActivity.updateStage(gameSaveId, stage);
  }

  /** {@inheritDoc} */
  @Override
  public void createItemInInventory(ItemRequest item) {
    inventoryActivity.createItemInInventory(gameSaveId, item);
  }

  /** {@inheritDoc} */
  @Override
  public void updateItemInInventory(ItemRequest item) {
    inventoryActivity.updateItemInInventory(gameSaveId, item.getClientId(), item);
  }

  /** {@inheritDoc} */
  @Override
  public void removeItemFromInventory(String itemClientId) {
    inventoryActivity.removeItemFromInventory(gameSaveId, itemClientId);
  }

  /** {@inheritDoc} */
  @Override
  public Currency getCurrency() {
    return currencyActivity.getCurrency(gameSaveId);
  }

  /** {@inheritDoc} */
  @Override
  public Characteristics getCharacteristics() {
    return characteristicsActivity.getCharacteristics(gameSaveId);
  }

  /** {@inheritDoc} */
  @Override
  public Stage getStage() {
    return stageActivity.getStage(gameSaveId);
  }

  /** {@inheritDoc} */
  @Override
  public String getGameSaveId() {
    return this.gameSaveId;
  }

  /** {@inheritDoc} */
  @Override
  public Set<Item> getInventoryItems() {
    return inventoryActivity.getInventoryItems(gameSaveId);
  }
}
