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

package com.lsadf.core.infra.valkey.cache.game.save.currency;

import com.lsadf.core.application.game.save.currency.CurrencyCachePort;
import com.lsadf.core.domain.game.save.currency.Currency;
import com.lsadf.core.infra.valkey.cache.HashModelMapper;
import com.lsadf.core.infra.valkey.cache.ValkeyCacheAdapter;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.repository.CrudRepository;

@Slf4j
public class CurrencyCacheAdapter extends ValkeyCacheAdapter<CurrencyHash, Currency, UUID>
    implements CurrencyCachePort {

  private final CrudRepository<CurrencyHash, UUID> currencyHashRepository;
  private CurrencyHashMapper currencyHashMapper = CurrencyHashMapper.INSTANCE;

  public CurrencyCacheAdapter(CrudRepository<CurrencyHash, UUID> currencyHashRepository) {
    this.currencyHashRepository = currencyHashRepository;
  }

  @Override
  protected CrudRepository<CurrencyHash, UUID> getRepository() {
    return this.currencyHashRepository;
  }

  @Override
  protected HashModelMapper<CurrencyHash, Currency> getMapper() {
    return this.currencyHashMapper;
  }

  @Override
  public void set(UUID key, Currency value) {
    CurrencyHash hash =
        CurrencyHash.builder()
            .gold(value.gold())
            .diamond(value.diamond())
            .emerald(value.emerald())
            .amethyst(value.amethyst())
            .build();
    var updated = this.currencyHashRepository.save(hash);
    log.info(updated.toString());
  }
}
