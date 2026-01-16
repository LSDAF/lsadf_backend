/*
 * Copyright © 2024-2026 LSDAF
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
package com.lsadf.application;

import com.lsadf.config.LsadfConfiguration;
import com.lsadf.core.infra.config.ApplicationUtils;
import com.lsadf.util.ProfileValidator;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Import;

/** Main class for the application */
@SpringBootApplication
@Import(LsadfConfiguration.class)
@Slf4j
public class LsadfApplication {

  public static void main(String[] args) throws UnknownHostException {
    SpringApplication application = new SpringApplication(LsadfApplication.class);
    ConfigurableApplicationContext context = application.run(args);

    // Perform profile validation again to ensure exactly one required profile is active
    List<String> activeProfiles =
        Arrays.stream(context.getEnvironment().getActiveProfiles()).toList();
    ProfileValidator.validateApplicationMode(activeProfiles);

    ApplicationUtils.printAccessUrl(context, log);
  }
}
