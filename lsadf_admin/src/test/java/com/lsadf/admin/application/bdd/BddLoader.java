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
package com.lsadf.admin.application.bdd;

import static com.lsadf.core.infra.config.BeanConstants.Cache.GAME_SAVE_OWNERSHIP_CACHE;

import com.lsadf.admin.application.auth.AdminAuthController;
import com.lsadf.admin.application.auth.AdminAuthControllerImpl;
import com.lsadf.admin.application.bdd.config.LsadfAdminBddConfiguration;
import com.lsadf.admin.application.cache.AdminCacheController;
import com.lsadf.admin.application.cache.AdminCacheControllerImpl;
import com.lsadf.admin.application.game.AdminGameSaveController;
import com.lsadf.admin.application.game.AdminGameSaveControllerImpl;
import com.lsadf.admin.application.game.inventory.AdminInventoryController;
import com.lsadf.admin.application.game.inventory.AdminInventoryControllerImpl;
import com.lsadf.admin.application.info.AdminGlobalInfoController;
import com.lsadf.admin.application.info.AdminGlobalInfoControllerImpl;
import com.lsadf.admin.application.search.AdminSearchController;
import com.lsadf.admin.application.search.AdminSearchControllerImpl;
import com.lsadf.admin.application.user.AdminUserController;
import com.lsadf.admin.application.user.AdminUserControllerImpl;
import com.lsadf.admin.config.LsadfAdminConfiguration;
import com.lsadf.core.application.game.characteristics.CharacteristicsService;
import com.lsadf.core.application.game.currency.CurrencyService;
import com.lsadf.core.application.game.game_save.GameSaveService;
import com.lsadf.core.application.game.inventory.InventoryService;
import com.lsadf.core.application.game.stage.StageService;
import com.lsadf.core.application.user.UserService;
import com.lsadf.core.domain.game.GameSave;
import com.lsadf.core.domain.game.characteristics.Characteristics;
import com.lsadf.core.domain.game.currency.Currency;
import com.lsadf.core.domain.game.stage.Stage;
import com.lsadf.core.domain.user.User;
import com.lsadf.core.infra.cache.Cache;
import com.lsadf.core.infra.cache.HistoCache;
import com.lsadf.core.infra.cache.flush.CacheFlushService;
import com.lsadf.core.infra.cache.properties.CacheExpirationProperties;
import com.lsadf.core.infra.cache.service.CacheService;
import com.lsadf.core.infra.clock.ClockService;
import com.lsadf.core.infra.persistence.game.characteristics.CharacteristicsRepository;
import com.lsadf.core.infra.persistence.game.currency.CurrencyRepository;
import com.lsadf.core.infra.persistence.game.game_save.GameSaveRepository;
import com.lsadf.core.infra.persistence.game.inventory.InventoryEntity;
import com.lsadf.core.infra.persistence.game.inventory.InventoryRepository;
import com.lsadf.core.infra.persistence.game.stage.StageRepository;
import com.lsadf.core.infra.web.client.keycloak.response.JwtAuthenticationResponse;
import com.lsadf.core.infra.web.config.keycloak.KeycloakProperties;
import com.lsadf.core.infra.web.controllers.advices.DynamicJsonViewAdvice;
import com.lsadf.core.infra.web.controllers.advices.GlobalExceptionHandler;
import com.lsadf.core.infra.web.responses.ApiResponse;
import com.lsadf.core.infra.web.responses.game.game_save.GameSaveResponse;
import com.lsadf.core.infra.web.responses.game.inventory.ItemResponse;
import com.lsadf.core.infra.web.responses.info.GlobalInfoResponse;
import dasniko.testcontainers.keycloak.KeycloakContainer;
import io.cucumber.spring.CucumberContextConfiguration;
import jakarta.mail.internet.MimeMessage;
import java.util.List;
import java.util.Set;
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
      GlobalExceptionHandler.class,
      DynamicJsonViewAdvice.class,
      // Precise both the interface and the implementation to avoid ambiguity & errors for testing
      LsadfAdminConfiguration.class,
      LsadfAdminBddConfiguration.class,
      AdminAuthController.class,
      AdminAuthControllerImpl.class,
      AdminInventoryController.class,
      AdminInventoryControllerImpl.class,
      AdminCacheController.class,
      AdminCacheControllerImpl.class,
      AdminUserController.class,
      AdminUserControllerImpl.class,
      AdminGameSaveController.class,
      AdminGameSaveControllerImpl.class,
      AdminSearchController.class,
      AdminSearchControllerImpl.class,
      AdminGlobalInfoController.class,
      AdminGlobalInfoControllerImpl.class
    })
@ExtendWith(MockitoExtension.class)
@EnableConfigurationProperties
@CucumberContextConfiguration
@EnableJpaRepositories(basePackages = "com.lsadf.core.infra.persistence")
@EntityScan(basePackages = "com.lsadf.core.infra.persistence")
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

  @Autowired protected Stack<Set<ItemResponse>> itemResponseSetStack;

  @Autowired protected Stack<List<GameSave>> gameSaveListStack;

  @Autowired protected Stack<List<GameSaveResponse>> gameSaveResponseListStack;

  @Autowired protected Stack<List<User>> userListStack;

  @Autowired protected Stack<MimeMessage> mimeMessageStack;

  @Autowired protected Stack<Characteristics> characteristicsStack;

  @Autowired protected Stack<Currency> currencyStack;

  @Autowired protected Stack<InventoryEntity> inventoryEntityStack;

  @Autowired protected Stack<ItemResponse> itemStack;

  @Autowired protected Stack<Stage> stageStack;

  @Autowired protected Stack<GlobalInfoResponse> globalInfoResponseStack;

  @Autowired protected Stack<Exception> exceptionStack;

  @Autowired protected Stack<ApiResponse<?>> responseStack;

  @Autowired protected Stack<Boolean> booleanStack;

  @Autowired protected Stack<JwtAuthenticationResponse> jwtAuthenticationResponseStack;

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
    registry.add("keycloak.url", keycloak::getAuthServerUrl);
    registry.add("keycloak.adminUrl", keycloak::getAuthServerUrl);
  }
}
