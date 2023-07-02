package control.tower.product.service.command.rest;

import control.tower.product.service.command.commands.CreateProductCommand;
import control.tower.product.service.command.commands.RemoveProductCommand;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("/products")
public class ProductsCommandController {

    @Autowired
    private CommandGateway commandGateway;

    @PostMapping
    public String createProduct(@Valid @RequestBody CreateProductRequestModel createProductRequestModel) {
        CreateProductCommand createProductCommand = CreateProductCommand.builder()
                .productId(UUID.randomUUID().toString())
                .name(createProductRequestModel.getName())
                .price(createProductRequestModel.getPrice())
                .build();

        return commandGateway.sendAndWait(createProductCommand);
    }

    @DeleteMapping
    public String removeProduct(@Valid @RequestBody RemoveProductRequestModel removeProductRequestModel) {
        RemoveProductCommand removeProductCommand = RemoveProductCommand.builder()
                .productId(removeProductRequestModel.getProductId())
                .build();

        return commandGateway.sendAndWait(removeProductCommand);
    }
}


