package control.tower.product.service.command;

import control.tower.product.service.core.events.ProductCreatedEvent;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;

import static control.tower.product.service.core.utils.Helper.isNullOrBlank;

@Aggregate
@NoArgsConstructor
@Getter
public class ProductAggregate {

    @AggregateIdentifier
    private String productId;
    private String name;

    @CommandHandler
    public ProductAggregate(CreateProductCommand command) {
        validateCreateInventoryItemCommand(command);

        ProductCreatedEvent event = ProductCreatedEvent.builder()
                .productId(command.getProductId())
                .name(command.getName())
                .build();

        AggregateLifecycle.apply(event);
    }

    @EventHandler
    public void on(ProductCreatedEvent event) {
        this.productId = event.getProductId();
        this.name = event.getName();
    }

    private void validateCreateInventoryItemCommand(CreateProductCommand command) {
        if (isNullOrBlank(command.getProductId())) {
            throw new IllegalArgumentException("ProductId cannot be empty");
        }

        if (isNullOrBlank(command.getName())) {
            throw new IllegalArgumentException("Name cannot be empty");
        }
    }
}
