package control.tower.product.service.command.rest.responses;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProductCreatedResponseModel {

    private String productId;
}
