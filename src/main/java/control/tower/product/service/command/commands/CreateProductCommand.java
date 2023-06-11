package control.tower.product.service.command.commands;

import lombok.Builder;
import lombok.Getter;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

import java.math.BigDecimal;

import static control.tower.core.utils.Helper.*;
import static control.tower.product.service.core.constants.ExceptionMessages.*;

@Getter
@Builder
public class CreateProductCommand {

    @TargetAggregateIdentifier
    private String productId;
    private String name;
    private BigDecimal price;

    public void validate() {
        throwExceptionIfParameterIsEmpty(this.getProductId(), PRODUCT_ID_CANNOT_BE_EMPTY);
        throwExceptionIfParameterIsEmpty(this.getName(), NAME_CANNOT_BE_EMPTY);
        throwExceptionIfParameterIsNull(this.getPrice(), PRICE_CANNOT_BE_NULL);
    }
}
