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
package com.lsadf.core.domain.game.stage;

import com.lsadf.core.shared.model.Model;
import java.io.Serial;
import java.util.Objects;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ToString
public class Stage implements Model {

  @Serial private static final long serialVersionUID = -7126306428235414817L;

  private Long currentStage;

  private Long maxStage;

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) return false;
    Stage stage = (Stage) o;
    return Objects.equals(currentStage, stage.currentStage)
        && Objects.equals(maxStage, stage.maxStage);
  }

  @Override
  public int hashCode() {
    return Objects.hash(currentStage, maxStage);
  }
}
