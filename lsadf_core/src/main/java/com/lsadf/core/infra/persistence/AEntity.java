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
package com.lsadf.core.infra.persistence;

import com.lsadf.core.infra.persistence.config.EntityAttributes;
import jakarta.persistence.*;
import java.io.Serial;
import java.util.Date;
import java.util.UUID;
import lombok.*;
import lombok.experimental.SuperBuilder;

@MappedSuperclass
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Getter
@Setter
@ToString
public abstract class AEntity implements Entity {

  @Serial private static final long serialVersionUID = 7495963088331648156L;

  @Id
  @Column(name = EntityAttributes.ID)
  @EqualsAndHashCode.Include
  protected String id;

  @Column(name = EntityAttributes.CREATED_AT, nullable = false, updatable = false)
  protected Date createdAt;

  @Column(name = EntityAttributes.UPDATED_AT)
  protected Date updatedAt;

  @PrePersist
  public void generateEntity() {
    // Generate UUID if not present
    if (id == null || id.isEmpty()) {
      id = UUID.randomUUID().toString();
    }

    // Set createdAt timestamp at creation
    createdAt = new Date();

    // Initialize updatedAt to the same value as createdAt
    updatedAt = createdAt;
  }

  @PreUpdate
  public void updateTimestamp() {
    // Update the updatedAt timestamp when the entity is modified
    updatedAt = new Date();
  }
}
