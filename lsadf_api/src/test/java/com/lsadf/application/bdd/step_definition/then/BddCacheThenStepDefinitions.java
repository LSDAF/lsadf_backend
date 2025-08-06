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
package com.lsadf.application.bdd.step_definition.then;

import static org.assertj.core.api.Assertions.assertThat;

import com.lsadf.application.bdd.BddLoader;
import com.lsadf.application.bdd.CacheEntryType;
import io.cucumber.java.en.Then;
import lombok.extern.slf4j.Slf4j;

/** Step definitions for the cache related then steps in the BDD scenarios */
@Slf4j(topic = "[CACHE THEN STEP DEFINITIONS]")
public class BddCacheThenStepDefinitions extends BddLoader {

  @Then("^the (.*) cache should be empty$")
  public void thenCacheShouldBeEmpty(String cacheType) {
    log.info("Checking {} if cache is empty...", cacheType);
    CacheEntryType cacheEntryType = CacheEntryType.fromString(cacheType);
    switch (cacheEntryType) {
      case CHARACTERISTICS -> assertThat(characteristicsCache.getAll()).isEmpty();
      case CHARACTERISTICS_HISTO -> assertThat(characteristicsCache.getAllHisto()).isEmpty();
      case CURRENCY -> assertThat(currencyCache.getAll()).isEmpty();
      case CURRENCY_HISTO -> assertThat(currencyCache.getAllHisto()).isEmpty();
      case GAME_METADATA -> assertThat(gameMetadataCache.getAll()).isEmpty();
      case STAGE -> assertThat(stageCache.getAll()).isEmpty();
      case STAGE_HISTO -> assertThat(stageCache.getAllHisto()).isEmpty();
    }
  }

}
