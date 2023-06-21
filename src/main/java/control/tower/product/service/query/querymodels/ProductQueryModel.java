package control.tower.product.service.query.querymodels;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class ProductQueryModel {

    private String productId;
    private String name;
    private BigDecimal price;
    private Integer stock;
}
