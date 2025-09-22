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
package com.lsadf.application.bdd.step_definition.given;

import static org.assertj.core.api.Assertions.assertThat;

import com.lsadf.application.bdd.BddLoader;
import com.lsadf.application.bdd.CacheEntryType;
import com.lsadf.core.application.shared.CachePort;
import com.lsadf.core.bdd.BddFieldConstants;
import com.lsadf.core.bdd.BddUtils;
import com.lsadf.core.domain.game.save.characteristics.Characteristics;
import com.lsadf.core.domain.game.save.currency.Currency;
import com.lsadf.core.domain.game.save.metadata.GameMetadata;
import com.lsadf.core.domain.game.save.stage.Stage;
import com.lsadf.core.exception.http.NotFoundException;
import com.lsadf.core.infra.persistence.impl.game.save.characteristics.CharacteristicsEntity;
import com.lsadf.core.infra.persistence.impl.game.save.currency.CurrencyEntity;
import com.lsadf.core.infra.persistence.impl.game.save.metadata.GameMetadataEntity;
import com.lsadf.core.infra.persistence.impl.game.save.stage.StageEntity;
import com.lsadf.core.infra.persistence.impl.game.session.GameSessionEntity;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import java.time.Clock;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/** Step definitions for the given steps in the BDD scenarios */
@Slf4j(topic = "[GIVEN STEP DEFINITIONS]")
public class BddGivenStepDefinitions extends BddLoader {

  @Autowired List<CachePort<?>> cachePorts;

  @Given("^the BDD engine is ready$")
  public void givenBddEngineIsReady() {
    BddUtils.initTestRestTemplate(testRestTemplate);

    log.info("BDD engine is ready. Using port: {}", this.serverPort);
  }

  @Given("^the time clock set to the present$")
  public void givenTimeClockSetToPresent() {
    log.info("Setting time clock to the present...");
    this.clockService.setClock(Clock.systemDefaultZone());
    log.info("Time clock set to the present");
  }

  @Given("^the cache is enabled$")
  public void givenCacheIsEnabled() {
    log.info("Checking cache status...");
    boolean cacheEnabled = redisCacheManager.isEnabled();
    if (!cacheEnabled) {
      log.info("Cache is disabled. Enabling cache...");
      redisCacheManager.toggleCacheEnabling();
      assertThat(redisCacheManager.isEnabled()).isTrue();
      log.info("Cache enabled");
    } else {
      log.info("Cache is already enabled");
    }
  }

  @Given("^the cache is disabled$")
  public void givenCacheIsDisabled() {
    log.info("Checking cache status...");
    boolean cacheEnabled = redisCacheManager.isEnabled();
    if (cacheEnabled) {
      log.info("Cache is enabled. Disabling cache...");
      redisCacheManager.toggleCacheEnabling();
      assertThat(redisCacheManager.isEnabled()).isFalse();
      log.info("Cache disabled");
    } else {
      log.info("Cache is already disabled");
    }
  }

  @Given("^a clean database$")
  @Transactional
  public void givenCleanDatabase() throws NotFoundException {
    log.info("Cleaning database repositories...");

    // Clear caches
    cacheFlushService.flushGameSaves();
    redisCacheManager.clearCaches();

    // delete game saves
    this.gameMetadataRepository.deleteAllGameSaveEntities();

    // clean game sessions
    this.gameSessionRepository.deleteAllGameSessions();

    assertThat(characteristicsRepository.count()).isZero();
    assertThat(currencyRepository.count()).isZero();
    assertThat(stageRepository.count()).isZero();
    assertThat(itemRepository.count()).isZero();
    assertThat(additionalItemStatsRepository.count()).isZero();
    assertThat(gameMetadataRepository.count()).isZero();
    assertThat(gameSessionRepository.count()).isZero();

    assertThat(characteristicsCache.getAll()).isEmpty();
    assertThat(currencyCache.getAll()).isEmpty();
    assertThat(stageCache.getAll()).isEmpty();
    assertThat(gameMetadataCache.getAll()).isEmpty();
    assertThat(gameSessionCache.getAll()).isEmpty();

    log.info("Database repositories + caches cleaned");
    log.info("Mocks initialized");
  }

  @Given("^the following game sessions$")
  @Transactional
  public void givenFollowingGameSessions(DataTable dataTable) {
    List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);
    log.info("Creating game sessions...");

    rows.forEach(
        row -> {
          GameSessionEntity gameSessionEntity = BddUtils.mapToGameSaveSessionEntity(row);
          gameSessionRepository.createNewGameSession(
              gameSessionEntity.getId(),
              gameSessionEntity.getGameSaveId(),
              gameSessionEntity.getEndTime(),
              gameSessionEntity.isCancelled(),
              gameSessionEntity.getVersion());
        });

    log.info("Game sessions created");
  }

  @Given("^the following game saves$")
  @Transactional
  public void givenFollowingGameSaves(DataTable dataTable) {
    List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);
    log.info("Creating game saves...");

    rows.forEach(
        row -> {
          GameMetadataEntity gameMetadataEntity = BddUtils.mapToGameSaveEntity(row);
          GameMetadataEntity newEntity =
              gameMetadataEntity.getId() != null
                  ? gameMetadataRepository.createNewGameSaveEntity(
                      gameMetadataEntity.getId(),
                      gameMetadataEntity.getUserEmail(),
                      gameMetadataEntity.getNickname())
                  : gameMetadataRepository.createNewGameSaveEntity(
                      gameMetadataEntity.getUserEmail(), gameMetadataEntity.getNickname());
          CurrencyEntity currencyEntity = BddUtils.mapToCurrencyEntity(row);
          currencyRepository.createNewCurrencyEntity(
              newEntity.getId(),
              currencyEntity.getGoldAmount(),
              currencyEntity.getDiamondAmount(),
              currencyEntity.getEmeraldAmount(),
              currencyEntity.getAmethystAmount());
          StageEntity stageEntity = BddUtils.mapToStageEntity(row);
          stageRepository.createNewStageEntity(
              newEntity.getId(), stageEntity.getCurrentStage(), stageEntity.getMaxStage());
          CharacteristicsEntity characteristicsEntity = BddUtils.mapToCharacteristicsEntity(row);
          characteristicsRepository.createNewCharacteristicsEntity(
              newEntity.getId(),
              characteristicsEntity.getAttack(),
              characteristicsEntity.getCritChance(),
              characteristicsEntity.getCritDamage(),
              characteristicsEntity.getHealth(),
              characteristicsEntity.getResistance());
          log.info("Game save created: {}", newEntity);
        });

    log.info("Game saves created");
  }

  @Given("^the following (.*) entries in cache$")
  public void givenFollowingCacheEntries(String cacheType, DataTable dataTable) {
    List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);
    log.info("Creating {} entries in cache...", cacheType);
    CacheEntryType cacheEntryType = CacheEntryType.fromString(cacheType);
    AtomicInteger count = new AtomicInteger();
    switch (cacheEntryType) {
      case CHARACTERISTICS ->
          rows.forEach(
              row -> {
                String gameSaveId =
                    row.get(BddFieldConstants.CharacteristicsCacheEntry.GAME_SAVE_ID);
                Characteristics characteristics = BddUtils.mapToCharacteristics(row);
                characteristicsCache.set(gameSaveId, characteristics);
                count.getAndIncrement();
              });
      case CURRENCY ->
          rows.forEach(
              row -> {
                String gameSaveId = row.get(BddFieldConstants.CurrencyCacheEntry.GAME_SAVE_ID);
                Currency currency = BddUtils.mapToCurrency(row);
                currencyCache.set(gameSaveId, currency);
                count.getAndIncrement();
              });
      case STAGE ->
          rows.forEach(
              row -> {
                String gameSaveId = row.get(BddFieldConstants.StageCacheEntry.GAME_SAVE_ID);
                Stage stage = BddUtils.mapToStage(row);
                stageCache.set(gameSaveId, stage);
                count.getAndIncrement();
              });
      case GAME_METADATA ->
          rows.forEach(
              row -> {
                String gameSaveId =
                    row.get(BddFieldConstants.GameSaveOwnershipCacheEntry.GAME_SAVE_ID);
                String userId = row.get(BddFieldConstants.GameSaveOwnershipCacheEntry.USER_EMAIL);
                GameMetadata gameMetadata =
                    new GameMetadata(UUID.fromString(gameSaveId), userId, null, null, null);
                gameMetadataCache.set(gameSaveId, gameMetadata);
                count.getAndIncrement();
              });
      default -> throw new IllegalArgumentException("Unknown cache type: " + cacheType);
    }

    int finalCount = count.get();

    log.info("{} {} entries in cache created", finalCount, cacheType);
  }
}
