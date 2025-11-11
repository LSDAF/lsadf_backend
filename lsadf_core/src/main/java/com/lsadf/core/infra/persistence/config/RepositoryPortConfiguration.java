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
package com.lsadf.core.infra.persistence.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lsadf.core.application.game.inventory.InventoryRepositoryPort;
import com.lsadf.core.application.game.mail.GameMailRepositoryPort;
import com.lsadf.core.application.game.mail.GameMailTemplateRepositoryPort;
import com.lsadf.core.application.game.save.GameSaveRepositoryPort;
import com.lsadf.core.application.game.save.characteristics.CharacteristicsRepositoryPort;
import com.lsadf.core.application.game.save.currency.CurrencyRepositoryPort;
import com.lsadf.core.application.game.save.metadata.GameMetadataRepositoryPort;
import com.lsadf.core.application.game.save.stage.StageRepositoryPort;
import com.lsadf.core.application.game.session.GameSessionRepositoryPort;
import com.lsadf.core.infra.persistence.adapter.game.inventory.InventoryRepositoryAdapter;
import com.lsadf.core.infra.persistence.adapter.game.mail.GameMailRepositoryAdapter;
import com.lsadf.core.infra.persistence.adapter.game.mail.GameMailTemplateRepositoryAdapter;
import com.lsadf.core.infra.persistence.adapter.game.mail.converter.GameMailAttachmentConverterRegistry;
import com.lsadf.core.infra.persistence.adapter.game.save.*;
import com.lsadf.core.infra.persistence.impl.game.inventory.AdditionalItemStatsRepository;
import com.lsadf.core.infra.persistence.impl.game.inventory.ItemRepository;
import com.lsadf.core.infra.persistence.impl.game.mail.GameMailRepository;
import com.lsadf.core.infra.persistence.impl.game.mail.template.GameMailTemplateAttachmentRepository;
import com.lsadf.core.infra.persistence.impl.game.mail.template.GameMailTemplateRepository;
import com.lsadf.core.infra.persistence.impl.game.save.characteristics.CharacteristicsRepository;
import com.lsadf.core.infra.persistence.impl.game.save.currency.CurrencyRepository;
import com.lsadf.core.infra.persistence.impl.game.save.metadata.GameMetadataRepository;
import com.lsadf.core.infra.persistence.impl.game.save.stage.StageRepository;
import com.lsadf.core.infra.persistence.impl.game.session.GameSessionRepository;
import com.lsadf.core.infra.persistence.impl.view.GameMailViewRepository;
import com.lsadf.core.infra.persistence.impl.view.GameSaveViewRepository;
import com.lsadf.core.infra.persistence.impl.view.GameSessionViewRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RepositoryPortConfiguration {

  // Adapter Bean Definitions
  @Bean
  public GameSaveRepositoryPort gameSaveRepositoryAdapter(
      GameSaveViewRepository gameSaveViewRepository) {
    return new GameSaveViewRepositoryAdapter(gameSaveViewRepository);
  }

  @Bean
  public InventoryRepositoryPort inventoryRepositoryAdapter(
      ItemRepository itemRepository, AdditionalItemStatsRepository additionalItemStatsRepository) {
    return new InventoryRepositoryAdapter(itemRepository, additionalItemStatsRepository);
  }

  @Bean
  public GameMetadataRepositoryPort gameMetadataRepositoryAdapter(
      GameMetadataRepository gameMetadataRepository) {
    return new GameMetadataRepositoryAdapter(gameMetadataRepository);
  }

  @Bean
  public CharacteristicsRepositoryPort characteristicsRepositoryAdapter(
      CharacteristicsRepository characteristicsRepository) {
    return new CharacteristicsRepositoryAdapter(characteristicsRepository);
  }

  @Bean
  public GameSessionRepositoryPort gameSessionRepositoryAdapter(
      GameSessionViewRepository gameSessionViewRepository,
      GameSessionRepository gameSessionRepository) {
    return new GameSessionRepositoryAdapter(gameSessionRepository, gameSessionViewRepository);
  }

  @Bean
  public CurrencyRepositoryPort currencyRepositoryAdapter(CurrencyRepository currencyRepository) {
    return new CurrencyRepositoryAdapter(currencyRepository);
  }

  @Bean
  public StageRepositoryPort stageRepositoryAdapter(StageRepository stageRepository) {
    return new StageRepositoryAdapter(stageRepository);
  }

  @Bean
  public GameMailRepositoryPort gameMailRepositoryAdapter(
      GameMailRepository gameMailRepository,
      GameMailTemplateAttachmentRepository gameMailAttachmentRepository,
      GameMailViewRepository gameMailViewRepository,
      GameMailAttachmentConverterRegistry converterRegistry) {
    return new GameMailRepositoryAdapter(
        gameMailRepository,
        gameMailAttachmentRepository,
        gameMailViewRepository,
        converterRegistry);
  }

  @Bean
  public GameMailTemplateRepositoryPort gameMailTemplateRepositoryAdapter(
      GameMailTemplateAttachmentRepository gameMailAttachmentRepository,
      GameMailTemplateRepository gameMailTemplateRepository,
      GameMailAttachmentConverterRegistry converterRegistry,
      ObjectMapper objectMapper) {
    return new GameMailTemplateRepositoryAdapter(
        gameMailTemplateRepository, gameMailAttachmentRepository, converterRegistry, objectMapper);
  }
}
