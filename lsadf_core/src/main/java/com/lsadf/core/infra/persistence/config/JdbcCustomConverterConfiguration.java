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

package com.lsadf.core.infra.persistence.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lsadf.core.domain.game.inventory.item.Item;
import com.lsadf.core.domain.game.save.currency.Currency;
import com.lsadf.core.infra.persistence.adapter.game.mail.converter.GameMailAttachmentConverter;
import com.lsadf.core.infra.persistence.adapter.game.mail.converter.GameMailAttachmentConverterRegistry;
import com.lsadf.core.infra.persistence.adapter.game.mail.converter.impl.GameMailAttachmentCurrencyConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JdbcCustomConverterConfiguration {
  @Bean
  public GameMailAttachmentConverterRegistry gameMailAttachmentConverterRegistry() {
    return new GameMailAttachmentConverterRegistry();
  }

  @Bean
  public GameMailAttachmentConverter<Currency> currencyGameMailAttachmentConverter(
      GameMailAttachmentConverterRegistry converterRegistry, ObjectMapper objectMapper) {
    GameMailAttachmentConverter<Currency> converter =
        new GameMailAttachmentCurrencyConverter(objectMapper);
    converterRegistry.registerConverter(converter);
    return converter;
  }

  @Bean
  public GameMailAttachmentConverter<Item> itemGameMailAttachmentConverter(
      GameMailAttachmentConverterRegistry converterRegistry, ObjectMapper objectMapper) {
    GameMailAttachmentConverter<Item> converter =
        new com.lsadf.core.infra.persistence.adapter.game.mail.converter.impl
            .GameMailAttachmentItemConverter(objectMapper);
    converterRegistry.registerConverter(converter);
    return converter;
  }
}
