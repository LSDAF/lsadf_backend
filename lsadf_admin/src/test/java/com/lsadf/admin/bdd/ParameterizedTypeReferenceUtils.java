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
package com.lsadf.admin.bdd;

import com.lsadf.core.models.*;
import com.lsadf.core.responses.GenericResponse;
import java.util.List;
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
   * Builds a ParameterizedTypeReference for a GenericResponse of Inventory
   *
   * @return ParameterizedTypeReference
   */
  public static ParameterizedTypeReference<GenericResponse<Inventory>>
      buildParameterizedInventoryResponse() {
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
