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

package com.lsadf.core.infra.persistence.impl.view;

import static com.lsadf.core.infra.persistence.impl.view.GameMailViewEntity.GameMailViewEntityAttributes.*;

import com.lsadf.core.infra.persistence.JdbcRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface GameMailViewRepository extends JdbcRepository<GameMailViewEntity> {
  @Query("SELECT * FROM v_game_mail_instance_vgmi")
  List<GameMailViewEntity> findAll();

  @Query("SELECT * FROM v_game_mail_instance_vgmi WHERE tgmi_id = :tgmi_id")
  Optional<GameMailViewEntity> findById(@Param(GAME_MAIL_VIEW_ID) String id);

  @Query("SELECT * FROM v_game_mail_instance_vgmi WHERE tgme_id = :tgme_id")
  List<GameMailViewEntity> findByGameSaveId(@Param(GAME_MAIL_VIEW_GAME_SAVE_ID) String id);

  @Query("SELECT * FROM v_game_mail_instance_vgmi WHERE tgmt_id = :tgmt_id")
  List<GameMailViewEntity> findByMailTemplateId(@Param(GAME_MAIL_VIEW_TEMPLATE_ID) String id);
}
