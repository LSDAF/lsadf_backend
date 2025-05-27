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

package com.lsadf.core.application.user;

import com.lsadf.core.infra.clock.ClockService;
import com.lsadf.core.infra.persistence.mappers.Mapper;
import com.lsadf.core.infra.web.config.auth.keycloak.KeycloakProperties;
import org.keycloak.admin.client.Keycloak;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for user-related beans.
 *
 * <p>This class is responsible for creating and configuring beans related to user management.
 */
@Configuration
public class UserConfiguration {
  @Bean
  public UserService userService(
      Keycloak keycloak,
      KeycloakProperties keycloakProperties,
      ClockService clockService,
      Mapper mapper) {
    return new UserServiceImpl(keycloak, keycloakProperties, clockService, mapper);
  }
}
