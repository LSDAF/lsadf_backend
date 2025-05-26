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

import static com.lsadf.core.infra.web.http_clients.HttpClientTypes.DEFAULT;

import feign.Contract;
import feign.Logger;
import feign.Request;
import feign.Retryer;
import feign.codec.Decoder;
import feign.codec.Encoder;
import feign.codec.ErrorDecoder;
import java.util.concurrent.TimeUnit;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.FeignClientProperties;
import org.springframework.cloud.openfeign.support.ResponseEntityDecoder;
import org.springframework.cloud.openfeign.support.SpringDecoder;
import org.springframework.cloud.openfeign.support.SpringEncoder;
import org.springframework.cloud.openfeign.support.SpringMvcContract;
import org.springframework.context.annotation.Bean;

public class CommonFeignConfiguration {

  @Bean
  public Decoder feignDecoder() {
    return new ResponseEntityDecoder(new SpringDecoder(HttpMessageConverters::new));
  }

  @Bean
  OptionalFeignFormatterRegistrar optionalFeignFormatterRegistrar() {
    return new OptionalFeignFormatterRegistrar();
  }

  @Bean
  public ErrorDecoder errorDecoder() {
    return new CommonErrorDecoder();
  }

  @Bean
  public Retryer retryer() {
    return new Retryer.Default(100, 1000, 3);
  }

  @Bean
  public Encoder feignEncoder() {
    return new SpringEncoder(HttpMessageConverters::new);
  }

  @Bean
  public Logger.Level feignLoggerLevel(FeignClientProperties feignClientProperties) {
    return feignClientProperties.getConfig().get(DEFAULT).getLoggerLevel();
  }

  @Bean
  public Request.Options feignRequestOptions(FeignClientProperties feignClientProperties) {
    FeignClientProperties.FeignClientConfiguration defaultConfig =
        feignClientProperties.getConfig().get(DEFAULT);

    return new Request.Options(
        defaultConfig.getConnectTimeout(),
        TimeUnit.MILLISECONDS,
        defaultConfig.getReadTimeout(),
        TimeUnit.MILLISECONDS,
        true);
  }

  @Bean
  public Contract feignContract() {
    return new SpringMvcContract();
  }
}
