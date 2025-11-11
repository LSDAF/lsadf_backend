-- Function to automatically set expiration date based on template
CREATE OR REPLACE FUNCTION set_mail_expiration()
    RETURNS TRIGGER AS
$$
BEGIN
    DECLARE
        v_expiration_days INTEGER;
    BEGIN
        -- Get expiration days from the template
        SELECT tgmt_expiration_days
        INTO v_expiration_days
        FROM t_game_mail_template_tgmt
        WHERE tgmt_id = NEW.tgmt_id;

        -- Set the expiration date based on created_at + expiration_days
        NEW.tgmi_expires_at := NEW.tgmi_created_at + INTERVAL '1 day' * v_expiration_days;

        RETURN NEW;
    END;
END;
$$ LANGUAGE plpgsql;

-- Trigger to automatically calculate expiration date
CREATE TRIGGER tr_set_mail_expiration
    BEFORE INSERT
    ON t_game_mail_instance_tgmi
    FOR EACH ROW
EXECUTE FUNCTION set_mail_expiration();


-- TRIGGER TRG_GAME_MAIL_TEMPLATE_UPDATE
CREATE OR REPLACE FUNCTION update_game_mail_template_timestamp()
    RETURNS trigger AS
$$
BEGIN
    -- Set the timestamp to the instant the command is executed
    NEW.tgmt_updated_at := now(); -- or CURRENT_TIMESTAMP
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_game_mail_template_update
    AFTER UPDATE
    ON t_game_mail_template_tgmt
    FOR EACH ROW
EXECUTE FUNCTION update_game_mail_template_timestamp();


-- TRIGGER TRG_GAME_MAIL_INSTANCE_UPDATE
CREATE OR REPLACE FUNCTION update_game_mail_instance_timestamp()
    RETURNS trigger AS
$$
BEGIN
    -- Set the timestamp to the instant the command is executed
    NEW.tgmi_updated_at := now(); -- or CURRENT_TIMESTAMP
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_game_mail_template_update
    AFTER UPDATE
    ON t_game_mail_instance_tgmi
    FOR EACH ROW
EXECUTE FUNCTION update_game_mail_instance_timestamp();