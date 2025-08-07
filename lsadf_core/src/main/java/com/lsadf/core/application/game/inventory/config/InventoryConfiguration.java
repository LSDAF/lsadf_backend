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
package com.lsadf.core.application.game.inventory.config;

import com.lsadf.core.application.game.inventory.InventoryRepositoryPort;
import com.lsadf.core.application.game.inventory.InventoryService;
import com.lsadf.core.application.game.inventory.impl.InventoryServiceImpl;
import com.lsadf.core.application.game.save.metadata.GameMetadataRepositoryPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InventoryConfiguration {
  @Bean
  public InventoryService inventoryService(
      InventoryRepositoryPort inventoryRepositoryPort,
      GameMetadataRepositoryPort gameMetadataRepositoryPort) {
    return new InventoryServiceImpl(inventoryRepositoryPort, gameMetadataRepositoryPort);
  }
}
