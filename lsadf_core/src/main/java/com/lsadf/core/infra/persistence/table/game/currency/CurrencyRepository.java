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
package com.lsadf.core.infra.persistence.table.game.currency;

import static com.lsadf.core.infra.persistence.config.EntityAttributes.ID;
import static com.lsadf.core.infra.persistence.table.game.currency.CurrencyEntity.CurrencyEntityAttributes.*;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CurrencyRepository
    extends org.springframework.data.repository.Repository<CurrencyEntity, UUID> {
  @Query(
      "insert into t_currency_tgcu (id, gold_amount, diamond_amount, emerald_amount, amethyst_amount) values (:id, :gold_amount, :diamond_amount, :emerald_amount, :amethyst_amount) returning *")
  CurrencyEntity createNewCurrencyEntity(
      @Param(ID) UUID id,
      @Param(CURRENCY_GOLD_AMOUNT) Long goldAmount,
      @Param(CURRENCY_DIAMOND_AMOUNT) Long diamondAmount,
      @Param(CURRENCY_EMERALD_AMOUNT) Long emeraldAmount,
      @Param(CURRENCY_AMETHYST_AMOUNT) Long amethystAmount);

  @Query("insert into t_currency_tgcu (id) values (:id) returning *")
  CurrencyEntity createNewCurrencyEntity(@Param(ID) UUID id);

  @Query(
      """
              update t_currency_tgcu
              set gold_amount=coalesce(:gold_amount, gold_amount),\
              diamond_amount=coalesce(:diamond_amount, diamond_amount),\
              emerald_amount=coalesce(:emerald_amount, emerald_amount),
              amethyst_amount=coalesce(:amethyst_amount, amethyst_amount)\
              where id=:id
              returning *
              """)
  CurrencyEntity updateCurrency(
      @Param(ID) UUID id,
      @Param(CURRENCY_GOLD_AMOUNT) Long goldAmount,
      @Param(CURRENCY_DIAMOND_AMOUNT) Long diamondAmount,
      @Param(CURRENCY_EMERALD_AMOUNT) Long emeraldAmount,
      @Param(CURRENCY_AMETHYST_AMOUNT) Long amethystAmount);

  @Query("select * from t_currency_tgcu where id=:id")
  Optional<CurrencyEntity> findCurrencyEntityById(@Param(ID) UUID id);

  @Query("select count(id) from t_currency_tgcu")
  Long count();
}
