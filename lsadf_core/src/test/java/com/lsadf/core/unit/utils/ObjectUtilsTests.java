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
package com.lsadf.core.unit.utils;

import static org.assertj.core.api.Assertions.assertThat;

import com.lsadf.core.infra.util.ObjectUtils;
import org.junit.jupiter.api.Test;

class ObjectUtilsTests {
  @Test
  void should_return_defaulted_value_on_null() {
    Object object = null;
    Object defaultValue = "default";
    Object result = ObjectUtils.getOrDefault(object, defaultValue);
    assertThat(defaultValue).isEqualTo(result);
  }

  @Test
  void should_return_original_value_on_not_null() {
    Object object = "test";
    Object defaultValue = "default";
    Object result = ObjectUtils.getOrDefault(object, defaultValue);
    assertThat(object).isEqualTo(result);
  }
}
