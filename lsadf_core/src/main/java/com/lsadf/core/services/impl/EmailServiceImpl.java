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
package com.lsadf.core.services.impl;

import com.lsadf.core.constants.ControllerConstants;
import com.lsadf.core.properties.EmailProperties;
import com.lsadf.core.properties.ServerProperties;
import com.lsadf.core.services.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.io.StringWriter;
import lombok.extern.slf4j.Slf4j;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

@Slf4j
public class EmailServiceImpl implements EmailService {

  private static final String VERIFICATION_LINK = "verificationLink";
  private static final String EMAIL_VERIFICATION_TEMPLATE_NAME = "templates/user_verification.vm";

  private final VelocityEngine velocityEngine;
  private final JavaMailSender emailSender;
  private final EmailProperties emailProperties;
  private final ServerProperties serverProperties;

  public EmailServiceImpl(
      VelocityEngine velocityEngine,
      JavaMailSender emailSender,
      EmailProperties emailProperties,
      ServerProperties serverProperties) {
    this.velocityEngine = velocityEngine;
    this.emailSender = emailSender;
    this.emailProperties = emailProperties;
    this.serverProperties = serverProperties;
  }

  /** {@inheritDoc} */
  @Override
  public void sendUserValidationEmail(String email, String token) {
    log.info("Sending user validation email to {}", email);

    MimeMessage message = emailSender.createMimeMessage();
    try {
      MimeMessageHelper helper = new MimeMessageHelper(message, true);
      helper.setTo(email);
      helper.setSubject("LSADF Gaming - Account validation");
      String from = emailProperties.getUsername();
      helper.setFrom(from);
      helper.setTo(email);

      VelocityContext context = new VelocityContext();

      String verificationLink = buildVerificationLink(token, serverProperties);

      context.put(VERIFICATION_LINK, verificationLink);
      StringWriter stringWriter = new StringWriter();
      Template template = velocityEngine.getTemplate(EMAIL_VERIFICATION_TEMPLATE_NAME);
      template.merge(context, stringWriter);
      helper.setText(stringWriter.toString(), true);

      emailSender.send(message);

    } catch (MessagingException e) {
      log.error("Error while creating email message", e);
    }
  }

  /**
   * Builds the verification link.
   *
   * @param token the token to use
   * @param serverProperties the server properties
   * @return the verification link
   */
  private static String buildVerificationLink(String token, ServerProperties serverProperties) {
    StringBuilder sb = new StringBuilder();
    sb.append(serverProperties.isHttps() ? "https://" : "http://");
    sb.append(serverProperties.getHostName());
    sb.append(":");
    sb.append(serverProperties.getPort());
    String endpoint =
        ControllerConstants.AUTH
            + ControllerConstants.Auth.VALIDATE_TOKEN.replace("{token}", token);
    sb.append(endpoint);

    return sb.toString();
  }
}
