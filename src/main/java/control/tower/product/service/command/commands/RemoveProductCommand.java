package control.tower.product.service.command.commands;

import lombok.Builder;
import lombok.Getter;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

import static control.tower.core.utils.Helper.throwExceptionIfParameterIsEmpty;
import static control.tower.product.service.core.constants.ExceptionMessages.PRODUCT_ID_CANNOT_BE_EMPTY;

@Getter
@Builder
public class RemoveProductCommand {

    @TargetAggregateIdentifier
    private String productId;

    public void validate() {
        throwExceptionIfParameterIsEmpty(this.getProductId(), PRODUCT_ID_CANNOT_BE_EMPTY);

    }
}
