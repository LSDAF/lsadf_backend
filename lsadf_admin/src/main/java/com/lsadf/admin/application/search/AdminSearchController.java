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
package com.lsadf.admin.application.search;

import static com.lsadf.core.infra.web.controller.ControllerConstants.Params.ORDER_BY;
import static com.lsadf.core.infra.web.controller.ControllerConstants.Swagger.Authentications.BEARER_AUTHENTICATION;
import static com.lsadf.core.infra.web.controller.ControllerConstants.Swagger.Authentications.OAUTH2_AUTHENTICATION;

import com.fasterxml.jackson.annotation.JsonView;
import com.lsadf.core.domain.user.User;
import com.lsadf.core.infra.web.controller.Controller;
import com.lsadf.core.infra.web.controller.ControllerConstants;
import com.lsadf.core.infra.web.controller.JsonViews;
import com.lsadf.core.infra.web.request.search.SearchRequest;
import com.lsadf.core.infra.web.responses.ApiResponse;
import com.lsadf.core.infra.web.responses.ResponseMessages;
import com.lsadf.core.infra.web.responses.game.game_save.GameSaveResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequestMapping(value = ControllerConstants.ADMIN_SEARCH)
@Tag(name = ControllerConstants.Swagger.ADMIN_SEARCH_CONTROLLER)
@SecurityRequirement(name = BEARER_AUTHENTICATION)
@SecurityRequirement(name = OAUTH2_AUTHENTICATION)
public interface AdminSearchController extends Controller {

  /**
   * Searches for users in function of the given search criteria
   *
   * @param jwt the requester JWT
   * @param searchRequest the search criteria
   * @param orderBy the sorting order if any
   * @return the list of users
   */
  @ApiResponses(
      value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "401",
            description = ResponseMessages.UNAUTHORIZED),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "403",
            description = ResponseMessages.FORBIDDEN),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = ResponseMessages.OK),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "400",
            description = ResponseMessages.BAD_REQUEST),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "500",
            description = ResponseMessages.INTERNAL_SERVER_ERROR)
      })
  @PostMapping(value = ControllerConstants.AdminSearch.SEARCH_USERS)
  @Operation(summary = "Searches for users in function of the give search criteria")
  @JsonView(JsonViews.Admin.class)
  ResponseEntity<ApiResponse<List<User>>> searchUsers(
      @AuthenticationPrincipal Jwt jwt,
      @Valid @RequestBody(required = false) SearchRequest searchRequest,
      @RequestParam(value = ORDER_BY, required = false) List<String> orderBy);

  /**
   * Searches for game saves in function of the given search criteria
   *
   * @param jwt the requester JWT
   * @param searchRequest the search criteria
   * @param orderBy
   * @return the list of game saves
   */
  @ApiResponses(
      value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "401",
            description = ResponseMessages.UNAUTHORIZED),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "403",
            description = ResponseMessages.FORBIDDEN),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = ResponseMessages.OK),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "400",
            description = ResponseMessages.BAD_REQUEST),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "500",
            description = ResponseMessages.INTERNAL_SERVER_ERROR)
      })
  @PostMapping(value = ControllerConstants.AdminSearch.SEARCH_GAME_SAVES)
  @Operation(summary = "Searches for game saves in function of the give search criteria")
  @JsonView(JsonViews.Admin.class)
  ResponseEntity<ApiResponse<List<GameSaveResponse>>> searchGameSaves(
      @AuthenticationPrincipal Jwt jwt,
      @Valid @RequestBody(required = false) SearchRequest searchRequest,
      @RequestParam(value = ORDER_BY, required = false) List<String> orderBy);
}
