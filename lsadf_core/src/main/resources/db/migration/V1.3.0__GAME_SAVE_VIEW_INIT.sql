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

CREATE VIEW v_game_save_vgsa AS
SELECT tgme.tgme_id,
       tgme.tgme_created_at,
       tgme.tgme_updated_at,
       tgme.tgme_user_email,
       tgme.tgme_nickname,

       -- Characteristics
       tgch.tgch_attack,
       tgch.tgch_crit_chance,
       tgch.tgch_crit_damage,
       tgch.tgch_health,
       tgch.tgch_resistance,

       -- Currency amounts
       tgcu.tgcu_gold_amount,
       tgcu.tgcu_diamond_amount,
       tgcu.tgcu_emerald_amount,
       tgcu.tgcu_amethyst_amount,

       -- Stage progression
       tgst.tgst_current_stage,
       tgst.tgst_max_stage

FROM t_game_metadata_tgme tgme
         LEFT JOIN t_characteristics_tgch tgch ON tgme.tgme_id = tgch.tgme_id
         LEFT JOIN t_currency_tgcu tgcu ON tgme.tgme_id = tgcu.tgme_id
         LEFT JOIN t_stage_tgst tgst ON tgme.tgme_id = tgst.tgme_id;
