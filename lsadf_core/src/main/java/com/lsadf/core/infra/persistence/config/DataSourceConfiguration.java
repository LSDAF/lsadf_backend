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
package com.lsadf.core.infra.persistence.config;

import com.lsadf.core.infra.persistence.config.properties.DataSourceProperties;
import javax.sql.DataSource;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/** Configuration class for the data source. */
@Configuration
public class DataSourceConfiguration {

  @Bean
  public DataSource dataSource(DataSourceProperties dataSourceProperties) {
    DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
    dataSourceBuilder.url(dataSourceProperties.getUrl());
    dataSourceBuilder.username(dataSourceProperties.getUsername());
    dataSourceBuilder.password(dataSourceProperties.getPassword());

    return dataSourceBuilder.build();
  }
}
