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
package com.lsadf.core.infra.web.config.keycloak;

import com.lsadf.core.infra.web.config.keycloak.mappers.UserRepresentationMapper;
import com.lsadf.core.infra.web.config.keycloak.mappers.UserToUserRepresentationMapper;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for setting up Keycloak integration.
 *
 * <p>This class provides beans to initialize and configure the Keycloak client used for
 * communicating with the Keycloak server. Two beans are defined:
 *
 * <p>1. A {@link Keycloak} instance configured with URL, realm, client ID, client secret, and grant
 * type for managing authentication and authorization.
 *
 * <p>2. A {@link UserToUserRepresentationMapper} for facilitating the mapping of user
 * representations.
 *
 * <p>The configuration leverages the properties defined in {@link KeycloakProperties} and {@link
 * KeycloakAdminProperties} to create and initialize the Keycloak client.
 */
@Configuration
public class KeycloakConfiguration {

  @Bean
  public Keycloak keycloak(
      KeycloakProperties keycloakProperties, KeycloakAdminProperties keycloakAdminProperties) {
    var builder =
        KeycloakBuilder.builder()
            .serverUrl(keycloakProperties.getUrl())
            .realm(keycloakProperties.getRealm())
            .clientId(keycloakAdminProperties.getClientId())
            .clientSecret(keycloakAdminProperties.getClientSecret())
            .grantType(OAuth2Constants.CLIENT_CREDENTIALS);

    return builder.build();
  }

  @Bean
  public UserToUserRepresentationMapper userMapper() {
    return new UserToUserRepresentationMapper();
  }

  @Bean
  public UserRepresentationMapper userRepresentationMapper() {
    return new UserRepresentationMapper();
  }
}
