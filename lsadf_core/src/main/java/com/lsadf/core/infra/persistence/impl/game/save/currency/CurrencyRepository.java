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
package com.lsadf.core.infra.persistence.impl.game.save.currency;

import static com.lsadf.core.infra.persistence.impl.game.save.currency.CurrencyEntity.CurrencyEntityAttributes.*;
import static com.lsadf.core.infra.persistence.impl.game.save.metadata.GameMetadataEntity.GameSaveMetadataAttributes.GAME_METADATA_ID;

import com.lsadf.core.infra.persistence.JdbcRepository;
import java.util.Optional;
import java.util.UUID;
import org.jspecify.annotations.Nullable;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CurrencyRepository extends JdbcRepository<CurrencyEntity> {
  @Query(
      "insert into t_currency_tgcu (tgme_id, tgcu_gold_amount, tgcu_diamond_amount, tgcu_emerald_amount, tgcu_amethyst_amount) values (:tgme_id, :tgcu_gold_amount, :tgcu_diamond_amount, :tgcu_emerald_amount, :tgcu_amethyst_amount) returning *")
  CurrencyEntity createNewCurrencyEntity(
      @Param(GAME_METADATA_ID) UUID id,
      @Param(CURRENCY_GOLD_AMOUNT) Long goldAmount,
      @Param(CURRENCY_DIAMOND_AMOUNT) Long diamondAmount,
      @Param(CURRENCY_EMERALD_AMOUNT) Long emeraldAmount,
      @Param(CURRENCY_AMETHYST_AMOUNT) Long amethystAmount);

  @Query("insert into t_currency_tgcu (tgme_id) values (:tgme_id) returning *")
  CurrencyEntity createNewCurrencyEntity(@Param(GAME_METADATA_ID) UUID id);

  @Query(
      """
              update t_currency_tgcu
              set tgcu_gold_amount=coalesce(:tgcu_gold_amount, tgcu_gold_amount),\
              tgcu_diamond_amount=coalesce(:tgcu_diamond_amount, tgcu_diamond_amount),\
              tgcu_emerald_amount=coalesce(:tgcu_emerald_amount, tgcu_emerald_amount),
              tgcu_amethyst_amount=coalesce(:tgcu_amethyst_amount, tgcu_amethyst_amount)\
              where tgme_id=:tgme_id
              returning *
              """)
  CurrencyEntity updateCurrency(
      @Nullable @Param(GAME_METADATA_ID) UUID id,
      @Nullable @Param(CURRENCY_GOLD_AMOUNT) Long goldAmount,
      @Nullable @Param(CURRENCY_DIAMOND_AMOUNT) Long diamondAmount,
      @Nullable @Param(CURRENCY_EMERALD_AMOUNT) Long emeraldAmount,
      @Nullable @Param(CURRENCY_AMETHYST_AMOUNT) Long amethystAmount);

  @Query("select * from t_currency_tgcu where tgme_id=:tgme_id")
  Optional<CurrencyEntity> findCurrencyEntityById(@Param(GAME_METADATA_ID) UUID id);

  @Query("select count(tgme_id) from t_currency_tgcu")
  Long count();
}
