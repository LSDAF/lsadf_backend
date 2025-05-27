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
package com.lsadf.core.application.game.currency;

import com.lsadf.core.domain.game.currency.Currency;
import com.lsadf.core.infra.cache.Cache;
import com.lsadf.core.infra.persistence.game.currency.CurrencyEntityMapper;
import com.lsadf.core.infra.persistence.game.currency.CurrencyRepository;
import com.lsadf.core.infra.web.requests.game.currency.CurrencyRequestMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * CurrencyConfiguration is a configuration class that defines the bean creation for
 * CurrencyService. It acts as a central configuration for providing the necessary dependencies
 * required by the CurrencyService implementation.
 */
@Configuration
public class CurrencyConfiguration {
  @Bean
  public CurrencyService currencyService(
      CurrencyRepository currencyRepository,
      Cache<Currency> currencyCache,
      CurrencyEntityMapper mapper) {
    return new CurrencyServiceImpl(currencyRepository, currencyCache, mapper);
  }

  @Bean
  public CurrencyEntityMapper currencyEntityModelMapper() {
    return new CurrencyEntityMapper();
  }

  @Bean
  public CurrencyRequestMapper currencyRequestModelMapper() {
    return new CurrencyRequestMapper();
  }
}
