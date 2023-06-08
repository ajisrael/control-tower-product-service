package control.tower.product.service.core.events;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProductRemovedEvent {

    private String productId;
    private String name;
}
