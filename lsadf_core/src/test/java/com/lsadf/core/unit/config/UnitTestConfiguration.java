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
package com.lsadf.core.unit.config;

import com.lsadf.core.application.cache.CacheManager;
import com.lsadf.core.application.game.inventory.InventoryService;
import com.lsadf.core.application.game.save.GameSaveService;
import com.lsadf.core.application.game.save.characteristics.CharacteristicsEventPublisherPort;
import com.lsadf.core.application.game.save.characteristics.CharacteristicsService;
import com.lsadf.core.application.game.save.currency.CurrencyEventPublisherPort;
import com.lsadf.core.application.game.save.currency.CurrencyService;
import com.lsadf.core.application.game.save.metadata.GameMetadataService;
import com.lsadf.core.application.game.save.stage.StageEventPublisherPort;
import com.lsadf.core.application.game.save.stage.StageService;
import com.lsadf.core.application.info.GlobalInfoService;
import com.lsadf.core.application.search.SearchService;
import com.lsadf.core.application.user.UserService;
import com.lsadf.core.infra.valkey.cache.flush.CacheFlushService;
import com.lsadf.core.infra.web.client.keycloak.KeycloakClient;
import javax.sql.DataSource;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.stream.Subscription;

@TestConfiguration
@Import({UnitCacheConfiguration.class})
public class UnitTestConfiguration {

  @MockBean private KeycloakClient keycloakClient;

  @MockBean private RedisMessageListenerContainer redisMessageListenerContainer;

  @MockBean private LettuceConnectionFactory lettuceConnectionFactory;

  @MockBean private StageService stageService;

  @MockBean private UserService userService;

  @MockBean private SearchService searchService;

  @MockBean private GameSaveService gameSaveService;

  @MockBean private GlobalInfoService globalInfoService;

  @MockBean private CharacteristicsService characteristicsService;

  @MockBean private GameMetadataService gameMetadataService;

  @MockBean private CurrencyService currencyService;

  @MockBean private InventoryService inventoryService;

  @MockBean private CacheManager redisCacheManager;

  @MockBean private CacheFlushService cacheFlushService;

  @MockBean private DataSource dataSource;

  @MockBean public CharacteristicsEventPublisherPort characteristicsEventPublisherPort;

  @MockBean public StageEventPublisherPort stageEventPublisherPort;

  @MockBean public CurrencyEventPublisherPort currencyEventPublisherPort;

  @MockBean public Subscription subscription;
}
