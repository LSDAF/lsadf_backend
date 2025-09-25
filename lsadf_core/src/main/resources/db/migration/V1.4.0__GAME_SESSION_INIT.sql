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

-- TABLE T_GAME_SESSION_TGSE
CREATE TABLE t_game_session_tgse
(
    tgse_id         UUID PRIMARY KEY,
    tgme_id         UUID                     NOT NULL,
    tgse_end_time   TIMESTAMP WITH TIME ZONE NOT NULL,
    tgse_cancelled  BOOLEAN                  NOT NULL DEFAULT FALSE,
    tgse_created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    tgse_updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    tgse_version    INTEGER                  NOT NULL DEFAULT 1
        CONSTRAINT chk_tgse_strict_positive_version CHECK (tgse_version >= 1)
);

ALTER TABLE t_game_session_tgse
    ADD CONSTRAINT fk_t_game_session_on_tgme FOREIGN KEY (tgme_id) REFERENCES t_game_metadata_tgme (tgme_id) ON DELETE CASCADE;

CREATE INDEX idx_tgse_id_version
    ON t_game_session_tgse (tgse_id, tgse_version DESC);



-- TRIGGER TRG_GAME_SESSION_UPDATE
CREATE OR REPLACE FUNCTION update_game_session_timestamp()
    RETURNS trigger AS
$$
BEGIN
    -- Set the timestamp to the instant the command is executed
    NEW.tgse_updated_at := now(); -- or CURRENT_TIMESTAMP
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_game_session_update
    AFTER UPDATE
    ON t_game_session_tgse
    FOR EACH ROW
EXECUTE FUNCTION update_game_session_timestamp();


-- VIEW V_GAME_SESSION_VGSE
CREATE VIEW v_game_session_vgse AS
SELECT tgse.tgse_id         AS "vgse_id",
       tgse.tgme_id         AS "vgse_game_save_id",
       tgse.tgse_end_time   AS "vgse_end_time",
       tgse.tgse_cancelled  AS "vgse_cancelled",
       tgse.tgse_created_at AS "vgse_created_at",
       tgse.tgse_updated_at AS "vgse_updated_at",
       tgme.tgme_user_email AS "vgse_user_email",
       tgse.tgse_version    AS "vgse_version"

FROM t_game_session_tgse tgse
         LEFT JOIN t_game_metadata_tgme tgme on tgme.tgme_id = tgse.tgme_id;
