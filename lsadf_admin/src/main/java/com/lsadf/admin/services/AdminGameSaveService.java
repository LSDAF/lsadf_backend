package com.lsadf.admin.services;

import com.lsadf.admin.utils.FilterUtils;
import com.lsadf.core.mappers.Mapper;
import com.lsadf.core.models.GameSave;
import com.lsadf.core.requests.game_save.GameSaveSortingParameter;
import com.lsadf.core.services.GameSaveService;
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
public class AdminGameSaveService implements ListService<GameSave> {

  private final GameSaveService gameSaveService;
  private final Mapper mapper;

  public AdminGameSaveService(GameSaveService gameSaveService, Mapper mapper) {
    this.gameSaveService = gameSaveService;
    this.mapper = mapper;
  }

  @Override
  @Nonnull
  @NonNull
  public List<@Nonnull GameSave> list(
      @NonNull Pageable pageable, @jakarta.annotation.Nullable @Nullable Filter filter) {
    Stream<GameSave> gameSaveStream =
        gameSaveService.getGameSaves().map(mapper::mapGameSaveEntityToGameSave);

    // Filter the stream
    if (filter != null) {
      gameSaveStream = FilterUtils.applyFilters(gameSaveStream, filter);
    }

    // Sort the stream

    // map pageable to a list of GameSaveSortingParameter
    List<GameSaveSortingParameter> sortingParameters =
        pageable.getSort().stream().map(GameSaveSortingParameter::fromOrder).toList();

    gameSaveStream = StreamUtils.sortGameSaves(gameSaveStream, sortingParameters);

    // Paginate the stream
    return gameSaveStream.skip(pageable.getOffset()).limit(pageable.getPageSize()).toList();
  }

  @Nonnull
  public GameSave get(@Nonnull String id) {
    var gameSave = gameSaveService.getGameSave(id);
    return mapper.mapGameSaveEntityToGameSave(gameSave);
  }
}
