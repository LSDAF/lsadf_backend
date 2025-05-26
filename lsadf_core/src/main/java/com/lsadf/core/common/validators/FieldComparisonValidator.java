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

import com.lsadf.core.common.validators.annotations.StageConsistency;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.BeanWrapperImpl;

public class FieldComparisonValidator implements ConstraintValidator<StageConsistency, Object> {

  private String firstField;
  private String secondField;

  @Override
  public void initialize(StageConsistency constraintAnnotation) {
    this.firstField = String.valueOf(constraintAnnotation.currentStageField());
    this.secondField = String.valueOf(constraintAnnotation.maxStageField());
  }

  @Override
  public boolean isValid(Object value, ConstraintValidatorContext constraintValidatorContext) {
    try {
      Object firstFieldValue = new BeanWrapperImpl(value).getPropertyValue(firstField);
      Object secondFieldValue = new BeanWrapperImpl(value).getPropertyValue(secondField);

      if (firstFieldValue == null || secondFieldValue == null) {
        return true; // Null values can be considered valid or you can adjust the logic here
      }

      if (firstFieldValue instanceof Comparable c1 && secondFieldValue instanceof Comparable c2) {
        return c1.compareTo(c2) <= 0;
      }

      return false;
    } catch (Exception e) {
      return false;
    }
  }
}
