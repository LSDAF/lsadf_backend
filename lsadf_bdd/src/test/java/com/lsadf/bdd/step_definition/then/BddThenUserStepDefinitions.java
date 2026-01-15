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
import com.lsadf.core.application.user.UserService;
import com.lsadf.core.infra.web.dto.response.ApiResponse;
import com.lsadf.core.infra.web.dto.response.user.UserResponse;
import io.cucumber.datatable.DataTable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j(topic = "[USER THEN STEP DEFINITIONS]")
@Component
public class BddThenUserStepDefinitions {

  @Autowired protected Stack<ApiResponse<?>> responseStack;
  @Autowired protected Stack<List<UserResponse>> userResponseListStack;
  @Autowired protected UserService userService;

  public void thenTheNumberOfUsersShouldBe(int expected) {
    long actual = userService.getUsers().count();
    assertThat(actual).isEqualTo(expected);
  }

  public void thenTheResponseShouldHaveTheFollowingUserResponse(DataTable dataTable) {
    List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);

    if (rows.size() > 1) {
      throw new IllegalArgumentException("Expected only one row in the DataTable");
    }

    Map<String, String> row = rows.get(0);

    UserResponse actual = (UserResponse) responseStack.peek().data();
    UserResponse expected = BddUtils.mapToUserResponse(row);

    assertThat(actual)
        .usingRecursiveComparison()
        .ignoringFields("userRoles", "createdTimestamp")
        .ignoringExpectedNullFields()
        .isEqualTo(expected);

    expected.userRoles().forEach(role -> assertThat(actual.userRoles()).contains(role));
  }

  public void thenTheResponseShouldHaveTheFollowingUsers(DataTable dataTable) {
    List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);

    List<UserResponse> actual = userResponseListStack.peek();

    for (Map<String, String> row : rows) {
      UserResponse expectedUser = BddUtils.mapToUserResponse(row);
      UserResponse actualUser =
          actual.stream()
              .filter(u -> u.username().equals(expectedUser.username()))
              .findFirst()
              .orElseThrow();

      assertThat(actualUser)
          .usingRecursiveComparison()
          .ignoringExpectedNullFields()
          .ignoringFields("userRoles", "createdTimestamp")
          .isEqualTo(expectedUser);

      expectedUser.userRoles().forEach(role -> assertThat(actualUser.userRoles()).contains(role));
    }
  }

  public void thenTheResponseShouldHaveTheFollowingUserResponsesInExactOrder(DataTable dataTable) {
    List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);
    List<UserResponse> actual = userResponseListStack.peek();
    List<UserResponse> expected = new ArrayList<>();

    for (Map<String, String> row : rows) {
      UserResponse expectedEntity = BddUtils.mapToUserResponse(row);
      expected.add(expectedEntity);
    }

    assertThat(actual).hasSameSizeAs(expected);

    for (int i = 0; i < actual.size(); i++) {
      UserResponse actualUser = actual.get(i);
      UserResponse expectedUser = expected.get(i);
      assertThat(actualUser)
          .usingRecursiveComparison()
          .ignoringExpectedNullFields()
          .ignoringFields("userRoles", "createdTimestamp")
          .isEqualTo(expectedUser);
    }
  }
}
