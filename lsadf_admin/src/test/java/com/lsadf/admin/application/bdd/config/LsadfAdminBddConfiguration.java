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
package com.lsadf.admin.application.bdd.config;

import com.lsadf.core.domain.game.GameSave;
import com.lsadf.core.domain.game.characteristics.Characteristics;
import com.lsadf.core.domain.game.currency.Currency;
import com.lsadf.core.domain.game.inventory.Inventory;
import com.lsadf.core.domain.game.stage.Stage;
import com.lsadf.core.domain.user.User;
import com.lsadf.core.infra.persistence.game.inventory.InventoryEntity;
import com.lsadf.core.infra.web.clients.keycloak.response.JwtAuthenticationResponse;
import com.lsadf.core.infra.web.responses.ApiResponse;
import com.lsadf.core.infra.web.responses.game.game_save.GameSaveResponse;
import com.lsadf.core.infra.web.responses.game.inventory.ItemResponse;
import com.lsadf.core.infra.web.responses.info.GlobalInfoResponse;
import jakarta.mail.internet.MimeMessage;
import java.util.List;
import java.util.Set;
import java.util.Stack;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

@TestConfiguration
public class LsadfAdminBddConfiguration {

  @Bean
  @Primary
  public BddStackCleaner bddStackCleaner() {
    return new BddStackCleaner();
  }

  @Bean
  public Stack<MimeMessage> mimeMessageStack(BddStackCleaner stackCleaner) {
    Stack<MimeMessage> stack = new Stack<>();
    stackCleaner.addStack(stack);
    return stack;
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
  public Stack<ItemResponse> itemResponseStack(BddStackCleaner stackCleaner) {
    Stack<ItemResponse> stack = new Stack<>();
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
  public Stack<Set<ItemResponse>> itemResponseSetStack(BddStackCleaner stackCleaner) {
    Stack<Set<ItemResponse>> stack = new Stack<>();
    stackCleaner.addStack(stack);
    return stack;
  }

  @Bean
  public Stack<List<GameSaveResponse>> gameSaveResponseListStack(BddStackCleaner stackCleaner) {
    var stack = new Stack<List<GameSaveResponse>>();
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
  public Stack<ApiResponse<?>> responseStack(BddStackCleaner stackCleaner) {
    var stack = new Stack<ApiResponse<?>>();
    stackCleaner.addStack(stack);
    return stack;
  }
}
