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
package com.lsadf.config;

/** Enum representing the different application modes based on Spring profiles. */
public enum ApplicationMode {
  /** API-only mode: enables API controllers but disables worker functionality */
  API,

  /** Worker-only mode: disables API controllers and enables only worker functionality */
  WORKER,

  /** Standalone mode: enables both API controllers and worker functionality */
  STANDALONE,

  /** Unknown mode: when no specific mode profile is detected */
  UNKNOWN
}
