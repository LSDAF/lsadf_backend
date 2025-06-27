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
package com.lsadf.application.bdd.config;

import com.lsadf.core.domain.game.GameSave;
import com.lsadf.core.domain.game.characteristics.Characteristics;
import com.lsadf.core.domain.game.currency.Currency;
import com.lsadf.core.domain.game.inventory.Inventory;
import com.lsadf.core.domain.game.stage.Stage;
import com.lsadf.core.domain.user.User;
import com.lsadf.core.domain.user.UserInfo;
import com.lsadf.core.infra.persistence.game.game_save.GameSaveEntity;
import com.lsadf.core.infra.persistence.game.inventory.InventoryEntity;
import com.lsadf.core.infra.web.client.keycloak.response.JwtAuthenticationResponse;
import com.lsadf.core.infra.web.response.ApiResponse;
import com.lsadf.core.infra.web.response.game.game_save.GameSaveResponse;
import com.lsadf.core.infra.web.response.game.inventory.ItemResponse;
import com.lsadf.core.infra.web.response.info.GlobalInfoResponse;
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
@Import(BddStackCleaner.class)
public class LsadfBddTestsConfiguration {

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
  public Stack<Inventory> inventoryStack(BddStackCleaner stackCleaner) {
    Stack<Inventory> stack = new Stack<>();
    stackCleaner.addStack(stack);
    return stack;
  }

  @Bean
  public Stack<InventoryEntity> inventoryEntityStack(BddStackCleaner stackCleaner) {
    Stack<InventoryEntity> stack = new Stack<>();
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
  public Stack<List<GameSaveEntity>> gameSaveEntityListStack(BddStackCleaner stackCleaner) {
    var stack = new Stack<List<GameSaveEntity>>();
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
