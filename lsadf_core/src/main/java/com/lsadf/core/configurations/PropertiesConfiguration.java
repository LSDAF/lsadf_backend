package com.lsadf.core.configurations;

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
  @ConfigurationProperties(prefix = "mail")
  public EmailProperties mailProperties() {
    return new EmailProperties();
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
