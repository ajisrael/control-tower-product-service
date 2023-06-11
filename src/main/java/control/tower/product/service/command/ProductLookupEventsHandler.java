package control.tower.product.service.command;

import control.tower.product.service.core.data.ProductLookupEntity;
import control.tower.product.service.core.data.ProductLookupRepository;
import control.tower.product.service.core.events.ProductCreatedEvent;
import control.tower.product.service.core.events.ProductRemovedEvent;
import lombok.AllArgsConstructor;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.stereotype.Component;

import static control.tower.core.utils.Helper.throwExceptionIfEntityDoesNotExist;
import static control.tower.product.service.core.constants.ExceptionMessages.PRODUCT_WITH_ID_DOES_NOT_EXIST;
import static control.tower.product.service.core.utils.ProductHasher.createProductHash;

@Component
@AllArgsConstructor
@ProcessingGroup("product-group")
public class ProductLookupEventsHandler {

    private ProductLookupRepository productLookupRepository;

    @EventHandler
    public void on(ProductCreatedEvent event) {
        productLookupRepository.save(new ProductLookupEntity(event.getProductId(), createProductHash(event)));
    }

    @EventHandler
    public void on(ProductRemovedEvent event) {
        ProductLookupEntity productLookupEntity = productLookupRepository.findByProductId(event.getProductId());
        throwExceptionIfEntityDoesNotExist(productLookupEntity, String.format(PRODUCT_WITH_ID_DOES_NOT_EXIST, event.getProductId()));
        productLookupRepository.delete(productLookupEntity);
    }
}
