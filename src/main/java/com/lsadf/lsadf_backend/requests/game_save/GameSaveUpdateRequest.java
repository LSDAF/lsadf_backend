package com.lsadf.lsadf_backend.requests.game_save;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import static com.lsadf.lsadf_backend.constants.JsonAttributes.GameSave.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GameSaveUpdateRequest {
    @JsonProperty(value = HP)
    @PositiveOrZero
    private long healthPoints;

    @JsonProperty(value = ATTACK)
    @PositiveOrZero
    private long attack;

    @JsonProperty(value = NICKNAME)
    @Schema(description = "Nickname of the user", example = "test")
    private String nickname;
}
