/*
 * Copyright © 2024-2026 LSDAF
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

import com.lsadf.core.infra.valkey.config.properties.ValkeyProperties;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.jspecify.annotations.Nullable;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import redis.embedded.RedisServer;

@Configuration
@ConditionalOnProperty(prefix = "valkey.config", name = "embedded", havingValue = "true")
public class ValkeyEmbeddedCacheConfiguration {

  @Nullable private RedisServer redisServer;

  public ValkeyEmbeddedCacheConfiguration(ValkeyProperties valkeyProperties) throws IOException {
    if (valkeyProperties.isEnabled() && valkeyProperties.isEmbedded()) {
      this.redisServer = initRedisServer(valkeyProperties);
    }
  }

  public RedisServer initRedisServer(ValkeyProperties valkeyProperties) throws IOException {
    Path tempDir = Files.createTempDirectory("redis-test-dir");
    return RedisServer.newRedisServer()
        .setting("requirepass " + valkeyProperties.getPassword())
        .setting("bind 127.0.0.1")
        .setting("notify-keyspace-events Ex")
        .setting("appendonly yes")
        .setting("appendfsync everysec")
        .setting("auto-aof-rewrite-percentage 100")
        .setting("auto-aof-rewrite-min-size 100mb")
        .setting("appendfilename test-appendonly.aof")
        .setting("dir " + tempDir.toAbsolutePath())
        .port(valkeyProperties.getPort())
        .build();
  }

  @PreDestroy
  public void preDestroy() throws IOException {
    if (redisServer != null) this.redisServer.stop();
  }

  @PostConstruct
  public void postConstruct() throws IOException {
    if (redisServer != null) this.redisServer.start();
  }
}
