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
// ...existing code...

package com.lsadf.util;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class ProfileValidatorTests {

  @ParameterizedTest
  @ValueSource(strings = {"api", "worker", "standalone"})
  @DisplayName("Should pass validation when only one valid application mode profile is active")
  void shouldPassValidation_WhenOnlyOneValidProfileActive(String profileName) {
    List<String> activeProfiles = Arrays.asList(profileName);

    assertDoesNotThrow(() -> ProfileValidator.validateApplicationMode(activeProfiles));
  }

  @Test
  @DisplayName(
      "Should pass validation when valid profile is mixed with other non-application-mode profiles")
  void shouldPassValidation_WhenValidProfileMixedWithOtherProfiles() {
    List<String> activeProfiles = Arrays.asList("api", "dev", "docker");

    assertDoesNotThrow(() -> ProfileValidator.validateApplicationMode(activeProfiles));
  }

  @Test
  @DisplayName("Should fail validation when profile has different case (case sensitive)")
  void shouldFailValidation_WhenProfileHasDifferentCase() {
    List<String> activeProfiles = Arrays.asList("API", "dev");

    IllegalStateException exception =
        assertThrows(
            IllegalStateException.class,
            () -> ProfileValidator.validateApplicationMode(activeProfiles));

    assertTrue(exception.getMessage().startsWith("No application mode profile is active"));
  }

  @Test
  @DisplayName("Should throw exception when no application mode profiles are active")
  void shouldThrowException_WhenNoApplicationModeProfilesActive() {
    List<String> activeProfiles = Arrays.asList("docker");

    IllegalStateException exception =
        assertThrows(
            IllegalStateException.class,
            () -> ProfileValidator.validateApplicationMode(activeProfiles));

    assertTrue(exception.getMessage().startsWith("No application mode profile is active"));
    assertTrue(exception.getMessage().contains("api"));
    assertTrue(exception.getMessage().contains("worker"));
    assertTrue(exception.getMessage().contains("standalone"));
  }

  @Test
  @DisplayName("Should throw exception when profile list is empty")
  void shouldThrowException_WhenProfileListIsEmpty() {
    List<String> activeProfiles = Collections.emptyList();

    IllegalStateException exception =
        assertThrows(
            IllegalStateException.class,
            () -> ProfileValidator.validateApplicationMode(activeProfiles));

    assertTrue(exception.getMessage().startsWith("No application mode profile is active"));
  }

  @Test
  @DisplayName(
      "Should throw exception when multiple application mode profiles are active - api and worker")
  void shouldThrowException_WhenApiAndWorkerProfilesActive() {
    List<String> activeProfiles = Arrays.asList("api", "worker", "docker");

    IllegalStateException exception =
        assertThrows(
            IllegalStateException.class,
            () -> ProfileValidator.validateApplicationMode(activeProfiles));

    String expectedMessage = exception.getMessage();
    assertTrue(expectedMessage.startsWith("Multiple application mode profiles are active:"));
    assertTrue(expectedMessage.contains("api"));
    assertTrue(expectedMessage.contains("worker"));
    assertTrue(expectedMessage.contains("Only one of these profiles must be active"));
  }

  @Test
  @DisplayName(
      "Should throw exception when multiple application mode profiles are active - api and standalone")
  void shouldThrowException_WhenApiAndStandaloneProfilesActive() {
    List<String> activeProfiles = Arrays.asList("api", "standalone");

    IllegalStateException exception =
        assertThrows(
            IllegalStateException.class,
            () -> ProfileValidator.validateApplicationMode(activeProfiles));

    String expectedMessage = exception.getMessage();
    assertTrue(expectedMessage.startsWith("Multiple application mode profiles are active:"));
    assertTrue(expectedMessage.contains("api"));
    assertTrue(expectedMessage.contains("standalone"));
    assertTrue(expectedMessage.contains("Only one of these profiles must be active"));
  }

  @Test
  @DisplayName(
      "Should throw exception when multiple application mode profiles are active - worker and standalone")
  void shouldThrowException_WhenWorkerAndStandaloneProfilesActive() {
    List<String> activeProfiles = Arrays.asList("worker", "standalone", "docker");

    IllegalStateException exception =
        assertThrows(
            IllegalStateException.class,
            () -> ProfileValidator.validateApplicationMode(activeProfiles));

    String expectedMessage = exception.getMessage();
    assertTrue(expectedMessage.startsWith("Multiple application mode profiles are active:"));
    assertTrue(expectedMessage.contains("worker"));
    assertTrue(expectedMessage.contains("standalone"));
    assertTrue(expectedMessage.contains("Only one of these profiles must be active"));
  }

  @Test
  @DisplayName("Should throw exception when all three application mode profiles are active")
  void shouldThrowException_WhenAllThreeApplicationModeProfilesActive() {
    List<String> activeProfiles = Arrays.asList("api", "worker", "standalone");

    IllegalStateException exception =
        assertThrows(
            IllegalStateException.class,
            () -> ProfileValidator.validateApplicationMode(activeProfiles));

    String expectedMessage = exception.getMessage();
    assertTrue(expectedMessage.startsWith("Multiple application mode profiles are active:"));
    assertTrue(expectedMessage.contains("api"));
    assertTrue(expectedMessage.contains("worker"));
    assertTrue(expectedMessage.contains("standalone"));
    assertTrue(expectedMessage.contains("Only one of these profiles must be active"));
  }

  @Test
  @DisplayName("Should throw NullPointerException when profile list is null")
  void shouldThrowNullPointerException_WhenProfileListIsNull() {
    List<String> activeProfiles = null;

    assertThrows(
        NullPointerException.class, () -> ProfileValidator.validateApplicationMode(activeProfiles));
  }

  @Test
  @DisplayName("Should handle duplicate profiles correctly")
  void shouldHandleDuplicateProfilesCorrectly() {
    List<String> activeProfiles = Arrays.asList("api", "api", "docker", "docker");

    assertDoesNotThrow(() -> ProfileValidator.validateApplicationMode(activeProfiles));
  }

  @Test
  @DisplayName("Should be case sensitive - uppercase profiles should not match")
  void shouldBeCaseSensitive_UppercaseProfilesShouldNotMatch() {
    List<String> activeProfiles = Arrays.asList("API", "WORKER", "STANDALONE");

    IllegalStateException exception =
        assertThrows(
            IllegalStateException.class,
            () -> ProfileValidator.validateApplicationMode(activeProfiles));

    assertTrue(exception.getMessage().startsWith("No application mode profile is active"));
  }
}
