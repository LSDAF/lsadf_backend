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
package com.lsadf.core.configurations;

import com.lsadf.core.infra.cache.properties.CacheExpirationProperties;
import com.lsadf.core.infra.cache.properties.CacheProperties;
import com.lsadf.core.properties.*;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/** Configuration class for properties */
@Configuration
public class PropertiesConfiguration {

  @Bean
  @ConfigurationProperties(prefix = "jpa")
  public JpaProperties jpaProperties() {
    return new JpaProperties();
  }

  @Bean
  @ConfigurationProperties(prefix = "json-view")
  public JsonViewProperties jsonViewProperties() {
    return new JsonViewProperties();
  }

  @Bean
  @ConfigurationProperties(prefix = "configuration-display")
  public ConfigurationDisplayProperties configurationDisplayProperties() {
    return new ConfigurationDisplayProperties();
  }

  @Bean
  @ConfigurationProperties(prefix = "http-log")
  public HttpLogProperties httpLogProperties() {
    return new HttpLogProperties();
  }

  @Bean
  @ConfigurationProperties(prefix = "keycloak")
  public KeycloakProperties keycloakProperties() {
    return new KeycloakProperties();
  }

  @Bean
  @ConfigurationProperties(prefix = "keycloak.admin")
  public KeycloakAdminProperties keycloakAdminProperties() {
    return new KeycloakAdminProperties();
  }

  @Bean
  @ConfigurationProperties(prefix = "cache")
  public CacheProperties cacheProperties() {
    return new CacheProperties();
  }

  @Bean
  @ConfigurationProperties(prefix = "server")
  public ServerProperties serverProperties() {
    return new ServerProperties();
  }

  @Bean
  @ConfigurationProperties(prefix = "clock")
  public ClockProperties clockProperties() {
    return new ClockProperties();
  }

  @Bean
  @ConfigurationProperties(prefix = "cache.expiration")
  public CacheExpirationProperties cacheExpirationProperties() {
    return new CacheExpirationProperties();
  }

  @Bean
  @ConfigurationProperties(prefix = "cache.redis")
  public RedisProperties redisProperties() {
    return new RedisProperties();
  }

  @Bean
  @ConfigurationProperties(prefix = "shutdown")
  public ShutdownProperties shutdownProperties() {
    return new ShutdownProperties();
  }

  @Bean
  @ConfigurationProperties(prefix = "db")
  public DataSourceProperties dataSourceProperties() {
    return new DataSourceProperties();
  }

  @ConfigurationProperties(prefix = "swagger")
  @Bean
  public SwaggerProperties swaggerProperties() {
    return new SwaggerProperties();
  }

  @ConfigurationProperties(prefix = "swagger.contact")
  @Bean
  public SwaggerContactProperties swaggerContactProperties() {
    return new SwaggerContactProperties();
  }

  @Bean
  @ConfigurationProperties(prefix = "cors")
  public CorsConfigurationProperties corsConfigurationProperties() {
    return new CorsConfigurationProperties();
  }

  @Bean
  @ConfigurationProperties(prefix = "oauth2")
  public OAuth2Properties oAuth2Properties() {
    return new OAuth2Properties();
  }
}
