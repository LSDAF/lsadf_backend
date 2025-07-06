package com.lsadf.core.infra.worker;

import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowClientOptions;
import io.temporal.serviceclient.WorkflowServiceStubs;
import io.temporal.serviceclient.WorkflowServiceStubsOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TemporalConfiguration {

  @Bean
  public WorkflowServiceStubsOptions workflowServiceStubsOptions(
      TemporalProperties temporalProperties) {
    return WorkflowServiceStubsOptions.newBuilder()
        .setTarget(temporalProperties.getServerAddress())
        .build();
  }

  @Bean
  public WorkflowServiceStubs workflowServiceStubs(WorkflowServiceStubsOptions options) {
    return WorkflowServiceStubs.newServiceStubs(options);
  }

  @Bean
  public WorkflowClientOptions workflowClientOptions(TemporalProperties temporalProperties) {
    return WorkflowClientOptions.newBuilder()
        .setNamespace(temporalProperties.getNamespace())
        .build();
  }

  @Bean
  public WorkflowClient workflowClient(
      WorkflowServiceStubs workflowServiceStubs, WorkflowClientOptions workflowClientOptions) {
    return WorkflowClient.newInstance(workflowServiceStubs, workflowClientOptions);
  }
}
