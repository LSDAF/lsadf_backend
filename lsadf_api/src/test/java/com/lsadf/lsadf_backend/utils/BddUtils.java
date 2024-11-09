package com.lsadf.lsadf_backend.utils;

import com.lsadf.lsadf_backend.bdd.BddFieldConstants;
import com.lsadf.lsadf_backend.entities.CharacteristicsEntity;
import com.lsadf.lsadf_backend.entities.CurrencyEntity;
import com.lsadf.lsadf_backend.entities.GameSaveEntity;
import com.lsadf.lsadf_backend.entities.StageEntity;
import com.lsadf.lsadf_backend.models.*;
import com.lsadf.lsadf_backend.requests.admin.AdminGameSaveCreationRequest;
import com.lsadf.lsadf_backend.requests.admin.AdminGameSaveUpdateRequest;
import com.lsadf.lsadf_backend.requests.admin.AdminUserCreationRequest;
import com.lsadf.lsadf_backend.requests.admin.AdminUserUpdateRequest;
import com.lsadf.lsadf_backend.requests.characteristics.CharacteristicsRequest;
import com.lsadf.lsadf_backend.requests.common.Filter;
import com.lsadf.lsadf_backend.requests.currency.CurrencyRequest;
import com.lsadf.lsadf_backend.requests.game_save.GameSaveUpdateNicknameRequest;
import com.lsadf.lsadf_backend.requests.stage.StageRequest;
import com.lsadf.lsadf_backend.requests.user.UserCreationRequest;
import com.lsadf.lsadf_backend.requests.user.UserLoginRequest;
import com.lsadf.lsadf_backend.requests.user.UserRefreshLoginRequest;
import com.lsadf.lsadf_backend.requests.user.UserUpdateRequest;
import lombok.experimental.UtilityClass;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Utility class for BDD tests
 */
@UtilityClass
public class BddUtils {

    private static final String COMMA = ",";

    /**
     * Maps a row from a BDD table to a CharacteristicsRequest
     *
     * @param row row from BDD table
     * @return CharacteristicsRequest
     */
    public static CharacteristicsRequest mapToCharacteristicsRequest(Map<String, String> row) {
        String attack = row.get(BddFieldConstants.Characteristics.ATTACK);
        String critChance = row.get(BddFieldConstants.Characteristics.CRIT_CHANCE);
        String critDamage = row.get(BddFieldConstants.Characteristics.CRIT_DAMAGE);
        String health = row.get(BddFieldConstants.Characteristics.HEALTH);
        String resistance = row.get(BddFieldConstants.Characteristics.RESISTANCE);

        long attackLong = attack == null ? 0 : Long.parseLong(attack);
        long critChanceLong = critChance == null ? 0 : Long.parseLong(critChance);
        long critDamageLong = critDamage == null ? 0 : Long.parseLong(critDamage);
        long healthLong = health == null ? 0 : Long.parseLong(health);
        long resistanceLong = resistance == null ? 0 : Long.parseLong(resistance);

        return new CharacteristicsRequest(attackLong, critChanceLong, critDamageLong, healthLong, resistanceLong);
    }

    /**
     * Maps a row from a BDD table to a CurrencyRequest
     *
     * @param row row from BDD table
     * @return CurrencyRequest
     */
    public static CurrencyRequest mapToCurrencyRequest(Map<String, String> row) {
        String gold = row.get(BddFieldConstants.Currency.GOLD);
        String diamond = row.get(BddFieldConstants.Currency.DIAMOND);
        String emerald = row.get(BddFieldConstants.Currency.EMERALD);
        String amethyst = row.get(BddFieldConstants.Currency.AMETHYST);

        long goldLong = gold == null ? 0 : Long.parseLong(gold);
        long diamondLong = diamond == null ? 0 : Long.parseLong(diamond);
        long emeraldLong = emerald == null ? 0 : Long.parseLong(emerald);
        long amethystLong = amethyst == null ? 0 : Long.parseLong(amethyst);

        return new CurrencyRequest(goldLong, diamondLong, emeraldLong, amethystLong);
    }

    /**
     * Maps a row from a BDD table to a StageRequest
     *
     * @param row row from BDD table
     * @return StageRequest
     */
    public static StageRequest mapToStageRequest(Map<String, String> row) {
        String currentStage = row.get(BddFieldConstants.Stage.CURRENT_STAGE);
        String maxStage = row.get(BddFieldConstants.Stage.MAX_STAGE);

        long currentStageLong = Long.parseLong(currentStage);
        long maxStageLong = Long.parseLong(maxStage);

        return new StageRequest(currentStageLong, maxStageLong);
    }


    /**
     * Maps a row from a BDD table to a GameSaveEntity
     *
     * @param row            row from BDD table
     * @return GameSaveEntity
     */
    public static GameSaveEntity mapToGameSaveEntity(Map<String, String> row) {
        String id = row.get(BddFieldConstants.GameSave.ID);
        String userEmail = row.get(BddFieldConstants.GameSave.USER_EMAIL);
        String nickname = row.get(BddFieldConstants.GameSave.NICKNAME);
        String gold = row.get(BddFieldConstants.Currency.GOLD);
        String diamond = row.get(BddFieldConstants.Currency.DIAMOND);
        String emerald = row.get(BddFieldConstants.Currency.EMERALD);
        String amethyst = row.get(BddFieldConstants.Currency.AMETHYST);
        String attack = row.get(BddFieldConstants.Characteristics.ATTACK);
        String critChance = row.get(BddFieldConstants.Characteristics.CRIT_CHANCE);
        String critDamage = row.get(BddFieldConstants.Characteristics.CRIT_DAMAGE);
        String health = row.get(BddFieldConstants.Characteristics.HEALTH);
        String resistance = row.get(BddFieldConstants.Characteristics.RESISTANCE);
        String currentStageString = row.get(BddFieldConstants.GameSave.CURRENT_STAGE);
        String maxStageString = row.get(BddFieldConstants.GameSave.MAX_STAGE);

        long goldLong = Long.parseLong(gold);
        long diamondLong = Long.parseLong(diamond);
        long emeraldLong = Long.parseLong(emerald);
        long amethystLong = Long.parseLong(amethyst);
        long attackLong = Long.parseLong(attack);
        long critChanceLong = Long.parseLong(critChance);
        long critDamageLong = Long.parseLong(critDamage);
        long healthLong = Long.parseLong(health);
        long resistanceLong = Long.parseLong(resistance);
        long currentStage = Long.parseLong(currentStageString);
        long maxStage = Long.parseLong(maxStageString);

        GameSaveEntity gameSaveEntity = GameSaveEntity.builder()
                .userEmail(userEmail)
                .nickname(nickname)
                .id(id)
                .build();

        CharacteristicsEntity characteristicsEntity = CharacteristicsEntity.builder()
                .gameSave(gameSaveEntity)
                .attack(attackLong)
                .critChance(critChanceLong)
                .critDamage(critDamageLong)
                .health(healthLong)
                .resistance(resistanceLong)
                .build();

        gameSaveEntity.setCharacteristicsEntity(characteristicsEntity);

        CurrencyEntity currencyEntity = CurrencyEntity.builder()
                .id(id)
                .gameSave(gameSaveEntity)
                .goldAmount(goldLong)
                .diamondAmount(diamondLong)
                .emeraldAmount(emeraldLong)
                .amethystAmount(amethystLong)
                .userEmail(userEmail)
                .build();

        gameSaveEntity.setCurrencyEntity(currencyEntity);

        StageEntity stageEntity = StageEntity.builder()
                .id(id)
                .currentStage(currentStage)
                .maxStage(maxStage)
                .gameSave(gameSaveEntity)
                .build();

        gameSaveEntity.setStageEntity(stageEntity);

        return gameSaveEntity;
    }

    /**
     * Maps a row from a BDD table to a UserInfo
     *
     * @param row row from BDD table
     * @return UserInfo
     */
    public static UserInfo mapToUserInfo(Map<String, String> row) {
        String email = row.get(BddFieldConstants.UserInfo.EMAIL);
        String name = row.get(BddFieldConstants.UserInfo.NAME);
        String rolesString = row.get(BddFieldConstants.UserInfo.ROLES);
        String verifiedString = row.get(BddFieldConstants.UserInfo.VERIFIED);

        Set<String> roles = Arrays.stream(rolesString.split(COMMA)).collect(Collectors.toSet());

        boolean verified = Boolean.parseBoolean(verifiedString);
        return new UserInfo(name, email, verified, roles);
    }


    /**
     * Initializes the RestTemplate inside TestRestTemplate
     *
     * @param testRestTemplate the TestRestTemplate
     */
    public void initTestRestTemplate(TestRestTemplate testRestTemplate) {
        RestTemplate template = testRestTemplate.getRestTemplate();
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
     * Maps a row from a BDD table to an AdminGameSaveCreationRequest
     *
     * @param row row from BDD table
     * @return AdminGameSaveCreationRequest
     */
    public static AdminGameSaveCreationRequest mapToAdminGameSaveCreationRequest(Map<String, String> row) {
        String goldString = row.get(BddFieldConstants.Currency.GOLD);
        String diamondString = row.get(BddFieldConstants.Currency.DIAMOND);
        String emeraldString = row.get(BddFieldConstants.Currency.EMERALD);
        String amethystString = row.get(BddFieldConstants.Currency.AMETHYST);
        String attackString = row.get(BddFieldConstants.Characteristics.ATTACK);
        String critChanceString = row.get(BddFieldConstants.Characteristics.CRIT_CHANCE);
        String critDamageString = row.get(BddFieldConstants.Characteristics.CRIT_DAMAGE);
        String healthString = row.get(BddFieldConstants.Characteristics.HEALTH);
        String resistanceString = row.get(BddFieldConstants.Characteristics.RESISTANCE);
        String currentStageString = row.get(BddFieldConstants.GameSave.CURRENT_STAGE);
        String maxStageString = row.get(BddFieldConstants.GameSave.MAX_STAGE);
        String nickname = row.get(BddFieldConstants.GameSave.NICKNAME);

        Long gold = Long.parseLong(goldString);
        Long diamond = Long.parseLong(diamondString);
        Long emerald = Long.parseLong(emeraldString);
        Long amethyst = Long.parseLong(amethystString);
        Long attack = Long.parseLong(attackString);
        Long critChance = Long.parseLong(critChanceString);
        Long critDamage = Long.parseLong(critDamageString);
        Long health = Long.parseLong(healthString);
        Long resistance = Long.parseLong(resistanceString);
        Long currentStage = Long.parseLong(currentStageString);
        Long maxStage = Long.parseLong(maxStageString);

        String id = row.get(BddFieldConstants.GameSave.ID);
        String userEmail = row.get(BddFieldConstants.GameSave.USER_EMAIL);

        StageRequest stageRequest = new StageRequest(currentStage, maxStage);
        CharacteristicsRequest characteristicsRequest = new CharacteristicsRequest(attack, critChance, critDamage, health, resistance);
        CurrencyRequest currencyRequest = new CurrencyRequest(gold, diamond, emerald, amethyst);

        return AdminGameSaveCreationRequest.builder()
                .nickname(nickname)
                .id(id)
                .stage(stageRequest)
                .characteristics(characteristicsRequest)
                .currency(currencyRequest)
                .userEmail(userEmail)
                .build();
    }

    /**
     * Maps a row from a BDD table to a User
     * @param row row from BDD table
     * @return User
     */
    public static User mapToUser(Map<String, String> row) {
        String email = row.get(BddFieldConstants.User.USERNAME);
        String firstName = row.get(BddFieldConstants.User.FIRST_NAME);
        String lastName = row.get(BddFieldConstants.User.LAST_NAME);
        String enabled = row.get(BddFieldConstants.User.ENABLED);
        String verified = row.get(BddFieldConstants.User.EMAIL_VERIFIED);
        String userRoles = row.get(BddFieldConstants.User.ROLES);

        List<String> roles = Arrays.stream(userRoles.split(COMMA)).toList();

        boolean enabledBoolean = Boolean.parseBoolean(enabled);
        boolean verifiedBoolean = Boolean.parseBoolean(verified);

        return User.builder()
                .username(email)
                .firstName(firstName)
                .lastName(lastName)
                .enabled(enabledBoolean)
                .emailVerified(verifiedBoolean)
                .userRoles(roles)
                .build();
    }

    /**
     * Maps a row from a BDD table to a GameSave
     *
     * @param row row from BDD table
     * @return GameSave
     */
    public static GameSave mapToGameSave(Map<String, String> row) {
        String id = row.get(BddFieldConstants.GameSave.ID);
        String userEmail = row.get(BddFieldConstants.GameSave.USER_EMAIL);
        String nickname = row.get(BddFieldConstants.GameSave.NICKNAME);

        Characteristics characteristics = mapToCharacteristics(row);
        Currency currency = mapToCurrency(row);
        Stage stage = mapToStage(row);

        return GameSave.builder()
                .userEmail(userEmail)
                .nickname(nickname)
                .id(id)
                .characteristics(characteristics)
                .currency(currency)
                .stage(stage)
                .build();
    }

    /**
     * Maps a row from a BDD table to a Characteristics POJO
     *
     * @param row row from BDD table
     * @return Characteristics
     */
    public static Characteristics mapToCharacteristics(Map<String, String> row) {
        String attack = row.get(BddFieldConstants.Characteristics.ATTACK);
        String critChance = row.get(BddFieldConstants.Characteristics.CRIT_CHANCE);
        String critDamage = row.get(BddFieldConstants.Characteristics.CRIT_DAMAGE);
        String health = row.get(BddFieldConstants.Characteristics.HEALTH);
        String resistance = row.get(BddFieldConstants.Characteristics.RESISTANCE);

        long attackLong = attack == null ? 1 : Long.parseLong(attack);
        long critChanceLong = critChance == null ? 1 : Long.parseLong(critChance);
        long critDamageLong = critDamage == null ? 1 : Long.parseLong(critDamage);
        long healthLong = health == null ? 1 : Long.parseLong(health);
        long resistanceLong = resistance == null ? 1 : Long.parseLong(resistance);

        return new Characteristics(attackLong, critChanceLong, critDamageLong, healthLong, resistanceLong);
    }

    /**
     * Maps a row from a BDD table to a Currency POJO
     *
     * @param row row from BDD table
     * @return Currency
     */
    public static Currency mapToCurrency(Map<String, String> row) {
        String gold = row.get(BddFieldConstants.Currency.GOLD);
        String diamond = row.get(BddFieldConstants.Currency.DIAMOND);
        String emerald = row.get(BddFieldConstants.Currency.EMERALD);
        String amethyst = row.get(BddFieldConstants.Currency.AMETHYST);

        Long goldLong = gold == null ? null : Long.parseLong(gold);
        Long diamondLong = diamond == null ? null : Long.parseLong(diamond);
        Long emeraldLong = emerald == null ? null : Long.parseLong(emerald);
        Long amethystLong = amethyst == null ? null : Long.parseLong(amethyst);

        return new Currency(goldLong, diamondLong, emeraldLong, amethystLong);
    }

    /**
     * Maps a row from a BDD table to a Stage
     *
     * @param row row from BDD table
     * @return Stage
     */
    public static Stage mapToStage(Map<String, String> row) {
        String currentStage = row.get(BddFieldConstants.Stage.CURRENT_STAGE);
        String maxStage = row.get(BddFieldConstants.Stage.MAX_STAGE);

        long currentStageLong = Long.parseLong(currentStage);
        long maxStageLong = Long.parseLong(maxStage);

        return Stage.builder()
                .currentStage(currentStageLong)
                .maxStage(maxStageLong)
                .build();
    }

    /**
     * Maps a row from a BDD table to a AdminGameSaveUpdateRequest
     *
     * @param row row from BDD table
     * @return AdminGameSaveUpdateRequest
     */
    public static AdminGameSaveUpdateRequest mapToAdminGameSaveUpdateRequest(Map<String, String> row) {
        String nickname = row.get(BddFieldConstants.GameSave.NICKNAME);

        return AdminGameSaveUpdateRequest.builder()
                .nickname(nickname)
                .build();
    }

    /**
     * Maps a row from a BDD table to a GameSaveUpdateUserRequest
     *
     * @param row row from BDD table
     * @return GameSaveUpdateRequest
     */
    public static GameSaveUpdateNicknameRequest mapToGameSaveUpdateUserRequest(Map<String, String> row) {
        String nickname = row.get(BddFieldConstants.GameSave.NICKNAME);

        return new GameSaveUpdateNicknameRequest(nickname);
    }

    /**
     * Maps a row from a BDD table to a UserCreationRequest
     *
     * @param row row from BDD table
     * @return UserCreationRequest
     */
    public static UserCreationRequest mapToUserCreationRequest(Map<String, String> row) {
        String email = row.get(BddFieldConstants.UserCreationRequest.EMAIL);
        String firstName = row.get(BddFieldConstants.UserCreationRequest.FIRST_NAME);
        String lastName = row.get(BddFieldConstants.UserCreationRequest.LAST_NAME);
        String password = row.get(BddFieldConstants.UserCreationRequest.PASSWORD);

        return UserCreationRequest.builder()
                .username(email)
                .firstName(firstName)
                .lastName(lastName)
                .password(password)
                .build();
    }

    /**
     * Builds a URL from the server port and the endpoint to call
     *
     * @param port     port
     * @param endpoint endpoint
     * @return the URL
     */
    public static String buildUrl(int port, String endpoint) {
        return "http://localhost:" + port + endpoint;
    }

    /**
     * Builds an HttpEntity with the given body and headers
     *
     * @param body body
     * @param <T>  type of the body
     * @return HttpEntity
     */
    public static <T> HttpEntity<T> buildHttpEntity(T body) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Content-Type", "application/json");
        return new HttpEntity<>(body, httpHeaders);
    }

    /**
     * Maps a row from a BDD table to a UserLoginRequest
     *
     * @param row row from BDD table
     * @return UserLoginRequest
     */
    public static UserLoginRequest mapToUserLoginRequest(Map<String, String> row) {
        String email = row.get(BddFieldConstants.UserLoginRequest.USERNAME);
        String password = row.get(BddFieldConstants.UserLoginRequest.PASSWORD);

        return new UserLoginRequest(email, password);
    }

    /**
     * Maps a row from a BDD table to a JwtAuthentication
     *
     * @param row row from BDD table
     * @return JwtAuthentication
     */
    public static UserUpdateRequest mapToUserUpdateRequest(Map<String, String> row) {
        String firstName = row.get(BddFieldConstants.UserUpdateRequest.FIRST_NAME);
        String lastName = row.get(BddFieldConstants.UserUpdateRequest.LAST_NAME);

        return new UserUpdateRequest(firstName, lastName);
    }

    /**
     * Maps a row from a BDD table to a AdminUserUpdateRequest
     *
     * @param row row from BDD table
     * @return AdminUserUpdateRequest
     */
    public static AdminUserUpdateRequest mapToAdminUserUpdateRequest(Map<String, String> row) {
        String firstName = row.get(BddFieldConstants.AdminUserUpdateRequest.FIRST_NAME);
        String lastName = row.get(BddFieldConstants.AdminUserUpdateRequest.LAST_NAME);
        String enabled = row.get(BddFieldConstants.AdminUserUpdateRequest.ENABLED);
        String verified = row.get(BddFieldConstants.AdminUserUpdateRequest.EMAIL_VERIFIED);
        String userRoles = row.get(BddFieldConstants.AdminUserUpdateRequest.USER_ROLES);

        List<String> roles = Arrays.stream(userRoles.split(COMMA)).toList();

        Boolean enabledBoolean = Boolean.parseBoolean(enabled);
        Boolean verifiedBoolean = Boolean.parseBoolean(verified);

        return new AdminUserUpdateRequest(firstName, lastName, verifiedBoolean, enabledBoolean, roles);
    }

    /**
     * Maps a row from a BDD table to a AdminUserCreationRequest
     *
     * @param row row from BDD table
     * @return AdminUserCreationRequest
     */
    public static AdminUserCreationRequest mapToAdminUserCreationRequest(Map<String, String> row) {
        String firstName = row.get(BddFieldConstants.AdminUserCreationRequest.FIRST_NAME);
        String lastName = row.get(BddFieldConstants.AdminUserCreationRequest.LAST_NAME);
        String email = row.get(BddFieldConstants.AdminUserCreationRequest.USERNAME);
        String enabled = row.get(BddFieldConstants.AdminUserCreationRequest.ENABLED);
        String verified = row.get(BddFieldConstants.AdminUserCreationRequest.EMAIL_VERIFIED);
        String roles = row.get(BddFieldConstants.AdminUserCreationRequest.USER_ROLES);

        Boolean enabledBoolean = Boolean.parseBoolean(enabled);
        Boolean verifiedBoolean = Boolean.parseBoolean(verified);

        List<String> userRoles = roles == null ? null : Arrays.stream(roles.split(COMMA)).toList();

        return new AdminUserCreationRequest(firstName, lastName, enabledBoolean, verifiedBoolean, email, userRoles);

    }

    /**
     * Maps a row from a BDD table to a SearchRequest Filter
     *
     * @param row row from BDD table
     * @return Filter
     */
    public static Filter mapToFilter(Map<String, String> row) {
        String key = row.get(BddFieldConstants.SearchRequest.KEY);
        String value = row.get(BddFieldConstants.SearchRequest.VALUE);
        return new Filter(key, value);
    }

    /**
     * Maps a row from a BDD table to a GlobalInfo
     *
     * @param row row from BDD table
     * @return GlobalInfo
     */
    public static GlobalInfo mapToGlobalInfo(Map<String, String> row) {
        String nbGameSaves = row.get(BddFieldConstants.GlobalInfo.GAME_SAVE_COUNTER);
        String nbUsers = row.get(BddFieldConstants.GlobalInfo.USER_COUNTER);
        String now = row.get(BddFieldConstants.GlobalInfo.NOW);

        Long nbGameSavesLong = Long.parseLong(nbGameSaves);
        Long nbUsersLong = Long.parseLong(nbUsers);

        Instant nowInstant = Instant.parse(now);

        return new GlobalInfo(nowInstant, nbGameSavesLong, nbUsersLong);
    }

    /**
     * Maps a row from a BDD table to a UserRefreshLoginRequest
     *
     * @param row row from BDD table
     * @return UserRefreshLoginRequest
     */
    public static UserRefreshLoginRequest mapToUserRefreshLoginRequest(Map<String, String> row) {
        String refreshToken = row.get(BddFieldConstants.UserRefreshLoginRequest.REFRESH_TOKEN);
        return new UserRefreshLoginRequest(refreshToken);
    }
}
