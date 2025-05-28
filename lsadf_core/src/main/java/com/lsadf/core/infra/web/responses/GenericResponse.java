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
package com.lsadf.core.infra.web.responses;

import static com.lsadf.core.infra.web.responses.GenericResponse.Attributes.*;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonView;
import com.lsadf.core.infra.web.controllers.JsonViews;
import java.io.Serial;
import java.io.Serializable;
import lombok.*;

/**
 * Generic POJO Response for all API controllers
 *
 * @param <T> Type of object to return
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({STATUS, MESSAGE, DATA})
public class GenericResponse<T> implements Serializable {
  @Serial private static final long serialVersionUID = 5392685232533641077L;

  @JsonProperty(value = STATUS)
  @JsonView(JsonViews.External.class)
  private int status;

  @JsonProperty(value = MESSAGE)
  @JsonView(JsonViews.External.class)
  private String message;

  @JsonProperty(value = DATA)
  @JsonView(JsonViews.External.class)
  private transient T data;

  @NoArgsConstructor(access = AccessLevel.PRIVATE)
  public static final class Attributes implements Serializable {
    public static final String STATUS = "status";
    public static final String MESSAGE = "message";
    public static final String DATA = "data";
  }
}
