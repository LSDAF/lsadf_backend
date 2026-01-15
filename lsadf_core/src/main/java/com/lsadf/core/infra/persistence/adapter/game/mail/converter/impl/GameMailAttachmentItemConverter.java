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

import static com.lsadf.core.infra.web.JsonAttributes.*;

import com.lsadf.core.domain.game.inventory.ItemStatistic;
import com.lsadf.core.domain.game.mail.GameMailAttachment;
import com.lsadf.core.domain.game.mail.GameMailAttachmentType;
import com.lsadf.core.infra.persistence.adapter.game.mail.converter.GameMailAttachmentConverter;
import com.lsadf.core.infra.web.dto.common.game.inventory.ItemStatDto;
import com.lsadf.core.infra.web.dto.request.Request;
import com.lsadf.core.infra.web.dto.request.game.inventory.ItemRequest;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.json.JsonMapper;

@RequiredArgsConstructor
public class GameMailAttachmentItemConverter implements GameMailAttachmentConverter<ItemRequest> {

  private final JsonMapper jsonMapper;

  @Override
  public GameMailAttachmentType getAttachmentType() {
    return GameMailAttachmentType.ITEM;
  }

  @Override
  public String toJson(GameMailAttachment<Request> attachment) throws JacksonException {
    return jsonMapper.writeValueAsString(attachment);
  }

  @Override
  public ItemRequest toRequest(String json) throws JacksonException {

    // read json as map
    JsonNode jsonNode = jsonMapper.readTree(json);

    var type = jsonNode.get(TYPE).asString();
    var blueprintId = jsonNode.get(BLUEPRINT_ID).asString();
    var rarity = jsonNode.get(RARITY).asString();
    var isEquipped = jsonNode.get(IS_EQUIPPED).asBoolean();
    var level = jsonNode.get(LEVEL).asInt();
    var mainStatistic = jsonNode.get(MAIN_STAT).get(STATISTIC);
    var mainBaseValue = jsonNode.get(MAIN_STAT).get(BASE_VALUE).asFloat();
    var mainStat =
        new ItemStatDto(ItemStatistic.fromString(mainStatistic.asString()), mainBaseValue);
    var additionalStatNode = jsonNode.get(ADDITIONAL_STATS);
    List<ItemStatDto> additionalStats = new ArrayList<>();
    if (additionalStatNode != null && additionalStatNode.isArray()) {
      for (JsonNode statNode : additionalStatNode) {
        var itemStat = ItemStatistic.fromString(statNode.get(STATISTIC).asString());
        var baseValue = statNode.get(BASE_VALUE).asFloat();
        additionalStats.add(new ItemStatDto(itemStat, baseValue));
      }
    }

    return ItemRequest.builder()
        .type(type)
        .blueprintId(blueprintId)
        .rarity(rarity)
        .isEquipped(isEquipped)
        .level(level)
        .mainStat(mainStat)
        .additionalStats(additionalStats)
        .build();
  }
}
