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
package com.lsadf.admin.application.ui.game_save;

import com.lsadf.admin.util.FilterUtils;
import com.lsadf.core.application.game.game_save.GameSaveService;
import com.lsadf.core.domain.game.GameSave;
import com.lsadf.core.infra.utils.StreamUtils;
import com.lsadf.core.infra.web.requests.game.game_save.GameSaveSortingParameter;
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
public class AdminGameSaveHillaUiService implements ListService<GameSave> {

  private final GameSaveService gameSaveService;

  public AdminGameSaveHillaUiService(GameSaveService gameSaveService) {
    this.gameSaveService = gameSaveService;
  }

  @Override
  @Nonnull
  @NonNull
  public List<@Nonnull GameSave> list(
      @NonNull Pageable pageable, @jakarta.annotation.Nullable @Nullable Filter filter) {
    Stream<GameSave> gameSaveStream = gameSaveService.getGameSaves();

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
    return gameSaveService.getGameSave(id);
  }
}
