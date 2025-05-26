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
package com.lsadf.application.configurations;

import com.lsadf.core.configurations.*;
import com.lsadf.core.infra.cache.configuration.NoRedisCacheConfiguration;
import com.lsadf.core.infra.cache.configuration.RedisCacheConfiguration;
import com.lsadf.core.infra.cache.configuration.RedisEmbeddedCacheConfiguration;
import com.lsadf.core.infra.listeners.ApplicationListenerConfiguration;
import com.lsadf.core.infra.logging.LoggingConfiguration;
import com.lsadf.core.infra.web.auth.keycloak.KeycloakConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Global Configuration class for the LSADF backend. It imports all other configurations to be used
 * in the application.
 */
@Configuration
@EnableScheduling
@EnableTransactionManagement
@EnableFeignClients(basePackages = "com.lsadf.core.http_clients")
@Import({
  DataSourceConfiguration.class,
  PropertiesConfiguration.class,
  SwaggerConfiguration.class,
  ServiceConfiguration.class,
  CorsConfiguration.class,
  SecurityConfiguration.class,
  LoggingConfiguration.class,
  RedisCacheConfiguration.class,
  ApplicationListenerConfiguration.class,
  RedisEmbeddedCacheConfiguration.class,
  RedisCacheConfiguration.class,
  NoRedisCacheConfiguration.class,
  ClockConfiguration.class,
  KeycloakConfiguration.class,
  JpaConfiguration.class,
  LsadfSecurityConfiguration.class
})
public class LsadfConfiguration {}
