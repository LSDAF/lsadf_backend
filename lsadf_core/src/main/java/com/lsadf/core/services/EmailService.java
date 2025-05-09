package com.lsadf.core.services;

public interface EmailService {
  /**
   * Sends a user validation email.
   *
   * @param email the email to send the email to
   * @param token the token to send
   */
  void sendUserValidationEmail(String email, String token);
}
