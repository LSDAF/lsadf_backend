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
package com.lsadf.core.infra.clock;

import java.time.Clock;
import java.time.Instant;
import java.util.Date;

public interface ClockService {
  /**
   * Get the clock
   *
   * @return the clock
   */
  Clock getClock();

  /**
   * Set the clock
   *
   * @param clock the clock
   */
  void setClock(Clock clock);

  /**
   * Get the current instant
   *
   * @return the current instant
   */
  Instant nowInstant();

  /**
   * Get the current date
   *
   * @return the current date
   */
  Date nowDate();
}
