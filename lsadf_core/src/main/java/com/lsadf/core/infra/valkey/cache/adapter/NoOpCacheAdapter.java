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
package com.lsadf.core.infra.valkey.cache.adapter;

import com.lsadf.core.application.shared.CachePort;
import java.util.Map;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NoOpCacheAdapter<T> implements CachePort<T> {

  @Override
  public Optional<T> get(String key) {
    return Optional.empty();
  }

  @Override
  public void set(String key, T value, int expirationSeconds) {
    // Do nothing
  }

  @Override
  public void set(String key, T value) {
    // Do nothing
  }

  @Override
  public Map<String, T> getAll() {
    return Map.of();
  }

  @Override
  public void clear() {
    // Do nothing
  }

  @Override
  public void unset(String key) {
    // Do nothing
  }
}
