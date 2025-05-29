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

package com.lsadf.core.infra.web.responses.game.characteristics;

import com.lsadf.core.domain.game.characteristics.Characteristics;
import com.lsadf.core.infra.web.responses.ModelResponseMapper;

/**
 * Class responsible for mapping {@link Characteristics} model objects to their corresponding {@link
 * CharacteristicsResponse} representations.
 *
 * <p>Implements the {@link ModelResponseMapper} interface to provide a concrete implementation of
 * the mapping process.
 */
public class CharacteristicsResponseMapper
    implements ModelResponseMapper<Characteristics, CharacteristicsResponse> {
  /** {@inheritDoc} */
  @Override
  public CharacteristicsResponse mapToResponse(Characteristics characteristics) {
    return CharacteristicsResponse.builder()
        .attack(characteristics.getAttack())
        .critDamage(characteristics.getCritDamage())
        .critChance(characteristics.getCritChance())
        .health(characteristics.getHealth())
        .resistance(characteristics.getResistance())
        .build();
  }
}
