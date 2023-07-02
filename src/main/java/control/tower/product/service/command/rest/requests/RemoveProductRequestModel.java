package control.tower.product.service.command.rest.requests;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
public class RemoveProductRequestModel {

    @NotBlank(message = "ProductId is a required field")
    private String productId;
}
