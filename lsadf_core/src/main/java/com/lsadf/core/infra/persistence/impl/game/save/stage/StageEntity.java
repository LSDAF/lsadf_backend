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
package com.lsadf.core.infra.persistence.impl.game.save.stage;

import static com.lsadf.core.infra.persistence.impl.game.save.metadata.GameMetadataEntity.GameSaveMetadataAttributes.GAME_METADATA_ID;
import static com.lsadf.core.infra.persistence.impl.game.save.stage.StageEntity.StageEntityAttributes.*;
import static com.lsadf.core.infra.persistence.impl.game.save.stage.StageEntity.StageEntityAttributes.STAGE_ENTITY;

import java.io.Serial;
import java.util.UUID;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Builder
@AllArgsConstructor
@Getter
@Setter
@Table(STAGE_ENTITY)
public class StageEntity implements com.lsadf.core.infra.persistence.Entity {

  @Serial private static final long serialVersionUID = -5093458201484300006L;

  @Id
  @Column(GAME_METADATA_ID)
  private UUID id;

  @Column(STAGE_CURRENT_STAGE)
  private Long currentStage;

  @Column(STAGE_MAX_STAGE)
  private Long maxStage;

  @Column(STAGE_WAVE)
  private Long wave;

  @NoArgsConstructor(access = AccessLevel.PRIVATE)
  public static final class StageEntityAttributes {
    public static final String STAGE_ENTITY = "t_stage_tgst";
    public static final String STAGE_CURRENT_STAGE = "tgst_current_stage";
    public static final String STAGE_MAX_STAGE = "tgst_max_stage";
    public static final String STAGE_WAVE = "tgst_wave";
  }
}
