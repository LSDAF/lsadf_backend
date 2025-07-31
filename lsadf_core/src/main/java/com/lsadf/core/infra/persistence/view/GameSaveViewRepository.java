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

package com.lsadf.core.infra.persistence.view;

import static com.lsadf.core.infra.persistence.table.game.save.metadata.GameMetadataEntity.GameSaveMetadataAttributes.GAME_METADATA_USER_EMAIL;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface GameSaveViewRepository
    extends org.springframework.data.repository.Repository<GameSaveViewEntity, UUID> {

  @Query("select * from v_game_save_vgsa")
  Stream<GameSaveViewEntity> findAllGameSaves();

  @Query("select * from v_game_save_vgsa where tgme_id=:tgme_id")
  Optional<GameSaveViewEntity> findGameSaveEntityById(UUID id);

  @Query("select * from v_game_save_vgsa tgme where tgme_user_email = :tgme_user_email")
  Stream<GameSaveViewEntity> findGameSaveEntitiesByUserEmail(
      @Param(GAME_METADATA_USER_EMAIL) String userEmail);
}
