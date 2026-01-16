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
package com.lsadf.core.infra.event.publisher.game.mail;

import com.lsadf.core.application.game.mail.GameMailEventPublisherPort;
import com.lsadf.core.domain.game.mail.event.GameMailAttachmentsClaimedEvent;
import com.lsadf.core.domain.game.mail.event.GameMailReadEvent;
import com.lsadf.core.infra.event.factory.game.mail.GameMailEventFactory;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;

@RequiredArgsConstructor
public class GameMailEventPublisher implements GameMailEventPublisherPort {
  private final ApplicationEventPublisher eventPublisher;
  private final GameMailEventFactory eventFactory;

  @Override
  public void publishGameMailReadEvent(String userId, UUID gameMailId) {
    GameMailReadEvent gameMailReadEvent = eventFactory.createGameMailReadEvent(gameMailId, userId);
    eventPublisher.publishEvent(gameMailReadEvent);
  }

  @Override
  public void publishGameMailAttachmentsClaimed(String userId, UUID gameMailId) {
    GameMailAttachmentsClaimedEvent gameMailAttachmentsClaimedEvent =
        eventFactory.createGameMailAttachmentsClaimedEvent(gameMailId, userId);
    eventPublisher.publishEvent(gameMailAttachmentsClaimedEvent);
  }
}
