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
package com.lsadf.admin.application.bdd;

import com.lsadf.core.domain.game.GameSave;
import com.lsadf.core.domain.game.characteristics.Characteristics;
import com.lsadf.core.domain.game.currency.Currency;
import com.lsadf.core.domain.game.inventory.item.ItemRarity;
import com.lsadf.core.domain.game.inventory.item.ItemStat;
import com.lsadf.core.domain.game.inventory.item.ItemStatistic;
import com.lsadf.core.domain.game.inventory.item.ItemType;
import com.lsadf.core.domain.game.stage.Stage;
import com.lsadf.core.domain.user.User;
import com.lsadf.core.infra.persistence.game.characteristics.CharacteristicsEntity;
import com.lsadf.core.infra.persistence.game.currency.CurrencyEntity;
import com.lsadf.core.infra.persistence.game.game_save.GameSaveEntity;
import com.lsadf.core.infra.persistence.game.inventory.InventoryEntity;
import com.lsadf.core.infra.persistence.game.inventory.item.ItemEntity;
import com.lsadf.core.infra.persistence.game.stage.StageEntity;
import com.lsadf.core.infra.web.request.common.Filter;
import com.lsadf.core.infra.web.request.game.characteristics.CharacteristicsRequest;
import com.lsadf.core.infra.web.request.game.currency.CurrencyRequest;
import com.lsadf.core.infra.web.request.game.game_save.creation.AdminGameSaveCreationRequest;
import com.lsadf.core.infra.web.request.game.game_save.update.AdminGameSaveUpdateRequest;
import com.lsadf.core.infra.web.request.game.game_save.update.GameSaveNicknameUpdateRequest;
import com.lsadf.core.infra.web.request.game.inventory.ItemRequest;
import com.lsadf.core.infra.web.request.game.stage.StageRequest;
import com.lsadf.core.infra.web.request.user.creation.AdminUserCreationRequest;
import com.lsadf.core.infra.web.request.user.creation.SimpleUserCreationRequest;
import com.lsadf.core.infra.web.request.user.login.UserLoginRequest;
import com.lsadf.core.infra.web.request.user.login.UserRefreshLoginRequest;
import com.lsadf.core.infra.web.request.user.update.AdminUserUpdateRequest;
import com.lsadf.core.infra.web.request.user.update.SimpleUserUpdateRequest;
import com.lsadf.core.infra.web.response.game.characteristics.CharacteristicsResponse;
import com.lsadf.core.infra.web.response.game.currency.CurrencyResponse;
import com.lsadf.core.infra.web.response.game.game_save.GameSaveResponse;
import com.lsadf.core.infra.web.response.game.inventory.ItemResponse;
import com.lsadf.core.infra.web.response.game.stage.StageResponse;
import com.lsadf.core.infra.web.response.info.GlobalInfoResponse;
import com.lsadf.core.infra.web.response.user.UserResponse;
import java.io.IOException;
import java.time.Instant;
import java.util.*;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import lombok.experimental.UtilityClass;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

/** Utility class for BDD tests */
@UtilityClass
public class BddUtils {

  private static final String COMMA = ",";

  /**
   * Maps a row from a BDD table to a StageResponse object. Extracts "CURRENT_STAGE" and "MAX_STAGE"
   * values from the provided map, converts them to long, and builds a StageResponse instance.
   *
   * @param row the map containing stage data with keys representing field names
   * @return a StageResponse object constructed using the values from the provided map
   * @throws IllegalArgumentException if either "CURRENT_STAGE" or "MAX_STAGE" is null
   * @throws NumberFormatException if the values for "CURRENT_STAGE" or "MAX_STAGE" cannot be parsed
   *     to long
   */
  public static StageResponse mapToStageResponse(Map<String, String> row) {
    String currentStage = row.get(BddFieldConstants.Stage.CURRENT_STAGE);
    String maxStage = row.get(BddFieldConstants.Stage.MAX_STAGE);

    if (currentStage == null || maxStage == null) {
      throw new IllegalArgumentException("Current and maximum stage cannot be null");
    }

    long currentStageLong = Long.parseLong(currentStage);
    long maxStageLong = Long.parseLong(maxStage);

    return StageResponse.builder().currentStage(currentStageLong).maxStage(maxStageLong).build();
  }

  /**
   * Maps a row from a BDD table to a CurrencyResponse object. Extracts currency fields such as
   * "GOLD", "DIAMOND", "EMERALD", and "AMETHYST" from the provided map, converts them to Long
   * values if present, and constructs a CurrencyResponse instance.
   *
   * @param row the map containing currency data with keys representing field names
   * @return a CurrencyResponse object constructed using the currency values from the provided map
   */
  public static CurrencyResponse mapToCurrencyResponse(Map<String, String> row) {
    String gold = row.get(BddFieldConstants.Currency.GOLD);
    String diamond = row.get(BddFieldConstants.Currency.DIAMOND);
    String emerald = row.get(BddFieldConstants.Currency.EMERALD);
    String amethyst = row.get(BddFieldConstants.Currency.AMETHYST);

    Long goldLong = gold == null ? null : Long.parseLong(gold);
    Long diamondLong = diamond == null ? null : Long.parseLong(diamond);
    Long emeraldLong = emerald == null ? null : Long.parseLong(emerald);
    Long amethystLong = amethyst == null ? null : Long.parseLong(amethyst);

    return new CurrencyResponse(goldLong, diamondLong, emeraldLong, amethystLong);
  }

  /**
   * Maps a row of characteristics data to a {@code CharacteristicsResponse} object.
   *
   * @param row a map where keys represent characteristics fields and values represent the
   *     corresponding data as strings
   * @return a {@code CharacteristicsResponse} object containing parsed and mapped characteristics
   *     data, or null for fields that are absent or cannot be parsed
   */
  public static CharacteristicsResponse mapToCharacteristicsResponse(Map<String, String> row) {
    String attack = row.get(BddFieldConstants.Characteristics.ATTACK);
    String critChance = row.get(BddFieldConstants.Characteristics.CRIT_CHANCE);
    String critDamage = row.get(BddFieldConstants.Characteristics.CRIT_DAMAGE);
    String health = row.get(BddFieldConstants.Characteristics.HEALTH);
    String resistance = row.get(BddFieldConstants.Characteristics.RESISTANCE);

    Long attackLong = attack == null ? null : Long.parseLong(attack);
    Long critChanceLong = critChance == null ? null : Long.parseLong(critChance);
    Long critDamageLong = critDamage == null ? null : Long.parseLong(critDamage);
    Long healthLong = health == null ? null : Long.parseLong(health);
    Long resistanceLong = resistance == null ? null : Long.parseLong(resistance);

    return new CharacteristicsResponse(
        attackLong, critChanceLong, critDamageLong, healthLong, resistanceLong);
  }

  /**
   * Maps a row from a BDD table to a GameSaveResponse object. Extracts fields such as ID, user
   * email, nickname, characteristics, currency, and stage from the provided map and constructs a
   * GameSaveResponse instance.
   *
   * @param row a map representing a row from the BDD table, where keys correspond to field names
   *     and values represent the field values
   * @return a GameSaveResponse object constructed using the values extracted from the provided map
   */
  public static GameSaveResponse mapToGameSaveResponse(Map<String, String> row) {
    String id = row.get(BddFieldConstants.GameSave.ID);
    String userEmail = row.get(BddFieldConstants.GameSave.USER_EMAIL);
    String nickname = row.get(BddFieldConstants.GameSave.NICKNAME);

    StageResponse stageResponse = mapToStageResponse(row);
    CurrencyResponse currencyResponse = mapToCurrencyResponse(row);
    CharacteristicsResponse characteristicsResponse = mapToCharacteristicsResponse(row);
    return GameSaveResponse.builder()
        .userEmail(userEmail)
        .nickname(nickname)
        .id(id)
        .characteristics(characteristicsResponse)
        .currency(currencyResponse)
        .stage(stageResponse)
        .build();
  }

  /**
   * Maps a row from a BDD table to a CharacteristicsRequest
   *
   * @param row row from BDD table
   * @return CharacteristicsRequest
   */
  public static CharacteristicsRequest mapToCharacteristicsRequest(Map<String, String> row) {
    String attack = row.get(BddFieldConstants.Characteristics.ATTACK);
    String critChance = row.get(BddFieldConstants.Characteristics.CRIT_CHANCE);
    String critDamage = row.get(BddFieldConstants.Characteristics.CRIT_DAMAGE);
    String health = row.get(BddFieldConstants.Characteristics.HEALTH);
    String resistance = row.get(BddFieldConstants.Characteristics.RESISTANCE);

    long attackLong = attack == null ? 0 : Long.parseLong(attack);
    long critChanceLong = critChance == null ? 0 : Long.parseLong(critChance);
    long critDamageLong = critDamage == null ? 0 : Long.parseLong(critDamage);
    long healthLong = health == null ? 0 : Long.parseLong(health);
    long resistanceLong = resistance == null ? 0 : Long.parseLong(resistance);

    return new CharacteristicsRequest(
        attackLong, critChanceLong, critDamageLong, healthLong, resistanceLong);
  }

  /**
   * Maps a row from a BDD table to a CurrencyRequest
   *
   * @param row row from BDD table
   * @return CurrencyRequest
   */
  public static CurrencyRequest mapToCurrencyRequest(Map<String, String> row) {
    String gold = row.get(BddFieldConstants.Currency.GOLD);
    String diamond = row.get(BddFieldConstants.Currency.DIAMOND);
    String emerald = row.get(BddFieldConstants.Currency.EMERALD);
    String amethyst = row.get(BddFieldConstants.Currency.AMETHYST);

    long goldLong = gold == null ? 0 : Long.parseLong(gold);
    long diamondLong = diamond == null ? 0 : Long.parseLong(diamond);
    long emeraldLong = emerald == null ? 0 : Long.parseLong(emerald);
    long amethystLong = amethyst == null ? 0 : Long.parseLong(amethyst);

    return new CurrencyRequest(goldLong, diamondLong, emeraldLong, amethystLong);
  }

  /**
   * Maps a row from a BDD table to a StageRequest
   *
   * @param row row from BDD table
   * @return StageRequest
   */
  public static StageRequest mapToStageRequest(Map<String, String> row) {
    String currentStage = row.get(BddFieldConstants.Stage.CURRENT_STAGE);
    String maxStage = row.get(BddFieldConstants.Stage.MAX_STAGE);

    long currentStageLong = Long.parseLong(currentStage);
    long maxStageLong = Long.parseLong(maxStage);

    return new StageRequest(currentStageLong, maxStageLong);
  }

  /**
   * Maps a row from a BDD table to a GameSaveEntity
   *
   * @param row row from BDD table
   * @return GameSaveEntity
   */
  public static GameSaveEntity mapToGameSaveEntity(Map<String, String> row) {
    String id = row.get(BddFieldConstants.GameSave.ID);
    String userEmail = row.get(BddFieldConstants.GameSave.USER_EMAIL);
    String nickname = row.get(BddFieldConstants.GameSave.NICKNAME);
    String gold = row.get(BddFieldConstants.Currency.GOLD);
    String diamond = row.get(BddFieldConstants.Currency.DIAMOND);
    String emerald = row.get(BddFieldConstants.Currency.EMERALD);
    String amethyst = row.get(BddFieldConstants.Currency.AMETHYST);
    String attack = row.get(BddFieldConstants.Characteristics.ATTACK);
    String critChance = row.get(BddFieldConstants.Characteristics.CRIT_CHANCE);
    String critDamage = row.get(BddFieldConstants.Characteristics.CRIT_DAMAGE);
    String health = row.get(BddFieldConstants.Characteristics.HEALTH);
    String resistance = row.get(BddFieldConstants.Characteristics.RESISTANCE);
    String currentStageString = row.get(BddFieldConstants.GameSave.CURRENT_STAGE);
    String maxStageString = row.get(BddFieldConstants.GameSave.MAX_STAGE);

    long goldLong = Long.parseLong(gold);
    long diamondLong = Long.parseLong(diamond);
    long emeraldLong = Long.parseLong(emerald);
    long amethystLong = Long.parseLong(amethyst);
    long attackLong = Long.parseLong(attack);
    long critChanceLong = Long.parseLong(critChance);
    long critDamageLong = Long.parseLong(critDamage);
    long healthLong = Long.parseLong(health);
    long resistanceLong = Long.parseLong(resistance);
    long currentStage = Long.parseLong(currentStageString);
    long maxStage = Long.parseLong(maxStageString);

    GameSaveEntity gameSaveEntity =
        GameSaveEntity.builder().userEmail(userEmail).nickname(nickname).id(id).build();

    CharacteristicsEntity characteristicsEntity =
        CharacteristicsEntity.builder()
            .gameSave(gameSaveEntity)
            .attack(attackLong)
            .critChance(critChanceLong)
            .critDamage(critDamageLong)
            .health(healthLong)
            .resistance(resistanceLong)
            .build();

    gameSaveEntity.setCharacteristicsEntity(characteristicsEntity);

    CurrencyEntity currencyEntity =
        CurrencyEntity.builder()
            .id(id)
            .gameSave(gameSaveEntity)
            .goldAmount(goldLong)
            .diamondAmount(diamondLong)
            .emeraldAmount(emeraldLong)
            .amethystAmount(amethystLong)
            .userEmail(userEmail)
            .build();

    gameSaveEntity.setCurrencyEntity(currencyEntity);

    StageEntity stageEntity =
        StageEntity.builder()
            .id(id)
            .currentStage(currentStage)
            .maxStage(maxStage)
            .gameSave(gameSaveEntity)
            .build();

    gameSaveEntity.setStageEntity(stageEntity);

    InventoryEntity inventoryEntity =
        InventoryEntity.builder().id(id).gameSave(gameSaveEntity).items(new HashSet<>()).build();

    gameSaveEntity.setInventoryEntity(inventoryEntity);

    return gameSaveEntity;
  }

  /**
   * Initializes the RestTemplate inside TestRestTemplate
   *
   * @param testRestTemplate the TestRestTemplate
   */
  public void initTestRestTemplate(TestRestTemplate testRestTemplate) {
    RestTemplate template = testRestTemplate.getRestTemplate();
    template.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
    template.setErrorHandler(
        new DefaultResponseErrorHandler() {
          @Override
          public boolean hasError(ClientHttpResponse response) throws IOException {
            HttpStatus status = HttpStatus.resolve(response.getStatusCode().value());
            return status.series() == HttpStatus.Series.SERVER_ERROR;
          }
        });
  }

  /**
   * Maps a row from a BDD table to an AdminGameSaveCreationRequest
   *
   * @param row row from BDD table
   * @return AdminGameSaveCreationRequest
   */
  public static AdminGameSaveCreationRequest mapToAdminGameSaveCreationRequest(
      Map<String, String> row) {
    String goldString = row.get(BddFieldConstants.Currency.GOLD);
    String diamondString = row.get(BddFieldConstants.Currency.DIAMOND);
    String emeraldString = row.get(BddFieldConstants.Currency.EMERALD);
    String amethystString = row.get(BddFieldConstants.Currency.AMETHYST);
    String attackString = row.get(BddFieldConstants.Characteristics.ATTACK);
    String critChanceString = row.get(BddFieldConstants.Characteristics.CRIT_CHANCE);
    String critDamageString = row.get(BddFieldConstants.Characteristics.CRIT_DAMAGE);
    String healthString = row.get(BddFieldConstants.Characteristics.HEALTH);
    String resistanceString = row.get(BddFieldConstants.Characteristics.RESISTANCE);
    String currentStageString = row.get(BddFieldConstants.GameSave.CURRENT_STAGE);
    String maxStageString = row.get(BddFieldConstants.GameSave.MAX_STAGE);
    String nickname = row.get(BddFieldConstants.GameSave.NICKNAME);

    Long gold = Long.parseLong(goldString);
    Long diamond = Long.parseLong(diamondString);
    Long emerald = Long.parseLong(emeraldString);
    Long amethyst = Long.parseLong(amethystString);
    Long attack = Long.parseLong(attackString);
    Long critChance = Long.parseLong(critChanceString);
    Long critDamage = Long.parseLong(critDamageString);
    Long health = Long.parseLong(healthString);
    Long resistance = Long.parseLong(resistanceString);
    Long currentStage = Long.parseLong(currentStageString);
    Long maxStage = Long.parseLong(maxStageString);

    String id = row.get(BddFieldConstants.GameSave.ID);
    String userEmail = row.get(BddFieldConstants.GameSave.USER_EMAIL);

    StageRequest stageRequest = new StageRequest(currentStage, maxStage);
    CharacteristicsRequest characteristicsRequest =
        new CharacteristicsRequest(attack, critChance, critDamage, health, resistance);
    CurrencyRequest currencyRequest = new CurrencyRequest(gold, diamond, emerald, amethyst);

    return AdminGameSaveCreationRequest.builder()
        .nickname(nickname)
        .id(id)
        .stage(stageRequest)
        .characteristics(characteristicsRequest)
        .currency(currencyRequest)
        .userEmail(userEmail)
        .build();
  }

  /**
   * Maps a row from a BDD table to a User
   *
   * @param row row from BDD table
   * @return User
   */
  public static User mapToUser(Map<String, String> row) {
    String email = row.get(BddFieldConstants.User.USERNAME);
    String firstName = row.get(BddFieldConstants.User.FIRST_NAME);
    String lastName = row.get(BddFieldConstants.User.LAST_NAME);
    String enabled = row.get(BddFieldConstants.User.ENABLED);
    String verified = row.get(BddFieldConstants.User.EMAIL_VERIFIED);
    String userRoles = row.get(BddFieldConstants.User.ROLES);

    List<String> roles = Arrays.stream(userRoles.split(COMMA)).toList();

    boolean enabledBoolean = Boolean.parseBoolean(enabled);
    boolean verifiedBoolean = Boolean.parseBoolean(verified);

    return User.builder()
        .username(email)
        .firstName(firstName)
        .lastName(lastName)
        .enabled(enabledBoolean)
        .emailVerified(verifiedBoolean)
        .userRoles(roles)
        .build();
  }

  /**
   * Maps a row of user data represented as a map to a UserResponse object.
   *
   * @param row a map containing user data with keys corresponding to user attributes such as
   *     username, first name, last name, enabled status, email verification status, and roles.
   * @return a UserResponse object constructed using the provided user data from the map.
   */
  public static UserResponse mapToUserResponse(Map<String, String> row) {
    String email = row.get(BddFieldConstants.User.USERNAME);
    String firstName = row.get(BddFieldConstants.User.FIRST_NAME);
    String lastName = row.get(BddFieldConstants.User.LAST_NAME);
    String enabled = row.get(BddFieldConstants.User.ENABLED);
    String verified = row.get(BddFieldConstants.User.EMAIL_VERIFIED);
    String userRoles = row.get(BddFieldConstants.User.ROLES);

    List<String> roles = Arrays.stream(userRoles.split(COMMA)).toList();

    boolean enabledBoolean = Boolean.parseBoolean(enabled);
    boolean verifiedBoolean = Boolean.parseBoolean(verified);

    return UserResponse.builder()
        .username(email)
        .firstName(firstName)
        .lastName(lastName)
        .enabled(enabledBoolean)
        .emailVerified(verifiedBoolean)
        .userRoles(roles)
        .build();
  }

  /**
   * Maps a row from a BDD table to a GameSave
   *
   * @param row row from BDD table
   * @return GameSave
   */
  public static GameSave mapToGameSave(Map<String, String> row) {
    String id = row.get(BddFieldConstants.GameSave.ID);
    String userEmail = row.get(BddFieldConstants.GameSave.USER_EMAIL);
    String nickname = row.get(BddFieldConstants.GameSave.NICKNAME);

    Characteristics characteristics = mapToCharacteristics(row);
    Currency currency = mapToCurrency(row);
    Stage stage = mapToStage(row);

    return GameSave.builder()
        .userEmail(userEmail)
        .nickname(nickname)
        .id(id)
        .characteristics(characteristics)
        .currency(currency)
        .stage(stage)
        .build();
  }

  /**
   * Maps a row from a BDD table to a Characteristics POJO
   *
   * @param row row from BDD table
   * @return Characteristics
   */
  public static Characteristics mapToCharacteristics(Map<String, String> row) {
    String attack = row.get(BddFieldConstants.Characteristics.ATTACK);
    String critChance = row.get(BddFieldConstants.Characteristics.CRIT_CHANCE);
    String critDamage = row.get(BddFieldConstants.Characteristics.CRIT_DAMAGE);
    String health = row.get(BddFieldConstants.Characteristics.HEALTH);
    String resistance = row.get(BddFieldConstants.Characteristics.RESISTANCE);

    long attackLong = attack == null ? 1 : Long.parseLong(attack);
    long critChanceLong = critChance == null ? 1 : Long.parseLong(critChance);
    long critDamageLong = critDamage == null ? 1 : Long.parseLong(critDamage);
    long healthLong = health == null ? 1 : Long.parseLong(health);
    long resistanceLong = resistance == null ? 1 : Long.parseLong(resistance);

    return new Characteristics(
        attackLong, critChanceLong, critDamageLong, healthLong, resistanceLong);
  }

  /**
   * Maps a row from a BDD table to a Currency POJO
   *
   * @param row row from BDD table
   * @return Currency
   */
  public static Currency mapToCurrency(Map<String, String> row) {
    String gold = row.get(BddFieldConstants.Currency.GOLD);
    String diamond = row.get(BddFieldConstants.Currency.DIAMOND);
    String emerald = row.get(BddFieldConstants.Currency.EMERALD);
    String amethyst = row.get(BddFieldConstants.Currency.AMETHYST);

    Long goldLong = gold == null ? null : Long.parseLong(gold);
    Long diamondLong = diamond == null ? null : Long.parseLong(diamond);
    Long emeraldLong = emerald == null ? null : Long.parseLong(emerald);
    Long amethystLong = amethyst == null ? null : Long.parseLong(amethyst);

    return new Currency(goldLong, diamondLong, emeraldLong, amethystLong);
  }

  /**
   * Maps a row from a BDD table to an Item
   *
   * @param row row from BDD table
   * @return Item
   */
  public static ItemResponse mapToItemResponse(Map<String, String> row) {
    String id = row.get(BddFieldConstants.Item.ID);
    String clientId = row.get(BddFieldConstants.Item.CLIENT_ID);
    ItemType itemType = ItemType.fromString(row.get(BddFieldConstants.Item.ITEM_TYPE));
    String blueprintId = row.get(BddFieldConstants.Item.BLUEPRINT_ID);
    ItemRarity itemRarity = ItemRarity.fromString(row.get(BddFieldConstants.Item.ITEM_RARITY));
    Boolean isEquipped = Boolean.parseBoolean(row.get(BddFieldConstants.Item.IS_EQUIPPED));
    Integer level = Integer.parseInt(row.get(BddFieldConstants.Item.LEVEL));

    ItemStat mainStat =
        BddUtils.mapToItemStat(
            row.get(BddFieldConstants.Item.MAIN_STAT_BASE_VALUE),
            row.get(BddFieldConstants.Item.MAIN_STAT_STATISTIC));
    ItemStat additionalStat1 =
        BddUtils.mapToItemStat(
            row.get(BddFieldConstants.Item.ADDITIONAL_STAT_1_BASE_VALUE),
            row.get(BddFieldConstants.Item.ADDITIONAL_STAT_1_STATISTIC));
    ItemStat additionalStat2 =
        BddUtils.mapToItemStat(
            row.get(BddFieldConstants.Item.ADDITIONAL_STAT_2_BASE_VALUE),
            row.get(BddFieldConstants.Item.ADDITIONAL_STAT_2_STATISTIC));
    ItemStat additionalStat3 =
        BddUtils.mapToItemStat(
            row.get(BddFieldConstants.Item.ADDITIONAL_STAT_3_BASE_VALUE),
            row.get(BddFieldConstants.Item.ADDITIONAL_STAT_3_STATISTIC));

    List<ItemStat> additionalStats = List.of(additionalStat1, additionalStat2, additionalStat3);

    return ItemResponse.builder()
        .id(id)
        .clientId(clientId)
        .itemType(itemType)
        .blueprintId(blueprintId)
        .itemRarity(itemRarity)
        .isEquipped(isEquipped)
        .level(level)
        .mainStat(mainStat)
        .additionalStats(additionalStats)
        .build();
  }

  /**
   * Maps a statistic and a base value as strings to an ItemStat
   *
   * @param statistic statistic as string
   * @param baseValue base value as string
   * @return ItemStat
   */
  public static ItemStat mapToItemStat(String baseValue, String statistic) {
    return new ItemStat(ItemStatistic.fromString(statistic), Float.parseFloat(baseValue));
  }

  /**
   * Maps a row from a BDD table to an ItemEntity
   *
   * @param row row from BDD table
   * @return ItemEntity
   */
  public static ItemEntity mapToItemEntity(Map<String, String> row) {
    String id = row.get(BddFieldConstants.Item.ID);
    String clientId = row.get(BddFieldConstants.Item.CLIENT_ID);
    String blueprintId = row.get(BddFieldConstants.Item.BLUEPRINT_ID);
    String itemType = row.get(BddFieldConstants.Item.ITEM_TYPE);
    String itemRarity = row.get(BddFieldConstants.Item.ITEM_RARITY);
    String isEquipped = row.get(BddFieldConstants.Item.IS_EQUIPPED);
    String level = row.get(BddFieldConstants.Item.LEVEL);

    String mainStatBaseValue = row.get(BddFieldConstants.Item.MAIN_STAT_BASE_VALUE);
    String mainStatStatistic = row.get(BddFieldConstants.Item.MAIN_STAT_STATISTIC);
    ItemStat mainStat = BddUtils.mapToItemStat(mainStatBaseValue, mainStatStatistic);
    String additionalStat1BaseValue = row.get(BddFieldConstants.Item.ADDITIONAL_STAT_1_BASE_VALUE);
    String additionalStat1Statistic = row.get(BddFieldConstants.Item.ADDITIONAL_STAT_1_STATISTIC);
    ItemStat additionalStat1 =
        BddUtils.mapToItemStat(additionalStat1BaseValue, additionalStat1Statistic);
    String additionalStat2BaseValue = row.get(BddFieldConstants.Item.ADDITIONAL_STAT_2_BASE_VALUE);
    String additionalStat2Statistic = row.get(BddFieldConstants.Item.ADDITIONAL_STAT_2_STATISTIC);
    ItemStat additionalStat2 =
        BddUtils.mapToItemStat(additionalStat2BaseValue, additionalStat2Statistic);
    String additionalStat3BaseValue = row.get(BddFieldConstants.Item.ADDITIONAL_STAT_3_BASE_VALUE);
    String additionalStat3Statistic = row.get(BddFieldConstants.Item.ADDITIONAL_STAT_3_STATISTIC);
    ItemStat additionalStat3 =
        BddUtils.mapToItemStat(additionalStat3BaseValue, additionalStat3Statistic);

    List<ItemStat> additionalStats = List.of(additionalStat1, additionalStat2, additionalStat3);

    return ItemEntity.builder()
        .id(id)
        .clientId(clientId)
        .blueprintId(blueprintId)
        .itemType(ItemType.fromString(itemType))
        .itemRarity(ItemRarity.fromString(itemRarity))
        .isEquipped(Boolean.parseBoolean(isEquipped))
        .level(Integer.parseInt(level))
        .mainStat(mainStat)
        .additionalStats(additionalStats)
        .build();
  }

  /**
   * Maps a row from a BDD table to a ItemRequest
   *
   * @param row row from BDD table
   * @return ItemRequest
   */
  public static ItemRequest mapToItemRequest(Map<String, String> row) {
    String clientId = row.get(BddFieldConstants.Item.CLIENT_ID);
    String itemType = row.get(BddFieldConstants.Item.ITEM_TYPE);
    String blueprintId = row.get(BddFieldConstants.Item.BLUEPRINT_ID);
    String itemRarity = row.get(BddFieldConstants.Item.ITEM_RARITY);
    Boolean isEquipped = Boolean.parseBoolean(row.get(BddFieldConstants.Item.IS_EQUIPPED));
    Integer level = Integer.parseInt(row.get(BddFieldConstants.Item.LEVEL));

    ItemStat mainStat =
        BddUtils.mapToItemStat(
            row.get(BddFieldConstants.Item.MAIN_STAT_BASE_VALUE),
            row.get(BddFieldConstants.Item.MAIN_STAT_STATISTIC));
    ItemStat additionalStat1 =
        BddUtils.mapToItemStat(
            row.get(BddFieldConstants.Item.ADDITIONAL_STAT_1_BASE_VALUE),
            row.get(BddFieldConstants.Item.ADDITIONAL_STAT_1_STATISTIC));
    ItemStat additionalStat2 =
        BddUtils.mapToItemStat(
            row.get(BddFieldConstants.Item.ADDITIONAL_STAT_2_BASE_VALUE),
            row.get(BddFieldConstants.Item.ADDITIONAL_STAT_2_STATISTIC));
    ItemStat additionalStat3 =
        BddUtils.mapToItemStat(
            row.get(BddFieldConstants.Item.ADDITIONAL_STAT_3_BASE_VALUE),
            row.get(BddFieldConstants.Item.ADDITIONAL_STAT_3_STATISTIC));

    List<ItemStat> additionalStats = List.of(additionalStat1, additionalStat2, additionalStat3);

    return new ItemRequest(
        clientId, itemType, blueprintId, itemRarity, isEquipped, level, mainStat, additionalStats);
  }

  /**
   * Maps a row from a BDD table to a Stage
   *
   * @param row row from BDD table
   * @return Stage
   */
  public static Stage mapToStage(Map<String, String> row) {
    String currentStage = row.get(BddFieldConstants.Stage.CURRENT_STAGE);
    String maxStage = row.get(BddFieldConstants.Stage.MAX_STAGE);

    long currentStageLong = Long.parseLong(currentStage);
    long maxStageLong = Long.parseLong(maxStage);

    return Stage.builder().currentStage(currentStageLong).maxStage(maxStageLong).build();
  }

  /**
   * Maps a row from a BDD table to a AdminGameSaveUpdateRequest
   *
   * @param row row from BDD table
   * @return AdminGameSaveUpdateRequest
   */
  public static AdminGameSaveUpdateRequest mapToAdminGameSaveUpdateRequest(
      Map<String, String> row) {
    String nickname = row.get(BddFieldConstants.GameSave.NICKNAME);

    // Currency (nullable)
    String gold = row.get(BddFieldConstants.Currency.GOLD);
    String diamond = row.get(BddFieldConstants.Currency.DIAMOND);
    String emerald = row.get(BddFieldConstants.Currency.EMERALD);
    String amethyst = row.get(BddFieldConstants.Currency.AMETHYST);
    Currency currency =
        Currency.builder()
            .gold(gold != null ? Long.parseLong(gold) : null)
            .amethyst(amethyst != null ? Long.parseLong(amethyst) : null)
            .diamond(diamond != null ? Long.parseLong(diamond) : null)
            .emerald(emerald != null ? Long.parseLong(emerald) : null)
            .build();
    if (currency.getGold() == null
        && currency.getAmethyst() == null
        && currency.getDiamond() == null) {
      currency = null;
    }

    // Stage
    String currentStage = row.get(BddFieldConstants.Stage.CURRENT_STAGE);
    String maxStage = row.get(BddFieldConstants.Stage.MAX_STAGE);
    Stage stage =
        Stage.builder()
            .currentStage(currentStage != null ? Long.parseLong(currentStage) : null)
            .maxStage(maxStage != null ? Long.parseLong(maxStage) : null)
            .build();

    if (stage.getCurrentStage() == null && stage.getMaxStage() == null) {
      stage = null;
    }

    // Characteristics
    String attack = row.get(BddFieldConstants.Characteristics.ATTACK);
    String critChance = row.get(BddFieldConstants.Characteristics.CRIT_CHANCE);
    String critDamage = row.get(BddFieldConstants.Characteristics.CRIT_DAMAGE);
    String health = row.get(BddFieldConstants.Characteristics.HEALTH);
    String resistance = row.get(BddFieldConstants.Characteristics.RESISTANCE);
    Characteristics characteristics =
        Characteristics.builder()
            .attack(attack != null ? Long.parseLong(attack) : null)
            .critChance(critChance != null ? Long.parseLong(critChance) : null)
            .critDamage(critDamage != null ? Long.parseLong(critDamage) : null)
            .health(health != null ? Long.parseLong(health) : null)
            .resistance(resistance != null ? Long.parseLong(resistance) : null)
            .build();

    if (characteristics.getAttack() == null
        || characteristics.getHealth() == null
        || characteristics.getCritChance() == null
        || characteristics.getCritDamage() == null
        || characteristics.getResistance() == null) {
      characteristics = null;
    }

    return AdminGameSaveUpdateRequest.builder()
        .nickname(nickname)
        .characteristics(characteristics)
        .currency(currency)
        .stage(stage)
        .build();
  }

  /**
   * Maps a row from a BDD table to a GameSaveUpdateUserRequest
   *
   * @param row row from BDD table
   * @return GameSaveUpdateRequest
   */
  public static GameSaveNicknameUpdateRequest mapToGameSaveUpdateUserRequest(
      Map<String, String> row) {
    String nickname = row.get(BddFieldConstants.GameSave.NICKNAME);

    return new GameSaveNicknameUpdateRequest(nickname);
  }

  /**
   * Maps a row from a BDD table to a SimpleUserCreationRequest
   *
   * @param row row from BDD table
   * @return SimpleUserCreationRequest
   */
  public static SimpleUserCreationRequest mapToUserCreationRequest(Map<String, String> row) {
    String email = row.get(BddFieldConstants.UserCreationRequest.USERNAME);
    String firstName = row.get(BddFieldConstants.UserCreationRequest.FIRST_NAME);
    String lastName = row.get(BddFieldConstants.UserCreationRequest.LAST_NAME);
    String password = row.get(BddFieldConstants.UserCreationRequest.PASSWORD);

    return SimpleUserCreationRequest.builder()
        .username(email)
        .firstName(firstName)
        .lastName(lastName)
        .password(password)
        .build();
  }

  /**
   * Builds a URL from the server port and the endpoint to call
   *
   * @param port port
   * @param endpoint endpoint
   * @return the URL
   */
  public static String buildUrl(int port, String endpoint) {
    return "http://localhost:" + port + endpoint;
  }

  /**
   * Builds an HttpEntity with the given body and headers
   *
   * @param body body
   * @param <T> type of the body
   * @return HttpEntity
   */
  public static <T> HttpEntity<T> buildHttpEntity(T body) {
    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.set("Content-Type", "application/json");
    return new HttpEntity<>(body, httpHeaders);
  }

  /**
   * Maps a row from a BDD table to a UserLoginRequest
   *
   * @param row row from BDD table
   * @return UserLoginRequest
   */
  public static UserLoginRequest mapToUserLoginRequest(Map<String, String> row) {
    String email = row.get(BddFieldConstants.UserLoginRequest.USERNAME);
    String password = row.get(BddFieldConstants.UserLoginRequest.PASSWORD);

    return new UserLoginRequest(email, password);
  }

  /**
   * Maps a row from a BDD table to a JwtAuthenticationResponse
   *
   * @param row row from BDD table
   * @return JwtAuthenticationResponse
   */
  public static SimpleUserUpdateRequest mapToUserUpdateRequest(Map<String, String> row) {
    String firstName = row.get(BddFieldConstants.UserUpdateRequest.FIRST_NAME);
    String lastName = row.get(BddFieldConstants.UserUpdateRequest.LAST_NAME);

    return new SimpleUserUpdateRequest(firstName, lastName);
  }

  /**
   * Maps a row from a BDD table to a AdminUserUpdateRequest
   *
   * @param row row from BDD table
   * @return AdminUserUpdateRequest
   */
  public static AdminUserUpdateRequest mapToAdminUserUpdateRequest(Map<String, String> row) {
    String firstName = row.get(BddFieldConstants.AdminUserUpdateRequest.FIRST_NAME);
    String lastName = row.get(BddFieldConstants.AdminUserUpdateRequest.LAST_NAME);
    String enabled = row.get(BddFieldConstants.AdminUserUpdateRequest.ENABLED);
    String verified = row.get(BddFieldConstants.AdminUserUpdateRequest.EMAIL_VERIFIED);
    String userRoles = row.get(BddFieldConstants.AdminUserUpdateRequest.USER_ROLES);

    List<String> roles = Arrays.stream(userRoles.split(COMMA)).toList();

    Boolean enabledBoolean = Boolean.parseBoolean(enabled);
    Boolean verifiedBoolean = Boolean.parseBoolean(verified);

    return new AdminUserUpdateRequest(firstName, lastName, verifiedBoolean, enabledBoolean, roles);
  }

  /**
   * Maps a row from a BDD table to a AdminUserCreationRequest
   *
   * @param row row from BDD table
   * @return AdminUserCreationRequest
   */
  public static AdminUserCreationRequest mapToAdminUserCreationRequest(Map<String, String> row) {
    String firstName = row.get(BddFieldConstants.AdminUserCreationRequest.FIRST_NAME);
    String lastName = row.get(BddFieldConstants.AdminUserCreationRequest.LAST_NAME);
    String username = row.get(BddFieldConstants.AdminUserCreationRequest.USERNAME);
    String enabled = row.get(BddFieldConstants.AdminUserCreationRequest.ENABLED);
    String password = row.get(BddFieldConstants.AdminUserCreationRequest.PASSWORD);
    String verified = row.get(BddFieldConstants.AdminUserCreationRequest.EMAIL_VERIFIED);
    String roles = row.get(BddFieldConstants.AdminUserCreationRequest.USER_ROLES);

    Boolean enabledBoolean = Boolean.parseBoolean(enabled);
    Boolean verifiedBoolean = Boolean.parseBoolean(verified);

    List<String> userRoles = roles == null ? null : Arrays.stream(roles.split(COMMA)).toList();

    return new AdminUserCreationRequest(
        firstName, lastName, enabledBoolean, password, verifiedBoolean, username, userRoles);
  }

  /**
   * Maps a row from a BDD table to a SearchRequest Filter
   *
   * @param row row from BDD table
   * @return Filter
   */
  public static Filter mapToFilter(Map<String, String> row) {
    String key = row.get(BddFieldConstants.SearchRequest.KEY);
    String value = row.get(BddFieldConstants.SearchRequest.VALUE);
    return new Filter(key, value);
  }

  /**
   * Maps a row from a BDD table to a GlobalInfoResponse
   *
   * @param row row from BDD table
   * @return GlobalInfo
   */
  public static GlobalInfoResponse mapToGlobalInfoResponse(Map<String, String> row) {
    String nbGameSaves = row.get(BddFieldConstants.GlobalInfo.GAME_SAVE_COUNTER);
    String nbUsers = row.get(BddFieldConstants.GlobalInfo.USER_COUNTER);
    String now = row.get(BddFieldConstants.GlobalInfo.NOW);

    Long nbGameSavesLong = Long.parseLong(nbGameSaves);
    Long nbUsersLong = Long.parseLong(nbUsers);

    Instant nowInstant = Instant.parse(now);

    return new GlobalInfoResponse(nowInstant, nbGameSavesLong, nbUsersLong);
  }

  /**
   * Maps a row from a BDD table to a UserRefreshLoginRequest
   *
   * @param row row from BDD table
   * @return UserRefreshLoginRequest
   */
  public static UserRefreshLoginRequest mapToUserRefreshLoginRequest(Map<String, String> row) {
    String refreshToken = row.get(BddFieldConstants.UserRefreshLoginRequest.REFRESH_TOKEN);
    return new UserRefreshLoginRequest(refreshToken);
  }
}
