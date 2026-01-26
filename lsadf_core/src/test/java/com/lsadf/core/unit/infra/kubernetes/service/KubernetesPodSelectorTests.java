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
package com.lsadf.core.unit.infra.kubernetes.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import com.lsadf.core.infra.kubernetes.config.KubernetesProperties;
import com.lsadf.core.infra.kubernetes.service.KubernetesPodSelector;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.models.*;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Unit tests for KubernetesPodSelector.
 *
 * <p>Note: Some tests that require mocking System.getenv() are commented out. These would be better
 * suited for integration tests or require refactoring the class to inject the environment variable
 * provider.
 *
 * <p>The tests focus on: - Round-robin pod selection logic - Kubernetes API interaction - Pod
 * readiness filtering - Error handling when API calls fail
 */
@ExtendWith(MockitoExtension.class)
class KubernetesPodSelectorTests {

  private static final String TEST_APP = "lsadf-api-123abc";
  private static final String TEST_NAMESPACE = "lsadf";

  @Mock private CoreV1Api coreV1Api;

  @Mock private CoreV1Api.APIlistNamespacedPodRequest podListRequest;

  @Mock private KubernetesProperties kubernetesProperties;

  private KubernetesPodSelector kubernetesPodSelector;

  @BeforeEach
  void setUp() {
    when(kubernetesProperties.getNamespace()).thenReturn(TEST_NAMESPACE);
    when(kubernetesProperties.getPodName()).thenReturn(TEST_APP);
    kubernetesPodSelector = new KubernetesPodSelector(coreV1Api, kubernetesProperties);
  }

  @Test
  void test_selectPodName_returnsRoundRobinPod_when_multipleReadyPodsExist() throws Exception {
    // Given - Assuming POD_NAME environment variable is set to "test-app-0"
    List<String> podNames = List.of("test-app-0", "test-app-1", "test-app-2");
    V1PodList podList = createPodList(podNames);
    mockKubernetesApiCall(podList);

    // When - Call selectPodName multiple times to verify round-robin behavior
    String firstPod = kubernetesPodSelector.selectPodName();
    String secondPod = kubernetesPodSelector.selectPodName();
    String thirdPod = kubernetesPodSelector.selectPodName();
    String fourthPod = kubernetesPodSelector.selectPodName();

    // Then - Should rotate through pods in round-robin fashion
    assertThat(firstPod).isEqualTo("test-app-0");
    assertThat(secondPod).isEqualTo("test-app-1");
    assertThat(thirdPod).isEqualTo("test-app-2");
    assertThat(fourthPod).isEqualTo("test-app-0"); // back to first
  }

  @Test
  void test_selectPodName_returnsLocalhost_when_kubernetesApiThrowsException() throws Exception {
    // Given - Assuming POD_NAME environment variable is set
    when(coreV1Api.listNamespacedPod(TEST_NAMESPACE)).thenReturn(podListRequest);
    when(podListRequest.labelSelector(anyString())).thenReturn(podListRequest);
    when(podListRequest.execute()).thenThrow(new ApiException("Connection error"));

    // When
    String selectedPod = kubernetesPodSelector.selectPodName();

    // Then - Should fallback to localhost on error
    assertThat(selectedPod).isEqualTo("localhost");
  }

  @Test
  void test_selectPodName_filtersOutNonReadyPods() throws Exception {
    // Given - Assuming POD_NAME environment variable is set to "test-app-0"
    V1PodList podList = new V1PodList();
    List<V1Pod> pods = new ArrayList<>();

    // Ready pod
    pods.add(createPod("test-app-0", true));
    // Non-ready pod
    pods.add(createPod("test-app-1", false));
    // Ready pod
    pods.add(createPod("test-app-2", true));
    // Pod with null status
    V1Pod podWithNullStatus = new V1Pod();
    podWithNullStatus.setMetadata(new V1ObjectMeta().name("test-app-3"));
    podWithNullStatus.setStatus(null);
    pods.add(podWithNullStatus);

    podList.setItems(pods);
    mockKubernetesApiCall(podList);

    // When
    String firstPod = kubernetesPodSelector.selectPodName();
    String secondPod = kubernetesPodSelector.selectPodName();
    String thirdPod = kubernetesPodSelector.selectPodName();

    // Then - Should only select from ready pods (test-app-0 and test-app-2)
    assertThat(firstPod).isEqualTo("test-app-0");
    assertThat(secondPod).isEqualTo("test-app-2");
    assertThat(thirdPod).isEqualTo("test-app-0"); // back to first ready pod
  }

  @Test
  void test_selectPodName_returnsLocalhost_when_podStatusConditionsIsNull() throws Exception {
    // Given - Assuming POD_NAME environment variable is set
    V1PodList podList = new V1PodList();
    List<V1Pod> pods = new ArrayList<>();

    V1Pod podWithNullConditions = new V1Pod();
    podWithNullConditions.setMetadata(new V1ObjectMeta().name("test-app-0"));
    V1PodStatus status = new V1PodStatus();
    status.setConditions(null);
    podWithNullConditions.setStatus(status);
    pods.add(podWithNullConditions);

    podList.setItems(pods);
    mockKubernetesApiCall(podList);

    // When
    String selectedPod = kubernetesPodSelector.selectPodName();

    // Then - Should fallback to localhost when no ready pods available
    assertThat(selectedPod).isEqualTo("localhost");
  }

  @Test
  void test_selectPodName_handlesStatefulSetNaming() throws Exception {
    // Given - Pod names following StatefulSet pattern: <statefulset-name>-<ordinal>
    List<String> podNames = List.of("myapp-0", "myapp-1", "myapp-2");
    V1PodList podList = createPodList(podNames);
    mockKubernetesApiCall(podList);

    // When
    String firstPod = kubernetesPodSelector.selectPodName();
    String secondPod = kubernetesPodSelector.selectPodName();

    // Then - Should select pods in round-robin fashion
    assertThat(firstPod).isIn(podNames);
    assertThat(secondPod).isIn(podNames);
    assertThat(firstPod).isNotEqualTo(secondPod);
  }

  @Test
  void test_selectPodName_handlesDeploymentNaming() throws Exception {
    // Given - Pod names following Deployment pattern: <deployment>-<replica-hash>-<pod-hash>
    List<String> podNames =
        List.of(
            "myapp-deployment-abc123-xyz789",
            "myapp-deployment-abc123-def456",
            "myapp-deployment-abc123-ghi123");
    V1PodList podList = createPodList(podNames);
    mockKubernetesApiCall(podList);

    // When
    String firstPod = kubernetesPodSelector.selectPodName();
    String secondPod = kubernetesPodSelector.selectPodName();
    String thirdPod = kubernetesPodSelector.selectPodName();

    // Then - Should select all pods in sequence
    assertThat(firstPod).isEqualTo("myapp-deployment-abc123-xyz789");
    assertThat(secondPod).isEqualTo("myapp-deployment-abc123-def456");
    assertThat(thirdPod).isEqualTo("myapp-deployment-abc123-ghi123");
  }

  @Test
  void test_selectPodName_handlesEmptyPodConditions() throws Exception {
    // Given - Pod with empty conditions list
    V1PodList podList = new V1PodList();
    List<V1Pod> pods = new ArrayList<>();

    V1Pod podWithEmptyConditions = new V1Pod();
    podWithEmptyConditions.setMetadata(new V1ObjectMeta().name("test-app-0"));
    V1PodStatus status = new V1PodStatus();
    status.setConditions(new ArrayList<>()); // Empty list
    podWithEmptyConditions.setStatus(status);
    pods.add(podWithEmptyConditions);

    podList.setItems(pods);
    mockKubernetesApiCall(podList);

    // When
    String selectedPod = kubernetesPodSelector.selectPodName();

    // Then - Should fallback to localhost when no ready condition found
    assertThat(selectedPod).isEqualTo("localhost");
  }

  @Test
  void test_selectPodName_handlesConditionWithFalseStatus() throws Exception {
    // Given - Pod with Ready=False condition
    V1PodList podList = new V1PodList();
    List<V1Pod> pods = new ArrayList<>();
    pods.add(createPod("test-app-0", false));
    podList.setItems(pods);
    mockKubernetesApiCall(podList);

    // When
    String selectedPod = kubernetesPodSelector.selectPodName();

    // Then - Should fallback to localhost when pod is not ready
    assertThat(selectedPod).isEqualTo("localhost");
  }

  @Test
  void test_selectPodName_verifyKubernetesApiInteraction() throws Exception {
    // Given
    V1PodList podList = createPodList(List.of("test-app-0"));
    mockKubernetesApiCall(podList);

    // When
    kubernetesPodSelector.selectPodName();

    // Then - Verify correct API calls were made
    verify(coreV1Api, times(1)).listNamespacedPod(TEST_NAMESPACE);
    verify(podListRequest, times(1)).labelSelector(anyString());
    verify(podListRequest, times(1)).execute();
  }

  // Helper methods

  private V1PodList createPodList(List<String> podNames) {
    V1PodList podList = new V1PodList();
    List<V1Pod> pods = new ArrayList<>();

    for (String podName : podNames) {
      pods.add(createPod(podName, true));
    }

    podList.setItems(pods);
    return podList;
  }

  private V1Pod createPod(String name, boolean ready) {
    V1Pod pod = new V1Pod();
    pod.setMetadata(new V1ObjectMeta().name(name));

    V1PodStatus status = new V1PodStatus();
    List<V1PodCondition> conditions = new ArrayList<>();

    V1PodCondition readyCondition = new V1PodCondition();
    readyCondition.setType("Ready");
    readyCondition.setStatus(ready ? "True" : "False");
    conditions.add(readyCondition);

    status.setConditions(conditions);
    pod.setStatus(status);

    return pod;
  }

  private void mockKubernetesApiCall(V1PodList podList) throws Exception {
    when(coreV1Api.listNamespacedPod(TEST_NAMESPACE)).thenReturn(podListRequest);
    when(podListRequest.labelSelector(anyString())).thenReturn(podListRequest);
    when(podListRequest.execute()).thenReturn(podList);
  }
}
