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

import java.io.Serializable;

/**
 * An interface representing a generic, serializable entity.
 *
 * <p>This interface is typically implemented by classes representing domain entities that are
 * intended to be stored in a persistent storage. It extends the {@link Serializable} interface to
 * ensure that implementing classes can be serialized, which is often required for persistence
 * operations or distributed communication.
 *
 * <p>Classes implementing this interface are expected to define their own unique identifier and any
 * additional attributes or behaviors required for their domain-specific functionality.
 *
 * <p>This serves as a marker interface, outlining a standard contract for entities without
 * enforcing specific method implementations.
 */
public interface Entity extends Serializable {}
