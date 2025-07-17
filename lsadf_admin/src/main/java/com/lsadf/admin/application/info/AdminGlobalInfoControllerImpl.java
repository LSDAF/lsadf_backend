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
package com.lsadf.admin.application.info;

import static com.lsadf.core.infra.web.response.ResponseUtils.generateResponse;

import com.lsadf.core.application.info.GlobalInfoService;
import com.lsadf.core.domain.info.GlobalInfo;
import com.lsadf.core.infra.web.controller.BaseController;
import com.lsadf.core.infra.web.response.ApiResponse;
import com.lsadf.core.infra.web.response.info.GlobalInfoResponse;
import com.lsadf.core.infra.web.response.info.GlobalInfoResponseMapper;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.RestController;

/** The implementation of the AdminGlobalInfoController */
@RestController
@Slf4j
public class AdminGlobalInfoControllerImpl extends BaseController
    implements AdminGlobalInfoController {

  private final GlobalInfoService globalInfoService;
  private static final GlobalInfoResponseMapper globalInfoResponseMapper =
      GlobalInfoResponseMapper.INSTANCE;

  @Autowired
  public AdminGlobalInfoControllerImpl(GlobalInfoService globalInfoService) {
    this.globalInfoService = globalInfoService;
  }

  /** {@inheritDoc} */
  @Override
  public Logger getLogger() {
    return log;
  }

  /** {@inheritDoc} */
  @Override
  public ResponseEntity<ApiResponse<GlobalInfoResponse>> getGlobalInfo(Jwt jwt) {
    validateUser(jwt);
    GlobalInfo globalInfo = globalInfoService.getGlobalInfo();
    GlobalInfoResponse response = globalInfoResponseMapper.map(globalInfo);
    return generateResponse(HttpStatus.OK, response);
  }
}
