package com.lsadf.workflow.activity.game.characteristics;

import com.lsadf.core.application.game.characteristics.CharacteristicsService;
import com.lsadf.core.domain.game.characteristics.Characteristics;

public class CharacteristicsActivityImpl implements CharacteristicsActivity {

  private final CharacteristicsService characteristicsService;

  public CharacteristicsActivityImpl(CharacteristicsService characteristicsService) {
    this.characteristicsService = characteristicsService;
  }

  @Override
  public Characteristics getCharacteristics(String gameSaveId) {
    return characteristicsService.getCharacteristics(gameSaveId);
  }

  @Override
  public void updateCharacteristics(String gameSaveId, Characteristics characteristics) {
    characteristicsService.saveCharacteristics(gameSaveId, characteristics, true);
  }
}
