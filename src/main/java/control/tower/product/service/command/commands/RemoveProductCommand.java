package control.tower.product.service.command.commands;

import lombok.Builder;
import lombok.Getter;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

import static control.tower.core.utils.Helper.isNullOrBlank;

@Getter
@Builder
public class RemoveProductCommand {

    @TargetAggregateIdentifier
    private String productId;

    public void validate() {
        if (isNullOrBlank(productId)) {
            throw new IllegalArgumentException("ProductId cannot be empty");
        }
    }
}
