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

import static control.tower.core.utils.Helper.throwErrorIfEntityDoesNotExist;

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
        productEntity.setStock(0);
        productRepository.save(productEntity);
    }

    @EventHandler
    public void on(ProductStockIncreasedForNewInventoryEvent event) {
        ProductEntity productEntity = productRepository.findByProductId(event.getProductId());

        throwErrorIfEntityDoesNotExist(productEntity,
                String.format("Product [%s] does not exist.", event.getProductId()));

        productEntity.setStock(productEntity.getStock() + 1);
        productRepository.save(productEntity);
    }

    @EventHandler
    public void on(ProductStockDecreasedForRemovedInventoryEvent event) {
        ProductEntity productEntity = productRepository.findByProductId(event.getProductId());

        throwErrorIfEntityDoesNotExist(productEntity,
                String.format("Product [%s] does not exist.", event.getProductId()));
        throwErrorIfProductStockIsZero(productEntity);

        productEntity.setStock(productEntity.getStock() - 1);
        productRepository.save(productEntity);
    }

    private void throwErrorIfProductStockIsZero(ProductEntity productEntity) {
        if (productEntity.getStock() <= 0) {
            throw new IllegalStateException(String.format("Failed to decrement product [%s] product stock cannot be negative"));
        }
    }
}
