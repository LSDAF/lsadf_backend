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

package com.lsadf.core.domain.game.mail;

import com.lsadf.core.shared.model.Model;
import java.io.Serial;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import lombok.*;
import org.jspecify.annotations.Nullable;

@RequiredArgsConstructor
@EqualsAndHashCode
@Getter
public final class GameMail implements Model {
  @Serial private static final long serialVersionUID = -8465791165591742351L;
  private final UUID id;
  private final UUID gameSaveId;
  private final String subject;
  private final String body;
  private final Instant createdAt;
  private final Instant updatedAt;
  private final Instant expiresAt;
  private final boolean read;
  private final boolean attachmentsClaimed;
  @Nullable private List<GameMailAttachment<?>> attachments;

  public void addAttachment(GameMailAttachment<?> attachment) {
    if (this.attachments == null) {
      attachments = new java.util.ArrayList<>();
    }
    this.attachments.add(attachment);
  }
}
