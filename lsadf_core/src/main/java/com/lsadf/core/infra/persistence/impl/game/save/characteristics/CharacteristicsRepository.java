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
package com.lsadf.core.infra.persistence.impl.game.save.characteristics;

import static com.lsadf.core.infra.persistence.impl.game.save.characteristics.CharacteristicsEntity.CharacteristicsEntityAttributes.*;
import static com.lsadf.core.infra.persistence.impl.game.save.metadata.GameMetadataEntity.GameSaveMetadataAttributes.GAME_METADATA_ID;

import com.lsadf.core.infra.persistence.JdbcRepository;
import java.util.Optional;
import java.util.UUID;
import org.jspecify.annotations.Nullable;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/** Repository class for CharacteristicsEntity */
@Repository
public interface CharacteristicsRepository extends JdbcRepository<CharacteristicsEntity> {
  @Query(
      """
              insert into t_characteristics_tgch
                  (tgme_id,
                   tgch_attack,
                   tgch_crit_chance,
                   tgch_crit_damage,
                   tgch_health,
                   tgch_resistance)
              values (:tgme_id,
                      :tgch_attack,
                      :tgch_crit_chance,
                      :tgch_crit_damage,
                      :tgch_health,
                      :tgch_resistance)
              returning *""")
  CharacteristicsEntity createNewCharacteristicsEntity(
      @Param(GAME_METADATA_ID) UUID id,
      @Param(CHARACTERISTICS_ATTACK) Long attack,
      @Param(CHARACTERISTICS_CRIT_CHANCE) Long critChance,
      @Param(CHARACTERISTICS_CRIT_DAMAGE) Long critDamage,
      @Param(CHARACTERISTICS_HEALTH) Long health,
      @Param(CHARACTERISTICS_RESISTANCE) Long resistance);

  @Query("insert into t_characteristics_tgch (tgme_id) values (:tgme_id) returning *")
  CharacteristicsEntity createNewCharacteristicsEntity(@Param(GAME_METADATA_ID) UUID id);

  @Query(
      """
              update t_characteristics_tgch set
              tgch_attack = coalesce(:tgch_attack, tgch_attack),
              tgch_crit_chance = coalesce(:tgch_crit_chance, tgch_crit_chance),
              tgch_crit_damage = coalesce(:tgch_crit_damage, tgch_crit_damage),
              tgch_health = coalesce(:tgch_health, tgch_health),
              tgch_resistance = coalesce(:tgch_resistance, tgch_resistance)
              where tgme_id = :tgme_id
              returning *
              """)
  CharacteristicsEntity updateCharacteristics(
      @Nullable @Param(GAME_METADATA_ID) UUID id,
      @Nullable @Param(CHARACTERISTICS_ATTACK) Long attack,
      @Nullable @Param(CHARACTERISTICS_CRIT_CHANCE) Long critChance,
      @Nullable @Param(CHARACTERISTICS_CRIT_DAMAGE) Long critDamage,
      @Nullable @Param(CHARACTERISTICS_HEALTH) Long health,
      @Nullable @Param(CHARACTERISTICS_RESISTANCE) Long resistance);

  @Query("select * from t_characteristics_tgch where tgme_id=:tgme_id")
  Optional<CharacteristicsEntity> findCharacteristicsEntityById(@Param(GAME_METADATA_ID) UUID id);

  @Query("select count(tgme_id) from t_characteristics_tgch")
  Long count();
}
