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

package com.lsadf.application.bdd.step_definition.when;

import static com.lsadf.core.bdd.ParameterizedTypeReferenceUtils.buildParameterizedUserInfoResponse;

import com.lsadf.application.bdd.BddLoader;
import com.lsadf.application.controller.constant.ApiPathConstants;
import com.lsadf.application.controller.user.UserController;
import com.lsadf.bdd.util.BddUtils;
import com.lsadf.core.infra.web.dto.response.ApiResponse;
import com.lsadf.core.infra.web.dto.response.jwt.JwtAuthenticationResponse;
import com.lsadf.core.infra.web.dto.response.user.UserInfoResponse;
import io.cucumber.java.en.When;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

@Slf4j(topic = "[USER INFO WHEN STEP DEFINITIONS]")
public class BddWhenUserInfoStepDefinitions extends BddLoader {

  @When("^the user requests the endpoint to get his UserInfo$")
  public void whenUserRequestsEndpointToGetUserInfo() {
    String fullPath = ApiPathConstants.USER + UserController.Constants.ApiPaths.ME;

    String url = BddUtils.buildUrl(this.serverPort, fullPath);
    try {

      JwtAuthenticationResponse jwtAuthenticationResponse = jwtAuthenticationResponseStack.peek();
      String token = jwtAuthenticationResponse.accessToken();
      HttpHeaders headers = new HttpHeaders();
      headers.setBearerAuth(token);
      HttpEntity<Void> request = new HttpEntity<>(headers);
      ResponseEntity<ApiResponse<UserInfoResponse>> result =
          testRestTemplate.exchange(
              url, HttpMethod.GET, request, buildParameterizedUserInfoResponse());
      ApiResponse<UserInfoResponse> body = result.getBody();
      responseStack.push(body);
      log.info("Response: {}", result);

    } catch (Exception e) {
      exceptionStack.push(e);
    }
  }
}
