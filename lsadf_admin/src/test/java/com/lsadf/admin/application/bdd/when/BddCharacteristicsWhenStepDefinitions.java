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

import static com.lsadf.admin.application.bdd.ParameterizedTypeReferenceUtils.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.lsadf.admin.application.bdd.BddLoader;
import com.lsadf.admin.application.bdd.BddUtils;
import com.lsadf.core.domain.game.characteristics.Characteristics;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.When;
import lombok.extern.slf4j.Slf4j;

/** Step definitions for the when steps in the BDD scenarios */
@Slf4j(topic = "[CHARACTERISTICS WHEN STEP DEFINITIONS]")
public class BddCharacteristicsWhenStepDefinitions extends BddLoader {

  @When("^we want to get the characteristics for the game save with id (.*)$")
  public void when_we_want_to_get_the_characteristics_for_the_game_save_with_id(String gameSaveId) {
    try {
      log.info("Getting characteristics for game save with id: {}", gameSaveId);
      Characteristics characteristics = this.characteristicsService.getCharacteristics(gameSaveId);
      characteristicsStack.push(characteristics);
    } catch (Exception e) {
      exceptionStack.push(e);
    }
  }

  @When(
      "^we want to set the following characteristics for the game save with id (.*) with toCache to (.*)$")
  public void when_we_want_to_set_the_characteristics_for_the_game_save_with_id_to_with_cache(
      String gameSaveId, boolean toCache, DataTable dataTable) {
    var data = dataTable.asMaps(String.class, String.class);
    assertThat(data).hasSize(1);

    Characteristics characteristics = BddUtils.mapToCharacteristics(data.get(0));

    try {
      log.info("Setting {} for game save with id: {}", characteristics, gameSaveId);
      this.characteristicsService.saveCharacteristics(gameSaveId, characteristics, toCache);
    } catch (Exception e) {
      exceptionStack.push(e);
    }
  }
}
