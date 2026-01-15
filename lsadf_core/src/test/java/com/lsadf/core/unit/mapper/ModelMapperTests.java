/*
 * Copyright © 2024-2026 LSDAF
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

import com.lsadf.core.domain.game.save.characteristics.Characteristics;
import com.lsadf.core.domain.game.save.currency.Currency;
import com.lsadf.core.domain.game.save.metadata.GameMetadata;
import com.lsadf.core.domain.game.save.stage.Stage;
import com.lsadf.core.domain.user.User;
import com.lsadf.core.infra.persistence.impl.game.save.characteristics.CharacteristicsEntity;
import com.lsadf.core.infra.persistence.impl.game.save.characteristics.CharacteristicsEntityMapper;
import com.lsadf.core.infra.persistence.impl.game.save.currency.CurrencyEntity;
import com.lsadf.core.infra.persistence.impl.game.save.currency.CurrencyEntityMapper;
import com.lsadf.core.infra.persistence.impl.game.save.metadata.GameMetadataEntity;
import com.lsadf.core.infra.persistence.impl.game.save.metadata.GameMetadataEntityMapper;
import com.lsadf.core.infra.persistence.impl.game.save.stage.StageEntity;
import com.lsadf.core.infra.persistence.impl.game.save.stage.StageEntityMapper;
import com.lsadf.core.infra.web.config.keycloak.mapper.UserRepresentationMapper;
import com.lsadf.core.infra.web.dto.request.game.characteristics.CharacteristicsRequest;
import com.lsadf.core.infra.web.dto.request.game.characteristics.CharacteristicsRequestMapper;
import com.lsadf.core.infra.web.dto.request.game.currency.CurrencyRequest;
import com.lsadf.core.infra.web.dto.request.game.currency.CurrencyRequestMapper;
import com.lsadf.core.infra.web.dto.request.game.stage.StageRequest;
import com.lsadf.core.infra.web.dto.request.game.stage.StageRequestMapper;
import com.lsadf.core.infra.web.dto.request.user.creation.SimpleUserCreationRequest;
import com.lsadf.core.infra.web.dto.request.user.creation.UserCreationRequestMapper;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.keycloak.representations.idm.UserRepresentation;

class ModelMapperTests {
  private final String userEmail = "toto@toto.com";

  @Test
  void test_map_mapsCorrectly_when_stageRequestToStage() {
    // given
    StageRequest stageRequest = new StageRequest(25L, 500L, 10L);
    StageRequestMapper mapper = StageRequestMapper.INSTANCE;
    // when
    Stage stage = mapper.map(stageRequest);

    // then
    assertThat(stage.currentStage()).isEqualTo(25L);
    assertThat(stage.maxStage()).isEqualTo(500L);
    assertThat(stage.wave()).isEqualTo(10L);
  }

  @Test
  void test_map_mapsCorrectly_when_stageEntityToStage() {
    // given
    StageEntityMapper stageEntityMapper = StageEntityMapper.INSTANCE;
    StageEntity stageEntity =
        StageEntity.builder().maxStage(500L).currentStage(25L).id(UUID.randomUUID()).build();

    // when
    Stage stage = stageEntityMapper.map(stageEntity);

    // then
    assertThat(stage.currentStage()).isEqualTo(25L);
    assertThat(stage.maxStage()).isEqualTo(500L);
  }

  @Test
  void test_map_mapsCorrectly_when_characteristicsEntityToCharacteristics() {
    // given
    CharacteristicsEntityMapper characteristicsEntityModelMapper =
        CharacteristicsEntityMapper.INSTANCE;
    CharacteristicsEntity characteristicsEntity =
        CharacteristicsEntity.builder()
            .id(UUID.randomUUID())
            .attack(100L)
            .critChance(200L)
            .critDamage(300L)
            .health(400L)
            .resistance(500L)
            .build();

    // when
    Characteristics characteristics = characteristicsEntityModelMapper.map(characteristicsEntity);

    // then
    assertThat(characteristics.attack()).isEqualTo(100L);
    assertThat(characteristics.critChance()).isEqualTo(200L);
    assertThat(characteristics.critDamage()).isEqualTo(300L);
    assertThat(characteristics.health()).isEqualTo(400L);
    assertThat(characteristics.resistance()).isEqualTo(500L);
  }

  @Test
  void test_map_mapsCorrectly_when_characteristicsRequestToCharacteristics() {
    // given
    CharacteristicsRequestMapper mapper = CharacteristicsRequestMapper.INSTANCE;
    CharacteristicsRequest characteristicsRequest =
        new CharacteristicsRequest(100L, 200L, 300L, 400L, 500L);

    // when
    Characteristics characteristics = mapper.map(characteristicsRequest);

    // then
    assertThat(characteristics.attack()).isEqualTo(100L);
    assertThat(characteristics.critChance()).isEqualTo(200L);
    assertThat(characteristics.critDamage()).isEqualTo(300L);
    assertThat(characteristics.health()).isEqualTo(400L);
    assertThat(characteristics.resistance()).isEqualTo(500L);
  }

  @Test
  void test_map_mapsCorrectly_when_currencyRequestToCurrency() {
    // given
    CurrencyRequest currencyRequest = new CurrencyRequest(100L, 200L, 300L, 400L);
    CurrencyRequestMapper mapper = CurrencyRequestMapper.INSTANCE;
    // when
    Currency currency = mapper.map(currencyRequest);

    // then
    assertThat(currency.gold()).isEqualTo(100L);
    assertThat(currency.diamond()).isEqualTo(200L);
    assertThat(currency.emerald()).isEqualTo(300L);
    assertThat(currency.amethyst()).isEqualTo(400L);
  }

  @Test
  void test_map_mapsCorrectly_when_currencyEntityToCurrency() {
    // given
    CurrencyEntityMapper mapper = CurrencyEntityMapper.INSTANCE;
    CurrencyEntity currencyEntity =
        CurrencyEntity.builder()
            .goldAmount(100L)
            .diamondAmount(200L)
            .emeraldAmount(300L)
            .amethystAmount(400L)
            .id(UUID.randomUUID())
            .build();

    // when
    Currency currency = mapper.map(currencyEntity);

    // then
    assertThat(currency.gold()).isEqualTo(100L);
    assertThat(currency.diamond()).isEqualTo(200L);
    assertThat(currency.emerald()).isEqualTo(300L);
    assertThat(currency.amethyst()).isEqualTo(400L);
  }

  @Test
  void test_map_mapsCorrectly_when_gameSaveEntityToGameSave() {
    // given
    GameMetadataEntityMapper mapper = GameMetadataEntityMapper.INSTANCE;
    var id = UUID.randomUUID();
    GameMetadataEntity gameMetadataEntity =
        GameMetadataEntity.builder()
            .id(id)
            .userEmail(userEmail)
            .nickname("toto")
            .createdAt(new Date())
            .updatedAt(new Date())
            .build();
    // when
    GameMetadata gameMetadata = mapper.map(gameMetadataEntity);

    // then
    assertThat(gameMetadata.id()).isEqualTo(gameMetadataEntity.getId());
    assertThat(gameMetadata.userEmail()).isEqualTo(gameMetadataEntity.getUserEmail());
    assertThat(gameMetadata.nickname()).isEqualTo(gameMetadataEntity.getNickname());
    assertThat(gameMetadata.createdAt()).isEqualTo(gameMetadataEntity.getCreatedAt());
    assertThat(gameMetadata.updatedAt()).isEqualTo(gameMetadataEntity.getUpdatedAt());
  }

  @Test
  void test_map_mapsCorrectly_when_userRepresentationToUser() {
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
    assertThat(user.getId()).isEqualTo(UUID.fromString(userRepresentation.getId()));
    assertThat(user.getEmailVerified()).isTrue();
  }

  @Test
  void test_map_mapsCorrectly_when_userCreationRequestToUserRepresentation() {
    // given
    UserCreationRequestMapper mapper = UserCreationRequestMapper.INSTANCE;
    SimpleUserCreationRequest simpleUserCreationRequest =
        SimpleUserCreationRequest.builder()
            .username(userEmail)
            .password("password")
            .lastName("tata")
            .firstName("toto")
            .build();

    // when
    User user = mapper.map(simpleUserCreationRequest);

    // then
    assertThat(user.getUsername()).isEqualTo(simpleUserCreationRequest.username());
    assertThat(user.getFirstName()).isEqualTo(simpleUserCreationRequest.firstName());
    assertThat(user.getLastName()).isEqualTo(simpleUserCreationRequest.lastName());
    assertThat(user.getEmailVerified()).isEqualTo(simpleUserCreationRequest.getEmailVerified());
    assertThat(user.getEnabled()).isEqualTo(simpleUserCreationRequest.getEnabled());
    assertThat(user.getUserRoles()).isEqualTo(simpleUserCreationRequest.getUserRoles());
    assertThat(user.getCreatedTimestamp()).isNull();
    assertThat(user.getId()).isNull();
  }
}
