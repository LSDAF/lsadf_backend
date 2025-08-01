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

package com.lsadf.core.infra.valkey.cache;

import com.lsadf.core.application.shared.CachePort;
import com.lsadf.core.shared.model.Model;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;

public abstract class ValkeyCacheAdapter<H extends Hash<I>, M extends Model, I>
    implements CachePort<M, I> {
  protected abstract CrudRepository<H, I> getRepository();

  protected abstract HashModelMapper<H, M> getMapper();

  @Override
  public Optional<M> get(I key) {
    Optional<H> optionalHash = getRepository().findById(key);
    return optionalHash.map(hash -> getMapper().map(hash));
  }

  @Override
  public void unset(I key) {
    getRepository().deleteById(key);
  }

  @Override
  public Map<I, M> getAll() {
    Map<I, M> map = new HashMap<>();
    getRepository().findAll().forEach(h -> map.put(h.getId(), getMapper().map(h)));
    return map;
  }

  @Override
  public void clear() {
    getRepository().deleteAll();
  }
}
