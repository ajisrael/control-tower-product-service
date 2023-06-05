package control.tower.product.service.command.rest;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;

@Getter
@NoArgsConstructor
public class CreateProductRestModel {

    @NotBlank(message = "Name is a required field")
    private String name;
    @Min(value=1, message="Price cannot be lower than 1")
    private BigDecimal price;
}
