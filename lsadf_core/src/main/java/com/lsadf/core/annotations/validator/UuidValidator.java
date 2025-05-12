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
package com.lsadf.core.annotations.validator;

import com.lsadf.core.annotations.Uuid;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.UUID;

public class UuidValidator implements ConstraintValidator<Uuid, String> {

  private boolean nullable;

  @Override
  public void initialize(Uuid constraintAnnotation) {
    this.nullable = constraintAnnotation.nullable();
  }

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    // Check if the value is a valid UUID
    if (value == null) {
      return nullable;
    }
    try {
      UUID.fromString(value);
      return true;
    } catch (IllegalArgumentException e) {
      return false;
    }
  }
}
