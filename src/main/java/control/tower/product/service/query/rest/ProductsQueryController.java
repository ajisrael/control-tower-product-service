package control.tower.product.service.query.rest;

import control.tower.product.service.query.queries.FindAllProductsQuery;
import control.tower.product.service.query.queries.FindProductQuery;
import control.tower.product.service.query.querymodels.ProductQueryModel;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
@Tag(name = "Products Query API")
public class ProductsQueryController {

    @Autowired
    QueryGateway queryGateway;

    @GetMapping
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get all products")
    public List<ProductQueryModel> getProducts() {
        return queryGateway.query(new FindAllProductsQuery(),
                ResponseTypes.multipleInstancesOf(ProductQueryModel.class)).join();
    }

    @GetMapping(params = "productId")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get product by id")
    public ProductQueryModel getProduct(String productId) {
        return queryGateway.query(new FindProductQuery(productId),
                ResponseTypes.instanceOf(ProductQueryModel.class)).join();
    }
}
