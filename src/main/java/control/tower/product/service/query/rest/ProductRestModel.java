package control.tower.product.service.query.rest;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class ProductRestModel {

    private String productId;
    private String name;
    private BigDecimal price;
    private Integer stock;
}
