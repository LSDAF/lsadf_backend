package com.lsadf.lsadf_backend.requests.admin;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.lsadf.lsadf_backend.constants.JsonAttributes;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class AdminGameSaveUpdateRequest {
    @JsonProperty(value = JsonAttributes.Currency.GOLD)
    @PositiveOrZero
    @Schema(description = "Amount of gold", example = "100")
    private Long gold;

    @JsonProperty(value = JsonAttributes.Currency.DIAMOND)
    @PositiveOrZero
    @Schema(description = "Amount of diamond", example = "100")
    private Long diamond;

    @JsonProperty(value = JsonAttributes.Currency.EMERALD)
    @PositiveOrZero
    @Schema(description = "Amount of emerald", example = "100")
    private Long emerald;

    @JsonProperty(value = JsonAttributes.Currency.AMETHYST)
    @PositiveOrZero
    @Schema(description = "Amount of amethyst", example = "100")
    private Long amethyst;

    @JsonProperty(value = JsonAttributes.Characteristic.HEALTH_POINTS)
    @PositiveOrZero
    @Schema(description = "Health points", example = "100")
    private Long healthPoints;

    @JsonProperty(value = JsonAttributes.Characteristic.ATTACK)
    @PositiveOrZero
    @Schema(description = "Attack points", example = "100")
    private Long attack;
}
