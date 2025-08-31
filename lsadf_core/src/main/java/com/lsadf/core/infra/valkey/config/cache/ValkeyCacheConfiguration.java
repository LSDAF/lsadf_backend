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
package com.lsadf.core.infra.valkey.config.cache;

import com.lsadf.core.application.game.save.characteristics.CharacteristicsCachePort;
import com.lsadf.core.application.game.save.characteristics.CharacteristicsService;
import com.lsadf.core.application.game.save.currency.CurrencyCachePort;
import com.lsadf.core.application.game.save.currency.CurrencyService;
import com.lsadf.core.application.game.save.metadata.GameMetadataCachePort;
import com.lsadf.core.application.game.save.stage.StageCachePort;
import com.lsadf.core.application.game.save.stage.StageService;
import com.lsadf.core.domain.game.save.characteristics.Characteristics;
import com.lsadf.core.domain.game.save.currency.Currency;
import com.lsadf.core.domain.game.save.metadata.GameMetadata;
import com.lsadf.core.domain.game.save.stage.Stage;
import com.lsadf.core.infra.valkey.ValkeyConstants;
import com.lsadf.core.infra.valkey.cache.adapter.game.save.characteristics.CharacteristicsCacheAdapter;
import com.lsadf.core.infra.valkey.cache.adapter.game.save.currency.CurrencyCacheAdapter;
import com.lsadf.core.infra.valkey.cache.adapter.game.save.metadata.GameMetadataCacheAdapter;
import com.lsadf.core.infra.valkey.cache.adapter.game.save.stage.StageCacheAdapter;
import com.lsadf.core.infra.valkey.cache.flush.CacheFlushService;
import com.lsadf.core.infra.valkey.cache.flush.impl.RedisCacheFlushServiceImpl;
import com.lsadf.core.infra.valkey.cache.listener.ValkeyRepositoryKeyExpirationListener;
import com.lsadf.core.infra.valkey.cache.manager.CacheManager;
import com.lsadf.core.infra.valkey.cache.manager.impl.ValkeyCacheManagerImpl;
import com.lsadf.core.infra.valkey.config.properties.ValkeyCacheExpirationProperties;
import com.lsadf.core.infra.valkey.config.properties.ValkeyProperties;
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
@ConditionalOnProperty(prefix = "valkey.config", name = "enabled", havingValue = "true")
@Import({ValkeyCacheRepositoryConfiguration.class, ValkeyEmbeddedCacheConfiguration.class})
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
    return container;
  }

  @Bean
  public ValkeyRepositoryKeyExpirationListener valkeyRepositoryKeyExpirationListener() {
    return new ValkeyRepositoryKeyExpirationListener();
  }

  @Bean
  public CacheFlushService cacheFlushService(
      CharacteristicsService characteristicsService,
      CurrencyService currencyService,
      StageService stageService,
      CharacteristicsCachePort characteristicsCachePort,
      CurrencyCachePort currencyCachePort,
      StageCachePort stageCachePort,
      RedisTemplate<String, String> redisTemplate) {
    return new RedisCacheFlushServiceImpl(
        characteristicsService,
        currencyService,
        stageService,
        characteristicsCachePort,
        currencyCachePort,
        stageCachePort,
        redisTemplate);
  }

  @Bean
  @ConditionalOnMissingBean
  public CharacteristicsCachePort characteristicsCachePort(
      RedisTemplate<String, Characteristics> characteristicsRedisTemplate,
      ValkeyCacheExpirationProperties valkeyCacheExpirationProperties) {
    return new CharacteristicsCacheAdapter(
        characteristicsRedisTemplate,
        ValkeyConstants.CHARACTERISTICS,
        valkeyCacheExpirationProperties.getCharacteristicsExpirationSeconds());
  }

  @Bean
  @ConditionalOnMissingBean
  public CurrencyCachePort currencyCachePort(
      RedisTemplate<String, Currency> currencyRedisTemplate,
      ValkeyCacheExpirationProperties valkeyCacheExpirationProperties) {
    return new CurrencyCacheAdapter(
        currencyRedisTemplate,
        ValkeyConstants.CURRENCY,
        valkeyCacheExpirationProperties.getCurrencyExpirationSeconds());
  }

  @Bean
  @ConditionalOnMissingBean
  public StageCachePort stageCachePort(
      RedisTemplate<String, Stage> stageRedisTemplate,
      ValkeyCacheExpirationProperties valkeyCacheExpirationProperties) {
    return new StageCacheAdapter(
        stageRedisTemplate,
        ValkeyConstants.STAGE,
        valkeyCacheExpirationProperties.getStageExpirationSeconds());
  }

  @Bean
  @ConditionalOnMissingBean
  public GameMetadataCachePort gameMetadataCachePort(
      RedisTemplate<String, GameMetadata> gameMetadataRedisTemplate,
      ValkeyCacheExpirationProperties valkeyCacheExpirationProperties) {
    return new GameMetadataCacheAdapter(
        gameMetadataRedisTemplate,
        ValkeyConstants.GAME_METADATA,
        valkeyCacheExpirationProperties.getGameMetadataExpirationSeconds());
  }
}
