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

package com.lsadf.core.application.game.save.currency.command;

import com.lsadf.core.domain.game.save.currency.Currency;
import java.util.UUID;
import org.jspecify.annotations.Nullable;

public record UpdateCacheCurrencyCommand(
    UUID gameSaveId,
    @Nullable Long gold,
    @Nullable Long diamond,
    @Nullable Long emerald,
    @Nullable Long amethyst) {
  public static UpdateCacheCurrencyCommand fromCurrency(UUID gameSaveId, Currency currency) {
    return new UpdateCacheCurrencyCommand(
        gameSaveId, currency.gold(), currency.diamond(), currency.emerald(), currency.amethyst());
  }
}
