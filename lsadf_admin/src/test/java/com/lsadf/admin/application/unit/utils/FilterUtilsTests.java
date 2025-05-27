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
package com.lsadf.admin.application.unit.utils;

import static org.assertj.core.api.Assertions.assertThat;

import com.lsadf.admin.application.utils.FilterUtils;
import com.lsadf.core.domain.user.User;
import com.lsadf.core.infra.utils.DateUtils;
import com.vaadin.hilla.crud.filter.AndFilter;
import com.vaadin.hilla.crud.filter.Filter;
import com.vaadin.hilla.crud.filter.OrFilter;
import com.vaadin.hilla.crud.filter.PropertyStringFilter;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FilterUtilsTests {

  private static final String ZONE = "Europe/Paris";
  private static final ZoneId ZONE_ID = ZoneId.of(ZONE);

  private Stream<User> userStream;

  private final User user1 =
      User.builder()
          .id("cc5a0889-9f5c-4869-bfe0-d8c7c1aca520")
          .firstName("John")
          .lastName("Doe")
          .username("john.doe@test.com")
          .enabled(true)
          .createdTimestamp(DateUtils.dateTimeStringToDate("2020-01-01 00:00:00.000", ZONE_ID))
          .build();

  private final User user2 =
      User.builder()
          .id("ceda21e6-6399-47d7-9548-ec3c7d23ba22")
          .firstName("Jane")
          .lastName("Doe")
          .username("jane.doe@test.com")
          .enabled(true)
          .createdTimestamp(DateUtils.dateTimeStringToDate("2021-01-01 00:00:00.000", ZONE_ID))
          .build();

  private final User user3 =
      User.builder()
          .id("854291f2-ab1b-45c6-9b65-6ef4f1dd4063")
          .firstName("John")
          .lastName("Smith")
          .username("john.smith@test.com")
          .enabled(false)
          .createdTimestamp(DateUtils.dateTimeStringToDate("2022-01-01 00:00:00.000", ZONE_ID))
          .build();

  private final User user4 =
      User.builder()
          .id("4a6a9ea1-4b90-4319-ad0e-e4f9a1a43b28")
          .firstName("Jane")
          .lastName("Smith")
          .username("jane.smith@test.com")
          .enabled(false)
          .createdTimestamp(DateUtils.dateTimeStringToDate("2023-01-01 00:00:00.000", ZONE_ID))
          .build();

  @BeforeEach
  void setUp() {
    userStream = Stream.of(user1, user2, user3, user4);
  }

  // test propertyStringFilter

  @Test
  void test_equals_propertyStringFilter() {
    // Given
    Filter filter =
        generatePropertyStringFilter("firstName", "John", PropertyStringFilter.Matcher.EQUALS);

    // When
    Stream<User> filteredStream = FilterUtils.applyFilters(userStream, filter);

    // Then
    assertThat(filteredStream).containsExactly(user1, user3);
  }

  @Test
  void test_contains_propertyStringFilter() {
    // Given
    Filter filter =
        generatePropertyStringFilter("username", "smith", PropertyStringFilter.Matcher.CONTAINS);

    // When
    Stream<User> filteredStream = FilterUtils.applyFilters(userStream, filter);

    // Then
    assertThat(filteredStream).containsExactly(user3, user4);
  }

  @Test
  void test_date_greater_than_propertyStringFilter() {
    // Given
    Filter filter =
        generatePropertyStringFilter(
            "createdTimestamp",
            "2021-01-01 00:00:00.000",
            PropertyStringFilter.Matcher.GREATER_THAN);

    // When
    Stream<User> filteredStream = FilterUtils.applyFilters(userStream, filter);

    // Then
    assertThat(filteredStream).containsExactly(user3, user4);
  }

  @Test
  void test_date_less_than_propertyStringFilter() {
    // Given
    Filter filter =
        generatePropertyStringFilter(
            "createdTimestamp", "2022-01-01 00:00:00.000", PropertyStringFilter.Matcher.LESS_THAN);

    // When
    Stream<User> filteredStream = FilterUtils.applyFilters(userStream, filter);

    // Then
    assertThat(filteredStream).containsExactly(user1, user2);
  }

  @Test
  void test_andFilter() {
    // Given
    Filter filter1 =
        generatePropertyStringFilter("firstName", "John", PropertyStringFilter.Matcher.EQUALS);
    Filter filter2 =
        generatePropertyStringFilter("enabled", "true", PropertyStringFilter.Matcher.EQUALS);
    AndFilter andFilter = generateAndFilter(filter1, filter2);

    // When
    Stream<User> filteredStream = FilterUtils.applyFilters(userStream, andFilter);

    // Then
    assertThat(filteredStream).containsExactly(user1);
  }

  @Test
  void test_orFilter() {
    // Given
    Filter filter1 =
        generatePropertyStringFilter("firstName", "John", PropertyStringFilter.Matcher.EQUALS);
    Filter filter2 =
        generatePropertyStringFilter("enabled", "true", PropertyStringFilter.Matcher.EQUALS);
    OrFilter orFilter = generateOrFilter(filter1, filter2);

    // When
    Stream<User> filteredStream = FilterUtils.applyFilters(userStream, orFilter);

    // Then
    assertThat(filteredStream).containsExactly(user1, user2, user3);
  }

  /**
   * Generate a PropertyStringFilter
   *
   * @param propertyId the property id
   * @param filterValue the filter value
   * @param matcher the matcher
   * @return the PropertyStringFilter
   */
  private static PropertyStringFilter generatePropertyStringFilter(
      String propertyId, String filterValue, PropertyStringFilter.Matcher matcher) {
    PropertyStringFilter propertyStringFilter = new PropertyStringFilter();
    propertyStringFilter.setPropertyId(propertyId);
    propertyStringFilter.setFilterValue(filterValue);
    propertyStringFilter.setMatcher(matcher);

    return propertyStringFilter;
  }

  /**
   * Generate an AndFilter
   *
   * @param filters the children filters
   * @return the AndFilter
   */
  private static AndFilter generateAndFilter(Filter... filters) {
    AndFilter andFilter = new AndFilter();
    andFilter.setChildren(List.of(filters));

    return andFilter;
  }

  /**
   * Generate an OrFilter
   *
   * @param filters the children filters
   * @return the OrFilter
   */
  private static OrFilter generateOrFilter(Filter... filters) {
    OrFilter orFilter = new OrFilter();
    orFilter.setChildren(List.of(filters));

    return orFilter;
  }
}
