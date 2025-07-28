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
package com.lsadf.core.infra.persistence.table.game.save.characteristics;

import static com.lsadf.core.infra.persistence.config.EntityAttributes.ID;
import static com.lsadf.core.infra.persistence.table.game.save.characteristics.CharacteristicsEntity.CharacteristicsEntityAttributes.*;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/** Repository class for CharacteristicsEntity */
@Repository
public interface CharacteristicsRepository
    extends org.springframework.data.repository.Repository<CharacteristicsEntity, UUID> {
  @Query(
      "insert into t_characteristics_tgch (id, attack, crit_chance, crit_damage, health, resistance) values (:id, :attack, :crit_chance, :crit_damage, :health, :resistance) returning *")
  CharacteristicsEntity createNewCharacteristicsEntity(
      @Param(ID) UUID id,
      @Param(CHARACTERISTICS_ATTACK) Long attack,
      @Param(CHARACTERISTICS_CRIT_CHANCE) Long critChance,
      @Param(CHARACTERISTICS_CRIT_DAMAGE) Long critDamage,
      @Param(CHARACTERISTICS_HEALTH) Long health,
      @Param(CHARACTERISTICS_RESISTANCE) Long resistance);

  @Query("insert into t_characteristics_tgch (id) values (:id) returning *")
  CharacteristicsEntity createNewCharacteristicsEntity(@Param(ID) UUID id);

  @Query(
      """
              update t_characteristics_tgch set
              attack = coalesce(:attack, attack),
              crit_chance = coalesce(:crit_chance, crit_chance),
              crit_damage = coalesce(:crit_damage, crit_damage),
              health = coalesce(:health, health),
              resistance = coalesce(:resistance, resistance)
              where id = :id
              returning *
              """)
  CharacteristicsEntity updateCharacteristics(
      @Param(ID) UUID id,
      @Param(CHARACTERISTICS_ATTACK) Long attack,
      @Param(CHARACTERISTICS_CRIT_CHANCE) Long critChance,
      @Param(CHARACTERISTICS_CRIT_DAMAGE) Long critDamage,
      @Param(CHARACTERISTICS_HEALTH) Long health,
      @Param(CHARACTERISTICS_RESISTANCE) Long resistance);

  @Query("select * from t_characteristics_tgch where id=:id")
  Optional<CharacteristicsEntity> findCharacteristicsEntityById(@Param(ID) UUID id);

  @Query("select count(id) from t_characteristics_tgch")
  Long count();
}
