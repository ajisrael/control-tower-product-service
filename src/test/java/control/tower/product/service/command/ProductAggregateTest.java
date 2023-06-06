package control.tower.product.service.command;

import control.tower.product.service.command.commands.CreateProductCommand;
import control.tower.product.service.core.events.ProductCreatedEvent;
import org.axonframework.test.aggregate.AggregateTestFixture;
import org.axonframework.test.aggregate.FixtureConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ProductAggregateTest {

    private final String PRODUCT_ID = "1234";
    private final String NAME = "Name";
    private final BigDecimal PRICE = BigDecimal.valueOf(10.00);

    private FixtureConfiguration<ProductAggregate> fixture;

    @BeforeEach
    void setup() {
        fixture = new AggregateTestFixture<>(ProductAggregate.class);
    }

    @Test
    void shouldCreateProductAggregate() {
        fixture.givenNoPriorActivity()
                .when(
                        CreateProductCommand.builder()
                                .productId(PRODUCT_ID)
                                .name(NAME)
                                .price(PRICE)
                                .build())
                .expectEvents(
                        ProductCreatedEvent.builder()
                                .productId(PRODUCT_ID)
                                .name(NAME)
                                .price(PRICE)
                                .build())
                .expectState(
                        productAggregate -> {
                            assertEquals(PRODUCT_ID, productAggregate.getProductId());
                            assertEquals(NAME, productAggregate.getName());
                            assertEquals(PRICE, productAggregate.getPrice());
                        }
                );
    }

    @Test
    void shouldNotCreateProductAggregateWhenProductIdIsNull() {
        fixture.givenNoPriorActivity()
                .when(
                        CreateProductCommand.builder()
                                .productId(null)
                                .name(NAME)
                                .price(PRICE)
                                .build())
                .expectException(IllegalArgumentException.class);
    }

    @Test
    void shouldNotCreateProductAggregateWhenProductIdIsEmpty() {
        fixture.givenNoPriorActivity()
                .when(
                        CreateProductCommand.builder()
                                .productId("")
                                .name(NAME)
                                .price(PRICE)
                                .build())
                .expectException(IllegalArgumentException.class);
    }

    @Test
    void shouldNotCreateProductAggregateWhenNameIsNull() {
        fixture.givenNoPriorActivity()
                .when(
                        CreateProductCommand.builder()
                                .productId(PRODUCT_ID)
                                .name(null)
                                .price(PRICE)
                                .build())
                .expectException(IllegalArgumentException.class);
    }

    @Test
    void shouldNotCreateProductAggregateWhenNameIsEmpty() {
        fixture.givenNoPriorActivity()
                .when(
                        CreateProductCommand.builder()
                                .productId(PRODUCT_ID)
                                .name("")
                                .price(PRICE)
                                .build())
                .expectException(IllegalArgumentException.class);
    }

    @Test
    void shouldNotCreateProductAggregateWhenPriceIsNull() {
        fixture.givenNoPriorActivity()
                .when(
                        CreateProductCommand.builder()
                                .productId(PRODUCT_ID)
                                .name(NAME)
                                .price(null)
                                .build())
                .expectException(IllegalArgumentException.class);
    }
}
