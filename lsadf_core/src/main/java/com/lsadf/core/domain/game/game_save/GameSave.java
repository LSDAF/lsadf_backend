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
package com.lsadf.core.domain.game.game_save;

import com.lsadf.core.domain.game.characteristics.Characteristics;
import com.lsadf.core.domain.game.currency.Currency;
import com.lsadf.core.domain.game.stage.Stage;
import com.lsadf.core.shared.model.Model;
import java.io.Serial;
import java.util.Date;
import java.util.UUID;
import lombok.*;

/** Game Save DTO */
@Builder
@AllArgsConstructor
@Getter
@Setter
public class GameSave implements Model {

  @Serial private static final long serialVersionUID = -2686370647354845265L;

  private final UUID id;

  private final String userEmail;

  private final Date createdAt;

  private final Date updatedAt;

  private final String nickname;

  private Characteristics characteristics;

  private Currency currency;

  private Stage stage;
}
