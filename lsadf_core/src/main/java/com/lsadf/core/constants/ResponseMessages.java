package com.lsadf.core.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/** Constant class containing all response messages for the application */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ResponseMessages {
  public static final String OK = "OK";
  public static final String BAD_REQUEST = "Bad Request - The request was invalid";
  public static final String UNAUTHORIZED =
      "Unauthorized - Authentication credentials were missing or incorrect.";
  public static final String FORBIDDEN =
      "Forbidden - The user does not have permission to perform this operation";
  public static final String INTERNAL_SERVER_ERROR = "Internal Server Error";
  public static final String NOT_FOUND = "Not Found";
}
