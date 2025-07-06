package com.lsadf.workflow.activity.game.stage;

import com.lsadf.core.application.game.stage.StageService;
import com.lsadf.core.domain.game.stage.Stage;

/**
 * The StageActivityImpl class provides an implementation for the StageActivity interface,
 * specifically handling operations related to game stage information retrieval and update.
 *
 * <p>This implementation acts as a concrete activity class in a workflow or process automation
 * framework, where stage data is managed for a specific game save identifier.
 */
public class StageActivityImpl implements StageActivity {

  private final StageService stageService;

  public StageActivityImpl(StageService stageService) {
    this.stageService = stageService;
  }

  /** {@inheritDoc} */
  @Override
  public Stage getStage(String gameSaveId) {
    return stageService.getStage(gameSaveId);
  }

  /** {@inheritDoc} */
  @Override
  public void updateStage(String gameSaveId, Stage stage) {
    this.stageService.saveStage(gameSaveId, stage, true);
  }
}
