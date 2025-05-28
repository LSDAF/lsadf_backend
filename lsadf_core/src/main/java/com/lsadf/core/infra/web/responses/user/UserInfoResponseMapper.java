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

package com.lsadf.core.infra.web.responses.user;

import com.lsadf.core.domain.user.UserInfo;
import com.lsadf.core.infra.web.responses.ModelResponseMapper;

/**
 * A class that implements the mapping logic from a UserInfo model object to a UserInfoResponse
 * object. This class serves to transform the UserInfo domain model into a response format
 * appropriate for API communication.
 */
public class UserInfoResponseMapper implements ModelResponseMapper<UserInfo, UserInfoResponse> {
  /**
   * Maps a given UserInfo model object to a corresponding UserInfoResponse object.
   *
   * @param model the UserInfo model object to be mapped
   * @return the UserInfoResponse object representing the mapped data
   */
  @Override
  public UserInfoResponse mapToResponse(UserInfo model) {
    return UserInfoResponse.builder()
        .email(model.getEmail())
        .name(model.getName())
        .roles(model.getRoles())
        .verified(model.isVerified())
        .build();
  }
}
