/*
 * Copyright © 2024-2026 LSDAF
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
package com.lsadf.bdd.step_definition.then;

import static com.lsadf.bdd.config.BddFieldConstants.Item.CLIENT_ID;
import static org.assertj.core.api.Assertions.assertThat;

import com.lsadf.bdd.util.BddUtils;
import com.lsadf.core.application.game.inventory.InventoryService;
import com.lsadf.core.infra.web.dto.common.game.inventory.ItemStatDto;
import com.lsadf.core.infra.web.dto.response.game.inventory.ItemResponse;
import io.cucumber.datatable.DataTable;
import java.util.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j(topic = "[ITEM THEN STEP DEFINITIONS]")
@Component
public class BddThenItemStepDefinitions {

  @Autowired protected Stack<Set<ItemResponse>> itemResponseSetStack;
  @Autowired protected InventoryService inventoryService;

  public void thenTheInventoryOfTheGameSaveWithIdShouldBeEmpty(String gameSaveId) {
    UUID uuid = UUID.fromString(gameSaveId);
    var results = inventoryService.getInventoryItems(uuid);
    assertThat(results).isEmpty();
  }

  public void thenTheResponseShouldHaveTheFollowingItemsInTheInventory(DataTable dataTable) {
    List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);

    Set<ItemResponse> inventory = itemResponseSetStack.peek();
    for (Map<String, String> row : rows) {
      ItemResponse actual =
          inventory.stream()
              .filter(g -> g.clientId().equals(row.get(CLIENT_ID)))
              .findFirst()
              .orElseThrow();

      ItemResponse expected = BddUtils.mapToItemResponse(row);

      assertThat(actual)
          .usingRecursiveComparison()
          .ignoringFields("id", "additionalStats")
          .isEqualTo(expected);

      assertThat(actual.additionalStats())
          .contains(expected.additionalStats().toArray(new ItemStatDto[0]));
    }
  }
}
