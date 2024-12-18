package com.lsadf.core.configurations;

import com.lsadf.core.properties.SwaggerContactProperties;
import com.lsadf.core.properties.SwaggerProperties;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.*;
import java.util.Arrays;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/** Configuration class for the Swagger. */
@Configuration
@Import({SwaggerContactProperties.class, SwaggerProperties.class})
public class SwaggerConfiguration {
  public static final String BEARER_AUTHENTICATION = "Bearer Authentication";
  public static final String OAUTH2_AUTHENTICATION = "OAuth2 Authentication";

  private static final String KEYCLOAK = "keycloak";

  private static final String BEARER = "bearer";
  private static final String JWT = "JWT";
  private static final String AUTHORIZATION = "Authorization";

  // HIDDEN ENDPOINTS
  private static final String[] HIDDEN_ENDPOINTS = {
    "/actuator", "/actuator/health/**",
  };

  @Bean
  public OpenApiCustomizer actuatorHealthCustomiser() {
    return openApi -> Arrays.stream(HIDDEN_ENDPOINTS).forEach(openApi.getPaths()::remove);
  }

  @Bean
  public OpenAPI openAPI(SwaggerProperties swaggerProperties) {
    Info info = new Info();

    info.setContact(buildContact(swaggerProperties.getContact()));
    info.setDescription(swaggerProperties.getDescription());
    info.setTitle(swaggerProperties.getTitle());

    return new OpenAPI()
        .addSecurityItem(new SecurityRequirement().addList(BEARER_AUTHENTICATION))
        .components(
            new Components()
                .addSecuritySchemes(BEARER_AUTHENTICATION, createApiKeyScheme())
                .addSecuritySchemes(
                    OAUTH2_AUTHENTICATION,
                    createOAuthScheme(swaggerProperties.getAuthenticationUri())))
        .info(info);
  }

  private SecurityScheme createOAuthScheme(String authUrl) {
    OAuthFlows flows = createOAuthFlows(authUrl);
    return new SecurityScheme().type(SecurityScheme.Type.OAUTH2).flows(flows);
  }

  private SecurityScheme createApiKeyScheme() {
    return new SecurityScheme()
        .name(AUTHORIZATION)
        .type(SecurityScheme.Type.HTTP)
        .bearerFormat(JWT)
        .scheme(BEARER);
  }

  private OAuthFlows createOAuthFlows(String authUrl) {
    OAuthFlow flow = createAuthorizationCodeFlow(authUrl);
    return new OAuthFlows().implicit(flow);
  }

  private OAuthFlow createAuthorizationCodeFlow(String authUrl) {
    return new OAuthFlow().authorizationUrl(authUrl).scopes(new Scopes());
  }

  private Contact buildContact(SwaggerContactProperties contactProperties) {
    Contact contact = new Contact();

    contact.setName(contactProperties.getName());
    contact.setEmail(contactProperties.getEmail());
    contact.setUrl(contactProperties.getUrl());

    return contact;
  }
}
