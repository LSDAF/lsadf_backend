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
package com.lsadf.core.infra.cache.service;

public class NoOpCacheServiceImpl implements CacheService {

  /** {@inheritDoc} */
  @Override
  public Boolean isEnabled() {
    return false;
  }

  /** {@inheritDoc} */
  @Override
  public void toggleCacheEnabling() {
    // Do nothing
  }

  /** {@inheritDoc} */
  @Override
  public void clearCaches() {
    // Do nothing
  }

  /** {@inheritDoc} */
  @Override
  public void clearGameSaveValues(String key) {
    // Do nothing
  }
}
