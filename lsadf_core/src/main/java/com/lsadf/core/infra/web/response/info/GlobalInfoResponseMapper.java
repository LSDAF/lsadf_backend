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

package com.lsadf.core.infra.web.response.info;

import com.lsadf.core.domain.info.GlobalInfo;
import com.lsadf.core.infra.web.response.ModelResponseMapper;

/**
 * A mapper that transforms a {@link GlobalInfo} model object into a {@link GlobalInfoResponse}
 * object. This is useful for converting internal model data structures into response
 * representations suitable for API communication.
 *
 * <p>This class implements the {@link ModelResponseMapper} interface, providing a specific mapping
 * logic for {@link GlobalInfo} to {@link GlobalInfoResponse}.
 */
public class GlobalInfoResponseMapper
    implements ModelResponseMapper<GlobalInfo, GlobalInfoResponse> {
  /**
   * Maps a GlobalInfo model object to a GlobalInfoResponse object.
   *
   * @param model the GlobalInfo model object containing the data to be transformed into a
   *     GlobalInfoResponse
   * @return a GlobalInfoResponse object populated with data from the given GlobalInfo model
   */
  @Override
  public GlobalInfoResponse mapToResponse(GlobalInfo model) {
    return GlobalInfoResponse.builder()
        .now(model.now())
        .gameSaveCounter(model.gameSaveCounter())
        .userCounter(model.userCounter())
        .build();
  }
}
