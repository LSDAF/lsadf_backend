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

package com.lsadf.core.unit.infra.valkey.stream.consumer.handler;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mock.Strictness.LENIENT;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.lsadf.core.infra.valkey.stream.consumer.handler.EventHandler;
import com.lsadf.core.infra.valkey.stream.consumer.handler.impl.EventHandlerRegistryImpl;
import com.lsadf.core.infra.valkey.stream.event.game.ValkeyGameSaveEventType;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class EventHandlerRegistryImplTests {

  private EventHandlerRegistryImpl registry;

  @Mock(strictness = LENIENT)
  private EventHandler stageHandler;

  @Mock(strictness = LENIENT)
  private EventHandler currencyHandler;

  @Mock(strictness = LENIENT)
  private EventHandler characteristicsHandler;

  @BeforeEach
  void setUp() {
    registry = new EventHandlerRegistryImpl();

    when(stageHandler.getEventType()).thenReturn(ValkeyGameSaveEventType.STAGE_UPDATED);
    when(currencyHandler.getEventType()).thenReturn(ValkeyGameSaveEventType.CURRENCY_UPDATED);
    when(characteristicsHandler.getEventType())
        .thenReturn(ValkeyGameSaveEventType.CHARACTERISTICS_UPDATED);
  }

  @Test
  void registerHandlerShouldAddHandlerToRegistry() {
    // Register handlers
    registry.registerHandler(ValkeyGameSaveEventType.STAGE_UPDATED, stageHandler);
    registry.registerHandler(ValkeyGameSaveEventType.CURRENCY_UPDATED, currencyHandler);

    // Verify handlers are properly stored
    Optional<EventHandler> retrievedStageHandler =
        registry.getHandler(ValkeyGameSaveEventType.STAGE_UPDATED);
    Optional<EventHandler> retrievedCurrencyHandler =
        registry.getHandler(ValkeyGameSaveEventType.CURRENCY_UPDATED);

    assertTrue(retrievedStageHandler.isPresent());
    assertTrue(retrievedCurrencyHandler.isPresent());
    assertEquals(stageHandler, retrievedStageHandler.get());
    assertEquals(currencyHandler, retrievedCurrencyHandler.get());
  }

  @Test
  void getHandlerShouldReturnCorrectHandler() {
    // Register a handler
    registry.registerHandler(
        ValkeyGameSaveEventType.CHARACTERISTICS_UPDATED, characteristicsHandler);

    // Get the handler
    Optional<EventHandler> retrievedHandler =
        registry.getHandler(ValkeyGameSaveEventType.CHARACTERISTICS_UPDATED);

    // Verify correct handler is returned
    assertTrue(retrievedHandler.isPresent());
    assertEquals(characteristicsHandler, retrievedHandler.get());
  }

  @Test
  void getHandlerShouldReturnEmptyOptionalForNonExistentHandler() {
    // Try to get a handler that hasn't been registered
    Optional<EventHandler> retrievedHandler =
        registry.getHandler(ValkeyGameSaveEventType.STAGE_UPDATED);

    // Should return an empty optional since the handler isn't registered yet
    assertEquals(Optional.empty(), retrievedHandler);
  }

  @Test
  void registerHandlerShouldReplaceExistingHandler() {
    // Register initial handler
    registry.registerHandler(ValkeyGameSaveEventType.STAGE_UPDATED, stageHandler);

    // Create and register a replacement handler
    EventHandler replacementHandler = mock(EventHandler.class);
    registry.registerHandler(ValkeyGameSaveEventType.STAGE_UPDATED, replacementHandler);

    // Get the handler and verify it's the replacement
    Optional<EventHandler> retrievedHandler =
        registry.getHandler(ValkeyGameSaveEventType.STAGE_UPDATED);

    assertTrue(retrievedHandler.isPresent());
    assertEquals(replacementHandler, retrievedHandler.get());
  }
}
