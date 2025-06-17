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

import com.lsadf.core.infra.utils.DateUtils;
import java.time.Clock;
import java.time.Instant;
import java.util.Date;

/** Implementation of ClockService */
public class ClockServiceImpl implements ClockService {

  private Clock clock;

  public ClockServiceImpl(Clock clock) {
    this.clock = clock;
  }

  /** {@inheritDoc} */
  @Override
  public Clock getClock() {
    return this.clock;
  }

  /** {@inheritDoc} */
  @Override
  public void setClock(Clock clock) {
    this.clock = clock;
  }

  /** {@inheritDoc} */
  @Override
  public Instant nowInstant() {
    return Instant.now(clock);
  }

  /** {@inheritDoc} */
  @Override
  public Date nowDate() {
    return DateUtils.dateFromClock(clock);
  }
}
