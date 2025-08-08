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

import com.lsadf.core.application.shared.HistoCachePort;
import com.lsadf.core.infra.valkey.cache.Hash;
import com.lsadf.core.infra.valkey.cache.HashModelMapper;
import com.lsadf.core.infra.valkey.cache.HashRepository;
import com.lsadf.core.shared.model.Model;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public abstract class ValkeyCacheRepositoryAdapter<T extends Model, H extends Hash<I>, I>
    implements HistoCachePort<T> {
  protected HashModelMapper<H, T> hashMapper;
  protected HashRepository<H, I> repository;
  protected long expirationSeconds;

  protected ValkeyCacheRepositoryAdapter(HashRepository<H, I> repository) {
    this.repository = repository;
  }

  @Override
  public Optional<T> getHisto(String key) {
    return Optional.empty();
  }

  @Override
  public Map<String, T> getAllHisto() {
    return Map.of();
  }

  @Override
  public Map<String, T> getAll() {
    Map<String, T> map = new HashMap<>();
    Iterable<H> hashes = repository.findAll();
    for (H hash : hashes) {
      T object = hashMapper.map(hash);
      map.put(hash.getId().toString(), object);
    }

    return map;
  }

  @Override
  public void clear() {
    repository.deleteAll();
  }
}
