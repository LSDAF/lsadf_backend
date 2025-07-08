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

import com.lsadf.core.infra.worker.TemporalProperties;
import com.lsadf.workflow.game.GameSessionWorkflow;
import com.lsadf.workflow.utils.GameSessionUtils;
import io.temporal.api.enums.v1.WorkflowExecutionStatus;
import io.temporal.api.enums.v1.WorkflowIdReusePolicy;
import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowNotFoundException;
import io.temporal.client.WorkflowOptions;
import io.temporal.client.WorkflowStub;
import lombok.extern.slf4j.Slf4j;

/**
 * The GameSessionWorkflowServiceImpl class is an implementation of the GameSessionWorkflowService
 * interface, responsible for retrieving or creating game session workflows identified by a unique
 * session ID.
 */
@Slf4j
public class GameSessionWorkflowServiceImpl implements GameSessionWorkflowService {

  private final WorkflowClient workflowClient;
  private final String gameSessionTaskQueue;

  public GameSessionWorkflowServiceImpl(
      WorkflowClient workflowClient, TemporalProperties temporalProperties) {
    this.workflowClient = workflowClient;
    this.gameSessionTaskQueue = temporalProperties.getGameSessionTaskQueue();
  }

  /** {@inheritDoc} */
  @Override
  public GameSessionWorkflow getOrCreateGameSessionWorkflow(String userId, String gameSaveId) {
    String sessionId = GameSessionUtils.buildSessionId(userId, gameSaveId);
    try {
      // Try to get existing workflow
      GameSessionWorkflow existingWorkflow =
          workflowClient.newWorkflowStub(GameSessionWorkflow.class, sessionId);

      // Check if exists and running
      WorkflowStub workflowStub = WorkflowStub.fromTyped(existingWorkflow);
      WorkflowExecutionStatus status =
          workflowStub.getResult(WorkflowExecutionStatus.class, WorkflowExecutionStatus.class);

      if (status == WorkflowExecutionStatus.WORKFLOW_EXECUTION_STATUS_RUNNING) {
        log.debug("Found existing & running workflow for session ID: {}", sessionId);
        return existingWorkflow;
      }

    } catch (WorkflowNotFoundException e) {
      log.warn(
          "Failed to get existing workflow for session ID {}: workflow not found. Recreating one",
          sessionId);
    } catch (Exception e) {
      log.error(
          "Failed to get existing workflow for session ID {}: Error. Recreating one", sessionId, e);
    }
    WorkflowOptions options =
        WorkflowOptions.newBuilder()
            .setWorkflowId(sessionId)
            .setTaskQueue(gameSessionTaskQueue)
            .setWorkflowIdReusePolicy(
                WorkflowIdReusePolicy.WORKFLOW_ID_REUSE_POLICY_REJECT_DUPLICATE)
            .build();

    try {
      GameSessionWorkflow newWorkflow =
          workflowClient.newWorkflowStub(GameSessionWorkflow.class, options);

      WorkflowClient.start(newWorkflow::startSession, sessionId);

      return newWorkflow;
    } catch (Exception e) {
      log.error("Failed to create new workflow for session ID {}: Error", sessionId, e);
      throw e;
    }
  }
}
