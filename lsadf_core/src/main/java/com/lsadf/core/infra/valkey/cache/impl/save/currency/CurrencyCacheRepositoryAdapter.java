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

package com.lsadf.core.infra.valkey.cache.impl.save.currency;

import com.lsadf.core.application.game.save.currency.CurrencyCachePort;
import com.lsadf.core.domain.game.save.currency.Currency;
import com.lsadf.core.infra.valkey.cache.HashModelMapper;
import com.lsadf.core.infra.valkey.cache.config.properties.CacheExpirationProperties;
import com.lsadf.core.infra.valkey.cache.impl.ValkeyCacheRepositoryAdapter;
import java.util.Optional;
import java.util.UUID;

public class CurrencyCacheRepositoryAdapter
    extends ValkeyCacheRepositoryAdapter<Currency, CurrencyHash, UUID>
    implements CurrencyCachePort {

  private static final HashModelMapper<CurrencyHash, Currency> CURRENCY_HASH_MAPPER =
      CurrencyHashMapper.INSTANCE;

  public CurrencyCacheRepositoryAdapter(
      CurrencyHashRepository currencyHashRepository,
      CacheExpirationProperties cacheExpirationProperties) {
    super(currencyHashRepository);
    this.hashMapper = CURRENCY_HASH_MAPPER;
    this.expirationSeconds = cacheExpirationProperties.getCurrencyExpirationSeconds();
  }

  @Override
  public Optional<Currency> get(String key) {
    UUID uuid = UUID.fromString(key);
    Optional<CurrencyHash> currencyHashOptional = repository.findById(uuid);
    if (currencyHashOptional.isPresent()) {
      CurrencyHash currencyHash = currencyHashOptional.get();
      return Optional.of(hashMapper.map(currencyHash));
    }
    return Optional.empty();
  }

  @Override
  public void set(String key, Currency value) {
    UUID uuid = UUID.fromString(key);
    CurrencyHash hash =
        CurrencyHash.builder()
            .gameSaveId(uuid)
            .gold(value.gold())
            .emerald(value.emerald())
            .diamond(value.diamond())
            .expiration(this.expirationSeconds)
            .amethyst(value.amethyst())
            .build();
    repository.save(hash);
  }

  @Override
  public void set(String key, Currency value, int expirationSeconds) {
    UUID uuid = UUID.fromString(key);
    Long expiration = (long) expirationSeconds;
    CurrencyHash hash =
        CurrencyHash.builder()
            .gameSaveId(uuid)
            .gold(value.gold())
            .emerald(value.emerald())
            .diamond(value.diamond())
            .amethyst(value.amethyst())
            .expiration(expiration)
            .build();
    repository.save(hash);
  }

  @Override
  public void unset(String key) {
    UUID uuid = UUID.fromString(key);
    repository.deleteById(uuid);
  }
}
