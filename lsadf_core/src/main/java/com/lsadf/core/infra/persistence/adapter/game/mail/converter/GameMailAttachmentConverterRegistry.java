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
package com.lsadf.core.infra.persistence.adapter.game.mail.converter;

import com.lsadf.core.domain.game.mail.GameMailAttachmentType;
import java.util.HashMap;
import java.util.Map;
import org.jspecify.annotations.Nullable;

public class GameMailAttachmentConverterRegistry {
  private final Map<GameMailAttachmentType, GameMailAttachmentConverter<?>> converters =
      new HashMap<>();

  public void registerConverter(GameMailAttachmentConverter<?> converter) {
    converters.put(converter.getAttachmentType(), converter);
  }

  public @Nullable GameMailAttachmentConverter<?> getConverterByType(GameMailAttachmentType type) {
    return converters.get(type);
  }
}
