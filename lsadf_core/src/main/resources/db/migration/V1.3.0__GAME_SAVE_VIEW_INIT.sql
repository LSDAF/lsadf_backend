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
SELECT tgsa.id,
       tgsa.created_at,
       tgsa.updated_at,
       tgsa.user_email,
       tgsa.nickname,

       -- Characteristics
       tgch.attack,
       tgch.crit_chance,
       tgch.crit_damage,
       tgch.health,
       tgch.resistance,

       -- Currency amounts
       tgcu.gold_amount,
       tgcu.diamond_amount,
       tgcu.emerald_amount,
       tgcu.amethyst_amount,

       -- Stage progression
       tgst.current_stage,
       tgst.max_stage

FROM t_game_save_tgsa tgsa
         LEFT JOIN t_characteristics_tgch tgch ON tgsa.id = tgch.id
         LEFT JOIN t_currency_tgcu tgcu ON tgsa.id = tgcu.id
         LEFT JOIN t_stage_tgst tgst ON tgsa.id = tgst.id;
