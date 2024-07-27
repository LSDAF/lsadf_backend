package com.lsadf.lsadf_backend.bdd;

import com.lsadf.lsadf_backend.constants.ControllerConstants;
import com.lsadf.lsadf_backend.entities.GameSaveEntity;
import com.lsadf.lsadf_backend.exceptions.NotFoundException;
import com.lsadf.lsadf_backend.models.GameSave;
import com.lsadf.lsadf_backend.models.JwtAuthentication;
import com.lsadf.lsadf_backend.models.LocalUser;
import com.lsadf.lsadf_backend.models.UserInfo;
import com.lsadf.lsadf_backend.requests.admin.AdminGameSaveCreationRequest;
import com.lsadf.lsadf_backend.requests.game_save.GameSaveUpdateRequest;
import com.lsadf.lsadf_backend.requests.user.UserCreationRequest;
import com.lsadf.lsadf_backend.requests.user.UserLoginRequest;
import com.lsadf.lsadf_backend.responses.GenericResponse;
import com.lsadf.lsadf_backend.utils.BddUtils;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.When;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;

import java.util.*;

import static com.lsadf.lsadf_backend.utils.BddUtils.buildParameterizedJwtAuthenticationResponse;
import static com.lsadf.lsadf_backend.utils.BddUtils.buildParameterizedUserInfoResponse;

@Slf4j(topic = "[WHEN STEP DEFINITIONS]")
public class BddWhenStepDefinitions extends BddLoader {

    @When("^the user with email (.*) gets the game save with id (.*)$")
    public void when_the_user_with_email_gets_a_game_save_with_id(String userEmail, String gameSaveId) {
        try {
            GameSaveEntity gameSaveEntity = gameSaveService.getGameSave(gameSaveId);
            gameSaveEntityListStack.push(Collections.singletonList(gameSaveEntity));
        } catch (Exception e) {
            exceptionStack.push(e);
        }
    }

    @When("^the user requests the endpoint to register a user with the following UserCreationRequest$")
    public void when_the_user_request_the_endpoint_to_register_a_user_with_the_following_UserCreationRequest(DataTable dataTable) {
        List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);

        // it should have only one line
        if (rows.size() > 1) {
            throw new IllegalArgumentException("Expected only one row in the DataTable");
        }

        Map<String, String> row = rows.get(0);
        UserCreationRequest userCreationRequest = BddUtils.mapToUserCreationRequest(row);
        String fullPath = ControllerConstants.AUTH + ControllerConstants.Auth.REGISTER;

        String url = BddUtils.buildUrl(this.serverPort, fullPath);
        HttpEntity<UserCreationRequest> request = BddUtils.buildHttpEntity(userCreationRequest);
        try {

            ResponseEntity<GenericResponse<UserInfo>> result = testRestTemplate.exchange(url, HttpMethod.POST, request, buildParameterizedUserInfoResponse());
            var body = result.getBody();
            responseStack.push(body);
            log.info("Response: {}", result);

        } catch (Exception e) {
            exceptionStack.push(e);
        }
    }

    @When("^we want to delete the game save with id (.*)$")
    public void when_the_user_with_email_deletes_a_game_save(String saveId) {
        try {
            gameSaveService.deleteGameSave(saveId);
        } catch (Exception e) {
            exceptionStack.push(e);
        }
    }


    @When("^we want to create a new game save for the user with email (.*)$")
    public void when_we_want_to_create_a_new_game_save_for_the_user_with_email(String userEmail) {
        try {
            GameSaveEntity gameSaveEntity = gameSaveService.createGameSave(userEmail);
            gameSaveEntityListStack.push(Collections.singletonList(gameSaveEntity));
        } catch (Exception e) {
            exceptionStack.push(e);
        }
    }

    @When("^we want to create a new game save with the following AdminGameSaveCreationRequest$")
    public void when_we_want_to_create_a_new_game_save_with_the_following_AdminGameSaveCreationRequest(DataTable dataTable) throws NotFoundException {
        List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);

        // it should have only one line
        if (rows.size() > 1) {
            throw new IllegalArgumentException("Expected only one row in the DataTable");
        }

        List<GameSaveEntity> list = new ArrayList<>();
        for (Map<String, String> row : rows) {
            AdminGameSaveCreationRequest request = BddUtils.mapToAdminGameSaveCreationRequest(row);
            GameSaveEntity gameSave = gameSaveService.createGameSave(request);
            list.add(gameSave);
        }


        try {
            gameSaveEntityListStack.push(list);
        } catch (Exception e) {
            exceptionStack.push(e);
        }
    }

    @When("^we want to get the game save with id (.*)$")
    public void when_we_want_to_get_the_game_save_with_id(String saveId) {
        try {
            GameSaveEntity gameSaveEntity = gameSaveService.getGameSave(saveId);
            gameSaveEntityListStack.push(Collections.singletonList(gameSaveEntity));
        } catch (Exception e) {
            exceptionStack.push(e);
        }
    }

    @When("^we want to get all game saves$")
    public void when_we_want_to_get_all_game_saves() {
        try {
            List<GameSaveEntity> gameSaves = gameSaveService.getGameSaves().toList();
            gameSaveEntityListStack.push(gameSaves);
        } catch (Exception e) {
            exceptionStack.push(e);
        }
    }

    @When("^we want to update the game save with id (.*) with the following GameSaveUpdateRequest$")
    public void when_we_want_to_update_the_game_save_with_user_id_with_the_following_GameSaveUpdateRequest(String saveId, DataTable dataTable) {
        List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);

        // it should have only one line
        if (rows.size() > 1) {
            throw new IllegalArgumentException("Expected only one row in the DataTable");
        }

        Map<String, String> row = rows.get(0);
        GameSaveUpdateRequest updateRequest = BddUtils.mapToGameSaveUpdateRequest(row);
        try {
            GameSaveEntity updatedGameSave = gameSaveService.updateGameSave(saveId, updateRequest);
            gameSaveEntityListStack.push(Collections.singletonList(updatedGameSave));
        } catch (Exception e) {
            exceptionStack.push(e);
        }
    }

    @When("^we check the game save ownership with id (.*) for the user with email (.*)$")
    public void when_we_check_the_game_save_ownership_with_id_for_the_user_with_email(String saveId, String userEmail) {
        try {
            gameSaveService.checkGameSaveOwnership(saveId, userEmail);
        } catch (Exception e) {
            exceptionStack.push(e);
        }
    }

    @When("the user logs in with the following credentials")
    public void when_the_user_logs_in_with_the_following_credentials(DataTable dataTable) {
        List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);

        // it should have only one line
        if (rows.size() > 1) {
            throw new IllegalArgumentException("Expected only one row in the DataTable");
        }

        UserLoginRequest userLoginRequest = BddUtils.mapToUserLoginRequest(rows.get(0));

        String fullPath = ControllerConstants.AUTH + ControllerConstants.Auth.LOGIN;

        String url = BddUtils.buildUrl(this.serverPort, fullPath);

        HttpEntity<UserLoginRequest> request = BddUtils.buildHttpEntity(userLoginRequest);
        try {
            ResponseEntity<GenericResponse<JwtAuthentication>> result = testRestTemplate.exchange(url, HttpMethod.POST, request, buildParameterizedJwtAuthenticationResponse());
            var body = result.getBody();
            responseStack.push(body);
            log.info("Response: {}", result);

        } catch (Exception e) {
            exceptionStack.push(e);
        }
    }

    @When("^the user requests the endpoint to generate a GameSave$")
    public void when_the_user_requests_the_endpoint_to_create_a_game_save() {
        String fullPath = ControllerConstants.GAME_SAVE + ControllerConstants.GameSave.GENERATE;

        String url = BddUtils.buildUrl(this.serverPort, fullPath);
        try {

            String token = jwtStack.peek();
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(token);
            HttpEntity<Void> request = new HttpEntity<>(headers);
            ResponseEntity<GenericResponse<GameSave>> result = testRestTemplate.exchange(url, HttpMethod.POST, request, BddUtils.buildParameterizedGameSaveResponse());
            var body = result.getBody();
            responseStack.push(body);
            log.info("Response: {}", result);

        } catch (Exception e) {
            exceptionStack.push(e);
        }
    }

    @When("^the user requests the endpoint to generate a game save with no token$")
    public void when_the_user_requests_the_endpoint_to_create_a_game_save_with_no_token() {
        String fullPath = ControllerConstants.GAME_SAVE + ControllerConstants.GameSave.GENERATE;

        String url = BddUtils.buildUrl(this.serverPort, fullPath);
        try {
            HttpEntity<Void> request = new HttpEntity<>(new HttpHeaders());
            ResponseEntity<GenericResponse<GameSave>> result = testRestTemplate.exchange(url, HttpMethod.POST, request, BddUtils.buildParameterizedGameSaveResponse());
            var body = result.getBody();
            responseStack.push(body);
            log.info("Response: {}", result);

        } catch (Exception e) {
            exceptionStack.push(e);
        }
    }


    @When("^the user with email (.*) logs in$")
    public void when_the_user_with_email_logs_in(String userEmail) throws NotFoundException {
        LocalUser localUser = userDetailsService.loadUserByEmail(userEmail);
        String jwt = tokenProvider.createToken(localUser);
        jwtStack.push(jwt);
    }

    @When("^the user requests the endpoint to update a GameSave with no token$")
    public void when_the_user_requests_the_endpoint_to_update_a_game_save_with_no_token() {
        String fullPath = ControllerConstants.GAME_SAVE + "/1";

        String url = BddUtils.buildUrl(this.serverPort, fullPath);
        try {
            HttpHeaders headers = new HttpHeaders();
            HttpEntity<GameSaveUpdateRequest> request = new HttpEntity<>(new GameSaveUpdateRequest(), headers);
            ResponseEntity<GenericResponse<Void>> result = testRestTemplate.exchange(url, HttpMethod.POST, request, BddUtils.buildParameterizedVoidResponse());
            var body = result.getBody();
            responseStack.push(body);
            log.info("Response: {}", result);

        } catch (Exception e) {
            exceptionStack.push(e);
        }
    }

    @When("^the user requests the endpoint to update a GameSave with id (.*) with the following GameSaveUpdateRequest$")
    public void when_the_user_requests_the_endpoint_to_update_a_game_save_with_id_with_the_following_game_save_update_request(String gameSaveId, DataTable dataTable) {
        List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);

        // it should have only one line
        if (rows.size() > 1) {
            throw new IllegalArgumentException("Expected only one row in the DataTable");
        }

        Map<String, String> row = rows.get(0);
        GameSaveUpdateRequest updateRequest = BddUtils.mapToGameSaveUpdateRequest(row);

        String fullPath = ControllerConstants.GAME_SAVE + "/" + gameSaveId;

        String url = BddUtils.buildUrl(this.serverPort, fullPath);
        try {

            String token = jwtStack.peek();
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(token);
            HttpEntity<GameSaveUpdateRequest> request = new HttpEntity<>(updateRequest, headers);
            ResponseEntity<GenericResponse<Void>> result = testRestTemplate.exchange(url, HttpMethod.POST, request, BddUtils.buildParameterizedVoidResponse());
            var body = result.getBody();
            responseStack.push(body);
            log.info("Response: {}", result);

        } catch (Exception e) {
            exceptionStack.push(e);
        }
    }
}
