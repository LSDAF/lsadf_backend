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
package com.lsadf.core.infra.web.request.common;

import static com.lsadf.core.infra.web.request.common.Filter.Attributes.TYPE;
import static com.lsadf.core.infra.web.request.common.Filter.Attributes.VALUE;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import java.io.Serial;
import java.io.Serializable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Filter implements Serializable {

  @Serial private static final long serialVersionUID = -9160944941964752810L;

  @Schema(description = "Type of filter", example = "name")
  @JsonProperty(value = TYPE)
  @NotNull
  private String type;

  @Schema(description = "Value of filter", example = "toto")
  @JsonProperty(value = VALUE)
  @NotNull
  private String value;

  @NoArgsConstructor(access = AccessLevel.PRIVATE)
  public static final class Attributes {
    public static final String TYPE = "type";
    public static final String VALUE = "value";
  }
}
