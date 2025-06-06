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

import com.lsadf.core.infra.persistence.game.inventory.InventoryRepository;
import com.lsadf.core.infra.persistence.game.inventory.item.ItemEntityMapper;
import com.lsadf.core.infra.persistence.game.inventory.item.ItemRepository;
import com.lsadf.core.infra.persistence.mapper.game.inventory.InventoryEntityMapper;
import com.lsadf.core.infra.web.response.game.inventory.ItemResponseMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for setting up the inventory service and its dependencies.
 *
 * <p>This configuration defines the required beans for managing game save inventories and items
 * corresponding to those inventories. The {@code InventoryService} allows operations such as
 * retrieving, creating, updating, and deleting inventory items.
 *
 * <p>Beans: - {@code InventoryService}: Provides inventory management functionalities. It is
 * implemented by {@link InventoryServiceImpl} and requires dependencies on {@link
 * InventoryRepository} and {@link ItemRepository}.
 *
 * <p>Dependencies: - {@link InventoryRepository}: Handles data persistence for inventory entities.
 * - {@link ItemRepository}: Handles data persistence for item entities.
 */
@Configuration
public class InventoryConfiguration {
  @Bean
  public InventoryService inventoryService(
      InventoryRepository inventoryRepository,
      ItemRepository itemRepository,
      ItemEntityMapper mapper) {
    return new InventoryServiceImpl(inventoryRepository, itemRepository, mapper);
  }

  @Bean
  public ItemResponseMapper itemResponseMapper() {
    return new ItemResponseMapper();
  }

  @Bean
  public ItemEntityMapper itemEntityModelMapper() {
    return new ItemEntityMapper();
  }

  @Bean
  public InventoryEntityMapper inventoryEntityModelMapper(ItemEntityMapper itemMapper) {
    return new InventoryEntityMapper(itemMapper);
  }
}
