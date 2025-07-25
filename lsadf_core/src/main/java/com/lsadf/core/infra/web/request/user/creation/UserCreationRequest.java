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

package com.lsadf.core.infra.web.request.user.creation;

import com.lsadf.core.infra.web.request.Request;
import java.util.List;

public interface UserCreationRequest extends Request {
  String getFirstName();

  String getLastName();

  String getPassword();

  String getUsername();

  Boolean getEnabled();

  Boolean getEmailVerified();

  List<String> getUserRoles();
}
