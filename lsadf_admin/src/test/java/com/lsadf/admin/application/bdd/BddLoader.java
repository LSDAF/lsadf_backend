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

import com.lsadf.admin.application.auth.AdminAuthController;
import com.lsadf.admin.application.auth.AdminAuthControllerImpl;
import com.lsadf.admin.application.auth.OAuth2Controller;
import com.lsadf.admin.application.auth.OAuth2ControllerImpl;
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
import com.lsadf.core.application.game.inventory.InventoryService;
import com.lsadf.core.application.game.save.GameSaveService;
import com.lsadf.core.application.game.save.characteristics.CharacteristicsCachePort;
import com.lsadf.core.application.game.save.characteristics.CharacteristicsService;
import com.lsadf.core.application.game.save.currency.CurrencyCachePort;
import com.lsadf.core.application.game.save.currency.CurrencyService;
import com.lsadf.core.application.game.save.metadata.GameMetadataCachePort;
import com.lsadf.core.application.game.save.stage.StageCachePort;
import com.lsadf.core.application.game.save.stage.StageService;
import com.lsadf.core.application.user.UserService;
import com.lsadf.core.domain.game.save.GameSave;
import com.lsadf.core.domain.game.save.characteristics.Characteristics;
import com.lsadf.core.domain.game.save.currency.Currency;
import com.lsadf.core.domain.game.save.stage.Stage;
import com.lsadf.core.infra.clock.ClockService;
import com.lsadf.core.infra.persistence.impl.game.inventory.AdditionalItemStatsRepository;
import com.lsadf.core.infra.persistence.impl.game.inventory.ItemRepository;
import com.lsadf.core.infra.persistence.impl.game.save.characteristics.CharacteristicsRepository;
import com.lsadf.core.infra.persistence.impl.game.save.currency.CurrencyRepository;
import com.lsadf.core.infra.persistence.impl.game.save.metadata.GameMetadataRepository;
import com.lsadf.core.infra.persistence.impl.game.save.stage.StageRepository;
import com.lsadf.core.infra.persistence.impl.view.GameSaveViewRepository;
import com.lsadf.core.infra.valkey.cache.flush.CacheFlushService;
import com.lsadf.core.infra.valkey.cache.manager.CacheManager;
import com.lsadf.core.infra.valkey.config.properties.ValkeyCacheExpirationProperties;
import com.lsadf.core.infra.web.config.keycloak.properties.KeycloakProperties;
import com.lsadf.core.infra.web.controller.advice.GlobalExceptionHandler;
import com.lsadf.core.infra.web.response.ApiResponse;
import com.lsadf.core.infra.web.response.game.inventory.ItemResponse;
import com.lsadf.core.infra.web.response.game.save.GameSaveResponse;
import com.lsadf.core.infra.web.response.info.GlobalInfoResponse;
import com.lsadf.core.infra.web.response.jwt.JwtAuthenticationResponse;
import com.lsadf.core.infra.web.response.user.UserResponse;
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
      GlobalExceptionHandler.class,
      // Precise both the interface and the implementation to avoid ambiguity & errors for testing
      LsadfAdminConfiguration.class,
      LsadfAdminBddConfiguration.class,
      AdminAuthController.class,
      AdminAuthControllerImpl.class,
      OAuth2Controller.class,
      OAuth2ControllerImpl.class,
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
@EnableRedisRepositories(
    basePackages = "com.lsadf.core.infra.valkey.cache",
    shadowCopy = RedisKeyValueAdapter.ShadowCopy.OFF,
    enableKeyspaceEvents = RedisKeyValueAdapter.EnableKeyspaceEvents.OFF)
@EnableJdbcRepositories(basePackages = "com.lsadf.core.infra.persistence")
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

  @Autowired protected GameMetadataCachePort gameMetadataCache;

  @Autowired protected CharacteristicsCachePort characteristicsCache;

  @Autowired protected CurrencyCachePort currencyCache;

  @Autowired protected StageCachePort stageCache;

  // Repositories
  @Autowired protected CharacteristicsRepository characteristicsRepository;

  @Autowired protected CurrencyRepository currencyRepository;

  @Autowired protected ItemRepository itemRepository;

  @Autowired protected StageRepository stageRepository;

  @Autowired protected GameMetadataRepository gameMetadataRepository;

  @Autowired protected GameSaveViewRepository gameSaveViewRepository;

  @Autowired protected AdditionalItemStatsRepository additionalItemStatsRepository;

  @Autowired protected PasswordEncoder passwordEncoder;

  // Services

  @Autowired protected UserService userService;

  @Autowired protected Keycloak keycloakAdminClient;

  @Autowired protected ClockService clockService;

  @Autowired protected CharacteristicsService characteristicsService;

  @Autowired protected CurrencyService currencyService;

  @Autowired protected InventoryService inventoryService;

  @Autowired protected StageService stageService;

  @Autowired protected CacheManager redisCacheManager;

  @Autowired protected CacheManager localCacheManager;

  @Autowired protected CacheFlushService cacheFlushService;

  @Autowired protected GameSaveService gameSaveService;

  // BDD Specific Stacks & Maps

  @Autowired protected Stack<Set<ItemResponse>> itemResponseSetStack;

  @Autowired protected Stack<List<GameSave>> gameSaveListStack;

  @Autowired protected Stack<List<GameSaveResponse>> gameSaveResponseListStack;

  @Autowired protected Stack<List<UserResponse>> userResponseListStack;

  @Autowired protected Stack<MimeMessage> mimeMessageStack;

  @Autowired protected Stack<Characteristics> characteristicsStack;

  @Autowired protected Stack<Currency> currencyStack;

  @Autowired protected Stack<ItemResponse> itemStack;

  @Autowired protected Stack<Stage> stageStack;

  @Autowired protected Stack<GlobalInfoResponse> globalInfoResponseStack;

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
