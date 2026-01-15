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
package com.lsadf.core.unit.infra.valkey.stream.consumer.handler;

import static com.lsadf.core.infra.valkey.stream.event.game.ValkeyGameSaveEventType.CURRENCY_UPDATED;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.lsadf.core.application.game.save.currency.CurrencyCommandService;
import com.lsadf.core.application.game.save.currency.command.UpdateCacheCurrencyCommand;
import com.lsadf.core.domain.game.save.currency.Currency;
import com.lsadf.core.infra.valkey.stream.consumer.handler.impl.CurrencyUpdateEventHandler;
import com.lsadf.core.infra.valkey.stream.event.game.ValkeyGameSaveUpdatedEvent;
import com.lsadf.core.shared.event.EventType;
import java.util.Map;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tools.jackson.databind.ObjectMapper;

@ExtendWith(MockitoExtension.class)
class CurrencyUpdateEventHandlerTests {

  @Mock private CurrencyCommandService currencyService;

  @Mock private ObjectMapper objectMapper;

  private CurrencyUpdateEventHandler handler;
  private UUID gameSaveId;
  private Map<String, String> payload;
  private ValkeyGameSaveUpdatedEvent event;
  private Currency currency;

  private Long gold = 10L;
  private Long diamond = 20L;
  private Long emerald = 30L;
  private Long amethyst = 40L;

  @BeforeEach
  void setUp() {
    handler = new CurrencyUpdateEventHandler(currencyService, objectMapper);
    gameSaveId = UUID.randomUUID();
    payload = Map.of("gold", "10", "diamond", "20", "emerald", "30", "amethyst", "40");
    currency = new Currency(gold, diamond, emerald, amethyst);

    event = new ValkeyGameSaveUpdatedEvent(CURRENCY_UPDATED, gameSaveId, "user123", null, payload);
  }

  @Test
  void getEventTypeReturnsCurrencyUpdate() {
    EventType eventType = handler.getEventType();
    assertEquals(CURRENCY_UPDATED, eventType);
  }

  @Test
  void handleEventCallsCurrencyServiceWithCorrectParameters() throws Exception {
    when(objectMapper.convertValue(payload, Currency.class)).thenReturn(currency);
    handler.handleEvent(event);

    verify(objectMapper).convertValue(payload, Currency.class);
    var command = new UpdateCacheCurrencyCommand(gameSaveId, gold, diamond, emerald, amethyst);
    verify(currencyService).updateCacheCurrency(command);
  }
}
