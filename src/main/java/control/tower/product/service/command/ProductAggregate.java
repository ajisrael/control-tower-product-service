package control.tower.product.service.command;

import control.tower.product.service.core.events.ProductCreatedEvent;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;

import java.math.BigDecimal;

import static control.tower.product.service.core.utils.Helper.isNullOrBlank;

@Aggregate
@NoArgsConstructor
@Getter
public class ProductAggregate {

    @AggregateIdentifier
    private String productId;
    private String name;
    private BigDecimal price;

    @CommandHandler
    public ProductAggregate(CreateProductCommand command) {
        validateCreateProductCommand(command);

        ProductCreatedEvent event = ProductCreatedEvent.builder()
                .productId(command.getProductId())
                .name(command.getName())
                .price(command.getPrice())
                .build();

        AggregateLifecycle.apply(event);
    }

    @EventHandler
    public void on(ProductCreatedEvent event) {
        this.productId = event.getProductId();
        this.name = event.getName();
        this.price = event.getPrice();
    }

    private void validateCreateProductCommand(CreateProductCommand command) {
        if (isNullOrBlank(command.getProductId())) {
            throw new IllegalArgumentException("ProductId cannot be empty");
        }

        if (isNullOrBlank(command.getName())) {
            throw new IllegalArgumentException("Name cannot be empty");
        }

        if (command.getPrice() == null) {
            throw new IllegalArgumentException("Price cannot be null");
        }
    }
}
