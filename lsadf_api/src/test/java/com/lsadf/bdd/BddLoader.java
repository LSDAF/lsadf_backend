package com.lsadf.bdd;

import static com.lsadf.core.constants.BeanConstants.Cache.GAME_SAVE_OWNERSHIP_CACHE;

import com.lsadf.bdd.config.LsadfBddTestsConfiguration;
import com.lsadf.configurations.LsadfConfiguration;
import com.lsadf.controllers.*;
import com.lsadf.controllers.impl.*;
import com.lsadf.core.cache.Cache;
import com.lsadf.core.cache.HistoCache;
import com.lsadf.core.controllers.advices.DynamicJsonViewAdvice;
import com.lsadf.core.controllers.advices.GlobalExceptionHandler;
import com.lsadf.core.entities.GameSaveEntity;
import com.lsadf.core.entities.InventoryEntity;
import com.lsadf.core.mappers.Mapper;
import com.lsadf.core.models.*;
import com.lsadf.core.properties.CacheExpirationProperties;
import com.lsadf.core.properties.KeycloakProperties;
import com.lsadf.core.repositories.*;
import com.lsadf.core.responses.GenericResponse;
import com.lsadf.core.services.*;
import dasniko.testcontainers.keycloak.KeycloakContainer;
import io.cucumber.spring.CucumberContextConfiguration;
import jakarta.mail.internet.MimeMessage;
import java.util.List;
import java.util.Stack;
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

/** BDD Loader class for the Cucumber tests */
@Slf4j
@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    classes = {
      LsadfConfiguration.class,
      LsadfBddTestsConfiguration.class,
      GlobalExceptionHandler.class,
      DynamicJsonViewAdvice.class,
      // Precise both the interface and the implementation to avoid ambiguity & errors for testing
      AuthController.class,
      AuthControllerImpl.class,
      GameSaveController.class,
      GameSaveControllerImpl.class,
      UserController.class,
      UserControllerImpl.class,
      CharacteristicsController.class,
      CharacteristicsControllerImpl.class,
      CurrencyController.class,
      CurrencyControllerImpl.class,
      InventoryController.class,
      InventoryControllerImpl.class,
      StageController.class,
      StageControllerImpl.class,
      OAuth2Controller.class,
      OAuth2ControllerImpl.class,
    })
@ExtendWith(MockitoExtension.class)
@EnableConfigurationProperties
@CucumberContextConfiguration
@EnableJpaRepositories(basePackages = "com.lsadf.core.repositories")
@EntityScan(basePackages = "com.lsadf.core.entities")
@EnableAutoConfiguration(
    exclude = {
      SecurityAutoConfiguration.class,
      ReactiveOAuth2ResourceServerAutoConfiguration.class,
      ReactiveOAuth2ClientAutoConfiguration.class,
    })
@ActiveProfiles("bdd")
@Testcontainers
public class BddLoader {

  // Caches

  @Autowired
  @Qualifier(GAME_SAVE_OWNERSHIP_CACHE)
  protected Cache<String> gameSaveOwnershipCache;

  @Autowired protected HistoCache<Characteristics> characteristicsCache;

  @Autowired protected HistoCache<Currency> currencyCache;

  @Autowired protected HistoCache<Stage> stageCache;

  // Repositories
  @Autowired protected CharacteristicsRepository characteristicsRepository;

  @Autowired protected CurrencyRepository currencyRepository;

  @Autowired protected InventoryRepository inventoryRepository;

  @Autowired protected StageRepository stageRepository;

  @Autowired protected GameSaveRepository gameSaveRepository;

  @Autowired protected Mapper mapper;

  @Autowired protected PasswordEncoder passwordEncoder;

  // Services

  @Autowired protected UserService userService;

  @Autowired protected Keycloak keycloakAdminClient;

  @Autowired protected ClockService clockService;

  @Autowired protected CharacteristicsService characteristicsService;

  @Autowired protected CurrencyService currencyService;

  @Autowired protected InventoryService inventoryService;

  @Autowired protected StageService stageService;

  @Autowired protected CacheService redisCacheService;

  @Autowired protected CacheService localCacheService;

  @Autowired protected CacheFlushService cacheFlushService;

  @Autowired protected GameSaveService gameSaveService;

  // BDD Specific Stacks & Maps

  @Autowired protected Stack<List<GameSave>> gameSaveListStack;

  @Autowired protected Stack<List<GameSaveEntity>> gameSaveEntityListStack;

  @Autowired protected Stack<List<User>> userListStack;

  @Autowired protected Stack<GlobalInfo> globalInfoStack;

  @Autowired protected Stack<MimeMessage> mimeMessageStack;

  @Autowired protected Stack<Characteristics> characteristicsStack;

  @Autowired protected Stack<Currency> currencyStack;

  @Autowired protected Stack<Inventory> inventoryStack;

  @Autowired protected Stack<InventoryEntity> inventoryEntityStack;

  @Autowired protected Stack<Item> itemStack;

  @Autowired protected Stack<Stage> stageStack;

  @Autowired protected Stack<List<UserInfo>> userInfoListStack;

  @Autowired protected Stack<Exception> exceptionStack;

  @Autowired protected Stack<GenericResponse<?>> responseStack;

  @Autowired protected Stack<Boolean> booleanStack;

  @Autowired protected Stack<JwtAuthentication> jwtAuthenticationStack;

  // Properties
  @Autowired protected CacheExpirationProperties cacheExpirationProperties;

  @Autowired protected KeycloakProperties keycloakProperties;

  // Controller testing properties

  @LocalServerPort protected int serverPort;

  @Autowired protected TestRestTemplate testRestTemplate;

  private static final Network testcontainersNetwork = Network.newNetwork();

  @Container
  private static final KeycloakContainer keycloak =
      new KeycloakContainer("quay.io/keycloak/keycloak:26.0.0")
          .withRealmImportFile("keycloak/bdd_realm-export.json")
          .withNetwork(testcontainersNetwork)
          .withNetworkAliases("keycloak-bdd");

  @Container
  private static final PostgreSQLContainer<?> postgreSqlContainer =
      new PostgreSQLContainer<>("postgres:16.0-alpine")
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
    registry.add(
        "spring.security.oauth2.resourceserver.jwt.issuer-uri",
        () -> keycloak.getAuthServerUrl() + "/realms/BDD_REALM");
    registry.add("keycloak.url", () -> keycloak.getAuthServerUrl());
    registry.add("keycloak.adminUrl", () -> keycloak.getAuthServerUrl());
  }
}
