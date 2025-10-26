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

@Builder
@Getter
@EqualsAndHashCode
@RequiredArgsConstructor
@AllArgsConstructor
public final class GameMailTemplate implements Model {
  @Serial private static final long serialVersionUID = -7542452237317816964L;
  private final UUID id;
  private final String name;
  private final String subject;
  private final String body;
  private final Integer expirationDays;
  private final Instant createdAt;
  private final Instant updatedAt;
  @Builder.Default @Nullable private List<GameMailAttachment<?>> attachments = null;

  public void addAttachment(GameMailAttachment<?> attachment) {
    if (this.attachments == null) {
      attachments = new java.util.ArrayList<>();
    }
    this.attachments.add(attachment);
  }
}
