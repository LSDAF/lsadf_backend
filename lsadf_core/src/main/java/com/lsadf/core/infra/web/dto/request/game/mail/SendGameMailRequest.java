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

package com.lsadf.core.infra.web.dto.request.game.mail;

import static com.lsadf.core.infra.web.JsonAttributes.*;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.lsadf.core.infra.web.dto.request.Request;
import jakarta.validation.constraints.NotNull;
import java.io.Serial;
import java.util.UUID;
import lombok.Builder;

/** Request DTO for sending game mail */
@Builder
public record SendGameMailRequest(
    @NotNull @JsonProperty(GAME_SAVE_ID) UUID gameSaveId,
    @NotNull @JsonProperty(GAME_MAIL_TEMPLATE_ID) UUID gameMailTemplateId)
    implements Request {
  @Serial private static final long serialVersionUID = 3425876093174523687L;
}
