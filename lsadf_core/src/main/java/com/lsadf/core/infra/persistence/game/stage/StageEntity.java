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
package com.lsadf.core.infra.persistence.game.stage;

import static com.lsadf.core.infra.persistence.game.stage.StageEntity.StageEntityAttribute.STAGE_ENTITY;
import static com.lsadf.core.infra.persistence.game.stage.StageEntity.StageEntityAttribute.STAGE_USER_EMAIL;

import com.lsadf.core.domain.game.stage.validation.StageConsistency;
import com.lsadf.core.infra.persistence.config.EntityAttributes;
import com.lsadf.core.infra.persistence.game.game_save.GameSaveEntity;
import jakarta.persistence.*;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.Positive;
import java.io.Serial;
import lombok.*;

@Data
@Entity(name = STAGE_ENTITY)
@Table(name = STAGE_ENTITY)
@Builder
@AllArgsConstructor
@ToString(callSuper = true)
@StageConsistency(currentStageField = "currentStage", maxStageField = "maxStage")
public class StageEntity implements com.lsadf.core.infra.persistence.Entity {

  @Serial private static final long serialVersionUID = -5093458201484300006L;

  @Id
  @Column(name = EntityAttributes.ID)
  private String id;

  @OneToOne(fetch = FetchType.LAZY)
  @MapsId
  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  private GameSaveEntity gameSave;

  @Column(name = STAGE_USER_EMAIL)
  private String userEmail;

  @Column(name = StageEntityAttribute.STAGE_CURRENT)
  @Positive
  @Builder.Default
  private Long currentStage = 1L;

  @Column(name = StageEntityAttribute.STAGE_MAX)
  @Positive
  @Builder.Default
  private Long maxStage = 1L;

  protected StageEntity() {
    super();
  }

  @NoArgsConstructor(access = AccessLevel.PRIVATE)
  public static final class StageEntityAttribute {
    public static final String STAGE_ENTITY = "t_stage";
    public static final String STAGE_CURRENT = "current_stage";
    public static final String STAGE_MAX = "max_stage";
    public static final String STAGE_USER_EMAIL = "user_email";
  }
}
