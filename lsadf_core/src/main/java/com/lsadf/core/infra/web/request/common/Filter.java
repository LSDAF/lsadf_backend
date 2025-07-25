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

import static com.lsadf.core.infra.web.JsonAttributes.TYPE;
import static com.lsadf.core.infra.web.JsonAttributes.VALUE;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import java.io.Serial;
import java.io.Serializable;
import lombok.*;

/**
 * Represents a filter with specified type and value. This record is used to define filtering
 * criteria for various search or query purposes.
 *
 * <p>This class is immutable and serializable. It may be used in scenarios where filtering is
 * needed based on a key (type) and a corresponding value.
 *
 * <p>Attributes: - type: Defines the type or key of the filter. - value: The corresponding value
 * for the given filter type.
 *
 * <p>Annotations: - {@link Builder}: Enables builder pattern for this class. - {@link
 * JsonProperty}: Specifies serialized property names. - {@link Schema}: Describes schema details
 * for documentation.
 *
 * <p>Implements: - {@link Serializable}: Allows objects of this class to be serialized for storage
 * or transmission.
 */
@Builder
public record Filter(
    @Schema(description = "Type of filter", example = "name") @JsonProperty(value = TYPE) @NotNull
        String type,
    @Schema(description = "Value of filter", example = "toto") @JsonProperty(value = VALUE) @NotNull
        String value)
    implements Serializable {

  @Serial private static final long serialVersionUID = -9160944941964752810L;
}
