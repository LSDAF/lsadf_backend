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
package com.lsadf.core.infra.valkey.cache.config;

import com.lsadf.core.application.game.save.characteristics.CharacteristicsCachePort;
import com.lsadf.core.application.game.save.characteristics.CharacteristicsService;
import com.lsadf.core.application.game.save.currency.CurrencyCachePort;
import com.lsadf.core.application.game.save.currency.CurrencyService;
import com.lsadf.core.application.game.save.metadata.GameMetadataCachePort;
import com.lsadf.core.application.game.save.stage.StageCachePort;
import com.lsadf.core.application.game.save.stage.StageService;
import com.lsadf.core.domain.game.inventory.Inventory;
import com.lsadf.core.domain.game.save.characteristics.Characteristics;
import com.lsadf.core.domain.game.save.currency.Currency;
import com.lsadf.core.domain.game.save.metadata.GameMetadata;
import com.lsadf.core.domain.game.save.stage.Stage;
import com.lsadf.core.infra.valkey.ValkeyConstants;
import com.lsadf.core.infra.valkey.cache.config.properties.CacheExpirationProperties;
import com.lsadf.core.infra.valkey.cache.config.properties.ValkeyProperties;
import com.lsadf.core.infra.valkey.cache.flush.CacheFlushService;
import com.lsadf.core.infra.valkey.cache.flush.impl.RedisCacheFlushServiceImpl;
import com.lsadf.core.infra.valkey.cache.impl.save.characteristics.CharacteristicsCacheAdapter;
import com.lsadf.core.infra.valkey.cache.impl.save.currency.CurrencyCacheAdapter;
import com.lsadf.core.infra.valkey.cache.impl.save.metadata.GameMetadataCacheAdapter;
import com.lsadf.core.infra.valkey.cache.impl.save.stage.StageCacheAdapter;
import com.lsadf.core.infra.valkey.cache.listener.ValkeyRepositoryKeyExpirationListener;
import com.lsadf.core.infra.valkey.cache.manager.CacheManager;
import com.lsadf.core.infra.valkey.cache.manager.impl.ValkeyCacheManagerImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@ConditionalOnProperty(prefix = "cache.redis", name = "enabled", havingValue = "true")
@Import(ValkeyCacheRepositoryConfiguration.class)
public class ValkeyCacheConfiguration {

  @Bean
  public RedisTemplate<String, String> redisTemplate(
      RedisConnectionFactory redisConnectionFactory) {
    RedisTemplate<String, String> template = new StringRedisTemplate();
    template.setConnectionFactory(redisConnectionFactory);
    return template;
  }

  @Bean
  public RedisTemplate<String, Long> redisLongTemplate(
      RedisConnectionFactory redisConnectionFactory) {
    RedisTemplate<String, Long> template = new RedisTemplate<>();
    template.setConnectionFactory(redisConnectionFactory);
    template.setKeySerializer(new StringRedisSerializer());
    template.setValueSerializer(new GenericToStringSerializer<>(Long.class));
    return template;
  }

  @Bean
  public RedisTemplate<String, GameMetadata> gameMetadataRedisTemplate(
      RedisConnectionFactory redisConnectionFactory) {
    RedisTemplate<String, GameMetadata> template = new RedisTemplate<>();
    template.setConnectionFactory(redisConnectionFactory);
    template.setKeySerializer(new StringRedisSerializer());
    template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
    template.setHashKeySerializer(new StringRedisSerializer());
    template.setHashValueSerializer(new GenericToStringSerializer<>(GameMetadata.class));
    return template;
  }

  @Bean
  public RedisTemplate<String, Characteristics> characteristicsRedisTemplate(
      RedisConnectionFactory redisConnectionFactory) {
    RedisTemplate<String, Characteristics> template = new RedisTemplate<>();
    template.setConnectionFactory(redisConnectionFactory);
    template.setKeySerializer(new StringRedisSerializer());
    template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
    template.setHashKeySerializer(new StringRedisSerializer());
    template.setHashValueSerializer(new GenericToStringSerializer<>(Characteristics.class));
    return template;
  }

  @Bean
  public RedisTemplate<String, Currency> currencyRedisTemplate(
      RedisConnectionFactory redisConnectionFactory) {
    RedisTemplate<String, Currency> template = new RedisTemplate<>();
    template.setConnectionFactory(redisConnectionFactory);
    template.setKeySerializer(new StringRedisSerializer());
    template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
    template.setHashKeySerializer(new StringRedisSerializer());
    template.setHashValueSerializer(new GenericToStringSerializer<>(Currency.class));
    return template;
  }

  @Bean
  public RedisTemplate<String, Inventory> inventoryRedisTemplate(
      RedisConnectionFactory redisConnectionFactory) {
    RedisTemplate<String, Inventory> template = new RedisTemplate<>();
    template.setConnectionFactory(redisConnectionFactory);
    template.setKeySerializer(new StringRedisSerializer());
    template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
    template.setHashKeySerializer(new StringRedisSerializer());
    template.setHashValueSerializer(new GenericToStringSerializer<>(Inventory.class));
    return template;
  }

  @Bean
  public RedisTemplate<String, Stage> stageRedisTemplate(
      RedisConnectionFactory redisConnectionFactory) {
    RedisTemplate<String, Stage> template = new RedisTemplate<>();
    template.setConnectionFactory(redisConnectionFactory);
    template.setKeySerializer(new StringRedisSerializer());
    template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
    template.setHashKeySerializer(new StringRedisSerializer());
    template.setHashValueSerializer(new GenericToStringSerializer<>(Stage.class));
    return template;
  }

  @Bean
  public CacheManager redisCacheService(
      GameMetadataCachePort gameMetadataCache,
      CharacteristicsCachePort characteristicsCache,
      CurrencyCachePort currencyCache,
      StageCachePort stageCache,
      ValkeyProperties valkeyProperties) {
    return new ValkeyCacheManagerImpl(
        gameMetadataCache, characteristicsCache, currencyCache, stageCache, valkeyProperties);
  }

  @Bean
  public LettuceConnectionFactory redisConnectionFactory(ValkeyProperties valkeyProperties) {
    RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration();
    configuration.setHostName(valkeyProperties.getHost());
    configuration.setPort(valkeyProperties.getPort());
    configuration.setDatabase(valkeyProperties.getDatabase());
    configuration.setPassword(valkeyProperties.getPassword());
    return new LettuceConnectionFactory(configuration);
  }

  @Bean
  public RedisMessageListenerContainer keyExpirationListenerContainer(
      RedisConnectionFactory connectionFactory) {
    RedisMessageListenerContainer container = new RedisMessageListenerContainer();
    container.setConnectionFactory(connectionFactory);
    // container.addMessageListener(
    //  valkeyKeyExpirationListener, new PatternTopic("__keyevent@0__:expired"));
    return container;
  }

  @Bean
  public ValkeyRepositoryKeyExpirationListener valkeyRepositoryKeyExpirationListener(
      CurrencyService currencyService,
      CharacteristicsService characteristicsService,
      StageService stageService) {
    return new ValkeyRepositoryKeyExpirationListener(
        stageService, characteristicsService, currencyService);
  }

  //  @Bean
  //  MessageListenerAdapter messageListener(ValkeyKeyExpirationListener
  // valkeyKeyExpirationListener) {
  //    return new MessageListenerAdapter(valkeyKeyExpirationListener);
  //  }

  //  @Bean
  //  public ValkeyKeyExpirationListener redisKeyExpirationListener(
  //      CharacteristicsService characteristicsService,
  //      CurrencyService currencyService,
  //      StageService stageService,
  //      RedisTemplate<String, Characteristics> characteristicsRedisTemplate,
  //      RedisTemplate<String, Currency> currencyRedisTemplate,
  //      RedisTemplate<String, Stage> stageRedisTemplate) {
  //    return new ValkeyKeyExpirationListener(
  //        characteristicsService,
  //        currencyService,
  //        stageService,
  //        characteristicsRedisTemplate,
  //        currencyRedisTemplate,
  //        stageRedisTemplate);
  //  }

  @Bean
  public CacheFlushService cacheFlushService(
      CharacteristicsService characteristicsService,
      CurrencyService currencyService,
      StageService stageService,
      CharacteristicsCachePort characteristicsCachePort,
      CurrencyCachePort currencyCachePort,
      StageCachePort stageCachePort) {
    return new RedisCacheFlushServiceImpl(
        characteristicsService,
        currencyService,
        stageService,
        characteristicsCachePort,
        currencyCachePort,
        stageCachePort);
  }

  @Bean
  @ConditionalOnMissingBean
  public CharacteristicsCachePort characteristicsCachePort(
      RedisTemplate<String, Characteristics> characteristicsRedisTemplate,
      CacheExpirationProperties cacheExpirationProperties) {
    return new CharacteristicsCacheAdapter(
        characteristicsRedisTemplate,
        ValkeyConstants.CHARACTERISTICS,
        cacheExpirationProperties.getCharacteristicsExpirationSeconds());
  }

  @Bean
  @ConditionalOnMissingBean
  public CurrencyCachePort currencyCachePort(
      RedisTemplate<String, Currency> currencyRedisTemplate,
      CacheExpirationProperties cacheExpirationProperties) {
    return new CurrencyCacheAdapter(
        currencyRedisTemplate,
        ValkeyConstants.CURRENCY,
        cacheExpirationProperties.getCurrencyExpirationSeconds());
  }

  @Bean
  @ConditionalOnMissingBean
  public StageCachePort stageCachePort(
      RedisTemplate<String, Stage> stageRedisTemplate,
      CacheExpirationProperties cacheExpirationProperties) {
    return new StageCacheAdapter(
        stageRedisTemplate,
        ValkeyConstants.STAGE,
        cacheExpirationProperties.getStageExpirationSeconds());
  }

  @Bean
  @ConditionalOnMissingBean
  public GameMetadataCachePort gameMetadataCachePort(
      RedisTemplate<String, GameMetadata> gameMetadataRedisTemplate,
      CacheExpirationProperties cacheExpirationProperties) {
    return new GameMetadataCacheAdapter(
        gameMetadataRedisTemplate,
        ValkeyConstants.GAME_METADATA,
        cacheExpirationProperties.getGameMetadataExpirationSeconds());
  }
}
