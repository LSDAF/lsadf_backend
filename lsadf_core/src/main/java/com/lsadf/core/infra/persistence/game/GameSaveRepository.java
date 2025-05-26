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
package com.lsadf.core.infra.persistence.game;

import java.util.Optional;
import java.util.stream.Stream;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/** Repository class for GameSaveEntity */
@Repository
public interface GameSaveRepository
    extends CrudRepository<GameSaveEntity, String>,
        PagingAndSortingRepository<GameSaveEntity, String> {
  @Query("select gs from t_game_save gs")
  Stream<GameSaveEntity> findAllGameSaves();

  Optional<GameSaveEntity> findGameSaveEntityByNickname(String nickname);

  @Query("select gs from t_game_save gs where gs.userEmail = :userId")
  Stream<GameSaveEntity> findGameSaveEntitiesByUserEmail(String userId);
}
