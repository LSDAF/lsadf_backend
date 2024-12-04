package com.lsadf.admin.services;

import com.lsadf.admin.utils.FilterUtils;
import com.lsadf.core.mappers.Mapper;
import com.lsadf.core.models.User;
import com.lsadf.core.requests.user.UserSortingParameter;
import com.lsadf.core.services.UserService;
import com.lsadf.core.utils.StreamUtils;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.hilla.BrowserCallable;
import com.vaadin.hilla.Nonnull;
import com.vaadin.hilla.Nullable;
import com.vaadin.hilla.crud.ListService;
import com.vaadin.hilla.crud.filter.Filter;
import java.util.List;
import java.util.stream.Stream;
import lombok.NonNull;
import org.springframework.data.domain.Pageable;

@BrowserCallable
@AnonymousAllowed
public class AdminUserService implements ListService<User> {
  private final UserService userService;
  private final Mapper mapper;

  public AdminUserService(UserService userService, Mapper mapper) {
    this.userService = userService;
    this.mapper = mapper;
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
