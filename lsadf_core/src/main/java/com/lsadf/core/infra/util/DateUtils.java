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
package com.lsadf.core.infra.util;

import java.sql.Timestamp;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import lombok.experimental.UtilityClass;

/** Utility class for date transformation operations */
@UtilityClass
public class DateUtils {
  public static final DateTimeFormatter formatter =
      DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
  public static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

  /**
   * Convert LocalDateTime to String
   *
   * @param dateTime LocalDateTime to convert
   * @return Converted Date in String format
   */
  public static String dateTimeToString(LocalDateTime dateTime) {
    return dateTime.format(formatter);
  }

  /**
   * Convert Date to String
   *
   * @param date Date to convert
   * @return Converted Date in String format
   */
  public static String dateToString(Date date) {
    return dateToLocalDateTime(date).format(formatter);
  }

  /**
   * Convert Date to LocalDateTime
   *
   * @param dateToConvert Date to convert
   * @return Converted LocalDateTime
   */
  public static LocalDateTime dateToLocalDateTime(Date dateToConvert) {
    return new Timestamp(dateToConvert.getTime()).toLocalDateTime();
  }

  /**
   * Convert timestamp to LocalDateTime
   *
   * @param timestamp timestamp to convert
   * @return Converted LocalDateTime
   */
  public static LocalDateTime timestampToLocalDateTime(long timestamp, ZoneId zoneId) {
    Instant instant = Instant.ofEpochSecond(timestamp);
    return LocalDateTime.ofInstant(instant, zoneId);
  }

  /**
   * Convert date time format string to Date
   *
   * @param date String to convert
   * @return Converted Date
   */
  public static Date dateTimeStringToDate(String date, ZoneId zoneId) {
    return Date.from(LocalDateTime.parse(date, formatter).atZone(zoneId).toInstant());
  }

  /**
   * Convert Date from clock
   *
   * @param clock Clock to use
   * @return Date
   */
  public static Date dateFromClock(Clock clock) {
    Instant instant = Instant.now(clock);
    return Date.from(instant);
  }

  /**
   * Converts a given timestamp to a Date object. If the provided timestamp is null, this method
   * returns null.
   *
   * @param timestamp the timestamp to convert, expressed in milliseconds since the epoch
   * @return the corresponding Date object, or null if the input timestamp is null
   */
  public static Date dateFromTimestamp(Long timestamp) {
    return (timestamp == null) ? null : new Date(timestamp);
  }
}
