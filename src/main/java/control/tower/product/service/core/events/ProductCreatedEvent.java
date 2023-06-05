package control.tower.product.service.core.events;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProductCreatedEvent {

    private String productId;
    private String name;
}
