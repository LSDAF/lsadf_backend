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
package com.lsadf.core.infra.kubernetes.config;

import com.lsadf.core.infra.kubernetes.service.KubernetesPodSelector;
import com.lsadf.core.infra.kubernetes.service.LocalPodSelector;
import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.util.Config;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class KubernetesConfiguration {

  @Bean
  @ConditionalOnProperty(prefix = "kubernetes", havingValue = "true", value = "enabled")
  public ApiClient kubernetesApiClient() throws Exception {
    log.info("Initializing Kubernetes API client with in-cluster configuration");
    ApiClient client = Config.defaultClient();
    io.kubernetes.client.openapi.Configuration.setDefaultApiClient(client);
    return client;
  }

  @Bean
  @ConditionalOnProperty(prefix = "kubernetes", havingValue = "true", value = "enabled")
  public CoreV1Api coreV1Api(ApiClient apiClient) {
    log.info("Creating CoreV1Api bean");
    return new CoreV1Api(apiClient);
  }

  @ConditionalOnProperty(prefix = "kubernetes", value = "enabled", havingValue = "true")
  @Bean
  public KubernetesPodSelector kubernetesHostnameResolver(
      CoreV1Api coreV1Api, KubernetesProperties kubernetesProperties) {
    return new KubernetesPodSelector(coreV1Api, kubernetesProperties);
  }

  @ConditionalOnProperty(
      prefix = "kubernetes",
      value = "enabled",
      havingValue = "false",
      matchIfMissing = true)
  @Bean
  public LocalPodSelector localHostnameResolutionService() {
    return new LocalPodSelector();
  }
}
