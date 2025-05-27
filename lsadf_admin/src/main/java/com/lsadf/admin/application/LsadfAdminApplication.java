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
package com.lsadf.admin.application;

import com.lsadf.admin.application.configuration.LsadfAdminConfiguration;
import com.lsadf.core.infra.config.ApplicationUtils;
import java.net.UnknownHostException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Import;

/** Main class for the admin application */
@SpringBootApplication
@Import(LsadfAdminConfiguration.class)
@Slf4j
public class LsadfAdminApplication {
  public static void main(String[] args) throws UnknownHostException {
    SpringApplication application = new SpringApplication(LsadfAdminApplication.class);
    ConfigurableApplicationContext context = application.run(args);
    ApplicationUtils.printAccessUrl(context, log);
  }
}
