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
package com.lsadf.core.infra.web.dto.response;

import static com.lsadf.core.infra.web.JsonAttributes.*;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serial;
import java.io.Serializable;
import lombok.Builder;
import org.jspecify.annotations.Nullable;

/**
 * Generic POJO Response for all API controllers
 *
 * @param <T> Type of object to return
 */
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiResponse<T>(
    @JsonProperty(value = STATUS) int status,
    @Nullable @JsonProperty(value = MESSAGE) String message,
    @Nullable @JsonProperty(value = DATA) T data)
    implements Serializable {
  @Serial private static final long serialVersionUID = 5392685232533641077L;
}
