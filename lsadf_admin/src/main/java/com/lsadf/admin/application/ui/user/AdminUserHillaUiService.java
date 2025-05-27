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
package com.lsadf.admin.application.ui.user;

import com.lsadf.admin.application.utils.FilterUtils;
import com.lsadf.core.application.user.UserService;
import com.lsadf.core.domain.user.User;
import com.lsadf.core.infra.utils.StreamUtils;
import com.lsadf.core.infra.web.requests.user.UserSortingParameter;
import com.vaadin.hilla.BrowserCallable;
import com.vaadin.hilla.Nonnull;
import com.vaadin.hilla.Nullable;
import com.vaadin.hilla.crud.ListService;
import com.vaadin.hilla.crud.filter.Filter;
import jakarta.annotation.security.RolesAllowed;
import java.util.List;
import java.util.stream.Stream;
import lombok.NonNull;
import org.springframework.data.domain.Pageable;

@BrowserCallable
@RolesAllowed("ADMIN")
public class AdminUserHillaUiService implements ListService<User> {
  private final UserService userService;

  public AdminUserHillaUiService(UserService userService) {
    this.userService = userService;
  }

  @Override
  @NonNull
  @Nonnull
  public List<@Nonnull User> list(
      @NonNull Pageable pageable, @jakarta.annotation.Nullable @Nullable Filter filter) {
    Stream<User> userStream = userService.getUsers();

    // Filter the stream
    if (filter != null) {
      userStream = FilterUtils.applyFilters(userStream, filter);
    }

    // map the pageable into a list of sorting parameters
    List<UserSortingParameter> sortingParameters =
        pageable.getSort().stream().map(UserSortingParameter::fromOrder).toList();

    // Sort the stream
    userStream = StreamUtils.sortUsers(userStream, sortingParameters);

    // Paginate the stream
    return userStream.skip(pageable.getOffset()).limit(pageable.getPageSize()).toList();
  }
}
