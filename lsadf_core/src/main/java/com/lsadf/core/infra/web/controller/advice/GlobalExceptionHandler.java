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

import static com.lsadf.core.infra.web.dto.response.ResponseUtils.generateResponse;

import com.lsadf.core.exception.AlreadyExistingGameSaveException;
import com.lsadf.core.exception.AlreadyExistingItemClientIdException;
import com.lsadf.core.exception.AlreadyExistingUserException;
import com.lsadf.core.exception.AlreadyTakenNicknameException;
import com.lsadf.core.exception.http.*;
import com.lsadf.core.infra.web.dto.response.ApiResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

  /**
   * Exception handler for MethodArgumentNotValidException
   *
   * @param e MethodArgumentNotValidException
   * @return ResponseEntity containing the errors
   */
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ApiResponse<Map<String, String>>> handleMethodArgumentNotValidException(
      MethodArgumentNotValidException e) {
    log.error("MethodArgumentNotValidException: ", e);
    Map<String, String> fieldErrorMap = new HashMap<>();
    e.getFieldErrors()
        .forEach(
            error -> {
              String field = error.getField();
              String msg = error.getDefaultMessage();
              fieldErrorMap.put(field, msg);
            });
    String message = "Validation failed for the following fields";
    return generateResponse(HttpStatus.BAD_REQUEST, message, fieldErrorMap);
  }

  /**
   * Exception handler for IllegalArgumentException
   *
   * @param e IllegalArgumentException
   * @return ResponseEntity containing the error
   */
  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<ApiResponse<Void>> handleIllegalArgumentException(
      IllegalArgumentException e) {
    log.error("IllegalArgumentException: ", e);
    return generateResponse(HttpStatus.BAD_REQUEST, "Illegal argument: " + e.getMessage(), null);
  }

  /**
   * Exception handler for AlreadyExistingGameSaveException
   *
   * @param e AlreadyExistingGameSaveException
   * @return ResponseEntity containing the error
   */
  @ExceptionHandler(HandlerMethodValidationException.class)
  public ResponseEntity<ApiResponse<Map<String, String>>> handleValidationException(
      HandlerMethodValidationException e) {
    log.error("HandlerMethodValidationException: ", e);
    Map<String, String> fieldErrorMap = new HashMap<>();
    e.getAllValidationResults()
        .forEach(
            parameterValidationResult -> {
              String rejectedValue =
                  Objects.requireNonNull(parameterValidationResult.getArgument()).toString();
              parameterValidationResult
                  .getResolvableErrors()
                  .forEach(
                      resolvableError -> {
                        String msg = resolvableError.getDefaultMessage();
                        fieldErrorMap.put(rejectedValue, msg);
                      });
            });
    return generateResponse(HttpStatus.BAD_REQUEST, "Fields validation failed", fieldErrorMap);
  }

  /**
   * Exception handler for HttpMessageNotReadableException
   *
   * @param e HttpMessageNotReadableException
   * @return ResponseEntity containing the error
   */
  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<ApiResponse<Void>> handleHttpMessageNotReadableException(
      HttpMessageNotReadableException e) {
    log.error("HttpMessageNotReadableException: ", e);
    String msg = e.getMessage();
    return generateResponse(HttpStatus.BAD_REQUEST, "Message not readable: " + msg, null);
  }

  /**
   * Exception handler for UnauthorizedException
   *
   * @param e UnauthorizedException
   * @return ResponseEntity containing the error
   */
  @ExceptionHandler(UnauthorizedException.class)
  public ResponseEntity<ApiResponse<Void>> handleUnauthorizedException(UnauthorizedException e) {
    log.error("UnauthorizedException: ", e);
    return generateResponse(HttpStatus.UNAUTHORIZED, "Unauthorized: " + e.getMessage(), null);
  }

  /**
   * Exception handler for NotFoundException
   *
   * @param e NotFoundException
   * @return ResponseEntity containing the error
   */
  @ExceptionHandler(NotFoundException.class)
  public ResponseEntity<ApiResponse<Void>> handleNotFoundException(NotFoundException e) {
    log.error("NotFoundException: ", e);
    return generateResponse(HttpStatus.NOT_FOUND, "Not found: " + e.getMessage(), null);
  }

  /**
   * Exception handler for ForbiddenException
   *
   * @param e ForbiddenException
   * @return ResponseEntity containing the error
   */
  @ExceptionHandler(ForbiddenException.class)
  public ResponseEntity<ApiResponse<Void>> handleForbiddenException(ForbiddenException e) {
    log.error("ForbiddenException: ", e);
    return generateResponse(HttpStatus.FORBIDDEN, "Forbidden: " + e.getMessage(), null);
  }

  /**
   * Exception handler for InternalServerErrorException
   *
   * @param e InternalServerErrorException
   * @return ResponseEntity containing the error
   */
  @ExceptionHandler(InternalServerErrorException.class)
  public ResponseEntity<ApiResponse<Void>> handleInternalServerErrorException(
      InternalServerErrorException e) {
    log.error("InternalServerErrorException: ", e);
    return generateResponse(
        HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error: " + e.getMessage(), null);
  }

  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  public ResponseEntity<ApiResponse<Void>> handleMethodArgumentTypeMismatchException(
      MethodArgumentTypeMismatchException e) {
    log.error("MethodArgumentTypeMismatchException: " + e);
    return generateResponse(
        HttpStatus.BAD_REQUEST,
        "Method argument type mismatch: " + e.getName() + " " + e.getValue(),
        null);
  }

  /**
   * Exception handler for BadRequestException
   *
   * @param e BadRequestException
   * @return ResponseEntity containing the error
   */
  @ExceptionHandler(BadRequestException.class)
  public ResponseEntity<ApiResponse<Void>> handleBadRequestException(BadRequestException e) {
    log.error("BadRequestException: ", e);
    return generateResponse(HttpStatus.BAD_REQUEST, "Bad request: " + e.getMessage(), null);
  }

  /**
   * Exception handler for AlreadyExistingGameSaveException
   *
   * @param e AlreadyExistingGameSaveException
   * @return ResponseEntity containing the error
   */
  @ExceptionHandler(AlreadyExistingGameSaveException.class)
  public ResponseEntity<ApiResponse<Void>> handleAlreadyExistingGameSaveException(
      AlreadyExistingGameSaveException e) {
    log.error("AlreadyExistingGameSaveException: ", e);
    return generateResponse(
        HttpStatus.BAD_REQUEST, "Game save already exists: " + e.getMessage(), null);
  }

  /**
   * Exception handler for AlreadyExistingItemClientIdException
   *
   * @param e AlreadyExistingItemClientIdException
   * @return ResponseEntity containing the error
   */
  @ExceptionHandler(AlreadyExistingItemClientIdException.class)
  public ResponseEntity<ApiResponse<Void>> handleAlreadyExistingItemClientIdException(
      AlreadyExistingItemClientIdException e) {
    log.error("AlreadyExistingItemClientIdException: ", e);
    return generateResponse(
        HttpStatus.BAD_REQUEST, "Item client id already exists: " + e.getMessage(), null);
  }

  /**
   * Exception handler for AlreadyExistingUserException
   *
   * @param e AlreadyExistingUserException
   * @return ResponseEntity containing the error
   */
  @ExceptionHandler(AlreadyExistingUserException.class)
  public ResponseEntity<ApiResponse<Void>> handleAlreadyExistingUserException(
      AlreadyExistingUserException e) {
    log.error("AlreadyExistingUserException: ", e);
    return generateResponse(HttpStatus.BAD_REQUEST, "User already exists: " + e.getMessage(), null);
  }

  /**
   * Exception handler for AlreadyTakenNicknameException
   *
   * @param e AlreadyTakenNicknameException
   * @return ResponseEntity containing the error
   */
  @ExceptionHandler(AlreadyTakenNicknameException.class)
  public ResponseEntity<ApiResponse<Void>> handleAlreadyTakenNicknameException(
      AlreadyTakenNicknameException e) {
    log.error("AlreadyTakenNicknameException: ", e);
    return generateResponse(
        HttpStatus.BAD_REQUEST, "Nickname already taken: " + e.getMessage(), null);
  }

  /**
   * Generic Exception handler for Exception
   *
   * @param e Exception
   * @return ResponseEntity containing the error
   */
  @ExceptionHandler(Exception.class)
  public ResponseEntity<ApiResponse<Void>> handleException(Exception e) {
    log.error("Exception: " + e);
    return generateResponse(
        HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error: " + e.getMessage(), null);
  }
}
