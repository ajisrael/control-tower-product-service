package control.tower.product.service.command.rest;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
public class RemoveProductRestModel {

    @NotBlank(message = "ProductId is a required field")
    private String productId;
}
