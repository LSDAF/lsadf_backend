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

CREATE TYPE game_mail_status AS ENUM (
    'UNREAD',
    'READ',
    'DELETED'
    );

CREATE TABLE t_game_mail_tgml
(
    tgml_id         UUID PRIMARY KEY         DEFAULT gen_random_uuid(),
    tgme_id         UUID         NOT NULL,
    tgml_subject    VARCHAR(255) NOT NULL,
    tgml_body       TEXT         NOT NULL,
    tgml_sent_at    TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    tgml_expires_at TIMESTAMP WITH TIME ZONE DEFAULT (CURRENT_TIMESTAMP + INTERVAL '30 days'),
    tgml_status     game_mail_status         DEFAULT 'UNREAD'
);

ALTER TABLE t_game_mail_tgml
    ADD CONSTRAINT fk_t_game_mail_on_tgme FOREIGN KEY (tgme_id) REFERENCES t_game_metadata_tgme (tgme_id) ON DELETE CASCADE;

CREATE INDEX idx_tgml_tgme_id
    ON t_game_mail_tgml (tgme_id);

CREATE TYPE game_mail_attachment_type AS ENUM (
    'ITEM',
    'CURRENCY'
    );

CREATE TABLE t_game_mail_attachment_tgma
(
    tgma_id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tgml_attachment_type game_mail_attachment_type NOT NULL,
    tgml_id              UUID                      NOT NULL,
    tgma_object          JSON                      NOT NULL
);

ALTER TABLE t_game_mail_attachment_tgma
    ADD CONSTRAINT fk_t_game_mail_attachment_on_tgml FOREIGN KEY (tgml_id) REFERENCES t_game_mail_tgml (tgml_id) ON DELETE CASCADE;

CREATE INDEX idx_tgma_tgml_id
    ON t_game_mail_attachment_tgma (tgml_id);