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
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class AdminGameSaveUpdateRequest implements GameSaveUpdateRequest {

  @Serial private static final long serialVersionUID = -1619677650296221394L;

  @JsonProperty(value = NICKNAME)
  @Nickname(nullable = true)
  @Schema(description = "Nickname of the user", example = "test")
  private String nickname;

  @JsonProperty(value = CHARACTERISTICS)
  @Schema(
      description = "Characteristics of the user",
      example = "{\"strength\":10,\"agility\":10,\"intelligence\":10,\"luck\":10}")
  private Characteristics characteristics;

  @JsonProperty(value = CURRENCY)
  @Schema(
      description = "Currency of the user",
      example = "{\"gold\":1000,\"silver\":1000,\"copper\":1000}")
  private Currency currency;

  @JsonProperty(value = STAGE)
  @Schema(description = "Stage of the user", example = "{\"stageId\":1,\"stageName\":\"test\"}")
  private Stage stage;
}
