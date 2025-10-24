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

-- Mail templates table - stores reusable mail content
CREATE TABLE t_game_mail_template_tgmt
(
    tgmt_id              UUID PRIMARY KEY         DEFAULT gen_random_uuid(),
    tgmt_name            VARCHAR(255) NOT NULL UNIQUE,        -- Template name for admin reference
    tgmt_subject         VARCHAR(255) NOT NULL,
    tgmt_body            TEXT         NOT NULL,
    tgmt_expiration_days INTEGER      NOT NULL    DEFAULT 30, -- Default expiration in days
    tgmt_created_at      TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    tgmt_updated_at      TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Template attachments table - fixed attachments per template
CREATE TABLE t_game_mail_template_attachment_tmta
(
    tmta_id     UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tgmt_id     UUID        NOT NULL,
    tmta_type   VARCHAR(20) NOT NULL,
    tmta_object TEXT        NOT NULL
);

-- Mail instances table - links templates to specific games (join table)
CREATE TABLE t_game_mail_instance_tgmi
(
    tgmi_id                 UUID PRIMARY KEY                  DEFAULT gen_random_uuid(),
    tgmt_id                 UUID                     NOT NULL, -- Reference to template
    tgme_id                 UUID                     NOT NULL, -- Reference to game metadata
    tgmi_read               BOOLEAN                  NOT NULL DEFAULT FALSE,
    tgmi_attachment_claimed BOOLEAN                  NOT NULL DEFAULT FALSE,
    tgmi_created_at         TIMESTAMP WITH TIME ZONE          DEFAULT CURRENT_TIMESTAMP,
    tgmi_updated_at         TIMESTAMP WITH TIME ZONE          DEFAULT CURRENT_TIMESTAMP,
    tgmi_expires_at         TIMESTAMP WITH TIME ZONE NOT NULL  -- Calculated: created_at + template.expiration_duration
);

-- Foreign key constraints
ALTER TABLE t_game_mail_template_attachment_tmta
    ADD CONSTRAINT fk_tgmta_template FOREIGN KEY (tgmt_id)
        REFERENCES t_game_mail_template_tgmt (tgmt_id) ON DELETE CASCADE;

ALTER TABLE t_game_mail_instance_tgmi
    ADD CONSTRAINT fk_tgmi_template FOREIGN KEY (tgmt_id)
        REFERENCES t_game_mail_template_tgmt (tgmt_id) ON DELETE CASCADE;

ALTER TABLE t_game_mail_instance_tgmi
    ADD CONSTRAINT fk_tgmi_game_metadata FOREIGN KEY (tgme_id)
        REFERENCES t_game_metadata_tgme (tgme_id) ON DELETE CASCADE;

-- Unique constraint to prevent duplicate mail instances for same template+game
ALTER TABLE t_game_mail_instance_tgmi
    ADD CONSTRAINT uk_tgmi_template_game UNIQUE (tgmt_id, tgme_id);

-- Indexes for performance
CREATE INDEX idx_tgmta_template_id ON t_game_mail_template_attachment_tmta (tgmt_id);
CREATE INDEX idx_tgmi_game_id ON t_game_mail_instance_tgmi (tgme_id);
CREATE INDEX idx_tgmi_template_id ON t_game_mail_instance_tgmi (tgmt_id);
CREATE INDEX idx_tgmi_expires_at ON t_game_mail_instance_tgmi (tgmi_expires_at);
CREATE INDEX idx_tgmi_unread ON t_game_mail_instance_tgmi (tgme_id, tgmi_read) WHERE tgmi_read = FALSE;
