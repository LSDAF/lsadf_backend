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

package com.lsadf.core.infra.web.request.game.currency;

import com.lsadf.core.domain.game.save.currency.Currency;
import com.lsadf.core.infra.web.request.RequestModelMapper;
import org.mapstruct.Mapper;

/**
 * Interface for mapping {@link CurrencyRequest} objects to {@link Currency} model objects. This
 * mapper uses the MapStruct library to generate implementation code at compile time. It extends
 * {@link RequestModelMapper} to inherit generic mapping capabilities.
 *
 * <p>The primary purpose of this mapper is to handle conversions between the request layer
 * representation of currency-related data and the application's internal domain representation.
 *
 * <p>Implementing Classes: - Uses MapStruct-generated implementation via {@link
 * org.mapstruct.factory.Mappers}.
 *
 * <p>Features: - Ensures consistency between JSON-based input requests and internal model objects.
 */
@Mapper
public interface CurrencyRequestMapper extends RequestModelMapper<CurrencyRequest, Currency> {
  CurrencyRequestMapper INSTANCE =
      org.mapstruct.factory.Mappers.getMapper(CurrencyRequestMapper.class);

  @Override
  Currency map(CurrencyRequest currencyRequest);
}
