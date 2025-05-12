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
package com.lsadf.core.services;

public interface CacheFlushService {
  /**
   * Flush the characteristics cache, and persists the characteristics of every entries in the
   * database
   */
  void flushCharacteristics();

  /** Flush the currency cache, and persists the currency of every entries in the database */
  void flushCurrencies();

  /** Flush the stage cache, and persists the stage of every entries in the database */
  void flushStages();
}
