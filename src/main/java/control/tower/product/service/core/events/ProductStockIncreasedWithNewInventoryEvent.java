package control.tower.product.service.core.events;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProductStockIncreasedWithNewInventoryEvent {

    private String productId;
    private String sku;
}
