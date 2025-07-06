package com.lsadf.engine.config;

import com.lsadf.core.infra.worker.TemporalConfiguration;
import com.lsadf.core.infra.worker.TemporalProperties;
import com.lsadf.workflow.activity.game.characteristics.CharacteristicsActivity;
import com.lsadf.workflow.activity.game.currency.CurrencyActivity;
import com.lsadf.workflow.activity.game.game_save.GameSaveActivity;
import com.lsadf.workflow.activity.game.inventory.InventoryActivity;
import com.lsadf.workflow.activity.game.stage.StageActivity;
import com.lsadf.workflow.game.GameSessionWorkflow;
import com.lsadf.workflow.game.GameSessionWorkflowConfiguration;
import io.temporal.client.WorkflowClient;
import io.temporal.worker.Worker;
import io.temporal.worker.WorkerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({GameSessionWorkflowConfiguration.class, TemporalConfiguration.class})
public class TemporalWorkerConfiguration {
  @Bean
  public WorkerFactory workerFactory(WorkflowClient workflowClient) {
    return WorkerFactory.newInstance(workflowClient);
  }

  @Bean
  public Worker gameSaveWorker(
      WorkerFactory workerFactory,
      TemporalProperties temporalProperties,
      ApplicationContext applicationContext) {

    Worker worker = workerFactory.newWorker(temporalProperties.getGameSessionTaskQueue());

    // Register workflow implementation
    worker.registerWorkflowImplementationTypes(GameSessionWorkflow.class);

    // Register activity implementations

    StageActivity stageActivity = applicationContext.getBean(StageActivity.class);
    CharacteristicsActivity characteristicsActivity =
        applicationContext.getBean(CharacteristicsActivity.class);
    CurrencyActivity currencyActivity = applicationContext.getBean(CurrencyActivity.class);
    InventoryActivity inventoryActivity = applicationContext.getBean(InventoryActivity.class);
    GameSaveActivity gameSaveActivity = applicationContext.getBean(GameSaveActivity.class);

    worker.registerActivitiesImplementations(
        stageActivity,
        characteristicsActivity,
        currencyActivity,
        inventoryActivity,
        gameSaveActivity);

    return worker;
  }
}
