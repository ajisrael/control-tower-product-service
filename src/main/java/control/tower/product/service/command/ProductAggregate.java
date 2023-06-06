package control.tower.product.service.command;

import control.tower.product.service.command.commands.CreateProductCommand;
import control.tower.product.service.core.events.ProductCreatedEvent;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;

import java.math.BigDecimal;

@Aggregate
@NoArgsConstructor
@Getter
public class ProductAggregate {

    @AggregateIdentifier
    private String productId;
    private String name;
    private BigDecimal price;
    private Integer quantity;

    @CommandHandler
    public ProductAggregate(CreateProductCommand command) {
        command.validate();

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
        this.quantity = 0;
    }

    }
}
