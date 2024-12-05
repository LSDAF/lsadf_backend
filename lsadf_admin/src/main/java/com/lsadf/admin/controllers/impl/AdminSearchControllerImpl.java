package com.lsadf.admin.controllers.impl;

import static com.lsadf.core.utils.ResponseUtils.generateResponse;

import com.lsadf.admin.controllers.AdminSearchController;
import com.lsadf.core.controllers.impl.BaseController;
import com.lsadf.core.mappers.Mapper;
import com.lsadf.core.models.GameSave;
import com.lsadf.core.models.User;
import com.lsadf.core.requests.game_save.GameSaveSortingParameter;
import com.lsadf.core.requests.search.SearchRequest;
import com.lsadf.core.requests.user.UserSortingParameter;
import com.lsadf.core.responses.GenericResponse;
import com.lsadf.core.services.SearchService;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.RestController;

/** The implementation of the AdminSearchController */
@Slf4j
@RestController
public class AdminSearchControllerImpl extends BaseController implements AdminSearchController {

  private final SearchService searchService;
  private final Mapper mapper;

  @Autowired
  public AdminSearchControllerImpl(SearchService searchService, Mapper mapper) {
    this.searchService = searchService;
    this.mapper = mapper;
  }

  /** {@inheritDoc} */
  @Override
  public Logger getLogger() {
    return log;
  }

  /**
   * {@inheritDoc}
   *
   * @return
   */
  @Override
  public ResponseEntity<GenericResponse<List<User>>> searchUsers(
      Jwt jwt, SearchRequest searchRequest, List<String> orderBy) {
    List<UserSortingParameter> sortingParameterList =
        Collections.singletonList(UserSortingParameter.NONE);
    if (orderBy != null && !orderBy.isEmpty()) {
      sortingParameterList = orderBy.stream().map(UserSortingParameter::fromString).toList();
    }
    validateUser(jwt);
    try (Stream<User> userStream = searchService.searchUsers(searchRequest, sortingParameterList)) {
      List<User> users = userStream.toList();
      return generateResponse(HttpStatus.OK, users);
    }
  }

  /**
   * {@inheritDoc}
   *
   * @return
   */
  @Override
  public ResponseEntity<GenericResponse<List<GameSave>>> searchGameSaves(
      Jwt jwt, SearchRequest searchRequest, List<String> orderBy) {
    List<GameSaveSortingParameter> gameSaveOrderBy =
        Collections.singletonList(GameSaveSortingParameter.NONE);
    if (orderBy != null && !orderBy.isEmpty()) {
      gameSaveOrderBy = orderBy.stream().map(GameSaveSortingParameter::valueOf).toList();
    }
    validateUser(jwt);
    try (Stream<GameSave> gameSaveStream =
        searchService.searchGameSaves(searchRequest, gameSaveOrderBy)) {
      List<GameSave> gameSaves = gameSaveStream.toList();
      return generateResponse(HttpStatus.OK, gameSaves);
    }
  }
}
