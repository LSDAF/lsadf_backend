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

package com.lsadf.core.infra.valkey.cache.impl.save.characteristics;

import static com.lsadf.core.infra.valkey.cache.impl.save.characteristics.CharacteristicsHash.CharacteristicsHashAttributes.*;

import com.lsadf.core.infra.valkey.cache.Hash;
import java.io.Serial;
import java.util.UUID;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
import org.springframework.data.relational.core.mapping.Column;

@RedisHash(value = CHARACTERISTICS_HASH_KEY)
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CharacteristicsHash implements Hash<UUID> {

  @Serial private static final long serialVersionUID = -653854275622285267L;

  @Id
  @Column(value = CHARACTERISTICS_GAME_SAVE_ID)
  private UUID gameSaveId;

  @Column(value = CHARACTERISTICS_ATTACK)
  private Long attack;

  @Column(value = CHARACTERISTICS_CRIT_DAMAGE)
  private Long critDamage;

  @Column(value = CHARACTERISTICS_CRIT_CHANCE)
  private Long critChance;

  @Column(value = CHARACTERISTICS_HEALTH)
  private Long health;

  @Column(value = CHARACTERISTICS_RESISTANCE)
  private Long resistance;

  @TimeToLive public Long expiration;

  public CharacteristicsHash(
      UUID gameSaveId,
      Long attack,
      Long critDamage,
      Long critChance,
      Long health,
      Long resistance) {
    this.gameSaveId = gameSaveId;
    this.attack = attack;
    this.critDamage = critDamage;
    this.critChance = critChance;
    this.health = health;
    this.resistance = resistance;
  }

  @NoArgsConstructor(access = AccessLevel.PRIVATE)
  public static class CharacteristicsHashAttributes {
    public static final String CHARACTERISTICS_HASH_KEY = "characteristics";
    public static final String CHARACTERISTICS_GAME_SAVE_ID = "gameSaveId";
    public static final String CHARACTERISTICS_ATTACK = "attack";
    public static final String CHARACTERISTICS_CRIT_DAMAGE = "critDamage";
    public static final String CHARACTERISTICS_CRIT_CHANCE = "critChance";
    public static final String CHARACTERISTICS_HEALTH = "health";
    public static final String CHARACTERISTICS_RESISTANCE = "resistance";
  }

  @Override
  public UUID getId() {
    return this.gameSaveId;
  }
}
