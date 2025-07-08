package com.lsadf.workflow.game;

import static com.lsadf.workflow.utils.GameSessionUtils.extractGameSaveIdFromSessionId;

import com.lsadf.core.domain.game.characteristics.Characteristics;
import com.lsadf.core.domain.game.currency.Currency;
import com.lsadf.core.domain.game.inventory.item.Item;
import com.lsadf.core.domain.game.stage.Stage;
import com.lsadf.core.infra.web.request.game.inventory.ItemRequest;
import com.lsadf.workflow.activity.game.characteristics.CharacteristicsActivity;
import com.lsadf.workflow.activity.game.currency.CurrencyActivity;
import com.lsadf.workflow.activity.game.inventory.InventoryActivity;
import com.lsadf.workflow.activity.game.stage.StageActivity;
import io.temporal.workflow.Workflow;
import java.util.Set;

/** The GameSessionWorkflowImpl class provides an implementation for the GameSessionWorkflow */
public class GameSessionWorkflowImpl implements GameSessionWorkflow {

  private String sessionId;
  private String gameSaveId;

  private CurrencyActivity currencyActivity;
  private InventoryActivity inventoryActivity;
  private StageActivity stageActivity;
  private CharacteristicsActivity characteristicsActivity;

  public GameSessionWorkflowImpl() {
    this.characteristicsActivity = Workflow.newActivityStub(CharacteristicsActivity.class);
    this.inventoryActivity = Workflow.newActivityStub(InventoryActivity.class);
    this.stageActivity = Workflow.newActivityStub(StageActivity.class);
    this.currencyActivity = Workflow.newActivityStub(CurrencyActivity.class);
  }

  /** {@inheritDoc} */
  @Override
  public void startSession(String sessionId) {
    this.sessionId = sessionId;
    this.gameSaveId = extractGameSaveIdFromSessionId(sessionId);
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
