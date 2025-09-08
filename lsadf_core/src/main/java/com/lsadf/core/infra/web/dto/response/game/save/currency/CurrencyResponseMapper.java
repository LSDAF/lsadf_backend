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

package com.lsadf.core.infra.web.dto.response.game.save.currency;

import com.lsadf.core.domain.game.save.currency.Currency;
import com.lsadf.core.infra.web.dto.response.ModelResponseMapper;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * Interface representing a mapper for converting a Currency domain model to its corresponding
 * CurrencyResponse Data Transfer Object (DTO).
 *
 * <p>This interface extends the {@link ModelResponseMapper} interface, which provides a generic
 * definition for mapping domain models to their associated response objects.
 *
 * <p>The implementation of this mapper is automatically generated at runtime by the MapStruct
 * library. The generated implementation is responsible for mapping all relevant fields between the
 * Currency and CurrencyResponse objects and is accessible as a static instance variable {@code
 * INSTANCE}.
 */
@Mapper
public interface CurrencyResponseMapper extends ModelResponseMapper<Currency, CurrencyResponse> {
  CurrencyResponseMapper INSTANCE = Mappers.getMapper(CurrencyResponseMapper.class);

  /**
   * Maps a Currency domain model to a CurrencyResponse DTO.
   *
   * @param currency the Currency instance to be mapped
   * @return a CurrencyResponse instance containing the mapped data
   */
  @Override
  CurrencyResponse map(Currency currency);
}
