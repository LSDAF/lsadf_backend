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
package com.lsadf.bdd.util;

import static com.lsadf.bdd.config.BddFieldConstants.Item.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lsadf.bdd.config.BddFieldConstants;
import com.lsadf.core.domain.game.inventory.Item;
import com.lsadf.core.domain.game.inventory.ItemRarity;
import com.lsadf.core.domain.game.inventory.ItemStat;
import com.lsadf.core.domain.game.inventory.ItemStatistic;
import com.lsadf.core.domain.game.inventory.ItemType;
import com.lsadf.core.domain.game.mail.GameMailAttachment;
import com.lsadf.core.domain.game.mail.GameMailAttachmentType;
import com.lsadf.core.domain.game.save.GameSave;
import com.lsadf.core.domain.game.save.characteristics.Characteristics;
import com.lsadf.core.domain.game.save.currency.Currency;
import com.lsadf.core.domain.game.save.metadata.GameMetadata;
import com.lsadf.core.domain.game.save.stage.Stage;
import com.lsadf.core.domain.info.GlobalInfo;
import com.lsadf.core.domain.user.User;
import com.lsadf.core.domain.user.UserInfo;
import com.lsadf.core.infra.persistence.impl.game.inventory.AdditionalItemStatEntity;
import com.lsadf.core.infra.persistence.impl.game.inventory.ItemEntity;
import com.lsadf.core.infra.persistence.impl.game.mail.GameMailEntity;
import com.lsadf.core.infra.persistence.impl.game.mail.template.GameMailTemplateAttachmentEntity;
import com.lsadf.core.infra.persistence.impl.game.mail.template.GameMailTemplateEntity;
import com.lsadf.core.infra.persistence.impl.game.save.characteristics.CharacteristicsEntity;
import com.lsadf.core.infra.persistence.impl.game.save.currency.CurrencyEntity;
import com.lsadf.core.infra.persistence.impl.game.save.metadata.GameMetadataEntity;
import com.lsadf.core.infra.persistence.impl.game.save.stage.StageEntity;
import com.lsadf.core.infra.persistence.impl.game.session.GameSessionEntity;
import com.lsadf.core.infra.web.dto.common.game.inventory.ItemStatDto;
import com.lsadf.core.infra.web.dto.request.common.Filter;
import com.lsadf.core.infra.web.dto.request.game.characteristics.CharacteristicsRequest;
import com.lsadf.core.infra.web.dto.request.game.currency.CurrencyRequest;
import com.lsadf.core.infra.web.dto.request.game.inventory.ItemRequest;
import com.lsadf.core.infra.web.dto.request.game.mail.GameMailAttachmentRequest;
import com.lsadf.core.infra.web.dto.request.game.mail.GameMailTemplateRequest;
import com.lsadf.core.infra.web.dto.request.game.metadata.GameMetadataRequest;
import com.lsadf.core.infra.web.dto.request.game.save.creation.AdminGameSaveCreationRequest;
import com.lsadf.core.infra.web.dto.request.game.save.update.AdminGameSaveUpdateRequest;
import com.lsadf.core.infra.web.dto.request.game.save.update.GameSaveNicknameUpdateRequest;
import com.lsadf.core.infra.web.dto.request.game.stage.StageRequest;
import com.lsadf.core.infra.web.dto.request.user.creation.AdminUserCreationRequest;
import com.lsadf.core.infra.web.dto.request.user.creation.SimpleUserCreationRequest;
import com.lsadf.core.infra.web.dto.request.user.login.UserLoginRequest;
import com.lsadf.core.infra.web.dto.request.user.login.UserRefreshLoginRequest;
import com.lsadf.core.infra.web.dto.request.user.update.AdminUserUpdateRequest;
import com.lsadf.core.infra.web.dto.request.user.update.SimpleUserUpdateRequest;
import com.lsadf.core.infra.web.dto.response.game.inventory.ItemResponse;
import com.lsadf.core.infra.web.dto.response.game.mail.GameMailResponse;
import com.lsadf.core.infra.web.dto.response.game.save.GameSaveResponse;
import com.lsadf.core.infra.web.dto.response.game.save.characteristics.CharacteristicsResponse;
import com.lsadf.core.infra.web.dto.response.game.save.currency.CurrencyResponse;
import com.lsadf.core.infra.web.dto.response.game.save.metadata.GameMetadataResponse;
import com.lsadf.core.infra.web.dto.response.game.save.stage.StageResponse;
import com.lsadf.core.infra.web.dto.response.game.session.GameSessionResponse;
import com.lsadf.core.infra.web.dto.response.info.GlobalInfoResponse;
import com.lsadf.core.infra.web.dto.response.user.UserResponse;
import java.io.IOException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;
import lombok.experimental.UtilityClass;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

/**
 * Consolidated utility class for BDD tests Contains common mapping methods used across all modules
 */
@UtilityClass
public class BddUtils {

  private static final String COMMA = ",";

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
   * Maps a row from a BDD table to a GameMailTemplateRequest
   *
   * @param row row from BDD table
   * @return GameMailTemplateRequest
   */
  public static GameMailTemplateRequest mapToGameMailTemplateRequest(Map<String, String> row) {
    String name = row.get(BddFieldConstants.GameMailTemplate.NAME);
    String subject = row.get(BddFieldConstants.GameMailTemplate.SUBJECT);
    String body = row.get(BddFieldConstants.GameMailTemplate.BODY);
    String expirationDaysString = row.get(BddFieldConstants.GameMailTemplate.EXPIRATION_DAYS);
    var builder = GameMailTemplateRequest.builder().name(name).subject(subject).body(body);

    if (expirationDaysString != null) {
      Integer expirationDays = Integer.parseInt(expirationDaysString);
      builder.expirationDays(expirationDays);
    }

    return builder.build();
  }

  /**
   * Maps a row from a BDD table to a GameMailEntity
   *
   * @param row row from BDD table
   * @return GameMailEntity
   */
  public static GameMailEntity mapToGameMailEntity(Map<String, String> row, boolean id) {
    var builder = GameMailEntity.builder();
    if (id) {
      String idStr = row.get(BddFieldConstants.GameMail.ID);
      UUID uuid = UUID.fromString(idStr);
      builder.id(uuid);
    }
    String templateId = row.get(BddFieldConstants.GameMail.MAIL_TEMPLATE_ID);
    UUID templateUuid = UUID.fromString(templateId);
    String gameSaveId = row.get(BddFieldConstants.GameMail.GAME_SAVE_ID);
    UUID gameSaveUuid = UUID.fromString(gameSaveId);
    String isReadString = row.get(BddFieldConstants.GameMail.IS_READ);
    boolean isRead = Boolean.parseBoolean(isReadString);
    String isAttachmentClaimedString = row.get(BddFieldConstants.GameMail.IS_ATTACHMENT_CLAIMED);
    boolean isAttachmentClaimed = Boolean.parseBoolean(isAttachmentClaimedString);
    return builder
        .mailTemplateId(templateUuid)
        .gameSaveId(gameSaveUuid)
        .isRead(isRead)
        .isAttachmentClaimed(isAttachmentClaimed)
        .build();
  }

  /**
   * Maps a row from a BDD table to a GameMailTemplateAttachmentEntity
   *
   * @param row row from BDD table
   * @return GameMailTemplateAttachmentEntity
   */
  public static GameMailTemplateAttachmentEntity mapToGameMailTemplateAttachmentEntity(
      Map<String, String> row) {
    String id = row.get(BddFieldConstants.GameMailTemplateAttachment.ID);
    UUID uuid = UUID.fromString(id);
    String templateId = row.get(BddFieldConstants.GameMailTemplateAttachment.MAIL_TEMPLATE_ID);
    UUID templateUuid = UUID.fromString(templateId);
    String object = row.get(BddFieldConstants.GameMailTemplateAttachment.OBJECT);
    String typeStr = row.get(BddFieldConstants.GameMailTemplateAttachment.TYPE);
    GameMailAttachmentType type = GameMailAttachmentType.valueOf(typeStr);
    return GameMailTemplateAttachmentEntity.builder()
        .id(uuid)
        .mailTemplateId(templateUuid)
        .type(type)
        .object(object)
        .build();
  }

  /**
   * Maps a row from a BDD table to a GameMailTemplateEntity
   *
   * @param row row from BDD table
   * @return GameMailTemplateEntity
   */
  public static GameMailTemplateEntity mapToGameMailTemplateEntity(Map<String, String> row) {
    String id = row.get(BddFieldConstants.GameMailTemplate.ID);
    UUID uuid = UUID.fromString(id);
    String name = row.get(BddFieldConstants.GameMailTemplate.NAME);
    String subject = row.get(BddFieldConstants.GameMailTemplate.SUBJECT);
    String body = row.get(BddFieldConstants.GameMailTemplate.BODY);
    String expirationDaysString = row.get(BddFieldConstants.GameMailTemplate.EXPIRATION_DAYS);
    Integer expirationDays =
        expirationDaysString == null ? null : Integer.parseInt(expirationDaysString);
    return GameMailTemplateEntity.builder()
        .id(uuid)
        .name(name)
        .subject(subject)
        .body(body)
        .expirationDays(expirationDays)
        .build();
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
    String wave = row.get(BddFieldConstants.Stage.WAVE);

    long currentStageLong = Long.parseLong(currentStage);
    long maxStageLong = Long.parseLong(maxStage);
    long waveLong = Long.parseLong(wave);

    return new StageRequest(currentStageLong, maxStageLong, waveLong);
  }

  /**
   * Maps a row from a BDD table to a GameMetadataEntity
   *
   * @param row row from BDD table
   * @return GameMetadataEntity
   */
  public static GameMetadataEntity mapToGameSaveEntity(Map<String, String> row) {
    String id = row.get(BddFieldConstants.GameSave.ID);
    UUID uuid = UUID.fromString(id);
    String userEmail = row.get(BddFieldConstants.GameSave.USER_EMAIL);
    String nickname = row.get(BddFieldConstants.GameSave.NICKNAME);
    return GameMetadataEntity.builder().userEmail(userEmail).nickname(nickname).id(uuid).build();
  }

  /**
   * Maps a row from a BDD table to a GameSessionEntity
   *
   * @param row row from BDD table
   * @return GameMetadataEntity
   */
  public static GameSessionEntity mapToGameSaveSessionEntity(Map<String, String> row) {
    var builder = GameSessionEntity.builder();
    UUID id = UUID.fromString(row.get(BddFieldConstants.GameSession.ID));
    UUID gameSaveId = UUID.fromString(row.get(BddFieldConstants.GameSession.GAME_SAVE_ID));
    String endTimeString = row.get(BddFieldConstants.GameSession.END_TIME);
    String cancelledString = row.get(BddFieldConstants.GameSession.CANCELLED);
    boolean cancelled = Boolean.parseBoolean(cancelledString);
    var versionString = row.get(BddFieldConstants.GameSession.VERSION);
    int version;
    Optional<Integer> versionOptional =
        versionString == null ? Optional.empty() : Optional.of(Integer.parseInt(versionString));
    version = versionOptional.orElse(1);
    Instant endTime =
        endTimeString != null
            ? Instant.parse(endTimeString)
            // ensuring end date is far
            : Instant.now().plus(10, ChronoUnit.HOURS);
    GameSessionEntity.GameSessionId gameSessionId =
        new GameSessionEntity.GameSessionId(id, version);
    return builder
        .id(gameSessionId)
        .gameSaveId(gameSaveId)
        .cancelled(cancelled)
        .endTime(endTime)
        .build();
  }

  /**
   * Maps the provided data from a Map of String key-value pairs to a StageEntity object.
   *
   * @param row a Map containing key-value pairs where the keys correspond to Stage field constants
   *     defined in BddFieldConstants.Stage, and the values represent the field values.
   * @return a StageEntity object built using the data provided in the map. Returns null for numeric
   *     fields if their corresponding map values are null.
   */
  public static StageEntity mapToStageEntity(Map<String, String> row) {
    String id = row.get(BddFieldConstants.Stage.ID);
    UUID uuid = UUID.fromString(id);
    String currentStage = row.get(BddFieldConstants.Stage.CURRENT_STAGE);
    String maxStage = row.get(BddFieldConstants.Stage.MAX_STAGE);
    String wave = row.get(BddFieldConstants.Stage.WAVE);
    return StageEntity.builder()
        .id(uuid)
        .currentStage(currentStage == null ? null : Long.parseLong(currentStage))
        .maxStage(maxStage == null ? null : Long.parseLong(maxStage))
        .wave(wave == null ? null : Long.parseLong(wave))
        .build();
  }

  /**
   * Maps a given row of data represented as a Map to a CharacteristicsEntity object.
   *
   * @param row a Map containing fields and values representing a CharacteristicsEntity. The map is
   *     expected to have specific keys such as ID, ATTACK, CRIT_CHANCE, CRIT_DAMAGE, HEALTH, and
   *     RESISTANCE.
   * @return a CharacteristicsEntity object built using the data extracted from the input map.
   */
  public static CharacteristicsEntity mapToCharacteristicsEntity(Map<String, String> row) {
    String id = row.get(BddFieldConstants.Characteristics.ID);
    UUID uuid = UUID.fromString(id);
    String attack = row.get(BddFieldConstants.Characteristics.ATTACK);
    String critChance = row.get(BddFieldConstants.Characteristics.CRIT_CHANCE);
    String critDamage = row.get(BddFieldConstants.Characteristics.CRIT_DAMAGE);
    String health = row.get(BddFieldConstants.Characteristics.HEALTH);
    String resistance = row.get(BddFieldConstants.Characteristics.RESISTANCE);

    return CharacteristicsEntity.builder()
        .id(uuid)
        .attack(attack == null ? null : Long.parseLong(attack))
        .critChance(critChance == null ? null : Long.parseLong(critChance))
        .critDamage(critDamage == null ? null : Long.parseLong(critDamage))
        .health(health == null ? null : Long.parseLong(health))
        .resistance(resistance == null ? null : Long.parseLong(resistance))
        .build();
  }

  /**
   * Maps a row of currency data represented as a map to a CurrencyEntity object.
   *
   * @param row a map containing currency data with keys representing field names and values
   *     representing the corresponding data.
   * @return a CurrencyEntity object created from the given map.
   */
  public static CurrencyEntity mapToCurrencyEntity(Map<String, String> row) {
    String id = row.get(BddFieldConstants.Currency.ID);
    UUID uuid = UUID.fromString(id);
    String gold = row.get(BddFieldConstants.Currency.GOLD);
    String diamond = row.get(BddFieldConstants.Currency.DIAMOND);
    String emerald = row.get(BddFieldConstants.Currency.EMERALD);
    String amethyst = row.get(BddFieldConstants.Currency.AMETHYST);
    return CurrencyEntity.builder()
        .id(uuid)
        .goldAmount(gold == null ? null : Long.parseLong(gold))
        .emeraldAmount(emerald == null ? null : Long.parseLong(emerald))
        .amethystAmount(amethyst == null ? null : Long.parseLong(amethyst))
        .diamondAmount(diamond == null ? null : Long.parseLong(diamond))
        .build();
  }

  /**
   * Maps a row from a BDD table to a UserInfo
   *
   * @param row row from BDD table
   * @return UserInfo
   */
  public static UserInfo mapToUserInfo(Map<String, String> row) {
    String email = row.get(BddFieldConstants.UserInfo.EMAIL);
    String name = row.get(BddFieldConstants.UserInfo.NAME);
    String rolesString = row.get(BddFieldConstants.UserInfo.ROLES);
    String verifiedString = row.get(BddFieldConstants.UserInfo.VERIFIED);

    Set<String> roles = Arrays.stream(rolesString.split(COMMA)).collect(Collectors.toSet());

    boolean verified = Boolean.parseBoolean(verifiedString);
    return new UserInfo(name, email, verified, roles);
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
    String waveString = row.get(BddFieldConstants.Stage.WAVE);
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
    Long wave = Long.parseLong(waveString);

    String id = row.get(BddFieldConstants.GameSave.ID);
    UUID uuid = UUID.fromString(id);
    String userEmail = row.get(BddFieldConstants.GameSave.USER_EMAIL);

    GameMetadataRequest metadataRequest = new GameMetadataRequest(uuid, userEmail, nickname);
    StageRequest stageRequest = new StageRequest(currentStage, maxStage, wave);
    CharacteristicsRequest characteristicsRequest =
        new CharacteristicsRequest(attack, critChance, critDamage, health, resistance);
    CurrencyRequest currencyRequest = new CurrencyRequest(gold, diamond, emerald, amethyst);

    return AdminGameSaveCreationRequest.builder()
        .metadata(metadataRequest)
        .stage(stageRequest)
        .characteristics(characteristicsRequest)
        .currency(currencyRequest)
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
    Characteristics characteristics = mapToCharacteristics(row);
    Currency currency = mapToCurrency(row);
    Stage stage = mapToStage(row);
    GameMetadata gameMetadata = mapToGameMetadata(row);

    return GameSave.builder()
        .metadata(gameMetadata)
        .characteristics(characteristics)
        .currency(currency)
        .stage(stage)
        .build();
  }

  /**
   * Maps a map of strings to a GameSaveResponse object by processing metadata, stage, currency, and
   * characteristics data from the input map.
   *
   * @param row a map where keys are string identifiers and values are corresponding string data
   *     used to construct the GameSaveResponse components.
   * @return a GameSaveResponse object constructed using the mapped metadata, stage, currency, and
   *     characteristics data.
   */
  public static GameSaveResponse mapToGameSaveResponse(Map<String, String> row) {
    GameMetadataResponse metadataResponse = mapToGameMetadataResponse(row);
    StageResponse stageResponse = mapToStageResponse(row);
    CurrencyResponse currencyResponse = mapToCurrencyResponse(row);
    CharacteristicsResponse characteristicsResponse = mapToCharacteristicsResponse(row);
    return GameSaveResponse.builder()
        .metadata(metadataResponse)
        .characteristics(characteristicsResponse)
        .currency(currencyResponse)
        .stage(stageResponse)
        .build();
  }

  /**
   * @param row
   * @return
   */
  public static GameSessionResponse mapToGameSessionResponse(Map<String, String> row) {
    var builder = GameSessionResponse.builder();
    String idString = row.get(BddFieldConstants.GameSession.ID);
    UUID id = idString != null ? UUID.fromString(idString) : null;
    if (id != null) {
      builder.id(id);
    }

    String endTimeString = row.get(BddFieldConstants.GameSession.END_TIME);
    Instant endTime = endTimeString != null ? Instant.parse(endTimeString) : null;
    if (endTime != null) {
      builder.endTime(endTime);
    }

    String versionString = row.get(BddFieldConstants.GameSession.VERSION);
    Integer version = versionString != null ? Integer.parseInt(versionString) : null;
    if (version != null) {
      builder.version(version);
    }

    return builder.build();
  }

  /**
   * Maps a row of data represented as a Map to a GameMetadata object.
   *
   * @param row a Map containing key-value pairs representing the game's metadata. Expected keys
   *     include ID, USER_EMAIL, and NICKNAME.
   * @return a populated GameMetadata object built using the data from the provided row map.
   */
  public static GameMetadata mapToGameMetadata(Map<String, String> row) {
    String id = row.get(BddFieldConstants.GameSave.ID);
    UUID uuid = UUID.fromString(id);
    String userEmail = row.get(BddFieldConstants.GameSave.USER_EMAIL);
    String nickname = row.get(BddFieldConstants.GameSave.NICKNAME);
    return GameMetadata.builder().id(uuid).userEmail(userEmail).nickname(nickname).build();
  }

  /**
   * Maps a given row of game metadata represented as a Map to a GameMetadataResponse object.
   *
   * @param row a Map containing game metadata where keys are strings representing field names and
   *     values are the corresponding data values.
   * @return a GameMetadataResponse object constructed from the provided metadata Map.
   */
  public static GameMetadataResponse mapToGameMetadataResponse(Map<String, String> row) {
    String id = row.get(BddFieldConstants.GameSave.ID);
    UUID uuid = (id != null) ? UUID.fromString(id) : null;
    String userEmail = row.get(BddFieldConstants.GameSave.USER_EMAIL);
    String nickname = row.get(BddFieldConstants.GameSave.NICKNAME);

    return new GameMetadataResponse(uuid, null, null, userEmail, nickname);
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
   * Maps a row from a BDD table to a GameMailAttachmentRequest
   *
   * @param row row from BDD table
   * @return GameMailAttachmentRequest
   */
  public static GameMailAttachmentRequest<?> mapToGameMailAttachmentRequest(
      Map<String, String> row) {
    String type = row.get(BddFieldConstants.GameMailAttachment.TYPE);
    String attachment = row.get(BddFieldConstants.GameMailAttachment.ATTACHMENT);

    GameMailAttachmentType attachmentType = GameMailAttachmentType.valueOf(type);

    return new GameMailAttachmentRequest<>(attachmentType, attachment);
  }

  /**
   * Maps a row from a BDD table to a Game Mail Attachment
   *
   * @param row row from BDD table
   * @return GameMailAttachment
   */
  public static GameMailAttachment<?> mapToGameMailAttachment(
      Map<String, String> row, ObjectMapper objectMapper) throws JsonProcessingException {
    String type = row.get(BddFieldConstants.GameMailAttachment.TYPE);
    String attachment = row.get(BddFieldConstants.GameMailAttachment.ATTACHMENT);
    Map<String, Object> attachmentMap = objectMapper.readValue(attachment, Map.class);

    GameMailAttachmentType attachmentType = GameMailAttachmentType.valueOf(type);

    return new GameMailAttachment<>(attachmentType, attachmentMap);
  }

  /**
   * @param row
   * @return
   */
  public static GameMailResponse mapToGameMailResponse(Map<String, String> row) {
    String id = row.get(BddFieldConstants.GameMailResponse.ID);
    String gameSaveId = row.get(BddFieldConstants.GameMailResponse.GAME_SAVE_ID);
    String subject = row.get(BddFieldConstants.GameMailResponse.SUBJECT);
    String body = row.get(BddFieldConstants.GameMailResponse.BODY);
    String read = row.get(BddFieldConstants.GameMailResponse.READ);
    String attachmentsClaimed = row.get(BddFieldConstants.GameMailResponse.ATTACHMENTS_CLAIMED);

    UUID uuid = UUID.fromString(id);
    UUID gameSaveUuid = UUID.fromString(gameSaveId);
    boolean readBoolean = Boolean.parseBoolean(read);
    boolean attachmentsClaimedBoolean = Boolean.parseBoolean(attachmentsClaimed);

    return GameMailResponse.builder()
        .id(uuid)
        .gameSaveId(gameSaveUuid)
        .subject(subject)
        .body(body)
        .read(readBoolean)
        .attachmentsClaimed(attachmentsClaimedBoolean)
        .build();
  }

  /**
   * Maps a row from a BDD table to an Item
   *
   * @param row row from BDD table
   * @return Item
   */
  public static Item mapToItem(Map<String, String> row) {
    String id = row.get(BddFieldConstants.Item.ID);
    UUID uuid = UUID.fromString(id);
    String clientId = row.get(BddFieldConstants.Item.CLIENT_ID);
    ItemType itemType = ItemType.fromString(row.get(BddFieldConstants.Item.ITEM_TYPE));
    String blueprintId = row.get(BddFieldConstants.Item.BLUEPRINT_ID);
    ItemRarity itemRarity = ItemRarity.fromString(row.get(BddFieldConstants.Item.ITEM_RARITY));
    Boolean isEquipped = Boolean.parseBoolean(row.get(BddFieldConstants.Item.IS_EQUIPPED));
    Integer level = Integer.parseInt(row.get(BddFieldConstants.Item.LEVEL));

    ItemStat mainStat =
        BddUtils.mapToItemStat(
            row.get(BddFieldConstants.Item.MAIN_STAT_STATISTIC),
            row.get(BddFieldConstants.Item.MAIN_STAT_BASE_VALUE));
    ItemStat additionalStat1 =
        BddUtils.mapToItemStat(
            row.get(BddFieldConstants.Item.ADDITIONAL_STAT_1_STATISTIC),
            row.get(ADDITIONAL_STAT_1_BASE_VALUE));
    ItemStat additionalStat2 =
        BddUtils.mapToItemStat(
            row.get(BddFieldConstants.Item.ADDITIONAL_STAT_2_STATISTIC),
            row.get(BddFieldConstants.Item.ADDITIONAL_STAT_2_BASE_VALUE));
    ItemStat additionalStat3 =
        BddUtils.mapToItemStat(
            row.get(BddFieldConstants.Item.ADDITIONAL_STAT_3_STATISTIC),
            row.get(BddFieldConstants.Item.ADDITIONAL_STAT_3_BASE_VALUE));

    List<ItemStat> additionalStats = List.of(additionalStat1, additionalStat2, additionalStat3);

    return Item.builder()
        .id(uuid)
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
   * Maps a row from a BDD table to an ItemResponse
   *
   * @param row row from BDD table
   * @return ItemResponse
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
            row.get(BddFieldConstants.Item.MAIN_STAT_STATISTIC),
            row.get(BddFieldConstants.Item.MAIN_STAT_BASE_VALUE));
    ItemStat additionalStat1 =
        BddUtils.mapToItemStat(
            row.get(BddFieldConstants.Item.ADDITIONAL_STAT_1_STATISTIC),
            row.get(ADDITIONAL_STAT_1_BASE_VALUE));
    ItemStat additionalStat2 =
        BddUtils.mapToItemStat(
            row.get(BddFieldConstants.Item.ADDITIONAL_STAT_2_STATISTIC),
            row.get(BddFieldConstants.Item.ADDITIONAL_STAT_2_BASE_VALUE));
    ItemStat additionalStat3 =
        BddUtils.mapToItemStat(
            row.get(BddFieldConstants.Item.ADDITIONAL_STAT_3_STATISTIC),
            row.get(BddFieldConstants.Item.ADDITIONAL_STAT_3_BASE_VALUE));

    List<ItemStatDto> additionalStats =
        List.of(additionalStat1, additionalStat2, additionalStat3).stream()
            .map(itemStat -> new ItemStatDto(itemStat.getStatistic(), itemStat.getBaseValue()))
            .toList();
    ItemStatDto mainStatDto = new ItemStatDto(mainStat.getStatistic(), mainStat.getBaseValue());

    return ItemResponse.builder()
        .id(id)
        .clientId(clientId)
        .itemType(itemType)
        .blueprintId(blueprintId)
        .itemRarity(itemRarity)
        .isEquipped(isEquipped)
        .level(level)
        .mainStat(mainStatDto)
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
  public static ItemStat mapToItemStat(String statistic, String baseValue) {
    return new ItemStat(ItemStatistic.fromString(statistic), Float.parseFloat(baseValue));
  }

  /**
   * Maps a given row of data in the form of a Map<String, String> to an instance of
   * AdditionalItemStatEntity.
   *
   * @param row a map representing a row of data where keys are column names and values are the
   *     corresponding data as strings. Expected keys include: -
   *     BddFieldConstants.AdditionalItemStatEntity.STATISTIC -
   *     BddFieldConstants.AdditionalItemStatEntity.ITEM_ID - ID -
   *     BddFieldConstants.AdditionalItemStatEntity.BASE_VALUE
   * @return an instance of AdditionalItemStatEntity, constructed using the data from the provided
   *     map.
   */
  public static List<AdditionalItemStatEntity> mapToAdditionalItemStatEntity(
      Map<String, String> row, UUID itemId) {
    List<AdditionalItemStatEntity> additionalItemStatEntities = new ArrayList<>();
    AdditionalItemStatEntity entity1 =
        AdditionalItemStatEntity.builder()
            .itemId(itemId)
            .statistic(
                row.get(ADDITIONAL_STAT_1_STATISTIC) == null
                    ? null
                    : ItemStatistic.fromString(row.get(ADDITIONAL_STAT_1_STATISTIC)))
            .baseValue(
                row.get(ADDITIONAL_STAT_1_BASE_VALUE) == null
                    ? null
                    : Float.parseFloat(row.get(ADDITIONAL_STAT_1_BASE_VALUE)))
            .build();
    additionalItemStatEntities.add(entity1);

    AdditionalItemStatEntity entity2 =
        AdditionalItemStatEntity.builder()
            .itemId(itemId)
            .statistic(
                row.get(ADDITIONAL_STAT_2_STATISTIC) == null
                    ? null
                    : ItemStatistic.fromString(row.get(ADDITIONAL_STAT_2_STATISTIC)))
            .baseValue(
                row.get(ADDITIONAL_STAT_2_BASE_VALUE) == null
                    ? null
                    : Float.parseFloat(row.get(ADDITIONAL_STAT_2_BASE_VALUE)))
            .build();
    additionalItemStatEntities.add(entity2);

    AdditionalItemStatEntity entity3 =
        AdditionalItemStatEntity.builder()
            .itemId(itemId)
            .statistic(
                row.get(ADDITIONAL_STAT_3_STATISTIC) == null
                    ? null
                    : ItemStatistic.fromString(row.get(ADDITIONAL_STAT_3_STATISTIC)))
            .baseValue(
                row.get(ADDITIONAL_STAT_3_BASE_VALUE) == null
                    ? null
                    : Float.parseFloat(row.get(ADDITIONAL_STAT_3_BASE_VALUE)))
            .build();
    additionalItemStatEntities.add(entity3);

    return additionalItemStatEntities;
  }

  /**
   * Maps a row from a BDD table to an ItemEntity
   *
   * @param row row from BDD table
   * @return ItemEntity
   */
  public static ItemEntity mapToItemEntity(Map<String, String> row) {
    String id = row.get(BddFieldConstants.Item.ID);
    UUID uuid = UUID.fromString(id);
    String clientId = row.get(BddFieldConstants.Item.CLIENT_ID);
    String blueprintId = row.get(BddFieldConstants.Item.BLUEPRINT_ID);
    String itemType = row.get(BddFieldConstants.Item.ITEM_TYPE);
    String itemRarity = row.get(BddFieldConstants.Item.ITEM_RARITY);
    String isEquipped = row.get(BddFieldConstants.Item.IS_EQUIPPED);
    String level = row.get(BddFieldConstants.Item.LEVEL);

    ItemStat mainStat =
        BddUtils.mapToItemStat(
            row.get(BddFieldConstants.Item.MAIN_STAT_STATISTIC),
            row.get(BddFieldConstants.Item.MAIN_STAT_BASE_VALUE));

    return ItemEntity.builder()
        .id(uuid)
        .blueprintId(blueprintId)
        .clientId(clientId)
        .itemType(ItemType.fromString(itemType))
        .itemRarity(ItemRarity.fromString(itemRarity))
        .isEquipped(Boolean.parseBoolean(isEquipped))
        .level(Integer.parseInt(level))
        .mainStatistic(mainStat.getStatistic())
        .mainBaseValue(mainStat.getBaseValue())
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
            row.get(BddFieldConstants.Item.MAIN_STAT_STATISTIC),
            row.get(BddFieldConstants.Item.MAIN_STAT_BASE_VALUE));
    ItemStat additionalStat1 =
        BddUtils.mapToItemStat(
            row.get(BddFieldConstants.Item.ADDITIONAL_STAT_1_STATISTIC),
            row.get(ADDITIONAL_STAT_1_BASE_VALUE));
    ItemStat additionalStat2 =
        BddUtils.mapToItemStat(
            row.get(BddFieldConstants.Item.ADDITIONAL_STAT_2_STATISTIC),
            row.get(BddFieldConstants.Item.ADDITIONAL_STAT_2_BASE_VALUE));
    ItemStat additionalStat3 =
        BddUtils.mapToItemStat(
            row.get(BddFieldConstants.Item.ADDITIONAL_STAT_3_STATISTIC),
            row.get(BddFieldConstants.Item.ADDITIONAL_STAT_3_BASE_VALUE));

    List<ItemStatDto> additionalStats =
        List.of(additionalStat1, additionalStat2, additionalStat3).stream()
            .map(itemStat -> new ItemStatDto(itemStat.getStatistic(), itemStat.getBaseValue()))
            .toList();
    ItemStatDto mainStatDto = new ItemStatDto(mainStat.getStatistic(), mainStat.getBaseValue());

    return new ItemRequest(
        clientId,
        itemType,
        blueprintId,
        itemRarity,
        isEquipped,
        level,
        mainStatDto,
        additionalStats);
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
    String wave = row.get(BddFieldConstants.Stage.WAVE);

    long currentStageLong = Long.parseLong(currentStage);
    long maxStageLong = Long.parseLong(maxStage);
    long waveLong = Long.parseLong(wave);

    return Stage.builder()
        .currentStage(currentStageLong)
        .maxStage(maxStageLong)
        .wave(waveLong)
        .build();
  }

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
    String wave = row.get(BddFieldConstants.Stage.WAVE);

    if (currentStage == null || maxStage == null || wave == null) {
      throw new IllegalArgumentException("Current, maximum stage and wave cannot be null");
    }

    long currentStageLong = Long.parseLong(currentStage);
    long maxStageLong = Long.parseLong(maxStage);
    long waveLong = Long.parseLong(wave);

    return StageResponse.builder()
        .currentStage(currentStageLong)
        .maxStage(maxStageLong)
        .wave(waveLong)
        .build();
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
    if (currency.gold() == null && currency.amethyst() == null && currency.diamond() == null) {
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

    if (stage.currentStage() == null && stage.maxStage() == null) {
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

    if (characteristics.attack() == null
        || characteristics.health() == null
        || characteristics.critChance() == null
        || characteristics.critDamage() == null
        || characteristics.resistance() == null) {
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
    String email = row.get(BddFieldConstants.UserCreationRequest.EMAIL);
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
    String email = row.get(BddFieldConstants.AdminUserCreationRequest.USERNAME);
    String enabled = row.get(BddFieldConstants.AdminUserCreationRequest.ENABLED);
    String password = row.get(BddFieldConstants.AdminUserCreationRequest.PASSWORD);
    String verified = row.get(BddFieldConstants.AdminUserCreationRequest.EMAIL_VERIFIED);
    String roles = row.get(BddFieldConstants.AdminUserCreationRequest.USER_ROLES);

    Boolean enabledBoolean = Boolean.parseBoolean(enabled);
    Boolean verifiedBoolean = Boolean.parseBoolean(verified);

    List<String> userRoles = roles == null ? null : Arrays.stream(roles.split(COMMA)).toList();

    return new AdminUserCreationRequest(
        firstName, lastName, enabledBoolean, password, verifiedBoolean, email, userRoles);
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
   * Maps a row from a BDD table to a GlobalInfo
   *
   * @param row row from BDD table
   * @return GlobalInfo
   */
  public static GlobalInfo mapToGlobalInfo(Map<String, String> row) {
    String nbGameSaves = row.get(BddFieldConstants.GlobalInfo.GAME_SAVE_COUNTER);
    String nbUsers = row.get(BddFieldConstants.GlobalInfo.USER_COUNTER);
    String now = row.get(BddFieldConstants.GlobalInfo.NOW);

    Long nbGameSavesLong = Long.parseLong(nbGameSaves);
    Long nbUsersLong = Long.parseLong(nbUsers);

    Instant nowInstant = Instant.parse(now);

    return new GlobalInfo(nowInstant, nbGameSavesLong, nbUsersLong);
  }

  /**
   * Maps a row from a BDD table to a GlobalInfoResponse
   *
   * @param row row from BDD table
   * @return GlobalInfoResponse
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
