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
package com.lsadf.admin.application.config;

import com.lsadf.core.application.ApplicationServiceConfiguration;
import com.lsadf.core.infra.cache.config.NoValkeyCacheConfiguration;
import com.lsadf.core.infra.cache.config.ValkeyCacheConfiguration;
import com.lsadf.core.infra.cache.config.ValkeyEmbeddedCacheConfiguration;
import com.lsadf.core.infra.clock.ClockConfiguration;
import com.lsadf.core.infra.config.ApplicationListenerConfiguration;
import com.lsadf.core.infra.config.PropertiesConfiguration;
import com.lsadf.core.infra.logging.LoggingConfiguration;
import com.lsadf.core.infra.persistence.config.DataSourceConfiguration;
import com.lsadf.core.infra.persistence.config.JpaConfiguration;
import com.lsadf.core.infra.web.config.auth.keycloak.KeycloakConfiguration;
import com.lsadf.core.infra.web.config.security.CorsConfiguration;
import com.lsadf.core.infra.web.config.security.SecurityConfiguration;
import com.lsadf.core.infra.web.config.swagger.SwaggerConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Global Configuration class for the LSADF Admin backend. It imports all other configurations to be
 * used in the application.
 */
@Configuration
@EnableScheduling
@EnableTransactionManagement
@EnableFeignClients(basePackages = "com.lsadf.core.infra.web.http_clients")
@Import({
  DataSourceConfiguration.class,
  PropertiesConfiguration.class,
  SwaggerConfiguration.class,
  ApplicationServiceConfiguration.class,
  JpaConfiguration.class,
  CorsConfiguration.class,
  SecurityConfiguration.class,
  LoggingConfiguration.class,
  ValkeyCacheConfiguration.class,
  ApplicationListenerConfiguration.class,
  ValkeyEmbeddedCacheConfiguration.class,
  ValkeyCacheConfiguration.class,
  NoValkeyCacheConfiguration.class,
  ClockConfiguration.class,
  KeycloakConfiguration.class,
  LsadfAdminSecurityConfiguration.class
})
public class LsadfAdminConfiguration {}
