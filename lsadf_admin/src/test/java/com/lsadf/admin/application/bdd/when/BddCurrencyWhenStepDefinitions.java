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
package com.lsadf.admin.application.bdd.when;

import static org.assertj.core.api.Assertions.assertThat;

import com.lsadf.admin.application.bdd.BddLoader;
import com.lsadf.core.bdd.BddUtils;
import com.lsadf.core.domain.game.save.currency.Currency;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.When;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;

/** Step definitions for the when steps in the BDD scenarios */
@Slf4j(topic = "[CURRENCY WHEN STEP DEFINITIONS]")
public class BddCurrencyWhenStepDefinitions extends BddLoader {
  @When("^we want to get the currencies for the game save with id (.*)$")
  public void when_we_want_to_get_the_currencies_for_the_game_save_with_id(String gameSaveId) {
    try {
      log.info("Getting currencies for game save with id: {}", gameSaveId);
      UUID uuid = UUID.fromString(gameSaveId);
      Currency currency = this.currencyService.getCurrency(uuid);
      currencyStack.push(currency);
    } catch (Exception e) {
      exceptionStack.push(e);
    }
  }

  @When(
      "^we want to set the following currencies for the game save with id (.*) with toCache to (.*)$")
  public void when_we_want_to_set_the_currencies_for_the_game_save_with_id_to_with_cache(
      String gameSaveId, boolean toCache, DataTable dataTable) {
    var data = dataTable.asMaps(String.class, String.class);
    assertThat(data).hasSize(1);

    Currency currency = BddUtils.mapToCurrency(data.get(0));

    try {
      log.info("Setting {} for game save with id: {}", currency, gameSaveId);
      UUID uuid = UUID.fromString(gameSaveId);
      this.currencyService.saveCurrency(uuid, currency, toCache);
    } catch (Exception e) {
      exceptionStack.push(e);
    }
  }
}
