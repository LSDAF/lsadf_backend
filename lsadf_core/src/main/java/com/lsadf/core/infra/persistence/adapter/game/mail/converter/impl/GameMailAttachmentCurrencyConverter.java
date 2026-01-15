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
package com.lsadf.core.infra.persistence.adapter.game.mail.converter.impl;

import static com.lsadf.core.domain.game.mail.GameMailAttachmentType.CURRENCY;

import com.lsadf.core.domain.game.mail.GameMailAttachment;
import com.lsadf.core.domain.game.mail.GameMailAttachmentType;
import com.lsadf.core.infra.persistence.adapter.game.mail.converter.GameMailAttachmentConverter;
import com.lsadf.core.infra.web.dto.request.Request;
import com.lsadf.core.infra.web.dto.request.game.currency.CurrencyRequest;
import lombok.RequiredArgsConstructor;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;

@RequiredArgsConstructor
public class GameMailAttachmentCurrencyConverter
    implements GameMailAttachmentConverter<CurrencyRequest> {

  private final ObjectMapper objectMapper;

  @Override
  public GameMailAttachmentType getAttachmentType() {
    return CURRENCY;
  }

  @Override
  public String toJson(GameMailAttachment<Request> attachment) throws JacksonException {
    Object attachmentObject = attachment.attachment();
    return objectMapper.writeValueAsString(attachmentObject);
  }

  @Override
  public CurrencyRequest toRequest(String json) throws JacksonException {
    return objectMapper.readValue(json, CurrencyRequest.class);
  }
}
