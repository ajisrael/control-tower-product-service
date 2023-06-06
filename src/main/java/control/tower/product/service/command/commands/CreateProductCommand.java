package control.tower.product.service.command.commands;

import lombok.Builder;
import lombok.Getter;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

import java.math.BigDecimal;

import static control.tower.core.utils.Helper.isNullOrBlank;

@Getter
@Builder
public class CreateProductCommand {

    @TargetAggregateIdentifier
    private String productId;
    private String name;
    private BigDecimal price;

    public void validate() {
        if (isNullOrBlank(productId)) {
            throw new IllegalArgumentException("ProductId cannot be empty");
        }

        if (isNullOrBlank(name)) {
            throw new IllegalArgumentException("Name cannot be empty");
        }

        if (price == null) {
            throw new IllegalArgumentException("Price cannot be null");
        }
    }
}
