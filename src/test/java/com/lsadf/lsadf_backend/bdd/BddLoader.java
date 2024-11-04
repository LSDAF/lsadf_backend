package com.lsadf.lsadf_backend.bdd;

import com.lsadf.lsadf_backend.bdd.config.LsadfBackendBddTestsConfiguration;
import com.lsadf.lsadf_backend.cache.Cache;
import com.lsadf.lsadf_backend.cache.HistoCache;
import com.lsadf.lsadf_backend.configurations.LsadfBackendConfiguration;
import com.lsadf.lsadf_backend.controllers.*;
import com.lsadf.lsadf_backend.controllers.admin.*;
import com.lsadf.lsadf_backend.controllers.admin.impl.*;
import com.lsadf.lsadf_backend.controllers.advices.DynamicJsonViewAdvice;
import com.lsadf.lsadf_backend.controllers.advices.GlobalExceptionHandler;
import com.lsadf.lsadf_backend.controllers.impl.*;
import com.lsadf.lsadf_backend.entities.GameSaveEntity;
import com.lsadf.lsadf_backend.mappers.Mapper;
import com.lsadf.lsadf_backend.models.*;
import com.lsadf.lsadf_backend.properties.CacheExpirationProperties;
import com.lsadf.lsadf_backend.properties.KeycloakProperties;
import com.lsadf.lsadf_backend.repositories.CurrencyRepository;
import com.lsadf.lsadf_backend.repositories.GameSaveRepository;
import com.lsadf.lsadf_backend.repositories.StageRepository;
import com.lsadf.lsadf_backend.responses.GenericResponse;
import com.lsadf.lsadf_backend.services.*;
import dasniko.testcontainers.keycloak.KeycloakContainer;
import io.cucumber.spring.CucumberContextConfiguration;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.extension.ExtendWith;
import org.keycloak.admin.client.Keycloak;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.security.oauth2.client.reactive.ReactiveOAuth2ClientAutoConfiguration;
import org.springframework.boot.autoconfigure.security.oauth2.resource.reactive.ReactiveOAuth2ResourceServerAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.Network;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.Stack;

import static com.lsadf.lsadf_backend.constants.BeanConstants.Cache.GAME_SAVE_OWNERSHIP_CACHE;

/**
 * BDD Loader class for the Cucumber tests
 */
@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = {
        LsadfBackendConfiguration.class,
        LsadfBackendBddTestsConfiguration.class,
        GlobalExceptionHandler.class,
        DynamicJsonViewAdvice.class,
        // Precise both the interface and the implementation to avoid ambiguity & errors for testing
        AuthController.class,
        AuthControllerImpl.class,
        GameSaveController.class,
        GameSaveControllerImpl.class,
        UserController.class,
        UserControllerImpl.class,
        CurrencyController.class,
        CurrencyControllerImpl.class,
        StageController.class,
        StageControllerImpl.class,
        OAuth2Controller.class,
        OAuth2ControllerImpl.class,
        // ADMIN
        AdminUserController.class,
        AdminUserControllerImpl.class,
        AdminGameSaveController.class,
        AdminGameSaveControllerImpl.class,
        AdminSearchController.class,
        AdminSearchControllerImpl.class,
        AdminGlobalInfoController.class,
        AdminGlobalInfoControllerImpl.class,
        AdminCacheController.class,
        AdminCacheControllerImpl.class,
})
@ExtendWith(MockitoExtension.class)
@EnableConfigurationProperties
@CucumberContextConfiguration
@EnableJpaRepositories(basePackages = "com.lsadf.lsadf_backend.repositories")
@EntityScan(basePackages = "com.lsadf.lsadf_backend.entities")
@EnableAutoConfiguration(exclude = {
        SecurityAutoConfiguration.class,
        ReactiveOAuth2ResourceServerAutoConfiguration.class,
        ReactiveOAuth2ClientAutoConfiguration.class,
})
@ActiveProfiles("test")
@Testcontainers
public class BddLoader {

    // Caches

    @Autowired
    @Qualifier(GAME_SAVE_OWNERSHIP_CACHE)
    protected Cache<String> gameSaveOwnershipCache;

    @Autowired
    protected HistoCache<Currency> currencyCache;

    @Autowired
    protected HistoCache<Stage> stageCache;

    // Repositories
    @Autowired
    protected CurrencyRepository currencyRepository;

    @Autowired
    protected StageRepository stageRepository;

    @Autowired
    protected GameSaveRepository gameSaveRepository;

    @Autowired
    protected Mapper mapper;

    @Autowired
    protected PasswordEncoder passwordEncoder;

    // Services

    @Autowired
    protected UserService userService;

    @Autowired
    protected Keycloak keycloakAdminClient;

    @Autowired
    protected ClockService clockService;

    @Autowired
    protected CurrencyService currencyService;

    @Autowired
    protected StageService stageService;

    @Autowired
    protected CacheService redisCacheService;

    @Autowired
    protected CacheService localCacheService;

    @Autowired
    protected CacheFlushService cacheFlushService;


    @Autowired
    protected GameSaveService gameSaveService;


    // BDD Specific Stacks & Maps

    @Autowired
    protected Stack<List<GameSave>> gameSaveListStack;

    @Autowired
    protected Stack<List<GameSaveEntity>> gameSaveEntityListStack;

    @Autowired
    protected Stack<List<User>> userListStack;

    @Autowired
    protected Stack<GlobalInfo> globalInfoStack;

    @Autowired
    protected Stack<MimeMessage> mimeMessageStack;

    @Autowired
    protected Stack<Currency> currencyStack;

    @Autowired
    protected Stack<Stage> stageStack;

    @Autowired
    protected Stack<List<UserInfo>> userInfoListStack;

    @Autowired
    protected Stack<Exception> exceptionStack;

    @Autowired
    protected Stack<GenericResponse<?>> responseStack;

    @Autowired
    protected Stack<Boolean> booleanStack;

    @Autowired
    protected Stack<JwtAuthentication> jwtAuthenticationStack;

    // Properties
    @Autowired
    protected CacheExpirationProperties cacheExpirationProperties;

    @Autowired
    protected KeycloakProperties keycloakProperties;

    // Controller testing properties

    @LocalServerPort
    protected int serverPort;

    @Autowired
    protected TestRestTemplate testRestTemplate;

    private static final Network testcontainersNetwork = Network.newNetwork();

    @Container
    private static final KeycloakContainer keycloak = new KeycloakContainer("quay.io/keycloak/keycloak:26.0.0")
            .withRealmImportFile("keycloak/bdd_realm-export.json")
            .withNetwork(testcontainersNetwork)
            .withNetworkAliases("keycloak-bdd");

    @Container
    private static final PostgreSQLContainer<?> postgreSqlContainer = new PostgreSQLContainer<>("postgres:16.0-alpine")
            .withDatabaseName("bdd")
            .withUsername("bdd")
            .withPassword("bdd")
            .withNetwork(testcontainersNetwork)
            .withNetworkAliases("postgres-bdd");

    static {
        log.info("Start BDD loader...");
        keycloak.start();
        postgreSqlContainer.start();
    }

    @DynamicPropertySource
    static void registerPostgresProperties(DynamicPropertyRegistry registry) {
        String url = postgreSqlContainer.getJdbcUrl();
        String username = postgreSqlContainer.getUsername();
        String password = postgreSqlContainer.getPassword();

        registry.add("db.url", () -> url);
        registry.add("db.username", () -> username);
        registry.add("db.password", () -> password);
    }

    @DynamicPropertySource
    static void registerResourceServerIssuerProperty(DynamicPropertyRegistry registry) {
        registry.add("spring.security.oauth2.resourceserver.jwt.issuer-uri", () -> keycloak.getAuthServerUrl() + "/realms/BDD_REALM");
        registry.add("keycloak.uri", () -> keycloak.getAuthServerUrl());
        registry.add("keycloak.adminUri", () -> keycloak.getAuthServerUrl());
    }

}
