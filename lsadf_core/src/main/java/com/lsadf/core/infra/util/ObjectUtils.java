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

package com.lsadf.core.infra.util;

import lombok.experimental.UtilityClass;
import org.jspecify.annotations.Nullable;

/** Utility class providing operations for handling and managing objects. */
@UtilityClass
public class ObjectUtils {

  /**
   * Returns the provided object if it is not null; otherwise, returns the specified default value.
   *
   * @param object the object to check, may be null
   * @param defaultValue the default value to return if the object is null
   * @return the non-null object, or the defaultValue if the object is null
   */
  public static <T> T getOrDefault(@Nullable T object, T defaultValue) {
    return object != null ? object : defaultValue;
  }
}
