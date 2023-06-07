package control.tower.product.service.query;

import control.tower.product.service.core.data.ProductEntity;
import control.tower.product.service.core.data.ProductRepository;
import control.tower.product.service.core.events.ProductCreatedEvent;
import control.tower.product.service.core.events.ProductStockDecreasedForRemovedInventoryEvent;
import control.tower.product.service.core.events.ProductStockIncreasedForNewInventoryEvent;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.messaging.interceptors.ExceptionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
@ProcessingGroup("product-group")
public class ProductEventsHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductEventsHandler.class);

    private final ProductRepository productRepository;

    public ProductEventsHandler(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @ExceptionHandler(resultType = Exception.class)
    public void handle(Exception exception) throws Exception {
        throw exception;
    }

    @ExceptionHandler(resultType = IllegalArgumentException.class)
    public void handle(IllegalArgumentException exception) {
        LOGGER.error(exception.getLocalizedMessage());
    }

    @EventHandler
    public void on(ProductCreatedEvent event) {
        ProductEntity productEntity = new ProductEntity();
        BeanUtils.copyProperties(event, productEntity);
        productEntity.setQuantity(0);
        productRepository.save(productEntity);
    }

    @EventHandler
    public void on(ProductStockIncreasedForNewInventoryEvent event) {
        ProductEntity productEntity = productRepository.findByProductId(event.getProductId());

        throwErrorIfProductDoesNotExist(productEntity, event.getProductId());

        productEntity.setQuantity(productEntity.getQuantity() + 1);
        productRepository.save(productEntity);
    }

    @EventHandler
    public void on(ProductStockDecreasedForRemovedInventoryEvent event) {
        ProductEntity productEntity = productRepository.findByProductId(event.getProductId());

        throwErrorIfProductDoesNotExist(productEntity, event.getProductId());
        throwErrorIfProductStockIsZero(productEntity);

        productEntity.setQuantity(productEntity.getQuantity() - 1);
        productRepository.save(productEntity);
    }

    private void throwErrorIfProductDoesNotExist(ProductEntity productEntity, String productId) {
        if (productEntity == null) {
            throw new IllegalStateException(String.format("Product [%s] does not exist.", productId));
        }
    }

    private void throwErrorIfProductStockIsZero(ProductEntity productEntity) {
        if (productEntity.getQuantity() <= 0) {
            throw new IllegalStateException(String.format("Failed to decrement product [%s] product stock cannot be negative"));
        }
    }
}
