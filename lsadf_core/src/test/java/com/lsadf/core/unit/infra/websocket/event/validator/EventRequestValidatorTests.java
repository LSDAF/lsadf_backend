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

package com.lsadf.core.unit.infra.websocket.event.validator;

import static org.junit.jupiter.api.Assertions.assertThrows;

import com.lsadf.core.infra.web.dto.request.game.currency.CurrencyRequest;
import com.lsadf.core.infra.websocket.event.EventRequestValidator;
import jakarta.validation.ValidationException;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

class EventRequestValidatorTests {
  private final Validator validator = new LocalValidatorFactoryBean();
  private final EventRequestValidator eventRequestValidator = new EventRequestValidator(validator);

  @Test
  void testValidate() {
    CurrencyRequest request = new CurrencyRequest(-1L, 0L, 0L, 0L);
    assertThrows(ValidationException.class, () -> eventRequestValidator.validate(request));
  }
}
