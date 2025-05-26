/*
 * Copyright 2024-2025 LSDAF
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package com.lsadf.core.infra.persistence.config.mappers;

import com.lsadf.core.domain.game.GameSave;
import com.lsadf.core.domain.game.characteristics.Characteristics;
import com.lsadf.core.domain.game.currency.Currency;
import com.lsadf.core.domain.game.inventory.Inventory;
import com.lsadf.core.domain.game.inventory.item.Item;
import com.lsadf.core.domain.game.stage.Stage;
import com.lsadf.core.domain.user.User;
import com.lsadf.core.infra.persistence.game.*;
import com.lsadf.core.infra.web.requests.admin.AdminUserCreationRequest;
import com.lsadf.core.infra.web.requests.characteristics.CharacteristicsRequest;
import com.lsadf.core.infra.web.requests.currency.CurrencyRequest;
import com.lsadf.core.infra.web.requests.game.stage.StageRequest;
import com.lsadf.core.infra.web.requests.user.UserCreationRequest;
import org.keycloak.representations.idm.UserRepresentation;

public interface Mapper {
  /**
   * Maps GameSaveEntity to GameSave
   *
   * @param gameSaveEntity GameSaveEntity
   * @return
   */
  GameSave mapGameSaveEntityToGameSave(GameSaveEntity gameSaveEntity);

  /**
   * Maps CharacteristicsRequest to Characteristics
   *
   * @param characteristicsRequest CharacteristicsRequest
   * @return Characteristics
   */
  Characteristics mapCharacteristicsRequestToCharacteristics(
      CharacteristicsRequest characteristicsRequest);

  /**
   * Maps CharacteristicsEntity to Characteristics
   *
   * @param characteristicsEntity CharacteristicsEntity
   * @return Characteristics
   */
  Characteristics mapCharacteristicsEntityToCharacteristics(
      CharacteristicsEntity characteristicsEntity);

  /**
   * Maps CurrencyRequest to Currency
   *
   * @param currencyRequest CurrencyRequest
   * @return Currency
   */
  Currency mapCurrencyRequestToCurrency(CurrencyRequest currencyRequest);

  /**
   * Maps Currency to CurrencyEntity
   *
   * @param currencyEntity CurrencyEntity
   * @return CurrencyEntity
   */
  Currency mapCurrencyEntityToCurrency(CurrencyEntity currencyEntity);

  /**
   * Maps Inventory to InventoryEntity
   *
   * @param inventoryEntity InventoryEntity
   * @return InventoryEntity
   */
  Inventory mapInventoryEntityToInventory(InventoryEntity inventoryEntity);

  /**
   * Maps ItemEntity to Item
   *
   * @param itemEntity ItemEntity
   * @return Item
   */
  Item mapItemEntityToItem(ItemEntity itemEntity);

  /**
   * Maps StageEntity to Stage
   *
   * @param stageEntity StageEntity
   * @return Stage
   */
  Stage mapStageEntityToStage(StageEntity stageEntity);

  /**
   * Maps StageRequest to Stage
   *
   * @param stageRequest StageRequest
   * @return Stage
   */
  Stage mapStageRequestToStage(StageRequest stageRequest);

  /**
   * Maps Keycloak UserRepresentation to User
   *
   * @param userRepresentation UserRepresentation
   * @return User
   */
  User mapUserRepresentationToUser(UserRepresentation userRepresentation);

  /**
   * Maps UserCreationRequest to Keycloak UserRepresentation
   *
   * @param userCreationRequest UserCreationRequest
   * @return UserRepresentation
   */
  UserRepresentation mapUserCreationRequestToUserRepresentation(
      UserCreationRequest userCreationRequest);

  /**
   * Maps AdminUserCreationRequest to UserCreationRequest
   *
   * @param adminUserCreationRequest AdminUserCreationRequest
   * @return UserCreationRequest
   */
  UserCreationRequest mapAdminUserCreationRequestToUserCreationRequest(
      AdminUserCreationRequest adminUserCreationRequest);
}
