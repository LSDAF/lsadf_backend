/*
 * Copyright © 2024-2026 LSDAF
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

import static com.lsadf.core.infra.valkey.cache.impl.save.currency.CurrencyHash.CurrencyHashAttributes.*;

import com.lsadf.core.infra.valkey.cache.Hash;
import java.util.UUID;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
import org.springframework.data.relational.core.mapping.Column;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@RedisHash(value = CURRENCY_HASH_KEY)
public class CurrencyHash implements Hash<UUID> {

  @Id
  @Column(value = CURRENCY_GAME_SAVE_ID)
  private UUID id;

  @Column(value = CURRENCY_GOLD)
  private Long gold;

  @Column(value = CURRENCY_DIAMOND)
  private Long diamond;

  @Column(value = CURRENCY_EMERALD)
  private Long emerald;

  @Column(value = CURRENCY_AMETHYST)
  private Long amethyst;

  @TimeToLive private Long expiration;

  @NoArgsConstructor(access = AccessLevel.PRIVATE)
  public static class CurrencyHashAttributes {
    public static final String CURRENCY_GAME_SAVE_ID = "gameSaveId";
    public static final String CURRENCY_HASH_KEY = "currency";
    public static final String CURRENCY_GOLD = "gold";
    public static final String CURRENCY_DIAMOND = "diamond";
    public static final String CURRENCY_EMERALD = "emerald";
    public static final String CURRENCY_AMETHYST = "amethyst";
  }
}
