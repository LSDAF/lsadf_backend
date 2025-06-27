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

/**
 * An abstract base class for persistent entities that standardizes common attributes and behavior
 * such as unique identifiers, creation timestamps, and update timestamps. This class implements the
 * {@link Entity} interface, ensuring each entity has a unique identifier.
 *
 * <p>The class leverages JPA annotations to define persistence-level details, such as primary keys
 * and columns. It also provides lifecycle callback methods to handle operations at entity creation
 * and update events.
 *
 * <ul>
 *   <li>{@code id}: The unique identifier for the entity, generated automatically if not provided.
 *   <li>{@code createdAt}: The timestamp indicating when the entity was created. Automatically set
 *       during entity creation.
 *   <li>{@code updatedAt}: The timestamp indicating when the entity was last updated. Updated
 *       automatically during modification.
 * </ul>
 *
 * To use this class, extend it with specific entity implementations while defining additional
 * fields and behavior as needed.
 *
 * <p>The class relies on {@code Lombok} for boilerplate code reduction, including annotations like
 * {@code @Getter}, {@code @Setter}, {@code @ToString}, {@code @EqualsAndHashCode}, and
 * {@code @SuperBuilder}.
 *
 * <p>This class is annotated with {@code @MappedSuperclass}, indicating that it serves as a
 * superclass for JPA entities without being directly mapped to a database table.
 */
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
