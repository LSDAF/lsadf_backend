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

import lombok.experimental.UtilityClass;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/** Utility class for response creation */
@UtilityClass
public class ResponseUtils {
  /**
   * Builds a response
   *
   * @param status HTTP status of the response
   * @param message Human readable message status
   * @param responseObj Object to return
   * @param <T> Type of object to return
   * @return ResponseEntity of GenericResponse containing all inputs
   */
  public static <T> ResponseEntity<GenericResponse<T>> generateResponse(
      HttpStatus status, String message, Object responseObj) {
    GenericResponse response = generateGenericResponse(status, message, responseObj);
    return new ResponseEntity<>(response, status);
  }

  /**
   * Builds a response
   *
   * @param status HTTP status of the response
   * @param responseObj Object to return
   * @param <T> Type of object to return
   * @return ResponseEntity of GenericResponse containing all inputs
   */
  public static <T> ResponseEntity<GenericResponse<T>> generateResponse(
      HttpStatus status, Object responseObj) {
    return generateResponse(status, null, responseObj);
  }

  /**
   * Builds a response
   *
   * @param status HTTP status of the response
   * @param <T> Type of object to return
   * @return ResponseEntity of GenericResponse containing all inputs
   */
  public static <T> ResponseEntity<GenericResponse<T>> generateResponse(HttpStatus status) {
    return generateResponse(status, null, null);
  }

  /**
   * private method to build a GenericResponse
   *
   * @param status HTTP status of the response
   * @param message Human-readable message status
   * @param responseObj Object to return
   * @param <T> Type of object to return
   * @return GenericResponse containing all inputs
   */
  private static <T> GenericResponse<T> generateGenericResponse(
      HttpStatus status, String message, T responseObj) {
    return GenericResponse.<T>builder()
        .status(status.value())
        .message(message)
        .data(responseObj)
        .build();
  }
}
