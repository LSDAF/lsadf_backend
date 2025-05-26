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
package com.lsadf.core.domain.user;

import lombok.Getter;

@Getter
public enum SocialProvider {
  FACEBOOK("facebook"),
  GOOGLE("google"),
  LOCAL("local");

  SocialProvider(String providerType) {
    this.providerType = providerType;
  }

  private final String providerType;

  public static SocialProvider getDefaultSocialProvider() {
    return LOCAL;
  }

  public static SocialProvider fromString(String providerType) {
    for (SocialProvider socialProvider : SocialProvider.values()) {
      if (socialProvider.providerType.equals(providerType)) {
        return socialProvider;
      }
    }
    return getDefaultSocialProvider();
  }
}
