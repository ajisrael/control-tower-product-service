package control.tower.product.service.query;

import control.tower.product.service.core.data.ProductEntity;
import control.tower.product.service.core.data.ProductRepository;
import control.tower.product.service.core.events.ProductCreatedEvent;
import control.tower.product.service.core.events.ProductStockIncreasedWithNewInventoryEvent;
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
    public void on(ProductStockIncreasedWithNewInventoryEvent event) {
        ProductEntity productEntity = productRepository.findByProductId(event.getProductId());
        productEntity.setQuantity(productEntity.getQuantity() + 1);
        productRepository.save(productEntity);
    }
}
