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
package com.lsadf.core.infra.web.config.security;

import com.lsadf.core.domain.user.UserRole;
import com.lsadf.core.infra.logging.interceptor.RequestLoggerInterceptor;
import com.lsadf.core.infra.logging.properties.HttpLogProperties;
import com.lsadf.core.infra.web.config.keycloak.KeycloakJwtAuthenticationConverter;
import java.util.Collection;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.convert.converter.Converter;
import org.springframework.messaging.Message;
import org.springframework.security.authorization.AuthorityAuthorizationManager;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.socket.EnableWebSocketSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.messaging.access.intercept.MessageMatcherDelegatingAuthorizationManager;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@Import({OAuth2Properties.class})
@EnableWebSecurity
@RequiredArgsConstructor
@EnableWebSocketSecurity
public class SecurityConfiguration implements WebMvcConfigurer {

  private RequestLoggerInterceptor requestLoggerInterceptor;

  private HttpLogProperties httpLogProperties;

  @Autowired
  public SecurityConfiguration(
      RequestLoggerInterceptor requestLoggerInterceptor, HttpLogProperties httpLogProperties) {
    this.requestLoggerInterceptor = requestLoggerInterceptor;
    this.httpLogProperties = httpLogProperties;
  }

  protected static final String[] WHITELIST_URLS = {
    "/api-docs/**",
    "/swagger-ui/**",
    "/swagger-ui.html",
    "/api/v1/auth/login",
    "/api/v1/auth/refresh",
    "/api/oauth2/callback",
    "/error",
    "/actuator",
    "/actuator/**",
    "/ws/game"
  };

  public static final String ADMIN_URLS = "/api/v1/admin/**";

  @Bean
  AuthorizationManager<Message<?>> messageAuthorizationManager(
      MessageMatcherDelegatingAuthorizationManager.Builder messages) {
    return AuthorityAuthorizationManager.hasAuthority(UserRole.USER.getRole());
  }

  @Bean
  public SecurityFilterChain filterChain(
      HttpSecurity security, JwtAuthenticationConverter customJwtAuthenticationProvider) {
    security
        .sessionManagement(
            configurer -> configurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .csrf(AbstractHttpConfigurer::disable)
        .httpBasic(AbstractHttpConfigurer::disable)
        .formLogin(AbstractHttpConfigurer::disable)
        .authorizeHttpRequests(
            configurer ->
                configurer
                    .requestMatchers(WHITELIST_URLS)
                    .permitAll()
                    .requestMatchers(ADMIN_URLS)
                    .hasAuthority(UserRole.ADMIN.getRole())
                    .anyRequest()
                    .authenticated())
        .oauth2ResourceServer(
            oauth2 ->
                oauth2.jwt(jwt -> jwt.jwtAuthenticationConverter(customJwtAuthenticationProvider)));

    return security.build();
  }

  @Bean
  public JwtAuthenticationConverter jwtAuthenticationConverter(
      Converter<Jwt, Collection<GrantedAuthority>> jwtGrantedAuthoritiesConverter) {
    JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
    jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);
    return jwtAuthenticationConverter;
  }

  @Bean
  public Converter<Jwt, Collection<GrantedAuthority>> jwtGrantedAuthoritiesConverter() {
    return new KeycloakJwtAuthenticationConverter();
  }

  @Bean
  public RestAuthenticationEntryPoint restAuthenticationEntryPoint() {
    return new RestAuthenticationEntryPoint();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    if (httpLogProperties.isEnabled()) {
      registry.addInterceptor(requestLoggerInterceptor);
    }
  }
}
