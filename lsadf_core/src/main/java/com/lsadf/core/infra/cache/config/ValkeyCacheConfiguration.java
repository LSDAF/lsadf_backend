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
package com.lsadf.core.infra.cache.config;

import static com.lsadf.core.infra.config.BeanConstants.Cache.*;

import com.lsadf.core.application.game.characteristics.CharacteristicsService;
import com.lsadf.core.application.game.currency.CurrencyService;
import com.lsadf.core.application.game.inventory.InventoryService;
import com.lsadf.core.application.game.stage.StageService;
import com.lsadf.core.domain.game.characteristics.Characteristics;
import com.lsadf.core.domain.game.currency.Currency;
import com.lsadf.core.domain.game.inventory.Inventory;
import com.lsadf.core.domain.game.stage.Stage;
import com.lsadf.core.infra.cache.Cache;
import com.lsadf.core.infra.cache.HistoCache;
import com.lsadf.core.infra.cache.flush.CacheFlushService;
import com.lsadf.core.infra.cache.flush.RedisCacheFlushServiceImpl;
import com.lsadf.core.infra.cache.listeners.ValkeyKeyExpirationListener;
import com.lsadf.core.infra.cache.services.CacheService;
import com.lsadf.core.infra.cache.services.ValkeyCacheServiceImpl;
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
  public CacheService redisCacheService(
      Cache<String> gameSaveOwnershipCache,
      HistoCache<Characteristics> characteristicsCache,
      HistoCache<Currency> currencyCache,
      HistoCache<Stage> stageCache) {
    return new ValkeyCacheServiceImpl(
        gameSaveOwnershipCache, characteristicsCache, currencyCache, stageCache);
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
      RedisConnectionFactory connectionFactory,
      ValkeyKeyExpirationListener valkeyKeyExpirationListener) {
    RedisMessageListenerContainer container = new RedisMessageListenerContainer();
    container.setConnectionFactory(connectionFactory);
    container.addMessageListener(
        valkeyKeyExpirationListener, new PatternTopic("__keyevent@0__:expired"));
    return container;
  }

  @Bean
  MessageListenerAdapter messageListener(ValkeyKeyExpirationListener valkeyKeyExpirationListener) {
    return new MessageListenerAdapter(valkeyKeyExpirationListener);
  }

  @Bean
  public ValkeyKeyExpirationListener redisKeyExpirationListener(
      CharacteristicsService characteristicsService,
      CurrencyService currencyService,
      InventoryService inventoryService,
      StageService stageService,
      RedisTemplate<String, Characteristics> characteristicsRedisTemplate,
      RedisTemplate<String, Currency> currencyRedisTemplate,
      RedisTemplate<String, Stage> stageRedisTemplate) {
    return new ValkeyKeyExpirationListener(
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
