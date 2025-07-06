package com.lsadf.workflow.activity.game.game_save;

import com.lsadf.core.infra.web.request.game.game_save.update.GameSaveUpdateRequest;
import io.temporal.activity.ActivityInterface;
import io.temporal.activity.ActivityMethod;

@ActivityInterface
public interface GameSaveActivity {
  @ActivityMethod
  void updateNickname(String gameSaveId, GameSaveUpdateRequest gameSaveUpdateRequest);
}
