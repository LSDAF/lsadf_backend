package com.lsadf.core.requests.item;

import static com.lsadf.core.constants.JsonAttributes.Item.*;
import static com.lsadf.core.constants.JsonAttributes.Item.ITEM_TYPE;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.lsadf.core.models.ItemStat;
import com.lsadf.core.requests.Request;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import java.io.Serial;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({ITEM_TYPE})
public class ItemRequest implements Request {

  @Serial private static final long serialVersionUID = -1116418739363127022L;

  @Schema(description = "Item type", example = "boots")
  @JsonProperty(value = ITEM_TYPE)
  @NotNull
  private String itemType;

  @Schema(description = "Item rarity", example = "LEGENDARY")
  @JsonProperty(value = ITEM_RARITY)
  @NotNull
  private String itemRarity;

  @Schema(description = "Is equipped", example = "true")
  @JsonProperty(value = IS_EQUIPPED)
  @NotNull
  private Boolean isEquipped;

  @Schema(description = "Item level", example = "20")
  @JsonProperty(value = LEVEL)
  @NotNull
  private Integer level;

  @Schema(description = "Main item stat")
  @JsonProperty(value = MAIN_STAT)
  @NotNull
  private ItemStat mainStat;

  @Schema(description = "Additional item stat list")
  @JsonProperty(value = ADDITIONAL_STATS)
  @NotNull
  private List<ItemStat> additionalStats;
}
