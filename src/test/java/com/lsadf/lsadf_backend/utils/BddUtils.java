package com.lsadf.lsadf_backend.utils;

import com.lsadf.lsadf_backend.bdd.BddFieldConstants;
import com.lsadf.lsadf_backend.constants.SocialProvider;
import com.lsadf.lsadf_backend.constants.UserRole;
import com.lsadf.lsadf_backend.exceptions.NotFoundException;
import com.lsadf.lsadf_backend.models.GameSave;
import com.lsadf.lsadf_backend.models.JwtAuthentication;
import com.lsadf.lsadf_backend.models.User;
import com.lsadf.lsadf_backend.entities.GameSaveEntity;
import com.lsadf.lsadf_backend.entities.UserEntity;
import com.lsadf.lsadf_backend.models.UserInfo;
import com.lsadf.lsadf_backend.repositories.UserRepository;
import com.lsadf.lsadf_backend.requests.admin.AdminGameSaveCreationRequest;
import com.lsadf.lsadf_backend.requests.game_save.GameSaveUpdateRequest;
import com.lsadf.lsadf_backend.requests.user.UserCreationRequest;
import com.lsadf.lsadf_backend.requests.user.UserLoginRequest;
import com.lsadf.lsadf_backend.responses.GenericResponse;
import lombok.experimental.UtilityClass;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.DefaultResponseErrorHandler;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Utility class for BDD tests
 */
@UtilityClass
public class BddUtils {

    private static final String COMMA = ",";

    /**
     * Maps a row from a BDD table to a GameSaveEntity
     *
     * @param row            row from BDD table
     * @param userRepository UserRepository
     * @return GameSaveEntity
     */
    public static GameSaveEntity mapToGameSaveEntity(Map<String, String> row, UserRepository userRepository) {
        String id = row.get(BddFieldConstants.GameSave.ID);
        String userId = row.get(BddFieldConstants.GameSave.USER_ID);
        String userEmail = row.get(BddFieldConstants.GameSave.USER_EMAIL);
        String gold = row.get(BddFieldConstants.GameSave.GOLD);
        String healthPoints = row.get(BddFieldConstants.GameSave.HEALTH_POINTS);
        String attack = row.get(BddFieldConstants.GameSave.ATTACK);

        long attackLong = attack == null ? 0 : Long.parseLong(attack);
        long healthPointsLong = healthPoints == null ? 0 : Long.parseLong(healthPoints);
        long goldLong = gold == null ? 0 : Long.parseLong(gold);
        Optional<UserEntity> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            userOptional = userRepository.findUserEntityByEmail(userEmail);
        }

        GameSaveEntity gameSaveEntity = GameSaveEntity.builder()
                .user(userOptional.orElse(null))
                .attack(attackLong)
                .healthPoints(healthPointsLong)
                .gold(goldLong)
                .build();

        gameSaveEntity.setId(id);

        return gameSaveEntity;
    }

    /**
     * Maps a row from a BDD table to a UserInfo
     * @param row row from BDD table
     * @return UserInfo
     */
    public static UserInfo mapToUserInfo(Map<String, String> row) {
        String id = row.get(BddFieldConstants.UserInfo.ID);
        String email = row.get(BddFieldConstants.UserInfo.EMAIL);
        String name = row.get(BddFieldConstants.UserInfo.NAME);
        var rolesString = row.get(BddFieldConstants.UserInfo.ROLES);
        List<UserRole> roles = Collections.emptyList();
        if (rolesString != null) {
            roles = Arrays.stream(rolesString.split(COMMA)).map(UserRole::valueOf).sorted().toList();
        }

        return new UserInfo(id, name, email, roles);
    }


    /**
     * Initializes the RestTemplate inside TestRestTemplate
     * @param testRestTemplate the TestRestTemplate
     */
    public void initTestRestTemplate(TestRestTemplate testRestTemplate) {
        var template = testRestTemplate.getRestTemplate();
        template.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
        template.setErrorHandler(new DefaultResponseErrorHandler() {
            @Override
            public boolean hasError(ClientHttpResponse response) throws IOException {
                HttpStatus status = HttpStatus.resolve(response.getStatusCode().value());
                return status.series() == HttpStatus.Series.SERVER_ERROR;
            }
        });
    }

    /**
     * Maps a row from a BDD table to a User
     *
     * @param row            row from BDD table
     * @param userRepository UserRepository
     * @return User
     * @throws NotFoundException if user is not found
     */
    public static User mapToUser(Map<String, String> row, UserRepository userRepository) throws NotFoundException {
        String id = row.get(BddFieldConstants.User.ID);
        String email = row.get(BddFieldConstants.User.EMAIL);
        String name = row.get(BddFieldConstants.User.NAME);

        UserEntity userEntity = userRepository.findById(id).orElseThrow(NotFoundException::new);

        return User.builder()
                .id(id)
                .email(email)
                .name(name)
                .build();
    }

    /**
     * Maps a row from a BDD table to an AdminGameSaveCreationRequest
     *
     * @param row row from BDD table
     * @return AdminGameSaveCreationRequest
     */
    public static AdminGameSaveCreationRequest mapToAdminGameSaveCreationRequest(Map<String, String> row) {
        long gold = Long.parseLong(row.get(BddFieldConstants.GameSave.GOLD));
        long healthPoints = Long.parseLong(row.get(BddFieldConstants.GameSave.HEALTH_POINTS));
        long attack = Long.parseLong(row.get(BddFieldConstants.GameSave.ATTACK));
        String userEmail = row.get(BddFieldConstants.GameSave.USER_EMAIL);

        return new AdminGameSaveCreationRequest(userEmail, gold, healthPoints, attack);
    }

    /**
     * Maps a row from a BDD table to a GameSave
     *
     * @param row row from BDD table
     * @return GameSave
     */
    public static GameSave mapToGameSave(Map<String, String> row) {
        String id = row.get(BddFieldConstants.GameSave.ID);
        String userId = row.get(BddFieldConstants.GameSave.USER_ID);
        String userEmail = row.get(BddFieldConstants.GameSave.USER_EMAIL);
        long gold = Long.parseLong(row.get(BddFieldConstants.GameSave.GOLD));
        long healthPoints = Long.parseLong(row.get(BddFieldConstants.GameSave.HEALTH_POINTS));
        long attack = Long.parseLong(row.get(BddFieldConstants.GameSave.ATTACK));

        return GameSave.builder()
                .attack(attack)
                .userId(userId)
                .userEmail(userEmail)
                .id(id)
                .healthPoints(healthPoints)
                .gold(gold)
                .build();
    }

    /**
     * Maps a row from a BDD table to a GameSaveUpdateRequest
     *
     * @param row row from BDD table
     * @return GameSaveUpdateRequest
     */
    public static GameSaveUpdateRequest mapToGameSaveUpdateRequest(Map<String, String> row) {
        String gold = row.get(BddFieldConstants.GameSave.GOLD);
        String healthPoints = row.get(BddFieldConstants.GameSave.HEALTH_POINTS);
        String attack = row.get(BddFieldConstants.GameSave.ATTACK);
        String id = row.get(BddFieldConstants.GameSave.ID);
        String userId = row.get(BddFieldConstants.GameSave.USER_ID);

        Long goldLong = gold == null ? null : Long.parseLong(gold);
        Long healthPointsLong = healthPoints == null ? null : Long.parseLong(healthPoints);
        Long attackLong = attack == null ? null : Long.parseLong(attack);


        return new GameSaveUpdateRequest(goldLong, healthPointsLong, attackLong);
    }

    /**
     * Maps a row from a BDD table to a UserEntity
     *
     * @param row row from BDD table
     * @return UserEntity
     */
    public static UserEntity mapToUserEntity(Map<String, String> row) {
        String email = row.get(BddFieldConstants.User.EMAIL);
        String name = row.get(BddFieldConstants.User.NAME);
        String id = row.get(BddFieldConstants.User.ID);
        String password = row.get(BddFieldConstants.User.PASSWORD);
        String provider = row.get(BddFieldConstants.User.PROVIDER);
        String enabled = row.get(BddFieldConstants.User.ENABLED);
        String roles = row.get(BddFieldConstants.User.ROLES);


        boolean enabledBoolean = enabled == null || Boolean.parseBoolean(enabled);
        SocialProvider socialProvider = SocialProvider.fromString(provider);
        Set<UserRole> roleSet = roles == null
                ? Set.of(UserRole.USER)
                : Arrays.stream(roles.split(COMMA))
                .map(UserRole::valueOf)
                .collect(Collectors.toSet());


        UserEntity userEntity = UserEntity.builder()
                .email(email)
                .name(name)
                .enabled(enabledBoolean)
                .password(password)
                .provider(socialProvider)
                .roles(roleSet)
                .build();

        userEntity.setId(id);

        return userEntity;
    }

    /**
     * Maps a row from a BDD table to a UserCreationRequest
     * @param row row from BDD table
     * @return UserCreationRequest
     */
    public static UserCreationRequest mapToUserCreationRequest(Map<String, String> row) {
        String email = row.get(BddFieldConstants.UserCreationRequest.EMAIL);
        String name = row.get(BddFieldConstants.UserCreationRequest.NAME);
        String password = row.get(BddFieldConstants.UserCreationRequest.PASSWORD);

        return UserCreationRequest.builder()
                .email(email)
                .name(name)
                .password(password)
                .build();
    }

    /**
     * Builds a URL from the server port and the endpoint to call
     * @param port port
     * @param endpoint endpoint
     * @return the URL
     */
    public static String buildUrl(int port, String endpoint) {
        return "http://localhost:" + port + endpoint;
    }

    /**
     * Builds an HttpEntity with the given body and headers
     * @param body body
     * @return HttpEntity
     * @param <T> type of the body
     */
    public static <T> HttpEntity<T> buildHttpEntity(T body) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Content-Type", "application/json");
        return new HttpEntity<>(body, httpHeaders);
    }

    /**
     * Maps a row from a BDD table to a UserLoginRequest
     * @param row row from BDD table
     * @return UserLoginRequest
     */
    public static UserLoginRequest mapToUserLoginRequest(Map<String, String> row) {
        String email = row.get(BddFieldConstants.UserLoginRequest.EMAIL);
        String password = row.get(BddFieldConstants.UserLoginRequest.PASSWORD);

        return new UserLoginRequest(email, password);
    }


    /**
     * Builds a ParameterizedTypeReference for a GenericResponse of UserInfo
     * @return ParameterizedTypeReference
     */
    public static ParameterizedTypeReference<GenericResponse<UserInfo>> buildParameterizedUserInfoResponse() {
        return new ParameterizedTypeReference<>() {
        };
    }

    /**
     * Builds a ParameterizedTypeReference for a GenericResponse of JwtAuthentication
     * @return ParameterizedTypeReference
     */
    public static ParameterizedTypeReference<GenericResponse<JwtAuthentication>> buildParameterizedJwtAuthenticationResponse() {
        return new ParameterizedTypeReference<>() {
        };
    }

    /**
     * Builds a ParameterizedTypeReference for a GenericResponse of GameSave
     * @return ParameterizedTypeReference
     */
    public static ParameterizedTypeReference<GenericResponse<GameSave>> buildParameterizedGameSaveResponse() {
        return new ParameterizedTypeReference<>() {
        };
    }

    public static ParameterizedTypeReference<GenericResponse<Void>> buildParameterizedVoidResponse() {
        return new ParameterizedTypeReference<>() {
        };
    }
}
