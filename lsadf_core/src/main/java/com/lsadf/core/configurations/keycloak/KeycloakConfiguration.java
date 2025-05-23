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
package com.lsadf.core.configurations.keycloak;

import com.lsadf.core.properties.KeycloakAdminProperties;
import com.lsadf.core.properties.KeycloakProperties;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
}
