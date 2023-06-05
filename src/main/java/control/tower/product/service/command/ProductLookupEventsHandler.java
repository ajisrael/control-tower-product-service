package control.tower.product.service.command;

import control.tower.product.service.core.data.ProductLookupEntity;
import control.tower.product.service.core.data.ProductLookupRepository;
import control.tower.product.service.core.events.ProductCreatedEvent;
import lombok.AllArgsConstructor;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@ProcessingGroup("inventory-item-group")
public class ProductLookupEventsHandler {

    private ProductLookupRepository productLookupRepository;

    @EventHandler
    public void on(ProductCreatedEvent event) {
        productLookupRepository.save(new ProductLookupEntity(event.getProductId()));
    }
}
