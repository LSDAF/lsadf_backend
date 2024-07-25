package com.lsadf.lsadf_backend.bdd;

import com.lsadf.lsadf_backend.entities.GameSaveEntity;
import com.lsadf.lsadf_backend.entities.UserEntity;
import com.lsadf.lsadf_backend.utils.BddUtils;
import com.lsadf.lsadf_backend.utils.MockUtils;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j(topic = "[GIVEN STEP DEFINITIONS]")
public class BddGivenStepDefinitions extends BddLoader {

    @Given("^the BDD engine is ready$")
    public void given_the_bdd_engine_is_ready() {
        this.gameSaveListStack.clear();
        this.exceptionStack.clear();
        this.gameSaveEntityListStack.clear();
        this.userListStack.clear();
        this.userEntityListStack.clear();
        log.info("BDD engine is ready. Using port: {}", this.serverPort);
    }

    @Given("^a clean database$")
    public void given_i_have_a_clean_database() {
        log.info("Cleaning database repositories...");

        this.gameSaveRepository.deleteAll();
        this.userRepository.deleteAll();

        assertThat(gameSaveRepository.count()).isEqualTo(0);
        assertThat(userRepository.count()).isEqualTo(0);


        // Init all repository mocks
        MockUtils.initGameSaveRepositoryMock(gameSaveRepository);
        MockUtils.initUserRepositoryMock(userRepository);

        log.info("Database repositories cleaned");
    }

    @Given("^the following users$")
    public void given_i_have_the_following_users(DataTable dataTable) {
        List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);
        log.info("Creating users...");

        rows.forEach(row -> {
            UserEntity user = BddUtils.mapToUserEntity(row);
            UserEntity newEntity = userRepository.save(user);
            log.info("User created: {}", newEntity);
        });

        log.info("Users created");
    }

    @Given("^the following game saves$")
    public void given_i_have_the_following_game_saves(DataTable dataTable) {
        List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);
        log.info("Creating game saves...");

        rows.forEach(row -> {
            GameSaveEntity gameSaveEntity = BddUtils.mapToGameSaveEntity(row, userRepository);
            GameSaveEntity newEntity = gameSaveRepository.save(gameSaveEntity);
            log.info("Game save created: {}", newEntity);
        });

        log.info("Game saves created");
    }
}
