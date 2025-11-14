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

package com.lsadf.bdd.step_definition.then;

import static org.assertj.core.api.Assertions.assertThat;

import com.lsadf.bdd.util.BddUtils;
import com.lsadf.core.application.game.save.GameSaveService;
import com.lsadf.core.domain.game.save.GameSave;
import com.lsadf.core.infra.web.dto.response.ApiResponse;
import com.lsadf.core.infra.web.dto.response.game.save.GameSaveResponse;
import io.cucumber.datatable.DataTable;
import java.util.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j(topic = "[GAME SAVE THEN STEP DEFINITIONS]")
@Component
public class BddThenGameSaveStepDefinitions {

  @Autowired protected Stack<ApiResponse<?>> responseStack;
  @Autowired protected Stack<List<GameSaveResponse>> gameSaveResponseListStack;
  @Autowired protected GameSaveService gameSaveService;

  public void thenTheResponseShouldHaveTheFollowingGameSavesInExactOrder(DataTable dataTable) {
    List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);

    List<GameSaveResponse> actual = gameSaveResponseListStack.peek();

    List<GameSaveResponse> expected = new ArrayList<>();

    for (Map<String, String> row : rows) {
      GameSaveResponse response = BddUtils.mapToGameSaveResponse(row);
      expected.add(response);
    }

    assertThat(actual).hasSameSizeAs(expected);

    for (int i = 0; i < actual.size(); i++) {
      GameSaveResponse actualGameSaveResponse = actual.get(i);
      GameSaveResponse expectedGameSaveResponse = expected.get(i);
      assertThat(actualGameSaveResponse)
          .usingRecursiveComparison()
          .ignoringFields("metadata.id", "metadata.createdAt", "metadata.updatedAt")
          .isEqualTo(expectedGameSaveResponse);
    }
  }

  public void thenTheNumberOfGameSavesShouldBe(int expected) {
    var list = gameSaveService.getGameSaves();
    assertThat(list).hasSize(expected);
  }

  public void thenResponseShouldHaveFollowingGameSaveResponse(DataTable dataTable) {
    List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);

    if (rows.size() > 1) {
      throw new IllegalArgumentException("Expected only one row in the DataTable");
    }

    Map<String, String> row = rows.get(0);

    GameSaveResponse actual = (GameSaveResponse) responseStack.peek().data();
    GameSaveResponse expected = BddUtils.mapToGameSaveResponse(row);

    assertThat(actual)
        .usingRecursiveComparison()
        .ignoringFields("metadata.id", "createdAt", "updatedAt")
        .ignoringExpectedNullFields()
        .isEqualTo(expected);
  }

  public void thenResponseShouldHaveFollowingGameSaves(DataTable dataTable) {
    List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);

    for (Map<String, String> row : rows) {
      List<GameSaveResponse> list = gameSaveResponseListStack.peek();
      String id = row.get("id");
      UUID uuid = UUID.fromString(id);
      GameSaveResponse actual =
          list.stream().filter(g -> g.metadata().id().equals(uuid)).findFirst().orElseThrow();
      GameSave expected = BddUtils.mapToGameSave(row);

      assertThat(actual)
          .usingRecursiveComparison()
          .ignoringFields("id", "metadata.createdAt", "metadata.updatedAt")
          .isEqualTo(expected);
    }
  }
}
