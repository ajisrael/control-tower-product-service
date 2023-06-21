package control.tower.product.service.query.rest;

import control.tower.product.service.query.queries.FindAllProductsQuery;
import control.tower.product.service.query.queries.FindProductQuery;
import control.tower.product.service.query.querymodels.ProductQueryModel;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductsQueryController {

    @Autowired
    QueryGateway queryGateway;

    @GetMapping
    public List<ProductQueryModel> getProducts() {
        return queryGateway.query(new FindAllProductsQuery(),
                ResponseTypes.multipleInstancesOf(ProductQueryModel.class)).join();
    }

    @GetMapping(params = "productId")
    public ProductQueryModel getProduct(String productId) {
        return queryGateway.query(new FindProductQuery(productId),
                ResponseTypes.instanceOf(ProductQueryModel.class)).join();
    }
}
