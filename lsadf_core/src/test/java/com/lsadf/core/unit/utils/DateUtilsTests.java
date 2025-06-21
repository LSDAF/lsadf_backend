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
package com.lsadf.core.unit.utils;

import static org.assertj.core.api.Assertions.assertThat;

import com.lsadf.core.infra.util.DateUtils;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import org.junit.jupiter.api.Test;

class DateUtilsTests {

  private static final String FIXED_DATE_TIME = "2020-01-01T00:00:00+01:00";

  private static final String ZONE = "Europe/Paris";
  private static final ZoneId ZONE_ID = ZoneId.of(ZONE);

  @Test
  void test_dateFromClock() {
    // Given
    Instant instant = Instant.parse(FIXED_DATE_TIME);
    Clock clock = Clock.fixed(instant, ZONE_ID);

    // When
    Date date = DateUtils.dateFromClock(clock);

    // Then
    assertThat(date).isNotNull().isEqualTo("2020-01-01 00:00:00.000");
  }

  @Test
  void test_dateTimeToString() {
    // Given
    Instant instant = Instant.parse(FIXED_DATE_TIME);
    LocalDateTime dateTime = LocalDateTime.ofInstant(instant, ZONE_ID);

    // When
    String dateTimeString = DateUtils.dateTimeToString(dateTime);

    // Then
    assertThat(dateTimeString).isNotNull().isEqualTo("2020-01-01 00:00:00.000");
  }

  @Test
  void test_dateToString() {
    // Given
    Instant instant = Instant.parse(FIXED_DATE_TIME);
    Date date = Date.from(instant);

    // When
    String dateString = DateUtils.dateToString(date);

    // Then
    assertThat(dateString).isNotNull().isEqualTo("2020-01-01 00:00:00.000");
  }

  @Test
  void test_dateToLocalDateTime() {
    // Given
    Instant instant = Instant.parse(FIXED_DATE_TIME);
    Date date = Date.from(instant);

    // When
    LocalDateTime localDateTime = DateUtils.dateToLocalDateTime(date);

    // Then
    assertThat(localDateTime).isNotNull().isEqualTo("2020-01-01T00:00:00.000");
  }

  @Test
  void test_timestampToLocalDateTime() {
    // Given
    Instant instant = Instant.parse(FIXED_DATE_TIME);
    long timestamp = instant.getEpochSecond();

    // When
    LocalDateTime localDateTime = DateUtils.timestampToLocalDateTime(timestamp, ZONE_ID);

    // Then
    assertThat(localDateTime).isNotNull().isEqualTo("2020-01-01T00:00:00.000");
  }

  @Test
  void test_dateStringToDate() {
    // Given
    String dateString = "2020-01-01 22:22:22.222";

    // When
    Date date = DateUtils.dateTimeStringToDate(dateString, ZONE_ID);

    // Then
    assertThat(date).isNotNull().isEqualTo("2020-01-01 22:22:22.222");
  }
}
