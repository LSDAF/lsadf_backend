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
package com.lsadf.application.bdd;

import com.lsadf.application.bdd.config.LsadfBddTestsConfiguration;
import com.lsadf.application.controller.auth.AuthController;
import com.lsadf.application.controller.auth.AuthControllerImpl;
import com.lsadf.application.controller.auth.OAuth2Controller;
import com.lsadf.application.controller.auth.OAuth2ControllerImpl;
import com.lsadf.application.controller.game.characteristics.CharacteristicsController;
import com.lsadf.application.controller.game.characteristics.CharacteristicsControllerImpl;
import com.lsadf.application.controller.game.currency.CurrencyController;
import com.lsadf.application.controller.game.currency.CurrencyControllerImpl;
import com.lsadf.application.controller.game.game_save.GameSaveController;
import com.lsadf.application.controller.game.game_save.GameSaveControllerImpl;
import com.lsadf.application.controller.game.inventory.InventoryController;
import com.lsadf.application.controller.game.inventory.InventoryControllerImpl;
import com.lsadf.application.controller.game.stage.StageController;
import com.lsadf.application.controller.game.stage.StageControllerImpl;
import com.lsadf.application.controller.user.UserController;
import com.lsadf.application.controller.user.UserControllerImpl;
import com.lsadf.config.LsadfConfiguration;
import com.lsadf.core.application.cache.CacheManager;
import com.lsadf.core.application.clock.ClockService;
import com.lsadf.core.application.game.inventory.InventoryService;
import com.lsadf.core.application.game.save.GameSaveService;
import com.lsadf.core.application.game.save.characteristics.CharacteristicsCommandService;
import com.lsadf.core.application.game.save.characteristics.CharacteristicsQueryService;
import com.lsadf.core.application.game.save.currency.CurrencyCommandService;
import com.lsadf.core.application.game.save.currency.CurrencyQueryService;
import com.lsadf.core.application.game.save.stage.StageCommandService;
import com.lsadf.core.application.game.save.stage.StageQueryService;
import com.lsadf.core.application.shared.CachePort;
import com.lsadf.core.application.shared.HistoCachePort;
import com.lsadf.core.application.user.UserService;
import com.lsadf.core.domain.game.save.GameSave;
import com.lsadf.core.domain.game.save.characteristics.Characteristics;
import com.lsadf.core.domain.game.save.currency.Currency;
import com.lsadf.core.domain.game.save.metadata.GameMetadata;
import com.lsadf.core.domain.game.save.stage.Stage;
import com.lsadf.core.domain.user.User;
import com.lsadf.core.domain.user.UserInfo;
import com.lsadf.core.infra.persistence.impl.game.inventory.AdditionalItemStatsRepository;
import com.lsadf.core.infra.persistence.impl.game.inventory.ItemRepository;
import com.lsadf.core.infra.persistence.impl.game.save.characteristics.CharacteristicsRepository;
import com.lsadf.core.infra.persistence.impl.game.save.currency.CurrencyRepository;
import com.lsadf.core.infra.persistence.impl.game.save.metadata.GameMetadataEntity;
import com.lsadf.core.infra.persistence.impl.game.save.metadata.GameMetadataRepository;
import com.lsadf.core.infra.persistence.impl.game.save.stage.StageRepository;
import com.lsadf.core.infra.valkey.cache.flush.CacheFlushService;
import com.lsadf.core.infra.valkey.config.properties.ValkeyCacheExpirationProperties;
import com.lsadf.core.infra.web.config.keycloak.properties.KeycloakProperties;
import com.lsadf.core.infra.web.controller.advice.GlobalExceptionHandler;
import com.lsadf.core.infra.web.dto.response.ApiResponse;
import com.lsadf.core.infra.web.dto.response.game.inventory.ItemResponse;
import com.lsadf.core.infra.web.dto.response.game.save.GameSaveResponse;
import com.lsadf.core.infra.web.dto.response.jwt.JwtAuthenticationResponse;
import dasniko.testcontainers.keycloak.KeycloakContainer;
import io.cucumber.spring.CucumberContextConfiguration;
import java.util.List;
import java.util.Set;
import java.util.Stack;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.extension.ExtendWith;
import org.keycloak.admin.client.Keycloak;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.oauth2.client.reactive.ReactiveOAuth2ClientAutoConfiguration;
import org.springframework.boot.autoconfigure.security.oauth2.resource.reactive.ReactiveOAuth2ResourceServerAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;
import org.springframework.data.redis.core.RedisKeyValueAdapter;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
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
@EnableJdbcRepositories(basePackages = "com.lsadf.core.infra.persistence")
@EnableRedisRepositories(
    basePackages = "com.lsadf.core.infra.valkey.cache",
    shadowCopy = RedisKeyValueAdapter.ShadowCopy.OFF,
    enableKeyspaceEvents = RedisKeyValueAdapter.EnableKeyspaceEvents.OFF)
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

  @Autowired protected CachePort<GameMetadata> gameMetadataCache;

  @Autowired protected HistoCachePort<Characteristics> characteristicsCache;

  @Autowired protected HistoCachePort<Currency> currencyCache;

  @Autowired protected HistoCachePort<Stage> stageCache;

  // Repositories
  @Autowired protected CharacteristicsRepository characteristicsRepository;

  @Autowired protected CurrencyRepository currencyRepository;

  @Autowired protected StageRepository stageRepository;

  @Autowired protected ItemRepository itemRepository;

  @Autowired protected AdditionalItemStatsRepository additionalItemStatsRepository;

  @Autowired protected GameMetadataRepository gameMetadataRepository;

  @Autowired protected PasswordEncoder passwordEncoder;

  // Services

  @Autowired protected UserService userService;

  @Autowired protected Keycloak keycloakAdminClient;

  @Autowired protected ClockService clockService;

  @Autowired protected CharacteristicsQueryService characteristicsQueryService;

  @Autowired protected CharacteristicsCommandService characteristicsCommandService;

  @Autowired protected CurrencyCommandService currencyCommandService;

  @Autowired protected CurrencyQueryService currencyQueryService;

  @Autowired protected InventoryService inventoryService;

  @Autowired protected StageCommandService stageCommandService;

  @Autowired protected StageQueryService stageQueryService;

  @Autowired protected CacheManager redisCacheManager;

  @Autowired protected CacheManager localCacheManager;

  @Autowired protected CacheFlushService cacheFlushService;

  @Autowired protected GameSaveService gameSaveService;

  // BDD Specific Stacks & Maps

  @Autowired protected Stack<List<GameSave>> gameSaveListStack;

  @Autowired protected Stack<List<GameSaveResponse>> gameSaveResponseListStack;

  @Autowired protected Stack<List<GameMetadataEntity>> gameSaveEntityListStack;

  @Autowired protected Stack<List<User>> userListStack;

  @Autowired protected Stack<Characteristics> characteristicsStack;

  @Autowired protected Stack<Currency> currencyStack;

  @Autowired protected Stack<ItemResponse> itemStack;

  @Autowired protected Stack<Set<ItemResponse>> itemResponseSetStack;

  @Autowired protected Stack<Stage> stageStack;

  @Autowired protected Stack<List<UserInfo>> userInfoListStack;

  @Autowired protected Stack<Exception> exceptionStack;

  @Autowired protected Stack<ApiResponse<?>> responseStack;

  @Autowired protected Stack<Boolean> booleanStack;

  @Autowired protected Stack<JwtAuthenticationResponse> jwtAuthenticationResponseStack;

  // Properties
  @Autowired protected ValkeyCacheExpirationProperties valkeyCacheExpirationProperties;

  @Autowired protected KeycloakProperties keycloakProperties;

  // Controller testing properties

  @LocalServerPort protected int serverPort;

  @Autowired protected TestRestTemplate testRestTemplate;

  private static final Network testcontainersNetwork = Network.newNetwork();

  @Container
  private static final KeycloakContainer keycloak =
      new KeycloakContainer("quay.io/keycloak/keycloak:26.0.4")
          .withRealmImportFile("keycloak/bdd_realm-export.json")
          .withNetwork(testcontainersNetwork)
          .withNetworkAliases("keycloak-bdd");

  @Container
  private static final PostgreSQLContainer<?> postgreSqlContainer =
      new PostgreSQLContainer<>("postgres:18.0-alpine")
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
