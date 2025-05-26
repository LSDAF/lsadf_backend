/*
 * Copyright 2024-2025 LSDAF
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package com.lsadf.core.infra.web.http_clients;

import com.lsadf.core.infra.exceptions.http.*;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CommonErrorDecoder implements ErrorDecoder {

  private final ErrorDecoder defaultErrorDecoder = new Default();

  @Override
  public Exception decode(String methodKey, Response response) {
    log.error("{} HTTP ERROR {} {}", methodKey, response.status(), response.reason());
    return switch (response.status()) {
      case 400 -> new BadRequestException(response.reason());
      case 401 -> new UnauthorizedException(response.reason());
      case 403 -> new ForbiddenException(response.reason());
      case 404 -> new NotFoundException(response.reason());
      case 500 -> new InternalServerErrorException(response.reason());
      default -> defaultErrorDecoder.decode(methodKey, response);
    };
  }
}
