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
package com.lsadf.core.infra.web.request.game.game_save.update;

import static com.lsadf.core.infra.web.JsonAttributes.GameSave.*;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.lsadf.core.domain.game.characteristics.Characteristics;
import com.lsadf.core.domain.game.currency.Currency;
import com.lsadf.core.domain.game.stage.Stage;
import com.lsadf.core.domain.user.validation.Nickname;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GameSaveNicknameUpdateRequest implements GameSaveUpdateRequest {

  @Serial private static final long serialVersionUID = -6478222007381338108L;

  @JsonProperty(value = NICKNAME)
  @Nickname
  @Schema(description = "Nickname of the user", example = "test")
  private String nickname;

  @Override
  public Characteristics getCharacteristics() {
    return null;
  }

  @Override
  public Currency getCurrency() {
    return null;
  }

  @Override
  public Stage getStage() {
    return null;
  }
}
