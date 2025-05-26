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
package com.lsadf.core.infra.web.requests.user;

import com.lsadf.core.constants.JsonAttributes;
import com.lsadf.core.infra.web.requests.common.SortingParameter;
import com.lsadf.core.user.User;
import java.util.Comparator;
import org.springframework.data.domain.Sort;

public enum UserSortingParameter implements SortingParameter<User> {
  ID(JsonAttributes.ID, Sort.Direction.ASC, Comparator.comparing(User::getId)),
  ID_DESC(JsonAttributes.ID, Sort.Direction.DESC, Comparator.comparing(User::getId).reversed()),
  USERNAME(
      JsonAttributes.User.USERNAME, Sort.Direction.ASC, Comparator.comparing(User::getUsername)),
  USERNAME_DESC(
      JsonAttributes.User.USERNAME,
      Sort.Direction.DESC,
      Comparator.comparing(User::getUsername).reversed()),
  FIRST_NAME(
      JsonAttributes.User.FIRST_NAME, Sort.Direction.ASC, Comparator.comparing(User::getFirstName)),
  FIRST_NAME_DESC(
      JsonAttributes.User.FIRST_NAME,
      Sort.Direction.DESC,
      Comparator.comparing(User::getFirstName).reversed()),
  LAST_NAME(
      JsonAttributes.User.LAST_NAME, Sort.Direction.ASC, Comparator.comparing(User::getLastName)),
  LAST_NAME_DESC(
      JsonAttributes.User.LAST_NAME,
      Sort.Direction.DESC,
      Comparator.comparing(User::getLastName).reversed()),
  CREATED_TIMESTAMP(
      JsonAttributes.User.CREATED_TIMESTAMP,
      Sort.Direction.ASC,
      Comparator.comparing(User::getCreatedTimestamp)),
  CREATED_TIMESTAMP_DESC(
      JsonAttributes.User.CREATED_TIMESTAMP,
      Sort.Direction.DESC,
      Comparator.comparing(User::getCreatedTimestamp).reversed()),
  USER_ROLES(
      JsonAttributes.User.USER_ROLES,
      Sort.Direction.ASC,
      Comparator.comparing(o -> o.getUserRoles().toString())),
  USER_ROLES_DESC(
      JsonAttributes.User.USER_ROLES,
      Sort.Direction.DESC,
      (o1, o2) -> o2.getUserRoles().toString().compareTo(o1.getUserRoles().toString())),
  ENABLED(JsonAttributes.User.ENABLED, Sort.Direction.ASC, Comparator.comparing(User::getEnabled)),
  ENABLED_DESC(
      JsonAttributes.User.ENABLED,
      Sort.Direction.DESC,
      Comparator.comparing(User::getEnabled).reversed()),
  NONE(null, null, null);

  UserSortingParameter(String fieldName, Sort.Direction direction, Comparator<User> comparator) {
    this.fieldName = fieldName;
    this.direction = direction;
    this.comparator = comparator;
  }

  private final String fieldName;
  private final Sort.Direction direction;
  private final Comparator<User> comparator;

  @Override
  public String getFieldName() {
    return this.fieldName;
  }

  @Override
  public Sort.Direction getDirection() {
    return this.direction;
  }

  @Override
  public Comparator<User> getComparator() {
    return this.comparator;
  }

  /**
   * Get the sorting parameter from a string
   *
   * @param parameter the parameter
   * @return the sorting parameter
   */
  public static UserSortingParameter fromString(String parameter) {
    for (UserSortingParameter b : UserSortingParameter.values()) {
      if (b.name().equalsIgnoreCase(parameter)) {
        return b;
      }
    }
    throw new IllegalArgumentException("Invalid sorting parameter: " + parameter);
  }

  /**
   * Get the sorting parameter from a Sort.Order object
   *
   * @param order the order
   * @return the sorting parameter
   */
  public static UserSortingParameter fromOrder(Sort.Order order) {
    for (UserSortingParameter b : UserSortingParameter.values()) {
      if (b.fieldName.equalsIgnoreCase(order.getProperty())
          && b.direction.equals(order.getDirection())) {
        return b;
      }
    }
    throw new IllegalArgumentException("Invalid order parameter");
  }
}
