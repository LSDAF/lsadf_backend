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
package com.lsadf.core.infra.web.controller.advice;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lsadf.core.infra.exception.DynamicJsonViewException;
import com.lsadf.core.infra.web.config.security.properties.JsonViewProperties;
import com.lsadf.core.infra.web.controller.JsonViews;
import com.lsadf.core.infra.web.response.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@ControllerAdvice
public class DynamicJsonViewAdvice implements ResponseBodyAdvice<Object> {

  private final ObjectMapper objectMapper;
  private final JsonViewProperties jsonViewProperties;

  @Autowired
  public DynamicJsonViewAdvice(ObjectMapper objectMapper, JsonViewProperties jsonViewProperties) {
    this.objectMapper = objectMapper;
    this.jsonViewProperties = jsonViewProperties;

    // init objectMapper typeReference registers
    objectMapper.getTypeFactory().constructType(ApiResponse.class);
  }

  @Override
  public boolean supports(
      MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
    return returnType.hasMethodAnnotation(JsonView.class);
  }

  @Override
  public Object beforeBodyWrite(
      Object body,
      MethodParameter returnType,
      MediaType selectedContentType,
      Class<? extends HttpMessageConverter<?>> selectedConverterType,
      ServerHttpRequest request,
      ServerHttpResponse response) {
    if (body == null) {
      return null;
    }

    // Check if we should override the JSON view
    if (jsonViewProperties.isDefaultJsonViewEnabled()) {
      JsonViews.JsonViewType viewType = jsonViewProperties.getDefaultJsonView();
      Class<?> viewClass =
          switch (viewType) {
            case ADMIN -> JsonViews.Admin.class;
            case INTERNAL -> JsonViews.Internal.class;
            case EXTERNAL -> JsonViews.External.class;
          };

      try {
        ApiResponse<Object> apiResponse = (ApiResponse<Object>) body;
        Object data = apiResponse.getData();
        String json = objectMapper.writerWithView(viewClass).writeValueAsString(data);
        JsonNode jsonNode = objectMapper.readTree(json);
        apiResponse.setData(jsonNode);
        return apiResponse;
      } catch (Exception e) {
        throw new DynamicJsonViewException("Failed to apply JSON view", e);
      }
    }

    // Otherwise, use the view specified in the @JsonView annotation on the method
    return body;
  }
}
