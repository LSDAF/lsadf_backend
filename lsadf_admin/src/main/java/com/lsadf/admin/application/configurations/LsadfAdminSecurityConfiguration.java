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
package com.lsadf.admin.application.configurations;

import static com.lsadf.core.infra.web.config.security.SecurityConfiguration.ADMIN_URLS;
import static com.lsadf.core.infra.web.config.security.SecurityConfiguration.WHITELIST_URLS;

import com.lsadf.core.domain.user.UserRole;
import com.lsadf.core.infra.web.config.security.SecurityConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;

@Configuration
@Import(SecurityConfiguration.class)
public class LsadfAdminSecurityConfiguration {

  @Bean
  public Customizer<
          AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry>
      customAuthorizationManager() {
    return configurer ->
        configurer
            .requestMatchers(WHITELIST_URLS)
            .permitAll()
            .requestMatchers(ADMIN_URLS)
            .hasAuthority(UserRole.ADMIN.getRole())
            .anyRequest()
            .permitAll();
  }
}
