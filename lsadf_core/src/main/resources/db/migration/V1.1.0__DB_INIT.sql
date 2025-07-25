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


CREATE TABLE t_game_save_tgsa
(
    id         UUID PRIMARY KEY                     DEFAULT gen_random_uuid(),
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITHOUT TIME ZONE          DEFAULT CURRENT_TIMESTAMP,
    user_email VARCHAR(255)                NOT NULL
        CONSTRAINT chk_tgsa_user_email_format CHECK (user_email ~* '^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$'),
    nickname   VARCHAR(100)                         DEFAULT gen_random_uuid()::text
);

-- Create an index on user_email for better query performance
CREATE INDEX idx_tgsa_user_email ON t_game_save_tgsa (user_email);

CREATE TABLE t_characteristics_tgch
(
    id          UUID PRIMARY KEY,
    attack      BIGINT NOT NULL DEFAULT 1
        CONSTRAINT chk_tgch_strict_positive_attack CHECK (attack > 0),
    crit_chance BIGINT NOT NULL DEFAULT 0
        CONSTRAINT chk_tgch_positive_crit_chance CHECK (crit_chance >= 0),
    crit_damage BIGINT NOT NULL DEFAULT 0
        CONSTRAINT chk_tgch_positive_crit_damage CHECK (crit_damage >= 0),
    health      BIGINT NOT NULL DEFAULT 1
        CONSTRAINT chk_tgch_strict_positive_health CHECK (health > 0),
    resistance  BIGINT NOT NULL DEFAULT 0
        CONSTRAINT chk_tgch_positive_resistance CHECK (resistance >= 0)
);

ALTER TABLE t_characteristics_tgch
    ADD CONSTRAINT fk_t_characteristics_on_tgsa FOREIGN KEY (id) REFERENCES t_game_save_tgsa (id) ON DELETE CASCADE;

CREATE TABLE t_currency_tgcu
(
    id              UUID PRIMARY KEY,
    gold_amount     BIGINT NOT NULL DEFAULT 0
        CONSTRAINT chk_tgcu_positive_gold_amount CHECK (gold_amount >= 0),
    diamond_amount  BIGINT NOT NULL DEFAULT 0
        CONSTRAINT chk_thcu_positive_diamond_amount CHECK (diamond_amount >= 0),
    emerald_amount  BIGINT NOT NULL DEFAULT 0
        CONSTRAINT chk_tgcu_positive_emerald_amount CHECK (emerald_amount >= 0),
    amethyst_amount BIGINT NOT NULL DEFAULT 0
        CONSTRAINT chk_tgcu_positive_amethyst_amount CHECK (amethyst_amount >= 0)
);

ALTER TABLE t_currency_tgcu
    ADD CONSTRAINT fk_t_currency_on_gsa FOREIGN KEY (id) REFERENCES t_game_save_tgsa (id) ON DELETE CASCADE;

CREATE TABLE t_stage_tgst
(
    id            UUID PRIMARY KEY,
    current_stage BIGINT NOT NULL DEFAULT 1
        CONSTRAINT chk_tgst_strict_positive_current_stage CHECK (current_stage > 0),
    max_stage     BIGINT NOT NULL DEFAULT 1
        CONSTRAINT chk_tgst_strict_positive_max_stage CHECK (max_stage > 0),
    CONSTRAINT chk_tgst_consistent_stage CHECK (max_stage >= current_stage)
);

ALTER TABLE t_stage_tgst
    ADD CONSTRAINT fk_t_stage_on_gsa FOREIGN KEY (id) REFERENCES t_game_save_tgsa (id) ON DELETE CASCADE;

CREATE TABLE t_item_tgit
(
    id              UUID PRIMARY KEY                     DEFAULT gen_random_uuid(),
    tgsa_id         UUID                        NOT NULL,
    created_at      TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP WITHOUT TIME ZONE          DEFAULT CURRENT_TIMESTAMP,
    client_id       VARCHAR(100) UNIQUE         NOT NULL,
    blueprint_id    VARCHAR(255)                NOT NULL,
    type            VARCHAR(32)                 NOT NULL,
    rarity          VARCHAR(32)                 NOT NULL,
    is_equipped     BOOLEAN,
    level           INTEGER                     NOT NULL DEFAULT 1
        CONSTRAINT chk_tgit_strict_positive_level CHECK (level >= 0),
    main_statistic  VARCHAR(64)                 NOT NULL,
    main_base_value FLOAT                       NOT NULL
);

ALTER TABLE t_item_tgit
    ADD CONSTRAINT fk_t_item_on_gsa FOREIGN KEY (tgsa_id) REFERENCES t_game_save_tgsa (id) ON DELETE CASCADE;

ALTER TABLE t_item_tgit
    ADD CONSTRAINT uc_t_item_client UNIQUE (client_id);

CREATE INDEX idx_tgit_client_id ON t_item_tgit (client_id);

CREATE TABLE t_additional_stat_tias
(
    id         UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tgit_id    UUID        NOT NULL,
    base_value FLOAT       NOT NULL,
    statistic  VARCHAR(64) NOT NULL
);

ALTER TABLE t_additional_stat_tias
    ADD CONSTRAINT fk_t_additional_stat_on_git FOREIGN KEY (tgit_id) references t_item_tgit (id) ON DELETE CASCADE;

CREATE INDEX idx_tias_tgit_id ON t_additional_stat_tias (tgit_id);