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

package com.lsadf.core.domain.game.session;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonView;
import com.lsadf.core.infra.web.controllers.JsonViews;
import com.lsadf.core.shared.event.Event;
import com.lsadf.core.shared.model.Model;
import com.lsadf.core.shared.validation.Uuid;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import java.util.LinkedList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Schema(name = "Game Session", description = "Game Session object")
@Data
@Builder
@JsonPropertyOrder()
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonView(JsonViews.Internal.class)
public class GameSession implements Model {
  @Uuid private final String id;

  @Uuid private final String gameSaveid;

  @Email private final String username;

  private final List<Event> events = new LinkedList<>();
}
