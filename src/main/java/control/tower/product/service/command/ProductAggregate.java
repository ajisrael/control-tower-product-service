package control.tower.product.service.command;

import control.tower.core.commands.DecreaseProductStockForRemovedInventoryCommand;
import control.tower.core.commands.IncreaseProductStockForNewInventoryCommand;
import control.tower.product.service.command.commands.CreateProductCommand;
import control.tower.product.service.command.commands.RemoveProductCommand;
import control.tower.product.service.core.events.ProductCreatedEvent;
import control.tower.product.service.core.events.ProductRemovedEvent;
import control.tower.product.service.core.events.ProductStockDecreasedForRemovedInventoryEvent;
import control.tower.product.service.core.events.ProductStockIncreasedForNewInventoryEvent;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;

import java.math.BigDecimal;

import static control.tower.product.service.core.utils.Helper.throwExceptionIfStockIsGreaterThanZero;
import static control.tower.product.service.core.utils.Helper.throwExceptionIfStockIsLessThanOrEqualToZero;

@Aggregate
@NoArgsConstructor
@Getter
public class ProductAggregate {

    @AggregateIdentifier
    private String productId;
    private String name;
    private BigDecimal price;
    private Integer stock;

    @CommandHandler
    public ProductAggregate(CreateProductCommand command) {
        ProductCreatedEvent event = ProductCreatedEvent.builder()
                .productId(command.getProductId())
                .name(command.getName())
                .price(command.getPrice())
                .build();

        AggregateLifecycle.apply(event);
    }

    @CommandHandler
    public void handle(IncreaseProductStockForNewInventoryCommand command) {
        command.validate(); // command called from external service needs to be validated in aggregate

        ProductStockIncreasedForNewInventoryEvent event = ProductStockIncreasedForNewInventoryEvent.builder()
                .productId(command.getProductId())
                .sku(command.getSku())
                .build();

        AggregateLifecycle.apply(event);
    }

    @CommandHandler
    public void handle(DecreaseProductStockForRemovedInventoryCommand command) {
        command.validate(); // command called from external service needs to be validated in aggregate

        throwExceptionIfStockIsLessThanOrEqualToZero(this.stock, command.getProductId());

        ProductStockDecreasedForRemovedInventoryEvent event = ProductStockDecreasedForRemovedInventoryEvent.builder()
                .productId(command.getProductId())
                .sku(command.getSku())
                .build();

        AggregateLifecycle.apply(event);
    }

    @CommandHandler
    public void handle(RemoveProductCommand command) {
        throwExceptionIfStockIsGreaterThanZero(this.stock, command.getProductId());

        ProductRemovedEvent event = ProductRemovedEvent.builder()
                .productId(command.getProductId())
                .name(name)
                .build();

        AggregateLifecycle.apply(event);
    }


    @EventSourcingHandler
    public void on(ProductCreatedEvent event) {
        this.productId = event.getProductId();
        this.name = event.getName();
        this.price = event.getPrice();
        this.stock = 0;
    }

    @EventSourcingHandler
    public void on(ProductStockIncreasedForNewInventoryEvent event) {
        this.stock += 1;
    }

    @EventSourcingHandler
    public void on(ProductStockDecreasedForRemovedInventoryEvent event) {
        this.stock -= 1;
    }

    @EventSourcingHandler
    public void on(ProductRemovedEvent event) {
        AggregateLifecycle.markDeleted();
    }
}
