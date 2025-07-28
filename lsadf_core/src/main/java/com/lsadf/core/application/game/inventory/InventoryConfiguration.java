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
package com.lsadf.core.application.game.inventory;

import com.lsadf.core.infra.persistence.table.game.inventory.AdditionalItemStatsRepository;
import com.lsadf.core.infra.persistence.table.game.inventory.ItemRepository;
import com.lsadf.core.infra.persistence.table.game.save.metadata.GameMetadataRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InventoryConfiguration {
  @Bean
  public InventoryService inventoryService(
      ItemRepository itemRepository,
      GameMetadataRepository gameMetadataRepository,
      AdditionalItemStatsRepository additionalItemStatsRepository) {
    return new InventoryServiceImpl(
        itemRepository, gameMetadataRepository, additionalItemStatsRepository);
  }
}
