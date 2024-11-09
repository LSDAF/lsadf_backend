package com.lsadf.lsadf_backend.unit.mapper;

import com.lsadf.lsadf_backend.entities.CharacteristicsEntity;
import com.lsadf.lsadf_backend.entities.CurrencyEntity;
import com.lsadf.lsadf_backend.entities.GameSaveEntity;
import com.lsadf.lsadf_backend.entities.StageEntity;
import com.lsadf.lsadf_backend.mappers.Mapper;
import com.lsadf.lsadf_backend.mappers.impl.MapperImpl;
import com.lsadf.lsadf_backend.models.*;
import com.lsadf.lsadf_backend.requests.admin.AdminUserCreationRequest;
import com.lsadf.lsadf_backend.requests.characteristics.CharacteristicsRequest;
import com.lsadf.lsadf_backend.requests.currency.CurrencyRequest;
import com.lsadf.lsadf_backend.requests.stage.StageRequest;
import com.lsadf.lsadf_backend.requests.user.UserCreationRequest;
import org.junit.jupiter.api.Test;
import org.keycloak.representations.idm.UserRepresentation;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class MapperTests {

    private final Mapper mapper = new MapperImpl();

    private final String userEmail = "toto@toto.com";

    @Test
    void should_map_stage_request_to_stage() {
        // given
        StageRequest stageRequest = new StageRequest(25L, 500L);

        // when
        Stage stage = mapper.mapStageRequestToStage(stageRequest);

        // then
        assertThat(stage.getCurrentStage()).isEqualTo(25L);
        assertThat(stage.getMaxStage()).isEqualTo(500L);
    }

    @Test
    void should_map_stage_entity_to_stage() {
        // given
        GameSaveEntity gameSaveEntity = GameSaveEntity.builder().build();
        StageEntity stageEntity = StageEntity.builder()
                .maxStage(500L)
                .currentStage(25L)
                .id(UUID.randomUUID().toString())
                .gameSave(gameSaveEntity)
                .userEmail(userEmail)
                .build();

        // when
        Stage stage = mapper.mapStageEntityToStage(stageEntity);

        // then
        assertThat(stage.getCurrentStage()).isEqualTo(25L);
        assertThat(stage.getMaxStage()).isEqualTo(500L);
    }

    @Test
    void should_map_characteristics_entity_to_characteristics() {
        // given
        GameSaveEntity gameSaveEntity = GameSaveEntity.builder().build();
        CharacteristicsEntity characteristicsEntity = CharacteristicsEntity.builder()
                .gameSave(gameSaveEntity)
                .attack(100L)
                .critChance(200L)
                .critDamage(300L)
                .health(400L)
                .resistance(500L)
                .build();

        // when
        Characteristics characteristics = mapper.mapCharacteristicsEntityToCharacteristics(characteristicsEntity);

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
        CharacteristicsRequest characteristicsRequest = new CharacteristicsRequest(100L, 200L, 300L, 400L, 500L);

        // when
        Characteristics characteristics = mapper.mapCharacteristicsRequestToCharacteristics(characteristicsRequest);

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

        // when
        Currency currency = mapper.mapCurrencyRequestToCurrency(currencyRequest);

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
        CurrencyEntity currencyEntity = CurrencyEntity.builder()
                .goldAmount(100L)
                .diamondAmount(200L)
                .emeraldAmount(300L)
                .amethystAmount(400L)
                .id(UUID.randomUUID().toString())
                .gameSave(gameSaveEntity)
                .userEmail(userEmail)
                .build();

        // when
        Currency currency = mapper.mapCurrencyEntityToCurrency(currencyEntity);

        // then
        assertThat(currency.getGold()).isEqualTo(100L);
        assertThat(currency.getDiamond()).isEqualTo(200L);
        assertThat(currency.getEmerald()).isEqualTo(300L);
        assertThat(currency.getAmethyst()).isEqualTo(400L);
    }

    @Test
    void should_map_game_save_entity_to_game_save() {
        // given
        String id = UUID.randomUUID().toString();
        GameSaveEntity gameSaveEntity = GameSaveEntity.builder()
                .id(id)
                .userEmail(userEmail)
                .nickname("toto")
                .createdAt(new Date())
                .updatedAt(new Date())
                .build();
        CharacteristicsEntity characteristicsEntity = CharacteristicsEntity.builder()
                .gameSave(gameSaveEntity)
                .attack(100L)
                .critChance(200L)
                .critDamage(300L)
                .health(400L)
                .resistance(500L)
                .build();
        CurrencyEntity currencyEntity = CurrencyEntity.builder()
                .goldAmount(100L)
                .diamondAmount(200L)
                .emeraldAmount(300L)
                .amethystAmount(400L)
                .id(id)
                .gameSave(gameSaveEntity)
                .userEmail(userEmail)
                .build();
        StageEntity stageEntity = StageEntity.builder()
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
        GameSave gameSave = mapper.mapGameSaveEntityToGameSave(gameSaveEntity);

        // then
        assertThat(gameSave.getId()).isEqualTo(gameSaveEntity.getId());
        assertThat(gameSave.getUserEmail()).isEqualTo(gameSaveEntity.getUserEmail());
        assertThat(gameSave.getNickname()).isEqualTo(gameSaveEntity.getNickname());
        assertThat(gameSave.getCharacteristics().getAttack()).isEqualTo(characteristicsEntity.getAttack());
        assertThat(gameSave.getCharacteristics().getCritChance()).isEqualTo(characteristicsEntity.getCritChance());
        assertThat(gameSave.getCharacteristics().getCritDamage()).isEqualTo(characteristicsEntity.getCritDamage());
        assertThat(gameSave.getCharacteristics().getHealth()).isEqualTo(characteristicsEntity.getHealth());
        assertThat(gameSave.getCharacteristics().getResistance()).isEqualTo(characteristicsEntity.getResistance());
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

        // when
        User user = mapper.mapUserRepresentationToUser(userRepresentation);

        // then
        assertThat(user.getUsername()).isEqualTo(userRepresentation.getUsername());
        assertThat(user.getFirstName()).isEqualTo(userRepresentation.getFirstName());
        assertThat(user.getLastName()).isEqualTo(userRepresentation.getLastName());
        assertThat(user.getCreatedTimestamp()).isNotNull();
        assertThat(user.getUserRoles()).containsExactlyInAnyOrder("user", "admin");
        assertThat(user.isEnabled()).isFalse();
        assertThat(user.getId()).isEqualTo(userRepresentation.getId());
        assertThat(user.isEmailVerified()).isTrue();
    }

    @Test
    void should_map_user_creation_request_to_user_representation() {
        // given
        UserCreationRequest userCreationRequest = UserCreationRequest.builder()
                .username(userEmail)
                .password("password")
                .lastName("tata")
                .firstName("toto")
                .userRoles(List.of("user", "admin"))
                .emailVerified(false)
                .enabled(false)
                .build();

        // when
        UserRepresentation userRepresentation = mapper.mapUserCreationRequestToUserRepresentation(userCreationRequest);

        // then
        assertThat(userRepresentation.getUsername()).isEqualTo(userCreationRequest.getUsername());
        assertThat(userRepresentation.getFirstName()).isEqualTo(userCreationRequest.getFirstName());
        assertThat(userRepresentation.getLastName()).isEqualTo(userCreationRequest.getLastName());
        assertThat(userRepresentation.isEmailVerified()).isEqualTo(userCreationRequest.isEmailVerified());
        assertThat(userRepresentation.isEnabled()).isEqualTo(userCreationRequest.isEnabled());
    }

    @Test
    void should_map_admin_user_creation_request_to_user_creation_request() {
        // given
        AdminUserCreationRequest adminUserCreationRequest = AdminUserCreationRequest.builder()
                .username(userEmail)
                .lastName("tata")
                .firstName("toto")
                .emailVerified(false)
                .enabled(false)
                .build();

        // when
        UserCreationRequest userCreationRequest = mapper.mapAdminUserCreationRequestToUserCreationRequest(adminUserCreationRequest);

        // then
        assertThat(userCreationRequest.getUsername()).isEqualTo(adminUserCreationRequest.getUsername());
        assertThat(userCreationRequest.getFirstName()).isEqualTo(adminUserCreationRequest.getFirstName());
        assertThat(userCreationRequest.getLastName()).isEqualTo(adminUserCreationRequest.getLastName());
        assertThat(userCreationRequest.isEmailVerified()).isEqualTo(adminUserCreationRequest.getEmailVerified());
        assertThat(userCreationRequest.isEnabled()).isEqualTo(adminUserCreationRequest.getEnabled());
    }
}
