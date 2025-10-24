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

-- Create new view for mail instances with template data
CREATE OR REPLACE VIEW v_game_mail_instance_vgmi AS
SELECT tgmi.tgmi_id                        AS tgmi_id,
       tgmi.tgmt_id                        AS tgmt_id,
       tgmi.tgme_id                        AS tgme_id,
       tgmt.tgmt_name                      AS tgmt_name,
       tgmt.tgmt_subject                   AS tgmt_subject,
       tgmt.tgmt_body                      AS tgmt_body,
       tgmi.tgmi_read                      AS tgmi_read,
       tgmi.tgmi_attachment_claimed        AS tgmi_attachment_claimed,
       tgmi.tgmi_created_at                AS tgmi_created_at,
       tgmi.tgmi_updated_at                AS tgmi_updated_at,
       tgmi.tgmi_expires_at                AS tgmi_expires_at,
       (SELECT COUNT(*)
        FROM t_game_mail_template_attachment_tmta tmta
        WHERE tmta.tmta_id = tgmt.tgmt_id) AS attachment_count
FROM t_game_mail_instance_tgmi tgmi
         JOIN t_game_mail_template_tgmt tgmt ON tgmi.tgmt_id = tgmt.tgmt_id;