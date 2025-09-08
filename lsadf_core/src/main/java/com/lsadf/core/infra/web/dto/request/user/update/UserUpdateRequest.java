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

package com.lsadf.core.infra.web.dto.request.user.update;

import com.lsadf.core.infra.web.dto.request.Request;
import java.util.List;
import org.jspecify.annotations.Nullable;

/**
 * Represents a request to update user information. This interface defines the contract for
 * retrieving user-related details such as personal information, account status, and roles in the
 * context of an update request.
 *
 * <p>Implementations of this interface should provide the necessary data for updating specific user
 * details while adhering to the defined structure.
 */
public interface UserUpdateRequest extends Request {
  /**
   * Retrieves the first name of the user associated with the update request.
   *
   * @return the first name of the user as a String
   */
  String getFirstName();

  /**
   * Retrieves the last name of the user associated with the update request.
   *
   * @return the last name of the user as a String
   */
  String getLastName();

  /**
   * Retrieves the email verification status of the user associated with the update request.
   *
   * @return the email verification status of the user as a Boolean, where true indicates the email
   *     is verified and false indicates it is not
   */
  @Nullable Boolean getEmailVerified();

  /**
   * Retrieves the enabled status of the user associated with the update request.
   *
   * @return the enabled status of the user as a Boolean, where true indicates the user is enabled
   *     and false indicates the user is disabled
   */
  @Nullable Boolean getEnabled();

  /**
   * Retrieves the roles assigned to the user associated with the update request.
   *
   * @return a list of role names as Strings assigned to the user
   */
  @Nullable List<String> getUserRoles();
}
