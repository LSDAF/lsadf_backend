package com.lsadf.workflow.activity.game.stage;

import com.lsadf.core.domain.game.stage.Stage;
import io.temporal.activity.ActivityInterface;
import io.temporal.activity.ActivityMethod;

/**
 * The StageActivity interface defines the methods to manage and interact with stage information
 * associated with a game save. It serves as an activity interface in the context of a workflow or
 * process automation framework.
 */
@ActivityInterface
public interface StageActivity {

  /**
   * Retrieves the stage information for the specified game save ID.
   *
   * @param gameSaveId the identifier of the game save for which the stage information is retrieved
   * @return the stage information, including the current and maximum stages, associated with the
   *     specified game save ID
   */
  @ActivityMethod
  Stage getStage(String gameSaveId);

  /**
   * Updates the stage information for the specified game save ID.
   *
   * @param gameSaveId the identifier of the game save for which the stage information is updated
   * @param stage the stage information, including the current and maximum stages, to be associated
   *     with the specified game save ID
   */
  @ActivityMethod
  void updateStage(String gameSaveId, Stage stage);
}
