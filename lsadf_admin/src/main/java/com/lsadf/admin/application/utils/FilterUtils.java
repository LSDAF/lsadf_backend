/*
 * Copyright 2024-2025 LSDAF
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package com.lsadf.admin.application.utils;

import com.lsadf.core.infra.exceptions.FilterProcessingException;
import com.lsadf.core.infra.utils.DateUtils;
import com.lsadf.core.infra.utils.StringUtils;
import com.vaadin.hilla.crud.filter.AndFilter;
import com.vaadin.hilla.crud.filter.Filter;
import com.vaadin.hilla.crud.filter.OrFilter;
import com.vaadin.hilla.crud.filter.PropertyStringFilter;
import java.util.Date;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;
import lombok.experimental.UtilityClass;

@UtilityClass
public final class FilterUtils {

  private static final String GET = "get";

  /**
   * Apply filters to a stream
   *
   * @param stream the stream
   * @param filter the filter
   * @param <T> the type of the stream
   * @return the filtered stream
   */
  public static <T> Stream<T> applyFilters(Stream<T> stream, Filter filter) {
    return stream.filter(evaluate(filter));
  }

  /**
   * Evaluate the filter
   *
   * @param filter the filter
   * @return the predicate
   */
  private static Predicate<Object> evaluate(Filter filter) {
    if (filter instanceof AndFilter andFilter) {
      List<Filter> children = andFilter.getChildren();
      return children.stream().map(FilterUtils::evaluate).reduce(Predicate::and).orElse(t -> true);
    } else if (filter instanceof OrFilter orFilter) {
      List<Filter> children = orFilter.getChildren();
      return children.stream().map(FilterUtils::evaluate).reduce(Predicate::or).orElse(t -> false);
    } else if (filter instanceof PropertyStringFilter propertyFilter) {
      return object -> {
        try {
          String getterMethod = GET + StringUtils.capitalize(propertyFilter.getPropertyId());
          Object propertyValue = object.getClass().getMethod(getterMethod).invoke(object);

          if (propertyValue instanceof Date date) {
            propertyValue = DateUtils.dateToString(date);
          }

          return evaluate(
              propertyFilter.getMatcher(),
              propertyValue.toString(),
              propertyFilter.getFilterValue());
        } catch (Exception e) {
          throw new FilterProcessingException(
              "Error accessing property: " + propertyFilter.getPropertyId(), e);
        }
      };
    }

    throw new IllegalArgumentException("Unknown filter type: " + filter.getClass());
  }

  /**
   * Evaluate the filter
   *
   * @param matcher the matcher
   * @param propertyValue the property value
   * @param filterValue the filter value
   * @return true if the filter is valid
   */
  private static boolean evaluate(
      PropertyStringFilter.Matcher matcher, String propertyValue, String filterValue) {
    return switch (matcher) {
      case EQUALS -> propertyValue.equals(filterValue);
      case CONTAINS -> propertyValue.contains(filterValue);
      case GREATER_THAN -> propertyValue.compareTo(filterValue) > 0;
      case LESS_THAN -> propertyValue.compareTo(filterValue) < 0;
    };
  }
}
