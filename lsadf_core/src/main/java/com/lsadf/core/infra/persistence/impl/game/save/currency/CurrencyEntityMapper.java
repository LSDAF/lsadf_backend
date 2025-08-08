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

package com.lsadf.core.infra.persistence.impl.game.save.currency;

import com.lsadf.core.domain.game.save.currency.Currency;
import com.lsadf.core.infra.persistence.EntityModelMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * A mapper interface responsible for converting {@link CurrencyEntity} objects into {@link
 * Currency} objects and vice versa.
 *
 * <p>This interface extends {@link EntityModelMapper}, inheriting the contract to map entities to
 * models. It provides an implementation specific to converting currency-related persistence-layer
 * entities into domain models used in the application layer.
 *
 * <p>This mapper is implemented using MapStruct, allowing for automatic generation of the
 * implementation at build time.
 *
 * <p>Key responsibilities: - Facilitates transforming {@link CurrencyEntity} objects into {@link
 * Currency} domain models. - Promotes separation of concerns by abstracting the conversion logic
 * out of business logic layers.
 *
 * <p>Singleton instance {@code INSTANCE} is provided for use in applications. MapStruct generates
 * the implementation during compilation, ensuring a consistent and performant mapping process.
 */
@Mapper
public interface CurrencyEntityMapper extends EntityModelMapper<CurrencyEntity, Currency> {
  CurrencyEntityMapper INSTANCE = Mappers.getMapper(CurrencyEntityMapper.class);

  @Override
  @Mapping(source = "goldAmount", target = "gold")
  @Mapping(source = "diamondAmount", target = "diamond")
  @Mapping(source = "emeraldAmount", target = "emerald")
  @Mapping(source = "amethystAmount", target = "amethyst")
  Currency map(CurrencyEntity currencyEntity);
}
