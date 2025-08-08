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


CREATE TABLE t_game_metadata_tgme
(
    tgme_id         UUID PRIMARY KEY                     DEFAULT gen_random_uuid(),
    tgme_created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    tgme_updated_at TIMESTAMP WITHOUT TIME ZONE          DEFAULT CURRENT_TIMESTAMP,
    tgme_user_email VARCHAR(255)                NOT NULL
        CONSTRAINT chk_tgme_user_email_format CHECK (tgme_user_email ~* '^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$'),
    tgme_nickname   VARCHAR(100)                         DEFAULT gen_random_uuid()::text
);

-- Create an index on user_email for better query performance
CREATE INDEX idx_tgme_user_email ON t_game_metadata_tgme (tgme_user_email);

CREATE TABLE t_characteristics_tgch
(
    tgme_id          UUID PRIMARY KEY,
    tgch_attack      BIGINT NOT NULL DEFAULT 1
        CONSTRAINT chk_tgch_strict_positive_attack CHECK (tgch_attack > 0),
    tgch_crit_chance BIGINT NOT NULL DEFAULT 0
        CONSTRAINT chk_tgch_positive_crit_chance CHECK (tgch_crit_chance >= 0),
    tgch_crit_damage BIGINT NOT NULL DEFAULT 0
        CONSTRAINT chk_tgch_positive_crit_damage CHECK (tgch_crit_damage >= 0),
    tgch_health      BIGINT NOT NULL DEFAULT 1
        CONSTRAINT chk_tgch_strict_positive_health CHECK (tgch_health > 0),
    tgch_resistance  BIGINT NOT NULL DEFAULT 0
        CONSTRAINT chk_tgch_positive_resistance CHECK (tgch_resistance >= 0)
);

ALTER TABLE t_characteristics_tgch
    ADD CONSTRAINT fk_t_characteristics_on_tgme FOREIGN KEY (tgme_id) REFERENCES t_game_metadata_tgme (tgme_id) ON DELETE CASCADE;

CREATE TABLE t_currency_tgcu
(
    tgme_id              UUID PRIMARY KEY,
    tgcu_gold_amount     BIGINT NOT NULL DEFAULT 0
        CONSTRAINT chk_tgcu_positive_gold_amount CHECK (tgcu_gold_amount >= 0),
    tgcu_diamond_amount  BIGINT NOT NULL DEFAULT 0
        CONSTRAINT chk_thcu_positive_diamond_amount CHECK (tgcu_diamond_amount >= 0),
    tgcu_emerald_amount  BIGINT NOT NULL DEFAULT 0
        CONSTRAINT chk_tgcu_positive_emerald_amount CHECK (tgcu_emerald_amount >= 0),
    tgcu_amethyst_amount BIGINT NOT NULL DEFAULT 0
        CONSTRAINT chk_tgcu_positive_amethyst_amount CHECK (tgcu_amethyst_amount >= 0)
);

ALTER TABLE t_currency_tgcu
    ADD CONSTRAINT fk_t_currency_on_gsa FOREIGN KEY (tgme_id) REFERENCES t_game_metadata_tgme (tgme_id) ON DELETE CASCADE;

CREATE TABLE t_stage_tgst
(
    tgme_id            UUID PRIMARY KEY,
    tgst_current_stage BIGINT NOT NULL DEFAULT 1
        CONSTRAINT chk_tgst_strict_positive_current_stage CHECK (tgst_current_stage > 0),
    tgst_max_stage     BIGINT NOT NULL DEFAULT 1
        CONSTRAINT chk_tgst_strict_positive_max_stage CHECK (tgst_max_stage > 0),
    CONSTRAINT chk_tgst_consistent_stage CHECK (tgst_max_stage >= tgst_current_stage)
);

ALTER TABLE t_stage_tgst
    ADD CONSTRAINT fk_t_stage_on_gsa FOREIGN KEY (tgme_id) REFERENCES t_game_metadata_tgme (tgme_id) ON DELETE CASCADE;

CREATE TABLE t_item_tgit
(
    tgit_id              UUID PRIMARY KEY                     DEFAULT gen_random_uuid(),
    tgme_id              UUID                        NOT NULL,
    tgit_created_at      TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    tgit_updated_at      TIMESTAMP WITHOUT TIME ZONE          DEFAULT CURRENT_TIMESTAMP,
    tgit_client_id       VARCHAR(100) UNIQUE         NOT NULL,
    tgit_blueprint_id    VARCHAR(255)                NOT NULL,
    tgit_type            VARCHAR(32)                 NOT NULL,
    tgit_rarity          VARCHAR(32)                 NOT NULL,
    tgit_is_equipped     BOOLEAN,
    tgit_level           INTEGER                     NOT NULL DEFAULT 1
        CONSTRAINT chk_tgit_strict_positive_level CHECK (tgit_level >= 0),
    tgit_main_statistic  VARCHAR(64)                 NOT NULL,
    tgit_main_base_value FLOAT                       NOT NULL
);

ALTER TABLE t_item_tgit
    ADD CONSTRAINT fk_t_item_on_gsa FOREIGN KEY (tgme_id) REFERENCES t_game_metadata_tgme (tgme_id) ON DELETE CASCADE;

ALTER TABLE t_item_tgit
    ADD CONSTRAINT uc_t_item_client UNIQUE (tgit_client_id);

CREATE INDEX idx_tgit_client_id ON t_item_tgit (tgit_client_id);

CREATE TABLE t_additional_stat_tias
(
    tias_id         UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tgit_id         UUID        NOT NULL,
    tias_base_value FLOAT       NOT NULL,
    tias_statistic  VARCHAR(64) NOT NULL
);

ALTER TABLE t_additional_stat_tias
    ADD CONSTRAINT fk_t_additional_stat_on_git FOREIGN KEY (tgit_id) references t_item_tgit (tgit_id) ON DELETE CASCADE;

CREATE INDEX idx_tias_tgit_id ON t_additional_stat_tias (tgit_id);