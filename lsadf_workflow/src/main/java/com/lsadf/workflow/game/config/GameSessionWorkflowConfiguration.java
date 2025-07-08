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

package com.lsadf.workflow.game.config;

import com.lsadf.core.application.game.characteristics.CharacteristicsConfiguration;
import com.lsadf.core.application.game.characteristics.CharacteristicsService;
import com.lsadf.core.application.game.currency.CurrencyConfiguration;
import com.lsadf.core.application.game.currency.CurrencyService;
import com.lsadf.core.application.game.game_save.GameSaveConfiguration;
import com.lsadf.core.application.game.game_save.GameSaveService;
import com.lsadf.core.application.game.inventory.InventoryConfiguration;
import com.lsadf.core.application.game.inventory.InventoryService;
import com.lsadf.core.application.game.stage.StageConfiguration;
import com.lsadf.core.application.game.stage.StageService;
import com.lsadf.workflow.activity.game.characteristics.CharacteristicsActivity;
import com.lsadf.workflow.activity.game.characteristics.CharacteristicsActivityImpl;
import com.lsadf.workflow.activity.game.currency.CurrencyActivity;
import com.lsadf.workflow.activity.game.currency.CurrencyActivityImpl;
import com.lsadf.workflow.activity.game.game_save.GameSaveActivity;
import com.lsadf.workflow.activity.game.game_save.GameSaveActivityImpl;
import com.lsadf.workflow.activity.game.inventory.InventoryActivity;
import com.lsadf.workflow.activity.game.inventory.InventoryActivityImpl;
import com.lsadf.workflow.activity.game.stage.StageActivity;
import com.lsadf.workflow.activity.game.stage.StageActivityImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({
  GameSaveConfiguration.class,
  CurrencyConfiguration.class,
  StageConfiguration.class,
  InventoryConfiguration.class,
  CharacteristicsConfiguration.class
})
public class GameSessionWorkflowConfiguration {
  @Bean
  public StageActivity stageActivity(StageService stageService) {
    return new StageActivityImpl(stageService);
  }

  @Bean
  public CharacteristicsActivity characteristicsActivity(
      CharacteristicsService characteristicsService) {
    return new CharacteristicsActivityImpl(characteristicsService);
  }

  @Bean
  public CurrencyActivity currencyActivity(CurrencyService currencyService) {
    return new CurrencyActivityImpl(currencyService);
  }

  @Bean
  public InventoryActivity inventoryActivity(InventoryService inventoryService) {
    return new InventoryActivityImpl(inventoryService);
  }

  @Bean
  public GameSaveActivity gameSaveActivity(GameSaveService gameSaveService) {
    return new GameSaveActivityImpl(gameSaveService);
  }
}
