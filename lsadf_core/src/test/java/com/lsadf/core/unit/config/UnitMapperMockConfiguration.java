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

package com.lsadf.core.unit.config;

import com.lsadf.core.infra.persistence.game.characteristics.CharacteristicsEntityMapper;
import com.lsadf.core.infra.persistence.game.currency.CurrencyEntityMapper;
import com.lsadf.core.infra.persistence.game.inventory.items.ItemEntityMapper;
import com.lsadf.core.infra.persistence.mappers.game.GameSaveEntityMapper;
import com.lsadf.core.infra.persistence.mappers.game.StageEntityMapper;
import com.lsadf.core.infra.persistence.mappers.game.inventory.InventoryEntityMapper;
import com.lsadf.core.infra.web.config.keycloak.mappers.UserRepresentationMapper;
import com.lsadf.core.infra.web.config.keycloak.mappers.UserToUserRepresentationMapper;
import com.lsadf.core.infra.web.requests.game.characteristics.CharacteristicsRequestMapper;
import com.lsadf.core.infra.web.requests.game.currency.CurrencyRequestMapper;
import com.lsadf.core.infra.web.requests.game.stage.StageRequestMapper;
import com.lsadf.core.infra.web.requests.user.creation.UserCreationRequestMapper;
import com.lsadf.core.infra.web.responses.game.characteristics.CharacteristicsResponseMapper;
import com.lsadf.core.infra.web.responses.game.currency.CurrencyResponseMapper;
import com.lsadf.core.infra.web.responses.game.game_save.GameSaveResponseMapper;
import com.lsadf.core.infra.web.responses.game.stage.StageResponseMapper;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;

@TestConfiguration
public class UnitMapperMockConfiguration {
  // entity mapper
  @MockBean public CharacteristicsEntityMapper characteristicsEntityModelMapper;
  @MockBean public CurrencyEntityMapper currencyEntityMapper;
  @MockBean public GameSaveEntityMapper gameSaveEntityMapper;
  @MockBean public InventoryEntityMapper inventoryEntityMapper;
  @MockBean public ItemEntityMapper itemEntityMapper;
  @MockBean public StageEntityMapper stageEntityMapper;

  // request mapper
  @MockBean public UserCreationRequestMapper userCreationRequestMapper;
  @MockBean public StageRequestMapper stageRequestMapper;
  @MockBean public CharacteristicsRequestMapper characteristicsRequestMapper;
  @MockBean public CurrencyRequestMapper currencyRequestMapper;

  // response mapper
  @MockBean public CharacteristicsResponseMapper characteristicsResponseMapper;
  @MockBean public StageResponseMapper stageResponseMapper;
  @MockBean public CurrencyResponseMapper currencyResponseMapper;
  @MockBean public GameSaveResponseMapper gameSaveResponseMapper;

  // keycloak mapper
  @MockBean public UserToUserRepresentationMapper userToUserRepresentationMapper;
  @MockBean public UserRepresentationMapper userRepresentationMapper;
}
