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
package com.lsadf.core.infra.persistence;

import java.util.UUID;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.Repository;

/**
 * A repository interface extending {@link Repository} for managing entities of type {@link Entity}
 * and uniquely identified by {@link UUID}.
 *
 * <p>The {@code JdbcRepository} interface serves as a Data Access Layer abstraction for interacting
 * with persistent storage, specifically for entities conforming to the {@link Entity} contract. By
 * extending {@link Repository}, this interface supports the default Spring Data repository
 * functionality, like CRUD operations and custom query methods.
 *
 * <p>Key Characteristics: - The entities managed by this repository must implement the {@link
 * Entity} interface. - Each entity is uniquely identified by a {@link UUID}.
 */
@NoRepositoryBean
public interface JdbcRepository<E extends Entity> extends Repository<E, UUID> {}
