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

import com.lsadf.core.infra.persistence.mappers.game.CharacteristicsEntityModelMapper;
import com.lsadf.core.infra.persistence.mappers.game.CurrencyEntityModelMapper;
import com.lsadf.core.infra.persistence.mappers.game.GameSaveEntityModelMapper;
import com.lsadf.core.infra.persistence.mappers.game.StageEntityModelMapper;
import com.lsadf.core.infra.persistence.mappers.game.inventory.InventoryEntityModelMapper;
import com.lsadf.core.infra.persistence.mappers.game.inventory.ItemEntityModelMapper;
import com.lsadf.core.infra.web.config.keycloak.UserRepresentationModelMapper;
import com.lsadf.core.infra.web.config.keycloak.UserToUserRepresentationMapper;
import com.lsadf.core.infra.web.requests.game.characteristics.CharacteristicsRequestModelMapper;
import com.lsadf.core.infra.web.requests.game.currency.CurrencyRequestModelMapper;
import com.lsadf.core.infra.web.requests.game.stage.StageRequestModelMapper;
import com.lsadf.core.infra.web.requests.user.creation.UserCreationRequestModelMapper;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;

@TestConfiguration
public class UnitMapperMockConfiguration {
  // entity mapper
  @MockBean public CharacteristicsEntityModelMapper characteristicsEntityModelMapper;
  @MockBean public CurrencyEntityModelMapper currencyEntityModelMapper;
  @MockBean public GameSaveEntityModelMapper gameSaveEntityModelMapper;
  @MockBean public InventoryEntityModelMapper inventoryEntityModelMapper;
  @MockBean public ItemEntityModelMapper itemEntityModelMapper;
  @MockBean public StageEntityModelMapper stageEntityModelMapper;

  // request mapper
  @MockBean public UserCreationRequestModelMapper userCreationRequestModelMapper;
  @MockBean public StageRequestModelMapper stageRequestModelMapper;
  @MockBean public CharacteristicsRequestModelMapper characteristicsRequestModelMapper;
  @MockBean public CurrencyRequestModelMapper currencyRequestModelMapper;

  // keycloak mapper
  @MockBean public UserToUserRepresentationMapper userToUserRepresentationMapper;
  @MockBean public UserRepresentationModelMapper userRepresentationModelMapper;
}
