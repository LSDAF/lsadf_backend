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

import static org.assertj.core.api.Assertions.assertThat;

import com.lsadf.bdd.util.BddUtils;
import com.lsadf.core.application.clock.ClockService;
import com.lsadf.core.infra.web.dto.response.ApiResponse;
import com.lsadf.core.infra.web.dto.response.game.session.GameSessionResponse;
import io.cucumber.datatable.DataTable;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j(topic = "[GAME SESSION THEN STEP DEFINITIONS]")
@Component
public class BddThenGameSessionStepDefinitions {

  @Autowired protected ClockService clockService;
  @Autowired protected Stack<ApiResponse<?>> responseStack;

  public void thenResponseShouldHaveFollowingGameSessionResponse(DataTable dataTable) {
    List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);
    if (rows.size() > 1) {
      throw new IllegalArgumentException("Expected only one row in the DataTable");
    }

    Map<String, String> row = rows.get(0);
    GameSessionResponse actual = (GameSessionResponse) responseStack.peek().data();
    GameSessionResponse expected = BddUtils.mapToGameSessionResponse(row);

    assertThat(actual).isNotNull();
    assertThat(actual).usingRecursiveComparison().ignoringFields("endTime").isEqualTo(expected);

    Instant now = clockService.nowInstant();
    assertThat(actual.endTime()).isAfter(now);
  }
}
