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
package com.lsadf.core.application.shared;

import java.util.Map;
import java.util.Optional;

public interface CachePort<T> {

  /**
   * Get the value for the key
   *
   * @param key The key to get the value for
   * @return The value for the key if present
   */
  Optional<T> get(String key);

  /**
   * Set the value for the key
   *
   * @param key The key to set the value for
   * @param value The value to set
   */
  void set(String key, T value);

  /**
   * Unset the value for the key
   *
   * @param key The key to unset the value for
   */
  void unset(String key);

  /**
   * Get all the entries in the cache
   *
   * @return A map of all the entries in the cache
   */
  Map<String, T> getAll();

  /** Clear the cache */
  void clear();
}
