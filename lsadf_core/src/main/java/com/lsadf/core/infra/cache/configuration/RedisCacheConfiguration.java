/*
 * Copyright 2024-2025 LSDAF
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package com.lsadf.core.infra.cache.configuration;

import static com.lsadf.core.constants.BeanConstants.Cache.*;
import static com.lsadf.core.constants.RedisConstants.GAME_SAVE_OWNERSHIP;

import com.lsadf.core.infra.cache.Cache;
import com.lsadf.core.infra.cache.HistoCache;
import com.lsadf.core.infra.cache.impl.RedisCache;
import com.lsadf.core.infra.cache.impl.RedisCharacteristicsCache;
import com.lsadf.core.infra.cache.impl.RedisCurrencyCache;
import com.lsadf.core.infra.cache.impl.RedisStageCache;
import com.lsadf.core.infra.cache.listeners.RedisKeyExpirationListener;
import com.lsadf.core.models.Characteristics;
import com.lsadf.core.models.Currency;
import com.lsadf.core.models.Inventory;
import com.lsadf.core.models.Stage;
import com.lsadf.core.infra.cache.properties.CacheExpirationProperties;
import com.lsadf.core.properties.RedisProperties;
import com.lsadf.core.services.*;
import com.lsadf.core.services.impl.RedisCacheFlushServiceImpl;
import com.lsadf.core.services.impl.RedisCacheServiceImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@ConditionalOnProperty(prefix = "cache.redis", name = "enabled", havingValue = "true")
public class RedisCacheConfiguration {

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

  @Bean(name = GAME_SAVE_OWNERSHIP_CACHE)
  public RedisCache<String> gameSaveOwnershipCache(
      RedisTemplate<String, String> redisTemplate,
      CacheExpirationProperties cacheExpirationProperties,
      RedisProperties redisProperties) {
    return new RedisCache<>(
        redisTemplate,
        GAME_SAVE_OWNERSHIP,
        cacheExpirationProperties.getGameSaveOwnershipExpirationSeconds(),
        redisProperties);
  }

  @Bean(name = CHARACTERISTICS_CACHE)
  public HistoCache<Characteristics> redisCharacteristicsCache(
      RedisTemplate<String, Characteristics> redisTemplate,
      CacheExpirationProperties cacheExpirationProperties,
      RedisProperties redisProperties) {
    return new RedisCharacteristicsCache(
        redisTemplate,
        cacheExpirationProperties.getCharacteristicsExpirationSeconds(),
        redisProperties);
  }

  @Bean(name = CURRENCY_CACHE)
  public HistoCache<Currency> redisCurrencyCache(
      RedisTemplate<String, Currency> redisTemplate,
      CacheExpirationProperties cacheExpirationProperties,
      RedisProperties redisProperties) {
    return new RedisCurrencyCache(
        redisTemplate, cacheExpirationProperties.getCurrencyExpirationSeconds(), redisProperties);
  }

  @Bean(name = STAGE_CACHE)
  public HistoCache<Stage> redisStageCache(
      RedisTemplate<String, Stage> redisTemplate,
      CacheExpirationProperties cacheExpirationProperties,
      RedisProperties redisProperties) {
    return new RedisStageCache(
        redisTemplate, cacheExpirationProperties.getStageExpirationSeconds(), redisProperties);
  }

  @Bean
  public CacheService redisCacheService(
      RedisCache<String> gameSaveOwnershipCache,
      HistoCache<Characteristics> characteristicsCache,
      HistoCache<Currency> currencyCache,
      HistoCache<Stage> stageCache) {
    return new RedisCacheServiceImpl(
        gameSaveOwnershipCache, characteristicsCache, currencyCache, stageCache);
  }

  @Bean
  public LettuceConnectionFactory redisConnectionFactory(RedisProperties redisProperties) {
    RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration();
    configuration.setHostName(redisProperties.getHost());
    configuration.setPort(redisProperties.getPort());
    configuration.setDatabase(redisProperties.getDatabase());
    configuration.setPassword(redisProperties.getPassword());
    return new LettuceConnectionFactory(configuration);
  }

  @Bean
  public RedisMessageListenerContainer keyExpirationListenerContainer(
      RedisConnectionFactory connectionFactory,
      RedisKeyExpirationListener redisKeyExpirationListener) {
    RedisMessageListenerContainer container = new RedisMessageListenerContainer();
    container.setConnectionFactory(connectionFactory);
    container.addMessageListener(
        redisKeyExpirationListener, new PatternTopic("__keyevent@0__:expired"));
    return container;
  }

  @Bean
  MessageListenerAdapter messageListener(RedisKeyExpirationListener redisKeyExpirationListener) {
    return new MessageListenerAdapter(redisKeyExpirationListener);
  }

  @Bean
  public RedisKeyExpirationListener redisKeyExpirationListener(
      CharacteristicsService characteristicsService,
      CurrencyService currencyService,
      InventoryService inventoryService,
      StageService stageService,
      RedisTemplate<String, Characteristics> characteristicsRedisTemplate,
      RedisTemplate<String, Currency> currencyRedisTemplate,
      RedisTemplate<String, Stage> stageRedisTemplate) {
    return new RedisKeyExpirationListener(
        characteristicsService,
        currencyService,
        inventoryService,
        stageService,
        characteristicsRedisTemplate,
        currencyRedisTemplate,
        stageRedisTemplate);
  }

  @Bean
  public CacheFlushService cacheFlushService(
      CharacteristicsService characteristicsService,
      CurrencyService currencyService,
      InventoryService inventoryService,
      StageService stageService,
      Cache<Characteristics> characteristicsCache,
      Cache<Currency> currencyCache,
      Cache<Stage> stageCache) {
    return new RedisCacheFlushServiceImpl(
        characteristicsService,
        currencyService,
        inventoryService,
        stageService,
        characteristicsCache,
        currencyCache,
        stageCache);
  }
}
