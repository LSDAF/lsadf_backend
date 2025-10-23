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

package com.lsadf.core.infra.persistence.adapter.game.mail.converter.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lsadf.core.domain.game.inventory.item.Item;
import com.lsadf.core.domain.game.mail.GameMailAttachmentType;
import com.lsadf.core.infra.persistence.adapter.game.mail.converter.GameMailAttachmentConverter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GameMailAttachmentItemConverter implements GameMailAttachmentConverter<Item> {

  private final ObjectMapper objectMapper;

  @Override
  public GameMailAttachmentType getAttachmentType() {
    return GameMailAttachmentType.ITEM;
  }

  @Override
  public String toJson(Item attachment) throws JsonProcessingException {
    return objectMapper.writeValueAsString(attachment);
  }

  @Override
  public Item toModel(String json) throws JsonProcessingException {
    return objectMapper.readValue(json, Item.class);
  }
}
