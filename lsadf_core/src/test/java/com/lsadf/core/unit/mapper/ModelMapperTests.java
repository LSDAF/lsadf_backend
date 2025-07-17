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
package com.lsadf.core.unit.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.lsadf.core.domain.game.GameSave;
import com.lsadf.core.domain.game.characteristics.Characteristics;
import com.lsadf.core.domain.game.currency.Currency;
import com.lsadf.core.domain.game.stage.Stage;
import com.lsadf.core.domain.user.User;
import com.lsadf.core.infra.persistence.game.characteristics.CharacteristicsEntity;
import com.lsadf.core.infra.persistence.game.characteristics.CharacteristicsEntityMapper;
import com.lsadf.core.infra.persistence.game.currency.CurrencyEntity;
import com.lsadf.core.infra.persistence.game.currency.CurrencyEntityMapper;
import com.lsadf.core.infra.persistence.game.game_save.GameSaveEntity;
import com.lsadf.core.infra.persistence.game.game_save.GameSaveEntityMapper;
import com.lsadf.core.infra.persistence.game.stage.StageEntity;
import com.lsadf.core.infra.persistence.game.stage.StageEntityMapper;
import com.lsadf.core.infra.web.config.keycloak.mapper.UserRepresentationMapper;
import com.lsadf.core.infra.web.request.game.characteristics.CharacteristicsRequest;
import com.lsadf.core.infra.web.request.game.characteristics.CharacteristicsRequestMapper;
import com.lsadf.core.infra.web.request.game.currency.CurrencyRequest;
import com.lsadf.core.infra.web.request.game.currency.CurrencyRequestMapper;
import com.lsadf.core.infra.web.request.game.stage.StageRequest;
import com.lsadf.core.infra.web.request.game.stage.StageRequestMapper;
import com.lsadf.core.infra.web.request.user.creation.SimpleUserCreationRequest;
import com.lsadf.core.infra.web.request.user.creation.UserCreationRequestMapper;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.keycloak.representations.idm.UserRepresentation;

class ModelMapperTests {
  private final String userEmail = "toto@toto.com";

  @Test
  void should_map_stage_request_to_stage() {
    // given
    StageRequest stageRequest = new StageRequest(25L, 500L);
    StageRequestMapper mapper = StageRequestMapper.INSTANCE;
    // when
    Stage stage = mapper.map(stageRequest);

    // then
    assertThat(stage.getCurrentStage()).isEqualTo(25L);
    assertThat(stage.getMaxStage()).isEqualTo(500L);
  }

  @Test
  void should_map_stage_entity_to_stage() {
    // given
    GameSaveEntity gameSaveEntity = GameSaveEntity.builder().build();
    StageEntityMapper stageEntityMapper = StageEntityMapper.INSTANCE;
    StageEntity stageEntity =
        StageEntity.builder()
            .maxStage(500L)
            .currentStage(25L)
            .id(UUID.randomUUID().toString())
            .gameSave(gameSaveEntity)
            .userEmail(userEmail)
            .build();

    // when
    Stage stage = stageEntityMapper.map(stageEntity);

    // then
    assertThat(stage.getCurrentStage()).isEqualTo(25L);
    assertThat(stage.getMaxStage()).isEqualTo(500L);
  }

  @Test
  void should_map_characteristics_entity_to_characteristics() {
    // given
    GameSaveEntity gameSaveEntity = GameSaveEntity.builder().build();
    CharacteristicsEntityMapper characteristicsEntityModelMapper =
        CharacteristicsEntityMapper.INSTANCE;
    CharacteristicsEntity characteristicsEntity =
        CharacteristicsEntity.builder()
            .gameSave(gameSaveEntity)
            .attack(100L)
            .critChance(200L)
            .critDamage(300L)
            .health(400L)
            .resistance(500L)
            .build();

    // when
    Characteristics characteristics = characteristicsEntityModelMapper.map(characteristicsEntity);

    // then
    assertThat(characteristics.getAttack()).isEqualTo(100L);
    assertThat(characteristics.getCritChance()).isEqualTo(200L);
    assertThat(characteristics.getCritDamage()).isEqualTo(300L);
    assertThat(characteristics.getHealth()).isEqualTo(400L);
    assertThat(characteristics.getResistance()).isEqualTo(500L);
  }

  @Test
  void should_map_characteristics_request_to_characteristics() {
    // given
    CharacteristicsRequestMapper mapper = CharacteristicsRequestMapper.INSTANCE;
    CharacteristicsRequest characteristicsRequest =
        new CharacteristicsRequest(100L, 200L, 300L, 400L, 500L);

    // when
    Characteristics characteristics = mapper.map(characteristicsRequest);

    // then
    assertThat(characteristics.getAttack()).isEqualTo(100L);
    assertThat(characteristics.getCritChance()).isEqualTo(200L);
    assertThat(characteristics.getCritDamage()).isEqualTo(300L);
    assertThat(characteristics.getHealth()).isEqualTo(400L);
    assertThat(characteristics.getResistance()).isEqualTo(500L);
  }

  @Test
  void should_map_currency_request_to_currency() {
    // given
    CurrencyRequest currencyRequest = new CurrencyRequest(100L, 200L, 300L, 400L);
    CurrencyRequestMapper mapper = CurrencyRequestMapper.INSTANCE;
    // when
    Currency currency = mapper.map(currencyRequest);

    // then
    assertThat(currency.getGold()).isEqualTo(100L);
    assertThat(currency.getDiamond()).isEqualTo(200L);
    assertThat(currency.getEmerald()).isEqualTo(300L);
    assertThat(currency.getAmethyst()).isEqualTo(400L);
  }

  @Test
  void should_map_currency_entity_to_currency() {
    // given
    GameSaveEntity gameSaveEntity = GameSaveEntity.builder().build();
    CurrencyEntityMapper mapper = CurrencyEntityMapper.INSTANCE;
    CurrencyEntity currencyEntity =
        CurrencyEntity.builder()
            .goldAmount(100L)
            .diamondAmount(200L)
            .emeraldAmount(300L)
            .amethystAmount(400L)
            .id(UUID.randomUUID().toString())
            .gameSave(gameSaveEntity)
            .userEmail(userEmail)
            .build();

    // when
    Currency currency = mapper.map(currencyEntity);

    // then
    assertThat(currency.getGold()).isEqualTo(100L);
    assertThat(currency.getDiamond()).isEqualTo(200L);
    assertThat(currency.getEmerald()).isEqualTo(300L);
    assertThat(currency.getAmethyst()).isEqualTo(400L);
  }

  @Test
  void should_map_game_save_entity_to_game_save() {
    // given
    GameSaveEntityMapper mapper = GameSaveEntityMapper.INSTANCE;
    String id = UUID.randomUUID().toString();
    GameSaveEntity gameSaveEntity =
        GameSaveEntity.builder()
            .id(id)
            .userEmail(userEmail)
            .nickname("toto")
            .createdAt(new Date())
            .updatedAt(new Date())
            .build();
    CharacteristicsEntity characteristicsEntity =
        CharacteristicsEntity.builder()
            .gameSave(gameSaveEntity)
            .attack(100L)
            .critChance(200L)
            .critDamage(300L)
            .health(400L)
            .resistance(500L)
            .build();
    CurrencyEntity currencyEntity =
        CurrencyEntity.builder()
            .goldAmount(100L)
            .diamondAmount(200L)
            .emeraldAmount(300L)
            .amethystAmount(400L)
            .id(id)
            .gameSave(gameSaveEntity)
            .userEmail(userEmail)
            .build();
    StageEntity stageEntity =
        StageEntity.builder()
            .maxStage(500L)
            .currentStage(25L)
            .id(id)
            .gameSave(gameSaveEntity)
            .userEmail(userEmail)
            .build();
    gameSaveEntity.setCharacteristicsEntity(characteristicsEntity);
    gameSaveEntity.setCurrencyEntity(currencyEntity);
    gameSaveEntity.setStageEntity(stageEntity);

    // when
    GameSave gameSave = mapper.map(gameSaveEntity);

    // then
    assertThat(gameSave.getId()).isEqualTo(gameSaveEntity.getId());
    assertThat(gameSave.getUserEmail()).isEqualTo(gameSaveEntity.getUserEmail());
    assertThat(gameSave.getNickname()).isEqualTo(gameSaveEntity.getNickname());
    assertThat(gameSave.getCharacteristics().getAttack())
        .isEqualTo(characteristicsEntity.getAttack());
    assertThat(gameSave.getCharacteristics().getCritChance())
        .isEqualTo(characteristicsEntity.getCritChance());
    assertThat(gameSave.getCharacteristics().getCritDamage())
        .isEqualTo(characteristicsEntity.getCritDamage());
    assertThat(gameSave.getCharacteristics().getHealth())
        .isEqualTo(characteristicsEntity.getHealth());
    assertThat(gameSave.getCharacteristics().getResistance())
        .isEqualTo(characteristicsEntity.getResistance());
    assertThat(gameSave.getCreatedAt()).isEqualTo(gameSaveEntity.getCreatedAt());
    assertThat(gameSave.getUpdatedAt()).isEqualTo(gameSaveEntity.getUpdatedAt());
    assertThat(gameSave.getCurrency().getGold()).isEqualTo(currencyEntity.getGoldAmount());
    assertThat(gameSave.getCurrency().getDiamond()).isEqualTo(currencyEntity.getDiamondAmount());
    assertThat(gameSave.getCurrency().getEmerald()).isEqualTo(currencyEntity.getEmeraldAmount());
    assertThat(gameSave.getCurrency().getAmethyst()).isEqualTo(currencyEntity.getAmethystAmount());
    assertThat(gameSave.getStage().getCurrentStage()).isEqualTo(stageEntity.getCurrentStage());
    assertThat(gameSave.getStage().getMaxStage()).isEqualTo(stageEntity.getMaxStage());
  }

  @Test
  void should_map_userRepresentationToUser() {
    // given
    UserRepresentation userRepresentation = new UserRepresentation();

    userRepresentation.setCreatedTimestamp(new Date().getTime());
    userRepresentation.setUsername(userEmail);
    userRepresentation.setId(UUID.randomUUID().toString());
    userRepresentation.setFirstName("toto");
    userRepresentation.setLastName("tata");
    userRepresentation.setEmailVerified(true);
    userRepresentation.setEnabled(false);
    userRepresentation.setRealmRoles(List.of("user", "admin"));

    UserRepresentationMapper mapper = UserRepresentationMapper.INSTANCE;

    // when
    User user = mapper.map(userRepresentation);

    // then
    assertThat(user.getUsername()).isEqualTo(userRepresentation.getUsername());
    assertThat(user.getFirstName()).isEqualTo(userRepresentation.getFirstName());
    assertThat(user.getLastName()).isEqualTo(userRepresentation.getLastName());
    assertThat(user.getCreatedTimestamp()).isNotNull();
    assertThat(user.getUserRoles()).containsExactlyInAnyOrder("user", "admin");
    assertThat(user.getEnabled()).isFalse();
    assertThat(user.getId()).isEqualTo(userRepresentation.getId());
    assertThat(user.getEmailVerified()).isTrue();
  }

  @Test
  void should_map_user_creation_request_to_user_representation() {
    // given
    UserCreationRequestMapper mapper = UserCreationRequestMapper.INSTANCE;
    SimpleUserCreationRequest simpleUserCreationRequest =
        SimpleUserCreationRequest.builder()
            .username(userEmail)
            .password("password")
            .lastName("tata")
            .firstName("toto")
            .userRoles(List.of("user", "admin"))
            .emailVerified(false)
            .enabled(false)
            .build();

    // when
    User user = mapper.map(simpleUserCreationRequest);

    // then
    assertThat(user.getUsername()).isEqualTo(simpleUserCreationRequest.getUsername());
    assertThat(user.getFirstName()).isEqualTo(simpleUserCreationRequest.getFirstName());
    assertThat(user.getLastName()).isEqualTo(simpleUserCreationRequest.getLastName());
    assertThat(user.getEmailVerified()).isEqualTo(simpleUserCreationRequest.getEmailVerified());
    assertThat(user.getEnabled()).isEqualTo(simpleUserCreationRequest.getEnabled());
  }
}
