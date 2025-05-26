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
package com.lsadf.controllers;

import com.lsadf.core.auth.JwtAuthentication;
import com.lsadf.core.constants.ControllerConstants;
import com.lsadf.core.web.responses.GenericResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping(value = ControllerConstants.OAUTH2)
@Tag(name = ControllerConstants.Swagger.OAUTH_2_CONTROLLER)
public interface OAuth2Controller {

  String CODE = "code";

  /**
   * Handle OAuth2 callback
   *
   * @param code OAuth2 code
   * @return GenericResponse with JwtAuthentication
   */
  @GetMapping(value = ControllerConstants.OAuth2.CALLBACK)
  @Operation(summary = "Handles the OAuth2 callback", hidden = true)
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "OK"),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "404", description = "Not Found"),
        @ApiResponse(responseCode = "500", description = "Internal Server Error")
      })
  ResponseEntity<GenericResponse<JwtAuthentication>> handleOAuth2Callback(String code);
}
