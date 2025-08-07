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

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * Interface for a hash-based repository, extending Spring Data's {@link CrudRepository}.
 *
 * <p>This repository is designed to work with objects implementing the {@link Hash} interface. It
 * provides standard CRUD operations for persistent storage and retrieval of hash-based entities.
 *
 * @param <H> the type of the hash entity, which must implement the {@link Hash} interface
 * @param <I> the type of the unique identifier for the hash entity
 */
@NoRepositoryBean
public interface HashRepository<H extends Hash<I>, I> extends CrudRepository<H, I> {}
