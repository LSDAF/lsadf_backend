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
package com.lsadf.core.bdd;

import com.lsadf.core.infra.web.response.ApiResponse;
import com.lsadf.core.infra.web.response.game.inventory.ItemResponse;
import com.lsadf.core.infra.web.response.game.save.GameSaveResponse;
import com.lsadf.core.infra.web.response.game.save.characteristics.CharacteristicsResponse;
import com.lsadf.core.infra.web.response.game.save.currency.CurrencyResponse;
import com.lsadf.core.infra.web.response.game.save.stage.StageResponse;
import com.lsadf.core.infra.web.response.info.GlobalInfoResponse;
import com.lsadf.core.infra.web.response.jwt.JwtAuthenticationResponse;
import com.lsadf.core.infra.web.response.user.UserInfoResponse;
import com.lsadf.core.infra.web.response.user.UserResponse;
import java.util.List;
import java.util.Set;
import lombok.experimental.UtilityClass;
import org.springframework.core.ParameterizedTypeReference;

/**
 * Consolidated utility class for building ParameterizedTypeReferences Contains methods used across
 * all modules for BDD testing
 */
@UtilityClass
public class ParameterizedTypeReferenceUtils {

  /**
   * Builds a ParameterizedTypeReference for a ApiResponse of UserInfoResponse
   *
   * @return ParameterizedTypeReference
   */
  public static ParameterizedTypeReference<ApiResponse<UserInfoResponse>>
      buildParameterizedUserInfoResponse() {
    return new ParameterizedTypeReference<>() {};
  }

  /**
   * Builds a ParameterizedTypeReference for a ApiResponse of JwtAuthenticationResponse
   *
   * @return ParameterizedTypeReference
   */
  public static ParameterizedTypeReference<ApiResponse<JwtAuthenticationResponse>>
      buildParameterizedJwtAuthenticationResponse() {
    return new ParameterizedTypeReference<>() {};
  }

  /**
   * Builds a ParameterizedTypeReference for a ApiResponse of GameSaveResponse
   *
   * @return ParameterizedTypeReference
   */
  public static ParameterizedTypeReference<ApiResponse<GameSaveResponse>>
      buildParameterizedGameSaveResponse() {
    return new ParameterizedTypeReference<>() {};
  }

  /**
   * Builds a ParameterizedTypeReference for a ApiResponse of CharacteristicsResponse
   *
   * @return ParameterizedTypeReference
   */
  public static ParameterizedTypeReference<ApiResponse<CharacteristicsResponse>>
      buildParameterizedCharacteristicsResponse() {
    return new ParameterizedTypeReference<>() {};
  }

  /**
   * Builds a ParameterizedTypeReference for a ApiResponse of CurrencyResponse
   *
   * @return ParameterizedTypeReference
   */
  public static ParameterizedTypeReference<ApiResponse<CurrencyResponse>>
      buildParameterizedCurrencyResponse() {
    return new ParameterizedTypeReference<>() {};
  }

  /**
   * Builds a ParameterizedTypeReference for a ApiResponse containing a Set of ItemResponse objects.
   *
   * @return ParameterizedTypeReference representing a response with a Set of ItemResponse objects
   *     wrapped in a ApiResponse.
   */
  public static ParameterizedTypeReference<ApiResponse<Set<ItemResponse>>>
      buildParameterizedItemSetResponse() {
    return new ParameterizedTypeReference<>() {};
  }

  /**
   * Builds a ParameterizedTypeReference for a ApiResponse of ItemResponse
   *
   * @return ParameterizedTypeReference
   */
  public static ParameterizedTypeReference<ApiResponse<ItemResponse>>
      buildParameterizedItemResponse() {
    return new ParameterizedTypeReference<>() {};
  }

  /**
   * Builds a ParameterizedTypeReference for a ApiResponse of StageResponse
   *
   * @return ParameterizedTypeReference
   */
  public static ParameterizedTypeReference<ApiResponse<StageResponse>>
      buildParameterizedStageResponse() {
    return new ParameterizedTypeReference<>() {};
  }

  /**
   * Builds a ParameterizedTypeReference for a ApiResponse of Void
   *
   * @return ParameterizedTypeReference
   */
  public static ParameterizedTypeReference<ApiResponse<Void>> buildParameterizedVoidResponse() {
    return new ParameterizedTypeReference<>() {};
  }

  /**
   * Builds a ParameterizedTypeReference for a ApiResponse of List of GameSaveResponse
   *
   * @return ParameterizedTypeReference
   */
  public static ParameterizedTypeReference<ApiResponse<List<GameSaveResponse>>>
      buildParameterizedGameSaveListResponse() {
    return new ParameterizedTypeReference<>() {};
  }

  /**
   * Builds a ParameterizedTypeReference for a ApiResponse of Boolean
   *
   * @return ParameterizedTypeReference
   */
  public static ParameterizedTypeReference<ApiResponse<Boolean>>
      buildParameterizedBooleanResponse() {
    return new ParameterizedTypeReference<>() {};
  }

  /**
   * Builds a ParameterizedTypeReference for a ApiResponse of GlobalInfoResponse
   *
   * @return ParameterizedTypeReference
   */
  public static ParameterizedTypeReference<ApiResponse<GlobalInfoResponse>>
      buildParameterizedGlobalInfoDtoResponse() {
    return new ParameterizedTypeReference<>() {};
  }

  /**
   * Builds a ParameterizedTypeReference for a ApiResponse containing a list of UserResponse
   * objects.
   *
   * @return a ParameterizedTypeReference representing an ApiResponse with a List of UserResponse
   *     objects as its data type
   */
  public static ParameterizedTypeReference<ApiResponse<List<UserResponse>>>
      buildParameterizedUserResponseList() {
    return new ParameterizedTypeReference<>() {};
  }

  /**
   * Builds a ParameterizedTypeReference for a ApiResponse of UserResponse
   *
   * @return ParameterizedTypeReference
   */
  public static ParameterizedTypeReference<ApiResponse<UserResponse>>
      buildParamaterizedUserDtoResponse() {
    return new ParameterizedTypeReference<>() {};
  }
}
