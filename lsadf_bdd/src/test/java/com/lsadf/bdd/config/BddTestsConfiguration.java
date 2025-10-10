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
package com.lsadf.bdd.config;

import com.lsadf.bdd.step_definition.given.BddGivenStepDefinitions;
import com.lsadf.bdd.step_definition.then.*;
import com.lsadf.bdd.step_definition.when.BddWhenStepDefinitions;
import com.lsadf.core.domain.game.save.GameSave;
import com.lsadf.core.domain.game.save.characteristics.Characteristics;
import com.lsadf.core.domain.game.save.currency.Currency;
import com.lsadf.core.domain.game.save.stage.Stage;
import com.lsadf.core.domain.user.User;
import com.lsadf.core.domain.user.UserInfo;
import com.lsadf.core.infra.persistence.impl.game.save.metadata.GameMetadataEntity;
import com.lsadf.core.infra.web.dto.response.ApiResponse;
import com.lsadf.core.infra.web.dto.response.game.inventory.ItemResponse;
import com.lsadf.core.infra.web.dto.response.game.save.GameSaveResponse;
import com.lsadf.core.infra.web.dto.response.game.session.GameSessionResponse;
import com.lsadf.core.infra.web.dto.response.info.GlobalInfoResponse;
import com.lsadf.core.infra.web.dto.response.jwt.JwtAuthenticationResponse;
import com.lsadf.core.infra.web.dto.response.user.UserResponse;
import java.util.List;
import java.util.Set;
import java.util.Stack;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/** Configuration class for BDD tests */
@TestConfiguration
@Import({
  BddStackCleaner.class,
  BddGivenStepDefinitions.class,
  BddWhenStepDefinitions.class,
  BddThenStepDefinitions.class,
  BddThenCacheStepDefinitions.class,
  BddThenCharacteristicsStepDefinitions.class,
  BddThenCurrencyStepDefinitions.class,
  BddThenGameSaveStepDefinitions.class,
  BddThenGameSessionStepDefinitions.class,
  BddThenGlobalInfoStepDefinitions.class,
  BddThenItemStepDefinitions.class,
  BddThenStageStepDefinitions.class,
  BddThenUserInfoStepDefinitions.class,
  BddThenUserStepDefinitions.class
})
public class BddTestsConfiguration {

  @Primary
  @Bean
  public BddStackCleaner bddStackCleaner() {
    return new BddStackCleaner();
  }

  @Bean
  public Stack<Characteristics> characteristicsStack(BddStackCleaner stackCleaner) {
    Stack<Characteristics> stack = new Stack<>();
    stackCleaner.addStack(stack);
    return stack;
  }

  @Bean
  public Stack<GameSessionResponse> gameSessionResponseStack(BddStackCleaner stackCleaner) {
    Stack<GameSessionResponse> stack = new Stack<>();
    stackCleaner.addStack(stack);
    return stack;
  }

  @Bean
  public Stack<Currency> currencyStack(BddStackCleaner stackCleaner) {
    Stack<Currency> stack = new Stack<>();
    stackCleaner.addStack(stack);
    return stack;
  }

  @Bean
  public Stack<Stage> stageStack(BddStackCleaner stackCleaner) {
    Stack<Stage> stack = new Stack<>();
    stackCleaner.addStack(stack);
    return stack;
  }

  @Bean
  public Stack<List<GameSave>> gameSaveStack(BddStackCleaner stackCleaner) {
    Stack<List<GameSave>> stack = new Stack<>();
    stackCleaner.addStack(stack);
    return stack;
  }

  @Bean
  public Stack<List<GameSaveResponse>> gameSaveResponseStack(BddStackCleaner stackCleaner) {
    Stack<List<GameSaveResponse>> stack = new Stack<>();
    stackCleaner.addStack(stack);

    return stack;
  }

  @Bean
  public Stack<List<UserResponse>> userResponseListStack(BddStackCleaner stackCleaner) {
    Stack<List<UserResponse>> stack = new Stack<>();
    stackCleaner.addStack(stack);
    return stack;
  }

  @Bean
  public Stack<ItemResponse> itemResponseStack(BddStackCleaner stackCleaner) {
    Stack<ItemResponse> stack = new Stack<>();
    stackCleaner.addStack(stack);
    return stack;
  }

  @Bean
  public Stack<Set<ItemResponse>> itemSetStack(BddStackCleaner stackCleaner) {
    Stack<Set<ItemResponse>> stack = new Stack<>();
    stackCleaner.addStack(stack);
    return stack;
  }

  @Bean
  public Stack<List<User>> userListStack(BddStackCleaner stackCleaner) {
    Stack<List<User>> stack = new Stack<>();
    stackCleaner.addStack(stack);
    return stack;
  }

  @Bean
  public Stack<GlobalInfoResponse> globalInfoResponseStack(BddStackCleaner stackCleaner) {
    Stack<GlobalInfoResponse> stack = new Stack<>();
    stackCleaner.addStack(stack);
    return stack;
  }

  @Bean
  public Stack<JwtAuthenticationResponse> jwtAuthenticationStack(BddStackCleaner stackCleaner) {
    Stack<JwtAuthenticationResponse> stack = new Stack<>();
    stackCleaner.addStack(stack);
    return stack;
  }

  @Bean
  public Stack<Boolean> booleanStack(BddStackCleaner stackCleaner) {
    var stack = new Stack<Boolean>();
    stackCleaner.addStack(stack);
    return stack;
  }

  @Bean
  public Stack<List<UserInfo>> userInfoListStack(BddStackCleaner stackCleaner) {
    Stack<List<UserInfo>> stack = new Stack<>();
    stackCleaner.addStack(stack);
    return stack;
  }

  @Bean
  public Stack<List<GameMetadataEntity>> gameSaveEntityListStack(BddStackCleaner stackCleaner) {
    var stack = new Stack<List<GameMetadataEntity>>();
    stackCleaner.addStack(stack);
    return stack;
  }

  @Bean
  public Stack<Exception> exceptionStack(BddStackCleaner stackCleaner) {
    var stack = new Stack<Exception>();
    stackCleaner.addStack(stack);
    return stack;
  }

  @Bean
  public Stack<ApiResponse<?>> genericResponseStack(BddStackCleaner stackCleaner) {
    var stack = new Stack<ApiResponse<?>>();
    stackCleaner.addStack(stack);
    return stack;
  }

  @Bean
  @Primary
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
