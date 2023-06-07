package control.tower.product.service.core.events;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProductStockIncreasedForNewInventoryEvent {

    private String productId;
    private String sku;
}
