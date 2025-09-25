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
package com.lsadf.util;

import com.lsadf.config.ApplicationMode;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

/**
 * Validates that exactly one of the required application mode profiles (api, worker, standalone) is
 * active when the application starts.
 */
@Slf4j
@UtilityClass
public class ProfileValidator {

  /**
   * Validates that exactly one of the required profiles is active. Throws an IllegalStateException
   * if validation fails.
   *
   * @param activeProfiles the list of currently active Spring profiles
   */
  public static void validateApplicationMode(List<String> activeProfiles) {

    Set<String> validProfileNames =
        Arrays.stream(ApplicationMode.values())
            .map(ApplicationMode::getProfileName)
            .collect(Collectors.toSet());

    Set<String> activeRequiredProfiles = new HashSet<>(validProfileNames);
    activeRequiredProfiles.retainAll(activeProfiles);

    int activeRequiredProfileCount = activeRequiredProfiles.size();

    if (activeRequiredProfileCount == 0) {
      throw new IllegalStateException(
          "No application mode profile is active. Exactly one of these profiles must be active: "
              + String.join(", ", validProfileNames));
    } else if (activeRequiredProfileCount > 1) {
      throw new IllegalStateException(
          "Multiple application mode profiles are active: "
              + String.join(", ", activeRequiredProfiles)
              + ". Only one of these profiles must be active: "
              + String.join(", ", validProfileNames));
    }

    // Valid: exactly one required profile is active
    log.info("Active application mode profile: {}", String.join("", activeRequiredProfiles));
  }
}
