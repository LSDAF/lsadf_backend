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
package com.lsadf.core.configurations.cache;

import com.lsadf.core.properties.RedisProperties;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import java.io.IOException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import redis.embedded.RedisServer;

@Configuration
@ConditionalOnProperty(prefix = "cache.redis", name = "embedded", havingValue = "true")
public class RedisEmbeddedCacheConfiguration {

  private RedisServer redisServer;

  public RedisEmbeddedCacheConfiguration(RedisProperties redisProperties) throws IOException {
    if (redisProperties.isEnabled() && redisProperties.isEmbedded()) {
      this.redisServer = initRedisServer(redisProperties);
    }
  }

  public RedisServer initRedisServer(RedisProperties redisProperties) throws IOException {
    return RedisServer.newRedisServer()
        .setting("requirepass " + redisProperties.getPassword())
        .setting("bind 127.0.0.1")
        .setting("notify-keyspace-events KEA")
        .port(redisProperties.getPort())
        .build();
  }

  @PreDestroy
  public void preDestroy() throws IOException {
    this.redisServer.stop();
  }

  @PostConstruct
  public void postConstruct() throws IOException {
    this.redisServer.start();
  }
}
