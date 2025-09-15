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
import com.lsadf.core.bdd.BddUtils;
import com.lsadf.core.domain.game.save.GameSave;
import com.lsadf.core.domain.user.UserInfo;
import com.lsadf.core.infra.web.dto.response.game.save.GameSaveResponse;
import com.lsadf.core.infra.web.dto.response.game.save.characteristics.CharacteristicsResponse;
import com.lsadf.core.infra.web.dto.response.game.save.characteristics.CharacteristicsResponseMapper;
import com.lsadf.core.infra.web.dto.response.game.save.currency.CurrencyResponse;
import com.lsadf.core.infra.web.dto.response.game.save.stage.StageResponse;
import com.lsadf.core.infra.web.dto.response.user.UserInfoResponse;
import com.lsadf.core.infra.web.dto.response.user.UserInfoResponseMapper;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Then;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;

/** Step definitions for the then steps in the BDD scenarios */
@Slf4j(topic = "[THEN STEP DEFINITIONS]")
public class BddThenStepDefinitions extends BddLoader {

  private static final UserInfoResponseMapper userInfoResponseMapper =
      UserInfoResponseMapper.INSTANCE;
  private static final CharacteristicsResponseMapper characteristicsResponseMapper =
      CharacteristicsResponseMapper.INSTANCE;

  @Then("^the response status code should be (.*)$")
  public void thenResponseStatusCodeShouldBe(int statusCode) {
    int actual = responseStack.peek().status();
    assertThat(actual).isEqualTo(statusCode);
  }

  @Then("^the response should have the following UserInfo$")
  public void thenResponseShouldHaveFollowingUserInfo(DataTable dataTable) {
    List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);

    if (rows.size() > 1) {
      throw new IllegalArgumentException("Expected only one row in the DataTable");
    }

    Map<String, String> row = rows.get(0);

    UserInfoResponse actual = (UserInfoResponse) responseStack.peek().data();
    UserInfo expected = BddUtils.mapToUserInfo(row);
    UserInfoResponse expectedResponse = userInfoResponseMapper.map(expected);

    assertThat(actual)
        .usingRecursiveComparison()
        .ignoringFields("id", "createdAt", "updatedAt", "roles")
        .isEqualTo(expectedResponse);

    assertThat(actual.roles()).containsAll(expectedResponse.roles());
  }

  @Then("^the response should have the following StageResponse$")
  public void thenResponseShouldHaveFollowingStageResponse(DataTable dataTable) {
    List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);

    if (rows.size() > 1) {
      throw new IllegalArgumentException("Expected only one row in the DataTable");
    }

    Map<String, String> row = rows.get(0);

    StageResponse expected = BddUtils.mapToStageResponse(row);
    StageResponse actual = (StageResponse) responseStack.peek().data();

    assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
  }

  @Then("^the response should have the following Characteristics$")
  public void thenResponseShouldHaveFollowingCharacteristics(DataTable dataTable) {
    List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);

    if (rows.size() > 1) {
      throw new IllegalArgumentException("Expected only one row in the DataTable");
    }

    Map<String, String> row = rows.get(0);

    CharacteristicsResponse expected =
        characteristicsResponseMapper.map(BddUtils.mapToCharacteristics(row));
    CharacteristicsResponse actual = (CharacteristicsResponse) responseStack.peek().data();

    assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
  }

  @Then("^the response should have the following CurrencyResponse$")
  public void thenResponseShouldHaveFollowingCurrency(DataTable dataTable) {
    List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);

    if (rows.size() > 1) {
      throw new IllegalArgumentException("Expected only one row in the DataTable");
    }

    Map<String, String> row = rows.get(0);

    CurrencyResponse expected = BddUtils.mapToCurrencyResponse(row);
    CurrencyResponse actual = (CurrencyResponse) responseStack.peek().data();

    assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
  }

  @Then("^the response should have the following GameSaveResponse$")
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

  @Then("^the response should have the following GameSaveResponses$")
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
