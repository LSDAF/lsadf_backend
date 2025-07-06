package com.lsadf.workflow.activity.game.characteristics;

import com.lsadf.core.domain.game.characteristics.Characteristics;
import io.temporal.activity.ActivityInterface;
import io.temporal.activity.ActivityMethod;

@ActivityInterface
public interface CharacteristicsActivity {
  @ActivityMethod
  Characteristics getCharacteristics(String gameSaveId);

  @ActivityMethod
  void updateCharacteristics(String gameSaveId, Characteristics characteristics);
}
