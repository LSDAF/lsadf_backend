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
package com.lsadf.core.infra.valkey;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/** Redis constants class */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ValkeyConstants {
  public static final String CHARACTERISTICS_HISTO = "characteristics_histo:";
  public static final String CHARACTERISTICS = "characteristics:";
  public static final String CURRENCY = "currency:";
  public static final String CURRENCY_HISTO = "currency_histo:";
  public static final String STAGE = "stage:";
  public static final String STAGE_HISTO = "stage_histo:";
  public static final String GAME_METADATA = "game_metadata:";
  public static final String INVALIDATED_JWT_TOKEN = "invalidated_jwt_token:";
}
