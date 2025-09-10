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
package com.lsadf.core.infra.persistence.adapter.game.save;

import com.lsadf.core.application.game.save.currency.CurrencyRepositoryPort;
import com.lsadf.core.domain.game.save.currency.Currency;
import com.lsadf.core.infra.persistence.impl.game.save.currency.CurrencyEntityMapper;
import com.lsadf.core.infra.persistence.impl.game.save.currency.CurrencyRepository;
import java.util.Optional;
import java.util.UUID;
import org.jspecify.annotations.Nullable;

public class CurrencyRepositoryAdapter implements CurrencyRepositoryPort {

  private final CurrencyRepository currencyRepository;
  private static final CurrencyEntityMapper currencyEntityMapper = CurrencyEntityMapper.INSTANCE;

  public CurrencyRepositoryAdapter(CurrencyRepository currencyRepository) {
    this.currencyRepository = currencyRepository;
  }

  @Override
  public Optional<Currency> findById(UUID id) {
    return currencyRepository.findCurrencyEntityById(id).map(currencyEntityMapper::map);
  }

  @Override
  public Currency create(UUID id, Long gold, Long diamond, Long emerald, Long amethyst) {
    var entity = currencyRepository.createNewCurrencyEntity(id, gold, diamond, emerald, amethyst);
    return currencyEntityMapper.map(entity);
  }

  @Override
  public Currency create(UUID id) {
    var entity = currencyRepository.createNewCurrencyEntity(id);
    return currencyEntityMapper.map(entity);
  }

  @Override
  public Currency update(
      UUID gameSaveId,
      @Nullable Long gold,
      @Nullable Long diamond,
      @Nullable Long emerald,
      @Nullable Long amethyst) {
    var entity = currencyRepository.updateCurrency(gameSaveId, gold, diamond, emerald, amethyst);
    return currencyEntityMapper.map(entity);
  }

  @Override
  public boolean existsById(UUID id) {
    return currencyRepository.findCurrencyEntityById(id).isPresent();
  }

  @Override
  public Long count() {
    return currencyRepository.count();
  }
}
