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
package com.lsadf.core.infra.web.clients.keycloak;

import com.lsadf.core.infra.web.clients.CommonFeignConfiguration;
import com.lsadf.core.infra.web.clients.HttpClientTypes;
import com.lsadf.core.infra.web.clients.keycloak.response.JwtAuthenticationResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@FeignClient(
    name = HttpClientTypes.KEYCLOAK,
    configuration = CommonFeignConfiguration.class,
    primary = false)
public interface KeycloakClient {

  String REALM = "realm";
  String TOKEN_ENDPOINT = "/realms/{realm}/protocol/openid-connect/token";

  @PostMapping(
      path = TOKEN_ENDPOINT,
      consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  JwtAuthenticationResponse getToken(
      @PathVariable(value = REALM) String realm, @RequestBody String body);
}
