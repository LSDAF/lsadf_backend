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
package com.lsadf.core.infra.web.client;

import com.lsadf.core.exception.http.*;
import com.lsadf.core.infra.web.client.keycloak.KeycloakRestClient;
import java.io.IOException;
import java.time.Duration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

/**
 * Configuration for RestClient-based HTTP clients, replacing OpenFeign. Provides error handling,
 * logging, and timeout configuration.
 */
@Slf4j
@Configuration
public class CommonHttpClientConfiguration {

  @Value("${spring.cloud.httpclient.connect-timeout:5000}")
  private int connectTimeout;

  @Value("${spring.cloud.httpclient.read-timeout:5000}")
  private int readTimeout;

  @Value("${keycloak.url}")
  private String keycloakUrl;

  /** RestClient for Keycloak communication. */
  @Bean
  public RestClient restClient(RestClient.Builder restClientBuilder) {
    return restClientBuilder
        .baseUrl(keycloakUrl)
        .requestInterceptor(loggingInterceptor())
        .defaultStatusHandler(HttpStatusCode::is4xxClientError, this::handleClientError)
        .defaultStatusHandler(HttpStatusCode::is5xxServerError, this::handleServerError)
        .build();
  }

  @Bean
  public KeycloakRestClient keycloakRestClient(RestClient restClient) {
    return new KeycloakRestClient(restClient);
  }

  /** Base RestClient.Builder with common configuration. */
  @Bean
  public RestClient.Builder restClientBuilder() {
    SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
    requestFactory.setConnectTimeout(Duration.ofMillis(connectTimeout));
    requestFactory.setReadTimeout(Duration.ofMillis(readTimeout));

    // Use buffering factory to allow reading response body multiple times (for logging)
    BufferingClientHttpRequestFactory bufferingFactory =
        new BufferingClientHttpRequestFactory(requestFactory);

    return RestClient.builder().requestFactory(bufferingFactory);
  }

  /** Logging interceptor for HTTP requests/responses. */
  private ClientHttpRequestInterceptor loggingInterceptor() {
    return new ClientHttpRequestInterceptor() {
      @Override
      public ClientHttpResponse intercept(
          HttpRequest request, byte[] body, ClientHttpRequestExecution execution)
          throws IOException {
        logRequest(request, body);
        ClientHttpResponse response = execution.execute(request, body);
        logResponse(response);
        return response;
      }

      private void logRequest(HttpRequest request, byte[] body) {
        if (log.isDebugEnabled()) {
          log.debug("=== HTTP Request ===");
          log.debug("URI: {}", request.getURI());
          log.debug("Method: {}", request.getMethod());
          log.debug("Headers: {}", request.getHeaders());
          if (body.length > 0 && !request.getURI().getPath().contains("token")) {
            // Don't log token request bodies (contain credentials)
            log.debug("Body: {}", new String(body));
          }
        }
      }

      private void logResponse(ClientHttpResponse response) throws IOException {
        if (log.isDebugEnabled()) {
          log.debug("=== HTTP Response ===");
          log.debug("Status code: {}", response.getStatusCode());
          log.debug("Status text: {}", response.getStatusText());
          log.debug("Headers: {}", response.getHeaders());
        }
      }
    };
  }

  /** Handle 4xx client errors. */
  private void handleClientError(HttpRequest request, ClientHttpResponse response)
      throws IOException {
    int statusCode = response.getStatusCode().value();
    String statusText = response.getStatusText();
    String uri = request.getURI().toString();

    log.error("HTTP Client Error {} {} for request: {}", statusCode, statusText, uri);

    switch (statusCode) {
      case 400 -> throw new BadRequestException(statusText);
      case 401 -> throw new UnauthorizedException(statusText);
      case 403 -> throw new ForbiddenException(statusText);
      case 404 -> throw new NotFoundException(statusText);
      default -> throw new BadRequestException("Client error: " + statusCode + " " + statusText);
    }
  }

  /** Handle 5xx server errors. */
  private void handleServerError(HttpRequest request, ClientHttpResponse response)
      throws IOException {
    int statusCode = response.getStatusCode().value();
    String statusText = response.getStatusText();
    String uri = request.getURI().toString();

    log.error("HTTP Server Error {} {} for request: {}", statusCode, statusText, uri);
    throw new InternalServerErrorException(statusText);
  }
}
