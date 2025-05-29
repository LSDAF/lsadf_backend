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
package com.lsadf.admin.application.bdd;

import com.lsadf.core.domain.game.characteristics.Characteristics;
import com.lsadf.core.domain.game.currency.Currency;
import com.lsadf.core.domain.game.inventory.item.Item;
import com.lsadf.core.domain.game.stage.Stage;
import com.lsadf.core.domain.info.GlobalInfo;
import com.lsadf.core.domain.user.User;
import com.lsadf.core.domain.user.UserInfo;
import com.lsadf.core.infra.web.clients.keycloak.response.JwtAuthenticationResponse;
import com.lsadf.core.infra.web.responses.ApiResponse;
import com.lsadf.core.infra.web.responses.game.game_save.GameSaveResponse;
import java.util.List;
import java.util.Set;
import lombok.experimental.UtilityClass;
import org.springframework.core.ParameterizedTypeReference;

/** Utility class for building ParameterizedTypeReferences */
@UtilityClass
public class ParameterizedTypeReferenceUtils {

  /**
   * Builds a ParameterizedTypeReference for a ApiResponse of UserInfo
   *
   * @return ParameterizedTypeReference
   */
  public static ParameterizedTypeReference<ApiResponse<UserInfo>>
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
   * Builds a ParameterizedTypeReference for a ApiResponse of Characteristics
   *
   * @return ParameterizedTypeReference
   */
  public static ParameterizedTypeReference<ApiResponse<Characteristics>>
      buildParameterizedCharacteristicsResponse() {
    return new ParameterizedTypeReference<>() {};
  }

  /**
   * Builds a ParameterizedTypeReference for a ApiResponse of Currency
   *
   * @return ParameterizedTypeReference
   */
  public static ParameterizedTypeReference<ApiResponse<Currency>>
      buildParameterizedCurrencyResponse() {
    return new ParameterizedTypeReference<>() {};
  }

  /**
   * Builds a ParameterizedTypeReference for a ApiResponse containing a Set of Item objects.
   *
   * @return a ParameterizedTypeReference representing a ApiResponse with a Set of Item objects as
   *     its data type
   */
  public static ParameterizedTypeReference<ApiResponse<Set<Item>>>
      buildParameterizedItemSetResponse() {
    return new ParameterizedTypeReference<>() {};
  }

  /**
   * Builds a ParameterizedTypeReference for a ApiResponse of Item
   *
   * @return ParameterizedTypeReference
   */
  public static ParameterizedTypeReference<ApiResponse<Item>> buildParameterizedItemResponse() {
    return new ParameterizedTypeReference<>() {};
  }

  /**
   * Builds a ParameterizedTypeReference for a ApiResponse of Stage
   *
   * @return ParameterizedTypeReference
   */
  public static ParameterizedTypeReference<ApiResponse<Stage>> buildParameterizedStageResponse() {
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
   * Builds a ParameterizedTypeReference for a ApiResponse of List of GameSave
   *
   * @return ParameterizedTypeReference
   */
  public static ParameterizedTypeReference<ApiResponse<List<GameSaveResponse>>>
      buildParameterizedGameSaveListResponse() {
    return new ParameterizedTypeReference<>() {};
  }

  /**
   * Builds a ParameterizedTypeReference for a ApiResponse of List of GameSaveEntity
   *
   * @return ParameterizedTypeReference
   */
  public static ParameterizedTypeReference<ApiResponse<Boolean>>
      buildParameterizedBooleanResponse() {
    return new ParameterizedTypeReference<>() {};
  }

  /**
   * Builds a ParameterizedTypeReference for a ApiResponse of GlobalInfo
   *
   * @return ParameterizedTypeReference
   */
  public static ParameterizedTypeReference<ApiResponse<GlobalInfo>>
      buildParameterizedGlobalInfoResponse() {
    return new ParameterizedTypeReference<>() {};
  }

  /**
   * Builds a ParameterizedTypeReference for a ApiResponse of list of User
   *
   * @return ParameterizedTypeReference
   */
  public static ParameterizedTypeReference<ApiResponse<List<User>>>
      buildParameterizedUserListResponse() {
    return new ParameterizedTypeReference<>() {};
  }

  /**
   * Builds a ParameterizedTypeReference for a ApiResponse of User
   *
   * @return ParameterizedTypeReference
   */
  public static ParameterizedTypeReference<ApiResponse<User>> buildParamaterizedUserResponse() {
    return new ParameterizedTypeReference<>() {};
  }
}
