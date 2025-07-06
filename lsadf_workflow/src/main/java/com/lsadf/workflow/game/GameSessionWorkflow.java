package com.lsadf.workflow.game;

import com.lsadf.core.domain.game.characteristics.Characteristics;
import com.lsadf.core.domain.game.currency.Currency;
import com.lsadf.core.domain.game.inventory.item.Item;
import com.lsadf.core.domain.game.stage.Stage;
import com.lsadf.core.infra.web.request.game.inventory.ItemRequest;
import io.temporal.workflow.QueryMethod;
import io.temporal.workflow.SignalMethod;
import io.temporal.workflow.WorkflowInterface;
import java.util.Set;

/**
 * The GameSessionWorkflow interface defines a set of methods that represent the workflow for
 * managing game session activities, such as updating or retrieving session-related information
 * including currency, characteristics, stage, inventory, and unique identifiers.
 */
@WorkflowInterface
public interface GameSessionWorkflow {

  /**
   * Updates the currency details associated with the current game session.
   *
   * @param currency a {@code Currency} object containing the updated currency details, including
   *     values for gold, diamond, emerald, and amethyst
   */
  @SignalMethod
  void updateCurrency(Currency currency);

  /**
   * Updates the characteristics associated with the current game session. This method modifies the
   * attributes related to the characteristics such as attack, critical chance, critical damage,
   * health, and resistance.
   *
   * @param characteristics a {@code Characteristics} object representing the updated characteristic
   *     attributes to be applied to the game session
   */
  @SignalMethod
  void updateCharacteristics(Characteristics characteristics);

  /**
   * Updates the stage information associated with the current game session. This method is used to
   * modify the current and/or maximum stage of the game session.
   *
   * @param stage a {@code Stage} object containing the updated stage details, including the current
   *     and maximum stage values
   */
  @SignalMethod
  void updateStage(Stage stage);

  /**
   * Adds a new item to the inventory associated with the current game session. The details of the
   * item to be created are specified in the {@code item} object.
   *
   * @param item an {@code ItemRequest} object containing information about the item to be added,
   *     including its client-generated identifier, type, blueprint ID, rarity, equipped status,
   *     level, main stat, and additional stats
   */
  @SignalMethod
  void createItemInInventory(ItemRequest item);

  /**
   * Updates an existing item in the inventory associated with a game session. The item to be
   * updated is identified by its client-generated identifier, and its details are modified as
   * specified in the {@code item} request object.
   *
   * @param item an {@code ItemRequest} object containing the updated details of the item to be
   *     modified, including its client-generated identifier, type, blueprint ID, rarity, equipped
   *     status, level, main stat, and additional stats
   */
  @SignalMethod
  void updateItemInInventory(ItemRequest item);

  /**
   * Removes a specific item from the inventory associated with a game session.
   *
   * @param itemClientId the unique client identifier of the item to be removed from the inventory
   */
  @SignalMethod
  void removeItemFromInventory(String itemClientId);

  /**
   * Retrieves the unique identifier associated with the current game session.
   *
   * @return a string representing the unique identifier of the session
   */
  @QueryMethod
  String getSessionId();

  /**
   * Retrieves the currency details associated with the game session.
   *
   * @return a {@code Currency} object containing the currency details, which include gold, diamond,
   *     emerald, and amethyst values
   */
  @QueryMethod
  Currency getCurrency();

  /**
   * Retrieves the characteristics associated with the game session.
   *
   * @return a {@code Characteristics} object containing the attributes such as attack, critical
   *     chance, critical damage, health, and resistance for the game session
   */
  @QueryMethod
  Characteristics getCharacteristics();

  /**
   * Retrieves the current stage information for the game session.
   *
   * @return a {@code Stage} object representing the current and maximum stages of the game session
   */
  @QueryMethod
  Stage getStage();

  /**
   * Retrieves the unique identifier associated with the game save for the current session.
   *
   * @return a string representing the unique identifier of the game save
   */
  @QueryMethod
  String getGameSaveId();

  /**
   * Retrieves the set of inventory items currently associated with the game session.
   *
   * @return a set containing the inventory items, represented as {@code Item} objects, for the game
   *     session
   */
  @QueryMethod
  Set<Item> getInventoryItems();
}
