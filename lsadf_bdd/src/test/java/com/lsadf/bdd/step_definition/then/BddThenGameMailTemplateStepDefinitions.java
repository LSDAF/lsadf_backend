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

import com.lsadf.core.infra.web.dto.response.game.mail.GameMailTemplateResponse;
import io.cucumber.datatable.DataTable;
import java.util.List;
import java.util.Stack;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j(topic = "[GAME MAIL TEMPLATE THEN STEP DEFINITIONS]")
@Component
public class BddThenGameMailTemplateStepDefinitions {

  @Autowired private Stack<List<GameMailTemplateResponse>> gameMailTemplateResponseListStack;
  @Autowired private Stack<GameMailTemplateResponse> gameMailTemplateResponseStack;

  public void shouldHaveTheFollowingGameMailTemplateResponses(DataTable dataTable) {
    // Implementation goes here
  }

  public void shouldHaveTheFollowingGameMailTemplateResponse(DataTable dataTable) {
    // Implementation goes here
  }
}
