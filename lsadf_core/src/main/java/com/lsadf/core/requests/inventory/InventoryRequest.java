package com.lsadf.core.requests.inventory;

import static com.lsadf.core.constants.JsonAttributes.Inventory.ITEMS;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.lsadf.core.requests.Request;
import com.lsadf.core.requests.item.ItemRequest;
import jakarta.validation.constraints.NotNull;
import java.io.Serial;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({ITEMS})
public class InventoryRequest implements Request {

  @Serial private static final long serialVersionUID = 9140883232214789509L;

  @JsonProperty(value = ITEMS)
  @NotNull
  private List<ItemRequest> items;
}
