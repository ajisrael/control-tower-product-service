package control.tower.product.service.command;

import control.tower.product.service.core.data.ProductEntity;
import control.tower.product.service.core.data.ProductLookupEntity;
import control.tower.product.service.core.data.ProductLookupRepository;
import control.tower.product.service.core.events.ProductCreatedEvent;
import control.tower.product.service.core.events.ProductRemovedEvent;
import lombok.AllArgsConstructor;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.stereotype.Component;

import static control.tower.core.utils.Helper.throwErrorIfEntityDoesNotExist;

@Component
@AllArgsConstructor
@ProcessingGroup("product-group")
public class ProductLookupEventsHandler {

    private ProductLookupRepository productLookupRepository;

    @EventHandler
    public void on(ProductCreatedEvent event) {
        productLookupRepository.save(new ProductLookupEntity(event.getProductId()));
    }

    @EventHandler
    public void on(ProductRemovedEvent event) {
        ProductLookupEntity productLookupEntity = productLookupRepository.findByProductId(event.getProductId());

        throwErrorIfEntityDoesNotExist(productLookupEntity,
                String.format("Product [%s] does not exist.", event.getProductId()));

        productLookupRepository.delete(productLookupEntity);
    }
}
