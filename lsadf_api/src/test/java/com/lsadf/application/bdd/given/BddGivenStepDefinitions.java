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
package com.lsadf.application.bdd.given;

import static org.assertj.core.api.Assertions.assertThat;

import com.lsadf.application.bdd.BddFieldConstants;
import com.lsadf.application.bdd.BddLoader;
import com.lsadf.application.bdd.BddUtils;
import com.lsadf.application.bdd.CacheEntryType;
import com.lsadf.core.domain.game.characteristics.Characteristics;
import com.lsadf.core.domain.game.currency.Currency;
import com.lsadf.core.domain.game.stage.Stage;
import com.lsadf.core.infra.exceptions.http.NotFoundException;
import com.lsadf.core.infra.persistence.game.game_save.GameSaveEntity;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

/** Step definitions for the given steps in the BDD scenarios */
@Slf4j(topic = "[GIVEN STEP DEFINITIONS]")
public class BddGivenStepDefinitions extends BddLoader {

  @Given("^the BDD engine is ready$")
  public void given_the_bdd_engine_is_ready() {
    BddUtils.initTestRestTemplate(testRestTemplate);

    log.info("BDD engine is ready. Using port: {}", this.serverPort);
  }

  @Given("^the time clock set to the present$")
  public void given_the_time_clock_set_to_the_present() {
    log.info("Setting time clock to the present...");
    this.clockService.setClock(Clock.systemDefaultZone());
    log.info("Time clock set to the present");
  }

  @Given("^the time clock set to the following value (.*)$")
  public void given_the_time_clock_set_to_the_following_value(String time) {
    log.info("Setting time clock to the following value: {}", time);
    Instant instant = Instant.parse(time);
    ZoneId zoneId = clockService.getClock().getZone();
    clockService.setClock(Clock.fixed(instant, zoneId));
    log.info("Time clock set to the following value: {}", time);
  }

  @Given("^the cache is enabled$")
  public void given_the_cache_is_enabled() {
    log.info("Checking cache status...");
    boolean cacheEnabled = redisCacheService.isEnabled();
    if (!cacheEnabled) {
      log.info("Cache is disabled. Enabling cache...");
      redisCacheService.toggleCacheEnabling();
      assertThat(redisCacheService.isEnabled()).isTrue();
      log.info("Cache enabled");
    } else {
      log.info("Cache is already enabled");
    }
  }

  @Given("^the cache is disabled$")
  public void given_the_cache_is_disabled() {
    log.info("Checking cache status...");
    boolean cacheEnabled = redisCacheService.isEnabled();
    if (cacheEnabled) {
      log.info("Cache is enabled. Disabling cache...");
      redisCacheService.toggleCacheEnabling();
      assertThat(redisCacheService.isEnabled()).isFalse();
      log.info("Cache disabled");
    } else {
      log.info("Cache is already disabled");
    }
  }

  @Given("^a clean database$")
  @Transactional
  public void given_i_have_a_clean_database() throws NotFoundException {
    log.info("Cleaning database repositories...");

    this.characteristicsRepository.deleteAll();
    this.currencyRepository.deleteAll();
    this.stageRepository.deleteAll();
    this.inventoryRepository.deleteAll();
    this.gameSaveRepository.deleteAll();

    assertThat(characteristicsRepository.count()).isZero();
    assertThat(currencyRepository.count()).isZero();
    assertThat(stageRepository.count()).isZero();
    assertThat(inventoryRepository.count()).isZero();
    assertThat(gameSaveRepository.count()).isZero();

    // Clear caches
    redisCacheService.clearCaches();
    localCacheService.clearCaches();

    assertThat(characteristicsCache.getAll()).isEmpty();
    assertThat(characteristicsCache.getAllHisto()).isEmpty();
    assertThat(currencyCache.getAll()).isEmpty();
    assertThat(currencyCache.getAllHisto()).isEmpty();
    assertThat(stageCache.getAll()).isEmpty();
    assertThat(stageCache.getAllHisto()).isEmpty();
    assertThat(gameSaveOwnershipCache.getAll()).isEmpty();

    log.info("Database repositories + caches cleaned");
    log.info("Mocks initialized");
  }

  @Given("^the following game saves$")
  public void given_i_have_the_following_game_saves(DataTable dataTable) {
    List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);
    log.info("Creating game saves...");

    rows.forEach(
        row -> {
          GameSaveEntity gameSaveEntity = BddUtils.mapToGameSaveEntity(row);
          GameSaveEntity newEntity = gameSaveRepository.save(gameSaveEntity);
          log.info("Game save created: {}", newEntity);
        });

    log.info("Game saves created");
  }

  @Given("^the following (.*) entries in cache$")
  public void given_the_following_cache_entries_in_cache(String cacheType, DataTable dataTable) {
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
      case GAME_SAVE_OWNERSHIP ->
          rows.forEach(
              row -> {
                String gameSaveId =
                    row.get(BddFieldConstants.GameSaveOwnershipCacheEntry.GAME_SAVE_ID);
                String userId = row.get(BddFieldConstants.GameSaveOwnershipCacheEntry.USER_EMAIL);
                gameSaveOwnershipCache.set(gameSaveId, userId);
                count.getAndIncrement();
              });
      default -> throw new IllegalArgumentException("Unknown cache type: " + cacheType);
    }

    int finalCount = count.get();

    log.info("{} {} entries in cache created", finalCount, cacheType);
  }

  @Given("^the expiration seconds properties set to (.*)$")
  public void given_the_expiration_seconds_properties_set_to(int expirationSeconds) {
    log.info("Setting expiration seconds properties to {}", expirationSeconds);
    characteristicsCache.setExpiration(expirationSeconds);
    currencyCache.setExpiration(expirationSeconds);
    stageCache.setExpiration(expirationSeconds);
    gameSaveOwnershipCache.setExpiration(expirationSeconds);
  }
}
