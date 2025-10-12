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

package com.lsadf.bdd.step_definition.given;

import static org.assertj.core.api.Assertions.assertThat;

import com.lsadf.bdd.config.BddFieldConstants;
import com.lsadf.bdd.config.CacheEntryType;
import com.lsadf.bdd.util.BddUtils;
import com.lsadf.core.application.cache.CacheManager;
import com.lsadf.core.application.clock.ClockService;
import com.lsadf.core.application.shared.CachePort;
import com.lsadf.core.application.shared.HistoCachePort;
import com.lsadf.core.domain.game.save.characteristics.Characteristics;
import com.lsadf.core.domain.game.save.currency.Currency;
import com.lsadf.core.domain.game.save.metadata.GameMetadata;
import com.lsadf.core.domain.game.save.stage.Stage;
import com.lsadf.core.domain.game.session.GameSession;
import com.lsadf.core.exception.http.NotFoundException;
import com.lsadf.core.infra.persistence.impl.game.inventory.AdditionalItemStatsRepository;
import com.lsadf.core.infra.persistence.impl.game.inventory.ItemEntity;
import com.lsadf.core.infra.persistence.impl.game.inventory.ItemRepository;
import com.lsadf.core.infra.persistence.impl.game.save.characteristics.CharacteristicsEntity;
import com.lsadf.core.infra.persistence.impl.game.save.characteristics.CharacteristicsRepository;
import com.lsadf.core.infra.persistence.impl.game.save.currency.CurrencyEntity;
import com.lsadf.core.infra.persistence.impl.game.save.currency.CurrencyRepository;
import com.lsadf.core.infra.persistence.impl.game.save.metadata.GameMetadataEntity;
import com.lsadf.core.infra.persistence.impl.game.save.metadata.GameMetadataRepository;
import com.lsadf.core.infra.persistence.impl.game.save.stage.StageEntity;
import com.lsadf.core.infra.persistence.impl.game.save.stage.StageRepository;
import com.lsadf.core.infra.persistence.impl.game.session.GameSessionEntity;
import com.lsadf.core.infra.persistence.impl.game.session.GameSessionRepository;
import com.lsadf.core.infra.valkey.cache.flush.CacheFlushService;
import io.cucumber.datatable.DataTable;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/** Step definitions for the given steps in the BDD scenarios */
@Slf4j(topic = "[GIVEN STEP DEFINITIONS]")
@Component
public class BddGivenStepDefinitions {
  @Autowired protected List<CachePort<?>> cachePorts;

  @Autowired protected TestRestTemplate testRestTemplate;

  @Autowired protected ClockService clockService;

  @Autowired protected CacheFlushService cacheFlushService;

  @Autowired protected CacheManager redisCacheManager;

  @Autowired protected HistoCachePort<Characteristics> characteristicsCache;

  @Autowired protected HistoCachePort<Currency> currencyCache;

  @Autowired protected HistoCachePort<Stage> stageCache;

  @Autowired protected CachePort<GameMetadata> gameMetadataCache;

  @Autowired protected HistoCachePort<GameSession> gameSessionCache;

  @Autowired protected CharacteristicsRepository characteristicsRepository;

  @Autowired protected CurrencyRepository currencyRepository;

  @Autowired protected StageRepository stageRepository;

  @Autowired protected ItemRepository itemRepository;

  @Autowired protected AdditionalItemStatsRepository additionalItemStatsRepository;

  @Autowired protected GameMetadataRepository gameMetadataRepository;

  @Autowired protected GameSessionRepository gameSessionRepository;

  public void givenBddEngineIsReady() {
    BddUtils.initTestRestTemplate(testRestTemplate);

    log.info("BDD engine is ready");
  }

  public void givenTimeClockSetToPresent() {
    log.info("Setting time clock to the present...");
    this.clockService.setClock(Clock.systemDefaultZone());
    log.info("Time clock set to the present");
  }

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

  @Transactional
  public void givenFollowingItemsInInventory(String gameSaveId, DataTable dataTable) {
    List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);

    UUID uuid = UUID.fromString(gameSaveId);
    var exists = gameMetadataRepository.existsById(uuid);
    if (!exists) {
      throw new NotFoundException("Game save with id: " + gameSaveId + " not found.");
    }

    log.info("Creating items...");
    rows.forEach(
        row -> {
          ItemEntity itemEntity = BddUtils.mapToItemEntity(row);
          itemEntity.setGameSaveId(uuid);
          ItemEntity newItemEntity;
          if (itemEntity.getId() != null) {
            newItemEntity =
                itemRepository.createNewItemEntity(
                    itemEntity.getId(),
                    itemEntity.getGameSaveId(),
                    itemEntity.getClientId(),
                    itemEntity.getBlueprintId(),
                    itemEntity.getItemType(),
                    itemEntity.getItemRarity(),
                    itemEntity.getIsEquipped(),
                    itemEntity.getLevel(),
                    itemEntity.getMainStatistic(),
                    itemEntity.getMainBaseValue());
          } else {
            newItemEntity =
                itemRepository.createNewItemEntity(
                    itemEntity.getGameSaveId(),
                    itemEntity.getClientId(),
                    itemEntity.getBlueprintId(),
                    itemEntity.getItemType(),
                    itemEntity.getItemRarity(),
                    itemEntity.getIsEquipped(),
                    itemEntity.getLevel(),
                    itemEntity.getMainStatistic(),
                    itemEntity.getMainBaseValue());
          }

          var additionalStats = BddUtils.mapToAdditionalItemStatEntity(row, newItemEntity.getId());
          additionalStats.forEach(
              additionalItemStatEntity -> {
                additionalItemStatsRepository.createNewAdditionalItemStatEntity(
                    additionalItemStatEntity.getItemId(),
                    additionalItemStatEntity.getStatistic(),
                    additionalItemStatEntity.getBaseValue());
              });
          log.info("Item created: {}", newItemEntity);
        });

    log.info("Items created");
  }

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

  public void givenTheTimeClockSetToTheFollowingValue(String time) {
    log.info("Setting time clock to the following value: {}", time);
    Instant instant = Instant.parse(time);
    ZoneId zoneId = clockService.getClock().getZone();
    clockService.setClock(Clock.fixed(instant, zoneId));
    log.info("Time clock set to the following value: {}", time);
  }

  @Transactional
  public void givenIHaveACleanDatabase() throws NotFoundException {
    log.info("Cleaning database repositories...");

    this.gameMetadataRepository.deleteAllGameSaveEntities();

    assertThat(characteristicsRepository.count()).isZero();
    assertThat(currencyRepository.count()).isZero();
    assertThat(stageRepository.count()).isZero();
    assertThat(itemRepository.count()).isZero();
    assertThat(additionalItemStatsRepository.count()).isZero();
    assertThat(gameMetadataRepository.count()).isZero();

    // Clear caches
    redisCacheManager.clearCaches();

    assertThat(characteristicsCache.getAll()).isEmpty();
    assertThat(characteristicsCache.getAllHisto()).isEmpty();
    assertThat(currencyCache.getAll()).isEmpty();
    assertThat(currencyCache.getAllHisto()).isEmpty();
    assertThat(stageCache.getAll()).isEmpty();
    assertThat(stageCache.getAllHisto()).isEmpty();
    assertThat(gameMetadataCache.getAll()).isEmpty();

    log.info("Database repositories + caches cleaned");
    log.info("Mocks initialized");
  }

  public void givenIHaveTheFollowingGameSaves(DataTable dataTable) {
    List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);
    log.info("Creating game saves...");

    rows.forEach(
        row -> {
          GameMetadataEntity gameMetadataEntity = BddUtils.mapToGameSaveEntity(row);
          CurrencyEntity currencyEntity = BddUtils.mapToCurrencyEntity(row);
          CharacteristicsEntity characteristicsEntity = BddUtils.mapToCharacteristicsEntity(row);
          StageEntity stageEntity = BddUtils.mapToStageEntity(row);

          GameMetadataEntity newGameMetadataEntity =
              gameMetadataRepository.createNewGameSaveEntity(
                  gameMetadataEntity.getId(),
                  gameMetadataEntity.getUserEmail(),
                  gameMetadataEntity.getNickname());
          currencyRepository.createNewCurrencyEntity(
              newGameMetadataEntity.getId(),
              currencyEntity.getGoldAmount(),
              currencyEntity.getDiamondAmount(),
              currencyEntity.getEmeraldAmount(),
              currencyEntity.getAmethystAmount());
          stageRepository.createNewStageEntity(
              newGameMetadataEntity.getId(),
              stageEntity.getCurrentStage(),
              stageEntity.getMaxStage());
          characteristicsRepository.createNewCharacteristicsEntity(
              newGameMetadataEntity.getId(),
              characteristicsEntity.getAttack(),
              characteristicsEntity.getCritChance(),
              characteristicsEntity.getCritDamage(),
              characteristicsEntity.getHealth(),
              characteristicsEntity.getResistance());
          log.info("Game save created: {}", newGameMetadataEntity);
        });

    log.info("Game saves created");
  }

  public void givenTheFollowingCacheEntriesInCache(String cacheType, DataTable dataTable) {
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
      default -> throw new IllegalArgumentException("Unknown cache type: " + cacheType);
    }

    int finalCount = count.get();

    log.info("{} {} entries in cache created", finalCount, cacheType);
  }
}
