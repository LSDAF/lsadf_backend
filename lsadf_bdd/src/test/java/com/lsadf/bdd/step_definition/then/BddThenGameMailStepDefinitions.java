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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lsadf.bdd.util.BddUtils;
import com.lsadf.core.application.game.mail.GameMailQueryService;
import com.lsadf.core.application.game.mail.GameMailTemplateQueryService;
import com.lsadf.core.domain.game.mail.GameMailAttachment;
import com.lsadf.core.infra.web.dto.response.game.mail.GameMailResponse;
import io.cucumber.datatable.DataTable;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j(topic = "[GAME MAIL THEN STEP DEFINITIONS]")
@Component
public class BddThenGameMailStepDefinitions {

  @Autowired private Stack<List<GameMailResponse>> gameMailResponseListStack;
  @Autowired private Stack<GameMailResponse> gameMailResponseStack;
  @Autowired private Stack<List<GameMailAttachment<?>>> gameMailAttachmentListStack;
  @Autowired private ObjectMapper objectMapper;
  @Autowired private GameMailQueryService gameMailQueryService;
  @Autowired private GameMailTemplateQueryService gameMailTemplateQueryService;

  public void thenResponseShouldHaveFollowingGameMailResponse(DataTable dataTable) {
    List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);
    assertThat(rows).hasSize(1);
    var row = rows.get(0);
    GameMailResponse actual = gameMailResponseStack.peek();
    GameMailResponse expected = BddUtils.mapToGameMailResponse(row);

    assertThat(actual)
        .usingRecursiveComparison()
        .ignoringFields("createdAt", "updatedAt", "expiresAt", "attachments")
        .isEqualTo(expected);
  }

  public void thenResponseShouldHaveFollowingGameMailAttachments(DataTable dataTable)
      throws JsonProcessingException {
    List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);
    List<GameMailAttachment<?>> gameMailAttachments = gameMailAttachmentListStack.peek();
    for (var row : rows) {
      GameMailAttachment<?> expected = BddUtils.mapToGameMailAttachment(row, objectMapper);
      GameMailAttachment<?> actual =
          gameMailAttachments.stream()
              .filter(gameMailAttachment -> expected.type().equals(gameMailAttachment.type()))
              .findFirst()
              .orElseThrow();

      assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }
  }

  public void thenResponseShouldNotContainAttachments() {
    List<GameMailAttachment<?>> gameMailAttachments = gameMailAttachmentListStack.peek();
    assertThat(gameMailAttachments).isEmpty();
  }

  public void thenResponseShouldHaveFollowingGameMailResponses(DataTable dataTable) {
    List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);

    List<GameMailResponse> gameMailResponses = gameMailResponseListStack.peek();
    for (Map<String, String> row : rows) {
      GameMailResponse actual =
          gameMailResponses.stream()
              .filter(gm -> gm.id().toString().equals(row.get("id")))
              .findFirst()
              .orElseThrow();

      GameMailResponse expected = BddUtils.mapToGameMailResponse(row);

      assertThat(actual)
          .usingRecursiveComparison()
          .ignoringFields("createdAt", "updatedAt", "expiresAt")
          .isEqualTo(expected);
    }
  }
}
