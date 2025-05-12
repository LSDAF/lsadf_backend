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
package com.lsadf.core.mappers.impl;

import com.lsadf.core.entities.*;
import com.lsadf.core.mappers.Mapper;
import com.lsadf.core.models.*;
import com.lsadf.core.requests.admin.AdminUserCreationRequest;
import com.lsadf.core.requests.characteristics.CharacteristicsRequest;
import com.lsadf.core.requests.currency.CurrencyRequest;
import com.lsadf.core.requests.stage.StageRequest;
import com.lsadf.core.requests.user.UserCreationRequest;
import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.NoArgsConstructor;
import org.keycloak.representations.idm.UserRepresentation;

@NoArgsConstructor
public class MapperImpl implements Mapper {

  /** {@inheritDoc} */
  @Override
  public Characteristics mapCharacteristicsRequestToCharacteristics(
      CharacteristicsRequest characteristicsRequest) {
    return new Characteristics(
        characteristicsRequest.getAttack(),
        characteristicsRequest.getCritChance(),
        characteristicsRequest.getCritDamage(),
        characteristicsRequest.getHealth(),
        characteristicsRequest.getResistance());
  }

  /** {@inheritDoc} */
  @Override
  public Characteristics mapCharacteristicsEntityToCharacteristics(
      CharacteristicsEntity characteristicsEntity) {
    return new Characteristics(
        characteristicsEntity.getAttack(),
        characteristicsEntity.getCritChance(),
        characteristicsEntity.getCritDamage(),
        characteristicsEntity.getHealth(),
        characteristicsEntity.getResistance());
  }

  /** {@inheritDoc} */
  @Override
  public Currency mapCurrencyRequestToCurrency(CurrencyRequest currencyRequest) {
    return new Currency(
        currencyRequest.getGold(),
        currencyRequest.getDiamond(),
        currencyRequest.getEmerald(),
        currencyRequest.getAmethyst());
  }

  /** {@inheritDoc} */
  @Override
  public GameSave mapGameSaveEntityToGameSave(GameSaveEntity gameSaveEntity) {
    Stage stage = mapStageEntityToStage(gameSaveEntity.getStageEntity());
    Characteristics characteristics =
        mapCharacteristicsEntityToCharacteristics(gameSaveEntity.getCharacteristicsEntity());
    Currency currency = mapCurrencyEntityToCurrency(gameSaveEntity.getCurrencyEntity());

    return GameSave.builder()
        .id(gameSaveEntity.getId())
        .userEmail(gameSaveEntity.getUserEmail())
        .nickname(gameSaveEntity.getNickname())
        .characteristics(characteristics)
        .currency(currency)
        .stage(stage)
        .createdAt(gameSaveEntity.getCreatedAt())
        .updatedAt(gameSaveEntity.getUpdatedAt())
        .build();
  }

  /** {@inheritDoc} */
  @Override
  public Currency mapCurrencyEntityToCurrency(CurrencyEntity currencyEntity) {
    return new Currency(
        currencyEntity.getGoldAmount(),
        currencyEntity.getDiamondAmount(),
        currencyEntity.getEmeraldAmount(),
        currencyEntity.getAmethystAmount());
  }

  /** {@inheritDoc} */
  @Override
  public Inventory mapInventoryEntityToInventory(InventoryEntity inventoryEntity) {
    Set<Item> items =
        inventoryEntity.getItems().stream()
            .map(this::mapItemEntityToItem)
            .collect(Collectors.toSet());

    return new Inventory(items);
  }

  /** {@inheritDoc} */
  @Override
  public Item mapItemEntityToItem(ItemEntity itemEntity) {
    return Item.builder()
        .id(itemEntity.getId())
        .clientId(itemEntity.getClientId())
        .blueprintId(itemEntity.getBlueprintId())
        .itemType(itemEntity.getItemType())
        .itemRarity(itemEntity.getItemRarity())
        .isEquipped(itemEntity.getIsEquipped())
        .level(itemEntity.getLevel())
        .mainStat(itemEntity.getMainStat())
        .additionalStats(itemEntity.getAdditionalStats())
        .build();
  }

  /** {@inheritDoc} */
  @Override
  public Stage mapStageEntityToStage(StageEntity stageEntity) {
    return Stage.builder()
        .maxStage(stageEntity.getMaxStage())
        .currentStage(stageEntity.getCurrentStage())
        .build();
  }

  /** {@inheritDoc} */
  @Override
  public Stage mapStageRequestToStage(StageRequest stageRequest) {
    return Stage.builder()
        .maxStage(stageRequest.getMaxStage())
        .currentStage(stageRequest.getCurrentStage())
        .build();
  }

  /** {@inheritDoc} */
  @Override
  public User mapUserRepresentationToUser(UserRepresentation userRepresentation) {
    Date createdTimestamp =
        (userRepresentation.getCreatedTimestamp() != null)
            ? new Date(userRepresentation.getCreatedTimestamp())
            : null;
    return User.builder()
        .username(userRepresentation.getUsername())
        .firstName(userRepresentation.getFirstName())
        .lastName(userRepresentation.getLastName())
        .id(userRepresentation.getId())
        .emailVerified(userRepresentation.isEmailVerified())
        .enabled(userRepresentation.isEnabled())
        .createdTimestamp(createdTimestamp)
        .userRoles(userRepresentation.getRealmRoles())
        .build();
  }

  /** {@inheritDoc} */
  @Override
  public UserRepresentation mapUserCreationRequestToUserRepresentation(
      UserCreationRequest userCreationRequest) {
    UserRepresentation userRepresentation = new UserRepresentation();
    userRepresentation.setUsername(userCreationRequest.getUsername());
    userRepresentation.setFirstName(userCreationRequest.getFirstName());
    userRepresentation.setLastName(userCreationRequest.getLastName());
    userRepresentation.setEmailVerified(userCreationRequest.isEmailVerified());
    userRepresentation.setEnabled(userCreationRequest.isEnabled());
    return userRepresentation;
  }

  /** {@inheritDoc} */
  @Override
  public UserCreationRequest mapAdminUserCreationRequestToUserCreationRequest(
      AdminUserCreationRequest adminUserCreationRequest) {
    return UserCreationRequest.builder()
        .username(adminUserCreationRequest.getUsername())
        .firstName(adminUserCreationRequest.getFirstName())
        .lastName(adminUserCreationRequest.getLastName())
        .emailVerified(adminUserCreationRequest.getEmailVerified())
        .userRoles(adminUserCreationRequest.getUserRoles())
        .enabled(adminUserCreationRequest.getEnabled())
        .build();
  }
}
