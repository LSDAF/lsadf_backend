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
package com.lsadf.core.unit.utils;

import static org.assertj.core.api.Assertions.assertThat;

import com.lsadf.core.infra.util.StringUtils;
import org.junit.jupiter.api.Test;

class StringUtilsTests {

  @Test
  void should_capitalize_string() {
    // given
    String str = "test";

    // when
    String result = StringUtils.capitalize(str);

    // then
    assertThat(result).isEqualTo("Test");
  }

  @Test
  void should_return_empty_string_when_input_is_empty() {
    // given
    String str = "";

    // when
    String result = StringUtils.capitalize(str);

    // then
    assertThat(result).isEmpty();
  }

  @Test
  void should_return_null_if_input_is_null() {
    // given
    String str = null;

    // when
    String result = StringUtils.capitalize(str);

    // then
    assertThat(result).isNull();
  }

  @Test
  void should_return_capitalized_string_if_already_capitalized() {
    // given
    String str = "Test";

    // when
    String result = StringUtils.capitalize(str);

    // then
    assertThat(result).isEqualTo("Test");
  }
}
