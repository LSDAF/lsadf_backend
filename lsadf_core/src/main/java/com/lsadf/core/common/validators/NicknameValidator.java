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
package com.lsadf.core.common.validators;

import com.lsadf.core.common.validators.annotations.Nickname;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class NicknameValidator implements ConstraintValidator<Nickname, String> {
  private static final int MIN_LENGTH = 3;
  private static final int MAX_LENGTH = 16;

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    // If nickname is null, then should return false
    if (value == null) {
      return false;
    }

    if (value.length() < MIN_LENGTH || value.length() > MAX_LENGTH) {
      return false;
    }

    return value.matches("^[a-zA-Z0-9-]*$");
  }
}
