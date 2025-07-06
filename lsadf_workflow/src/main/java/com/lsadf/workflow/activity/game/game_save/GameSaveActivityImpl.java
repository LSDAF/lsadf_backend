package com.lsadf.workflow.activity.game.game_save;

import com.lsadf.core.application.game.game_save.GameSaveService;
import com.lsadf.core.infra.web.request.game.game_save.update.GameSaveUpdateRequest;

public class GameSaveActivityImpl implements GameSaveActivity {

  private final GameSaveService gameSaveService;

  public GameSaveActivityImpl(GameSaveService gameSaveService) {
    this.gameSaveService = gameSaveService;
  }

  @Override
  public void updateNickname(String gameSaveId, GameSaveUpdateRequest gameSaveUpdateRequest) {
    gameSaveService.updateGameSave(gameSaveId, gameSaveUpdateRequest);
  }
}
