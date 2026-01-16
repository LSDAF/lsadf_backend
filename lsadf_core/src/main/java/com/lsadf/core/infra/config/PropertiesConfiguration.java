/*
 * Copyright © 2024-2026 LSDAF
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
package com.lsadf.core.infra.config;

import com.lsadf.core.infra.clock.ClockProperties;
import com.lsadf.core.infra.logging.properties.ConfigurationDisplayProperties;
import com.lsadf.core.infra.logging.properties.HttpLogProperties;
import com.lsadf.core.infra.persistence.config.JdbcProperties;
import com.lsadf.core.infra.persistence.config.properties.DataSourceProperties;
import com.lsadf.core.infra.scheduling.config.GameMailCleanupProperties;
import com.lsadf.core.infra.scheduling.config.SchedulingProperties;
import com.lsadf.core.infra.valkey.config.properties.ValkeyCacheExpirationProperties;
import com.lsadf.core.infra.valkey.config.properties.ValkeyGameStreamPersistenceProperties;
import com.lsadf.core.infra.valkey.config.properties.ValkeyGameStreamProperties;
import com.lsadf.core.infra.valkey.config.properties.ValkeyProperties;
import com.lsadf.core.infra.web.config.api.properties.ApiConfigurationProperties;
import com.lsadf.core.infra.web.config.keycloak.properties.KeycloakAdminProperties;
import com.lsadf.core.infra.web.config.keycloak.properties.KeycloakProperties;
import com.lsadf.core.infra.web.config.security.OAuth2Properties;
import com.lsadf.core.infra.web.config.security.properties.CorsConfigurationProperties;
import com.lsadf.core.infra.web.config.swagger.properties.SwaggerContactProperties;
import com.lsadf.core.infra.web.config.swagger.properties.SwaggerProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/** Configuration class for properties */
@Configuration
public class PropertiesConfiguration {

  @Bean
  @ConfigurationProperties(prefix = "jdbc")
  public JdbcProperties jdbcProperties() {
    return new JdbcProperties();
  }

  @Bean
  @ConfigurationProperties(prefix = "configuration-display")
  public ConfigurationDisplayProperties configurationDisplayProperties() {
    return new ConfigurationDisplayProperties();
  }

  @Bean
  @ConfigurationProperties(prefix = "async")
  public AsyncProperties asyncProperties() {
    return new AsyncProperties();
  }

  @Bean
  @ConfigurationProperties(prefix = "valkey.stream.game")
  public ValkeyGameStreamProperties streamGameValkeyProperties() {
    return new ValkeyGameStreamProperties();
  }

  @Bean
  @ConfigurationProperties(prefix = "valkey.stream.game.persistence")
  public ValkeyGameStreamPersistenceProperties valkeyGameStreamPersistenceProperties() {
    return new ValkeyGameStreamPersistenceProperties();
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
  @ConfigurationProperties(prefix = "valkey.cache.expiration")
  public ValkeyCacheExpirationProperties cacheExpirationProperties() {
    return new ValkeyCacheExpirationProperties();
  }

  @Bean
  @ConfigurationProperties(prefix = "valkey.config")
  public ValkeyProperties valkeyProperties() {
    return new ValkeyProperties();
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

  @Bean
  @ConfigurationProperties(prefix = "api")
  public ApiConfigurationProperties apiConfigurationProperties() {
    return new ApiConfigurationProperties();
  }

  @Bean
  @ConfigurationProperties(prefix = "scheduling")
  public SchedulingProperties schedulingProperties() {
    return new SchedulingProperties();
  }

  @Bean
  @ConfigurationProperties(prefix = "scheduling.game-mail-cleanup")
  public GameMailCleanupProperties expiredGameMailCleanupProperties() {
    return new GameMailCleanupProperties();
  }
}
