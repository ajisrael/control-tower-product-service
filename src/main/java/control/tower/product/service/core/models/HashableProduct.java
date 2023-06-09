package control.tower.product.service.core.models;

import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@NoArgsConstructor
public class HashableProduct {

    private String name;

    public String getCombinedValues() {
        return name;
    }
}
