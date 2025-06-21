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

package com.lsadf.core.infra.web.response.info;

import static com.lsadf.core.infra.web.JsonAttributes.GlobalInfo.*;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import com.lsadf.core.infra.web.controller.JsonViews;
import com.lsadf.core.infra.web.response.Response;
import java.io.Serial;
import java.time.Instant;
import lombok.Builder;

@Builder
@JsonView(JsonViews.Admin.class)
public record GlobalInfoResponse(
    @JsonView(JsonViews.Admin.class) @JsonProperty(value = NOW) Instant now,
    @JsonView(JsonViews.Admin.class) @JsonProperty(value = GAME_SAVE_COUNTER) Long gameSaveCounter,
    @JsonView(JsonViews.Admin.class) @JsonProperty(value = USER_COUNTER) Long userCounter)
    implements Response {

  @Serial private static final long serialVersionUID = -5539057784012769955L;
}
