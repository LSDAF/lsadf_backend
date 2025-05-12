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
package com.lsadf.bdd;

public enum CacheEntryType {
  CHARACTERISTICS,
  CHARACTERISTICS_HISTO,
  CURRENCY,
  CURRENCY_HISTO,
  STAGE,
  STAGE_HISTO,
  GAME_SAVE_OWNERSHIP;

  public static CacheEntryType fromString(String cacheEntryType) {
    for (CacheEntryType value : CacheEntryType.values()) {
      if (value.name().equalsIgnoreCase(cacheEntryType)) {
        return value;
      }
    }
    throw new IllegalArgumentException("Invalid cache entry type: " + cacheEntryType);
  }
}
