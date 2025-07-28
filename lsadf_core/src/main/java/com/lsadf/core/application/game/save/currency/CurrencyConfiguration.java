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
package com.lsadf.core.application.game.save.currency;

import com.lsadf.core.domain.game.save.currency.Currency;
import com.lsadf.core.infra.cache.Cache;
import com.lsadf.core.infra.cache.HistoCache;
import com.lsadf.core.infra.cache.NoOpHistoCache;
import com.lsadf.core.infra.cache.config.ValkeyProperties;
import com.lsadf.core.infra.cache.properties.CacheExpirationProperties;
import com.lsadf.core.infra.persistence.table.game.save.currency.CurrencyRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * CurrencyConfiguration is a configuration class that defines the bean creation for
 * CurrencyService. It acts as a central configuration for providing the necessary dependencies
 * required by the CurrencyService implementation.
 */
@Configuration
public class CurrencyConfiguration {

  public static final String CURRENCY_CACHE = "currencyCache";

  @Bean
  public CurrencyService currencyService(
      CurrencyRepository currencyRepository, Cache<Currency> currencyCache) {
    return new CurrencyServiceImpl(currencyRepository, currencyCache);
  }

  @Bean(name = CURRENCY_CACHE)
  @ConditionalOnProperty(prefix = "cache.redis", name = "enabled", havingValue = "true")
  public HistoCache<Currency> redisCurrencyCache(
      RedisTemplate<String, Currency> redisTemplate,
      CacheExpirationProperties cacheExpirationProperties,
      ValkeyProperties valkeyProperties) {
    return new ValkeyCurrencyCache(
        redisTemplate, cacheExpirationProperties.getCurrencyExpirationSeconds(), valkeyProperties);
  }

  @Bean(name = CURRENCY_CACHE)
  @ConditionalOnMissingBean
  public HistoCache<Currency> noOpCurrencyCache() {
    return new NoOpHistoCache<>();
  }
}
