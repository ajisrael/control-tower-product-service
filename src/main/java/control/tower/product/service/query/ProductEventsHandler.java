package control.tower.product.service.query;

import control.tower.product.service.core.data.ProductEntity;
import control.tower.product.service.core.data.ProductRepository;
import control.tower.product.service.core.events.ProductCreatedEvent;
import control.tower.product.service.core.events.ProductRemovedEvent;
import control.tower.product.service.core.events.ProductStockDecreasedForRemovedInventoryEvent;
import control.tower.product.service.core.events.ProductStockIncreasedForNewInventoryEvent;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.messaging.interceptors.ExceptionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import static control.tower.core.utils.Helper.throwExceptionIfEntityDoesNotExist;
import static control.tower.product.service.core.constants.ExceptionMessages.FAILED_TO_DECREMENT_PRODUCT_STOCK_CANNOT_BE_NEGATIVE;
import static control.tower.product.service.core.constants.ExceptionMessages.PRODUCT_WITH_ID_DOES_NOT_EXIST;
import static control.tower.product.service.core.utils.Helper.throwExceptionIfStockIsGreaterThanZero;
import static control.tower.product.service.core.utils.Helper.throwExceptionIfStockIsLessThanOrEqualToZero;

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

        throwExceptionIfEntityDoesNotExist(productEntity,
                String.format(PRODUCT_WITH_ID_DOES_NOT_EXIST, event.getProductId()));

        productEntity.setStock(productEntity.getStock() + 1);
        productRepository.save(productEntity);
    }

    @EventHandler
    public void on(ProductStockDecreasedForRemovedInventoryEvent event) {
        ProductEntity productEntity = productRepository.findByProductId(event.getProductId());

        throwExceptionIfEntityDoesNotExist(productEntity,
                String.format(PRODUCT_WITH_ID_DOES_NOT_EXIST, event.getProductId()));

        throwExceptionIfStockIsLessThanOrEqualToZero(productEntity.getStock(), productEntity.getProductId());

        productEntity.setStock(productEntity.getStock() - 1);
        productRepository.save(productEntity);
    }

    @EventHandler
    public void on(ProductRemovedEvent event) {
        ProductEntity productEntity = productRepository.findByProductId(event.getProductId());

        throwExceptionIfEntityDoesNotExist(productEntity,
                String.format(PRODUCT_WITH_ID_DOES_NOT_EXIST, event.getProductId()));

        throwExceptionIfStockIsGreaterThanZero(productEntity.getStock(), productEntity.getProductId());

        productRepository.delete(productEntity);
    }
}
