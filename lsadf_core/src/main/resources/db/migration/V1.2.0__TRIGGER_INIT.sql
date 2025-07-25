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


CREATE OR REPLACE FUNCTION update_game_save_timestamp()
    RETURNS TRIGGER AS
$$
DECLARE
    game_save_uuid UUID;
BEGIN
    -- Determine the game save ID based on the table
    CASE TG_TABLE_NAME
        WHEN 't_characteristics_tgch', 't_currency_tgcu', 't_stage_tgst' THEN game_save_uuid := COALESCE(NEW.id, OLD.id);
        WHEN 't_item_tgit' THEN game_save_uuid := COALESCE(NEW.tgsa_id, OLD.tgsa_id);
        WHEN 't_additional_stat_tias' THEN SELECT tgit.tgsa_id
                                           INTO game_save_uuid
                                           FROM t_item_tgit tgit
                                           WHERE tgit.id = COALESCE(NEW.tgit_id, OLD.tgit_id);
        END CASE;

    -- Update the game save timestamp
    UPDATE t_game_save_tgsa
    SET updated_at = CURRENT_TIMESTAMP
    WHERE id = game_save_uuid;

    RETURN COALESCE(NEW, OLD);
END;
$$ LANGUAGE plpgsql;


-- Apply to all child tables
CREATE TRIGGER trg_characteristics_update_game_save
    AFTER UPDATE
    ON t_characteristics_tgch
    FOR EACH ROW
EXECUTE FUNCTION update_game_save_timestamp();

CREATE TRIGGER trg_currency_update_game_save
    AFTER UPDATE
    ON t_currency_tgcu
    FOR EACH ROW
EXECUTE FUNCTION update_game_save_timestamp();

CREATE TRIGGER trg_stage_update_game_save
    AFTER UPDATE
    ON t_stage_tgst
    FOR EACH ROW
EXECUTE FUNCTION update_game_save_timestamp();

CREATE TRIGGER trg_item_update_game_save
    AFTER INSERT OR UPDATE OR DELETE
    ON t_item_tgit
    FOR EACH ROW
EXECUTE FUNCTION update_game_save_timestamp();

CREATE TRIGGER trg_additional_stat_update_game_save
    AFTER INSERT OR UPDATE OR DELETE
    ON t_additional_stat_tias
    FOR EACH ROW
EXECUTE FUNCTION update_game_save_timestamp();