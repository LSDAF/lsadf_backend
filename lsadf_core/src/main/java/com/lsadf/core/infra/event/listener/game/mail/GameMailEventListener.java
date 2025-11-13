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

package com.lsadf.core.infra.event.listener.game.mail;

import com.lsadf.core.application.game.mail.GameMailCommandService;
import com.lsadf.core.application.game.mail.GameMailEventListenerPort;
import com.lsadf.core.domain.game.mail.event.GameMailAttachmentsClaimedEvent;
import com.lsadf.core.domain.game.mail.event.GameMailReadEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;

@Slf4j
@RequiredArgsConstructor
public class GameMailEventListener implements GameMailEventListenerPort {
  private final GameMailCommandService gameMailCommandService;

  @EventListener
  @Override
  @Async
  public void onGameMailRead(GameMailReadEvent event) {
    log.debug("{}: {}", event.getEventType(), event);
    gameMailCommandService.readGameMailById(event.getGameMailId());
  }

  @EventListener
  @Override
  @Async
  public void onGameMailAttachmentsClaimed(GameMailAttachmentsClaimedEvent event) {
    log.debug("{}: {}", event.getEventType(), event);
    gameMailCommandService.claimGameMailAttachments(event.getGameMailId());
  }
}
