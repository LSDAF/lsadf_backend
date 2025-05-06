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
            .realm(keycloakProperties.getRealm())
            .clientId(keycloakAdminProperties.getClientId())
            .clientSecret(keycloakAdminProperties.getClientSecret())
            .grantType(OAuth2Constants.CLIENT_CREDENTIALS);

    if (keycloakProperties.getInternalUrl() != null) {
      builder.serverUrl(keycloakProperties.getInternalUrl());
    } else {
      builder.serverUrl(keycloakProperties.getUrl());
    }

    return builder.build();
  }
}
