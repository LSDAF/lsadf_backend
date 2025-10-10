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

import com.lsadf.core.infra.web.dto.response.ApiResponse;
import java.util.Stack;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/** Step definitions for the then steps in the BDD scenarios */
@Slf4j(topic = "[THEN STEP DEFINITIONS]")
@Component
public class BddThenStepDefinitions {

  @Autowired protected Stack<ApiResponse<?>> responseStack;

  public void thenResponseStatusCodeShouldBe(int statusCode) {
    int actual = responseStack.peek().status();
    assertThat(actual).isEqualTo(statusCode);
  }

  public void thenTheResponseShouldHaveTheFollowingBoolean(boolean expected) {
    boolean actual = (boolean) responseStack.peek().data();
    assertThat(actual).isEqualTo(expected);
  }
}
