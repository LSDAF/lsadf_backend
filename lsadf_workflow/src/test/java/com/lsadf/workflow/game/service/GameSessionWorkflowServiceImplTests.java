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

package com.lsadf.workflow.game.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

import com.lsadf.core.infra.worker.TemporalProperties;
import com.lsadf.workflow.game.GameSessionWorkflow;
import io.temporal.api.enums.v1.WorkflowExecutionStatus;
import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowNotFoundException;
import io.temporal.client.WorkflowOptions;
import io.temporal.client.WorkflowStub;
import io.temporal.workflow.Functions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

class GameSessionWorkflowServiceImplTests {

  private static final String TEST_USER_ID = "b28f426e-1c95-a5d5-71072a917c3c";
  private static final String TEST_GAME_SAVE_ID = "37d1c911-0e22-4252-43edcbc8e6b3";
  private static final String TEST_SESSION_ID = TEST_USER_ID + "_" + TEST_GAME_SAVE_ID;

  private final TemporalProperties temporalProperties =
      new TemporalProperties("http://test.com", "lsadf", "gameSession-task-queue");
  private final WorkflowClient workflowClient = Mockito.mock(WorkflowClient.class);
  private GameSessionWorkflowServiceImpl gameSessionWorkflowService;
  private final GameSessionWorkflow gameSessionWorkflow = Mockito.mock(GameSessionWorkflow.class);
  private final WorkflowStub mockedWorkflowStub = Mockito.mock(WorkflowStub.class);

  @BeforeEach
  void setUp() {
    Mockito.reset(workflowClient);
    gameSessionWorkflowService =
        new GameSessionWorkflowServiceImpl(workflowClient, temporalProperties);
  }

  @Test
  void getOrCreateGameSessionWorkflow_should_return_existing_workflow_instance() {
    try (MockedStatic<WorkflowStub> workflowStubMockedStatic =
        Mockito.mockStatic(WorkflowStub.class)) {
      when(workflowClient.newWorkflowStub(eq(GameSessionWorkflow.class), anyString()))
          .thenReturn(gameSessionWorkflow);

      when(mockedWorkflowStub.getResult(
              WorkflowExecutionStatus.class, WorkflowExecutionStatus.class))
          .thenReturn(WorkflowExecutionStatus.WORKFLOW_EXECUTION_STATUS_RUNNING);

      workflowStubMockedStatic
          .when(() -> WorkflowStub.fromTyped(any()))
          .thenReturn(mockedWorkflowStub);
      var actualWorkflow =
          gameSessionWorkflowService.getOrCreateGameSessionWorkflow(
              TEST_USER_ID, TEST_GAME_SAVE_ID);
      workflowStubMockedStatic.verify(() -> WorkflowStub.fromTyped(any()));
      workflowStubMockedStatic.verifyNoMoreInteractions();

      assertThat(actualWorkflow).isEqualTo(gameSessionWorkflow);
    }
  }

  @Test
  void getOrCreateGameSessionWorkflow_should_create_new_workflow_instance_if_not_found() {
    try (MockedStatic<WorkflowClient> workflowClientMockedStatic =
        Mockito.mockStatic(WorkflowClient.class)) {
      try (MockedStatic<WorkflowStub> workflowStubMockedStatic =
          Mockito.mockStatic(WorkflowStub.class)) {

        when(workflowClient.newWorkflowStub(eq(GameSessionWorkflow.class), anyString()))
            .thenThrow(WorkflowNotFoundException.class);
        when(workflowClient.newWorkflowStub(
                eq(GameSessionWorkflow.class), any(WorkflowOptions.class)))
            .thenReturn(gameSessionWorkflow);
        var actualWorkflow =
            gameSessionWorkflowService.getOrCreateGameSessionWorkflow(
                TEST_USER_ID, TEST_GAME_SAVE_ID);
        workflowClientMockedStatic.verify(
            () -> WorkflowClient.start(any(Functions.Proc1.class), eq(TEST_SESSION_ID)));

        assertThat(actualWorkflow).isEqualTo(gameSessionWorkflow);
      }
    }
  }

  @Test
  void
      getOrCreateGameSessionWorkflow_should_throw_exception_if_workflow_not_found_and_not_created() {
    when(workflowClient.newWorkflowStub(eq(GameSessionWorkflow.class), anyString()))
        .thenThrow(WorkflowNotFoundException.class);
    when(workflowClient.newWorkflowStub(eq(GameSessionWorkflow.class), any(WorkflowOptions.class)))
        .thenThrow(RuntimeException.class);
    assertThatThrownBy(
            () ->
                gameSessionWorkflowService.getOrCreateGameSessionWorkflow(
                    TEST_USER_ID, TEST_GAME_SAVE_ID))
        .isInstanceOf(RuntimeException.class);
  }
}
