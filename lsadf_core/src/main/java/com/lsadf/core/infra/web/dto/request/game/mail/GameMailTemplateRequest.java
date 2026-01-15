/*
 * Copyright © 2024-2026 LSDAF
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

import com.lsadf.core.infra.web.dto.request.Request;
import jakarta.validation.constraints.NotEmpty;
import java.io.Serial;
import lombok.Builder;
import org.jspecify.annotations.Nullable;

@Builder
public record GameMailTemplateRequest(
    @NotEmpty String name,
    @NotEmpty String subject,
    @NotEmpty String body,
    @Nullable Integer expirationDays)
    implements Request {
  @Serial private static final long serialVersionUID = -1408819149940233516L;
}
