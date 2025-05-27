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

package com.lsadf.core.infra.web.requests.game.currency;

import com.lsadf.core.domain.game.currency.Currency;
import com.lsadf.core.infra.web.requests.RequestModelMapper;

/**
 * A mapper implementation for transforming a {@link CurrencyRequest} object into its corresponding
 * {@link Currency} model representation. This class provides the necessary mapping logic for
 * transferring data from a request-layer representation to a domain-layer representation.
 *
 * <p>The mapping operation utilizes the fields of {@link CurrencyRequest} to construct an instance
 * of {@link Currency}, ensuring alignment between the input request and the target domain model.
 *
 * <p>Implements the {@link RequestModelMapper} interface with {@link CurrencyRequest} as the
 * request type and {@link Currency} as the target model type.
 */
public class CurrencyRequestModelMapper implements RequestModelMapper<CurrencyRequest, Currency> {
  /** {@inheritDoc} */
  @Override
  public Currency mapToModel(CurrencyRequest currencyRequest) {
    return new Currency(
        currencyRequest.getGold(),
        currencyRequest.getDiamond(),
        currencyRequest.getEmerald(),
        currencyRequest.getAmethyst());
  }
}
