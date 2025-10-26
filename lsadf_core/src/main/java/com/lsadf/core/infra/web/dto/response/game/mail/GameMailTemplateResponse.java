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

package com.lsadf.core.infra.web.dto.response.game.mail;

import com.lsadf.core.domain.game.mail.GameMailAttachment;
import com.lsadf.core.infra.web.dto.response.Response;
import java.io.Serial;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import lombok.Builder;
import org.jspecify.annotations.Nullable;

@Builder
public record GameMailTemplateResponse(
    UUID id,
    String name,
    String subject,
    String body,
    Integer expirationDays,
    Instant createdAt,
    Instant updatedAt,
    @Nullable List<GameMailAttachment<?>> attachments)
    implements Response {
  @Serial private static final long serialVersionUID = 167454968378687444L;
}
