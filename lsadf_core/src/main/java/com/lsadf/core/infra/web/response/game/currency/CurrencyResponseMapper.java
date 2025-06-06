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

package com.lsadf.core.infra.web.response.game.currency;

import com.lsadf.core.domain.game.currency.Currency;
import com.lsadf.core.infra.web.response.ModelResponseMapper;

/**
 * Maps a {@link Currency} model object to a corresponding {@link CurrencyResponse} response object.
 * This class implements the {@link ModelResponseMapper} interface, providing a specific mapping
 * between the {@link Currency} domain model and its externally exposed {@link CurrencyResponse}
 * representation.
 *
 * <p>The implementation uses the builder pattern to construct a new {@link CurrencyResponse} object
 * by transferring the properties from the provided {@link Currency} instance.
 */
public class CurrencyResponseMapper implements ModelResponseMapper<Currency, CurrencyResponse> {
  /**
   * Maps a {@link Currency} model object to a {@link CurrencyResponse} response object.
   *
   * @param currency the {@link Currency} model object to be mapped
   * @return the {@link CurrencyResponse} response object that corresponds to the provided model
   */
  @Override
  public CurrencyResponse mapToResponse(Currency currency) {
    return CurrencyResponse.builder()
        .amethyst(currency.getAmethyst())
        .gold(currency.getGold())
        .diamond(currency.getDiamond())
        .emerald(currency.getEmerald())
        .build();
  }
}
