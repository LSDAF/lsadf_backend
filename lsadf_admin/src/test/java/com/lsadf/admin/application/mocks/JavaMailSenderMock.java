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
package com.lsadf.admin.application.mocks;

import jakarta.mail.internet.MimeMessage;
import java.util.Stack;
import org.jetbrains.annotations.NotNull;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

public class JavaMailSenderMock extends JavaMailSenderImpl implements JavaMailSender {

  private final Stack<MimeMessage> mimeMessageStack;

  public JavaMailSenderMock(Stack<MimeMessage> mimeMessageStack) {
    this.mimeMessageStack = mimeMessageStack;
  }

  @Override
  public void send(@NotNull MimeMessage mimeMessage) throws MailException {
    this.mimeMessageStack.push(mimeMessage);
  }
}
