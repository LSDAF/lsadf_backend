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

import com.lsadf.core.domain.game.GameSave;
import com.lsadf.core.domain.game.characteristics.Characteristics;
import com.lsadf.core.domain.game.currency.Currency;
import com.lsadf.core.domain.game.inventory.item.Item;
import com.lsadf.core.domain.game.stage.Stage;
import com.lsadf.core.domain.info.GlobalInfo;
import com.lsadf.core.domain.user.User;
import com.lsadf.core.domain.user.UserInfo;
import com.lsadf.core.infra.web.config.auth.JwtAuthentication;
import com.lsadf.core.infra.web.responses.GenericResponse;
import java.util.List;
import java.util.Set;
import lombok.experimental.UtilityClass;
import org.springframework.core.ParameterizedTypeReference;

/** Utility class for building ParameterizedTypeReferences */
@UtilityClass
public class ParameterizedTypeReferenceUtils {

  /**
   * Builds a ParameterizedTypeReference for a GenericResponse of UserInfo
   *
   * @return ParameterizedTypeReference
   */
  public static ParameterizedTypeReference<GenericResponse<UserInfo>>
      buildParameterizedUserInfoResponse() {
    return new ParameterizedTypeReference<>() {};
  }

  /**
   * Builds a ParameterizedTypeReference for a GenericResponse of JwtAuthentication
   *
   * @return ParameterizedTypeReference
   */
  public static ParameterizedTypeReference<GenericResponse<JwtAuthentication>>
      buildParameterizedJwtAuthenticationResponse() {
    return new ParameterizedTypeReference<>() {};
  }

  /**
   * Builds a ParameterizedTypeReference for a GenericResponse of GameSave
   *
   * @return ParameterizedTypeReference
   */
  public static ParameterizedTypeReference<GenericResponse<GameSave>>
      buildParameterizedGameSaveResponse() {
    return new ParameterizedTypeReference<>() {};
  }

  /**
   * Builds a ParameterizedTypeReference for a GenericResponse of Characteristics
   *
   * @return ParameterizedTypeReference
   */
  public static ParameterizedTypeReference<GenericResponse<Characteristics>>
      buildParameterizedCharacteristicsResponse() {
    return new ParameterizedTypeReference<>() {};
  }

  /**
   * Builds a ParameterizedTypeReference for a GenericResponse of Currency
   *
   * @return ParameterizedTypeReference
   */
  public static ParameterizedTypeReference<GenericResponse<Currency>>
      buildParameterizedCurrencyResponse() {
    return new ParameterizedTypeReference<>() {};
  }

  /**
   * Builds a ParameterizedTypeReference for a GenericResponse containing a Set of Item objects.
   *
   * @return a ParameterizedTypeReference representing a GenericResponse with a Set of Item objects
   *     as its data type
   */
  public static ParameterizedTypeReference<GenericResponse<Set<Item>>>
      buildParameterizedItemSetResponse() {
    return new ParameterizedTypeReference<>() {};
  }

  /**
   * Builds a ParameterizedTypeReference for a GenericResponse of Item
   *
   * @return ParameterizedTypeReference
   */
  public static ParameterizedTypeReference<GenericResponse<Item>> buildParameterizedItemResponse() {
    return new ParameterizedTypeReference<>() {};
  }

  /**
   * Builds a ParameterizedTypeReference for a GenericResponse of Stage
   *
   * @return ParameterizedTypeReference
   */
  public static ParameterizedTypeReference<GenericResponse<Stage>>
      buildParameterizedStageResponse() {
    return new ParameterizedTypeReference<>() {};
  }

  /**
   * Builds a ParameterizedTypeReference for a GenericResponse of Void
   *
   * @return ParameterizedTypeReference
   */
  public static ParameterizedTypeReference<GenericResponse<Void>> buildParameterizedVoidResponse() {
    return new ParameterizedTypeReference<>() {};
  }

  /**
   * Builds a ParameterizedTypeReference for a GenericResponse of List of GameSave
   *
   * @return ParameterizedTypeReference
   */
  public static ParameterizedTypeReference<GenericResponse<List<GameSave>>>
      buildParameterizedGameSaveListResponse() {
    return new ParameterizedTypeReference<>() {};
  }

  /**
   * Builds a ParameterizedTypeReference for a GenericResponse of List of GameSaveEntity
   *
   * @return ParameterizedTypeReference
   */
  public static ParameterizedTypeReference<GenericResponse<Boolean>>
      buildParameterizedBooleanResponse() {
    return new ParameterizedTypeReference<>() {};
  }

  /**
   * Builds a ParameterizedTypeReference for a GenericResponse of GlobalInfo
   *
   * @return ParameterizedTypeReference
   */
  public static ParameterizedTypeReference<GenericResponse<GlobalInfo>>
      buildParameterizedGlobalInfoResponse() {
    return new ParameterizedTypeReference<>() {};
  }

  /**
   * Builds a ParameterizedTypeReference for a GenericResponse of list of User
   *
   * @return ParameterizedTypeReference
   */
  public static ParameterizedTypeReference<GenericResponse<List<User>>>
      buildParameterizedUserListResponse() {
    return new ParameterizedTypeReference<>() {};
  }

  /**
   * Builds a ParameterizedTypeReference for a GenericResponse of User
   *
   * @return ParameterizedTypeReference
   */
  public static ParameterizedTypeReference<GenericResponse<User>> buildParamaterizedUserResponse() {
    return new ParameterizedTypeReference<>() {};
  }
}
