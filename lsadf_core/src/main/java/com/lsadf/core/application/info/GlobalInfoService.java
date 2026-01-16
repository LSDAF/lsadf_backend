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
package com.lsadf.core.application.info;

import com.lsadf.core.domain.info.GlobalInfo;

/**
 * Provides methods to retrieve global information related to system state, including current time,
 * save counters, and user metrics.
 */
public interface GlobalInfoService {
  /**
   * Retrieves the global information including current timestamp, game save counter, and user
   * counter.
   *
   * @return a GlobalInfo object containing the global data such as the current time, game save
   *     count, and user count.
   */
  GlobalInfo getGlobalInfo();
}
