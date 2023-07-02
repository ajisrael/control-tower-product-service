package control.tower.product.service.command.rest;

import control.tower.product.service.command.commands.CreateProductCommand;
import control.tower.product.service.command.commands.RemoveProductCommand;
import control.tower.product.service.command.rest.requests.CreateProductRequestModel;
import control.tower.product.service.command.rest.requests.RemoveProductRequestModel;
import control.tower.product.service.command.rest.responses.ProductCreatedResponseModel;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("/products")
@Tag(name = "Products Command API")
public class ProductsCommandController {

    @Autowired
    private CommandGateway commandGateway;

    @PostMapping
    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create product")
    public ProductCreatedResponseModel createProduct(@Valid @RequestBody CreateProductRequestModel createProductRequestModel) {
        CreateProductCommand createProductCommand = CreateProductCommand.builder()
                .productId(UUID.randomUUID().toString())
                .name(createProductRequestModel.getName())
                .price(createProductRequestModel.getPrice())
                .build();

        String productId = commandGateway.sendAndWait(createProductCommand);

        return ProductCreatedResponseModel.builder().productId(productId).build();
    }

    @DeleteMapping
    public void removeProduct(@Valid @RequestBody RemoveProductRequestModel removeProductRequestModel) {
        RemoveProductCommand removeProductCommand = RemoveProductCommand.builder()
                .productId(removeProductRequestModel.getProductId())
                .build();

        commandGateway.sendAndWait(removeProductCommand);
    }
}


