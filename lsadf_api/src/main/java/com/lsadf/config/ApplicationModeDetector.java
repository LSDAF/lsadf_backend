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

import java.util.Arrays;
import lombok.experimental.UtilityClass;
import org.springframework.core.env.Environment;

/** Utility class for detecting application mode from Spring profiles. */
@UtilityClass
public class ApplicationModeDetector {

  /**
   * Detect the application mode from active Spring profiles
   *
   * @param env Spring environment
   * @return detected application mode
   */
  public static ApplicationMode detectApplicationMode(Environment env) {
    String[] activeProfiles = env.getActiveProfiles();

    if (Arrays.asList(activeProfiles).contains("api")) {
      return ApplicationMode.API;
    } else if (Arrays.asList(activeProfiles).contains("worker")) {
      return ApplicationMode.WORKER;
    } else if (Arrays.asList(activeProfiles).contains("standalone")) {
      return ApplicationMode.STANDALONE;
    }

    return ApplicationMode.UNKNOWN;
  }
}
