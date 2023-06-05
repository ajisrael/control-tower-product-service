package control.tower.product.service.command;

import control.tower.product.service.core.events.ProductCreatedEvent;
import org.axonframework.test.aggregate.AggregateTestFixture;
import org.axonframework.test.aggregate.FixtureConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ProductAggregateTest {

    private String PRODUCT_ID = "1234";
    private String NAME = "Name";

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
                                .build())
                .expectEvents(
                        ProductCreatedEvent.builder()
                                .productId(PRODUCT_ID)
                                .name(NAME)
                                .build())
                .expectState(
                        productAggregate -> {
                            assertEquals(PRODUCT_ID, productAggregate.getProductId());
                            assertEquals(NAME, productAggregate.getName());
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
                                .build())
                .expectException(IllegalArgumentException.class);
    }
}
