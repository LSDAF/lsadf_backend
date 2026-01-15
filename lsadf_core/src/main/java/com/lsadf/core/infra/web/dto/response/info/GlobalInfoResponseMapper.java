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
package com.lsadf.core.infra.web.dto.response.info;

import com.lsadf.core.domain.info.GlobalInfo;
import com.lsadf.core.infra.web.dto.response.ModelResponseMapper;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * Mapper interface for converting {@link GlobalInfo} model objects into {@link GlobalInfoResponse}
 * representation objects.
 *
 * <p>This interface extends {@link ModelResponseMapper} to define the mapping logic specific to
 * transforming {@code GlobalInfo} domain model objects into their API response format, {@code
 * GlobalInfoResponse}. It enables consistent and reusable mapping strategies using the MapStruct
 * library.
 *
 * <p>The primary use case for this interface is to facilitate the mapping of backend data
 * structures to API responses in a way that supports immutability and serialization requirements.
 *
 * <p>The {@code GlobalInfoResponseMapper} provides a singleton instance via the {@code INSTANCE}
 * field, which is automatically generated and initialized by MapStruct.
 *
 * <p>Key features include: - Supports automatic mapping of fields between {@code GlobalInfo} and
 * {@code GlobalInfoResponse}, provided their names and types are compatible. - Enables
 * extensibility via MapStruct annotations for custom mapping configurations or advanced use cases.
 *
 * <p>This interface is utilized in the context of controller implementations to convert data
 * retrieved from services into format-ready response objects that comply with API contracts.
 *
 * <p>Example usage context includes scenarios where controllers (such as {@code
 * AdminGlobalInfoControllerImpl}) need a lightweight and efficient conversion of domain objects
 * into API responses.
 */
@Mapper
public interface GlobalInfoResponseMapper
    extends ModelResponseMapper<GlobalInfo, GlobalInfoResponse> {

  GlobalInfoResponseMapper INSTANCE = Mappers.getMapper(GlobalInfoResponseMapper.class);

  /**
   * Maps a GlobalInfo model object to a GlobalInfoResponse object.
   *
   * @param model the GlobalInfo model object containing the data to be transformed into a
   *     GlobalInfoResponse
   * @return a GlobalInfoResponse object populated with data from the given GlobalInfo model
   */
  @Override
  GlobalInfoResponse map(GlobalInfo model);
}
