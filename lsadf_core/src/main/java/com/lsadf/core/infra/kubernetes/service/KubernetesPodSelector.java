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
package com.lsadf.core.infra.kubernetes.service;

import com.lsadf.core.exception.http.ServiceUnavailableException;
import com.lsadf.core.infra.kubernetes.config.KubernetesProperties;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.models.V1Pod;
import io.kubernetes.client.openapi.models.V1PodList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
public class KubernetesPodSelector implements PodSelector {

  private final CoreV1Api coreV1Api;
  private final KubernetesProperties kubernetesProperties;
  private final AtomicInteger roundRobinCounter = new AtomicInteger(0);

  @Override
  public String selectPodName() {
    try {
      String podName = kubernetesProperties.getPodName();
      if (podName == null || podName.isEmpty()) {
        log.error("Didn't manage to get pod name from environment variable POD_NAME");
        throw new ServiceUnavailableException("Pod name environment variable POD_NAME not set");
      }

      String labelSelector = buildLabelSelector(podName);
      V1PodList podList =
          coreV1Api
              .listNamespacedPod(kubernetesProperties.getNamespace())
              .labelSelector(labelSelector)
              .execute();
      List<String> readyPods =
          podList.getItems().stream()
              .filter(this::isPodReady)
              .map(pod -> pod.getMetadata().getName())
              .toList();

      log.debug("Ready pods: {}", readyPods);

      if (readyPods.isEmpty()) {
        log.error("No ready pods found with label selector: {}", labelSelector);
        throw new ServiceUnavailableException("No ready pods available");
      }

      int index = roundRobinCounter.getAndIncrement() % readyPods.size();
      String selectedPod = readyPods.get(index);

      log.info("Selected pod {} from {} available replicas", selectedPod, readyPods.size());

      return selectedPod;
    } catch (Exception e) {
      log.error("Error resolving hostname from Kubernetes API, falling back to localhost", e);
      return "localhost";
    }
  }

  private boolean isPodReady(V1Pod pod) {
    if (pod.getStatus() == null || pod.getStatus().getConditions() == null) {
      return false;
    }

    return pod.getStatus().getConditions().stream()
        .anyMatch(
            condition ->
                "Ready".equals(condition.getType()) && "True".equals(condition.getStatus()));
  }

  private String buildLabelSelector(String podName) {
    // Extract the StatefulSet/Deployment name from pod name
    // Assuming pod names follow pattern: <deployment-name>-<replica-hash>-<pod-hash>
    // or StatefulSet: <statefulset-name>-<ordinal>
    String[] parts = podName.split("-");
    if (parts.length >= 2) {
      // Use app label which is typically the deployment/statefulset name
      return "app=" + parts[0];
    }
    throw new IllegalArgumentException("Pod name is not parseable");
  }
}
