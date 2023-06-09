package control.tower.product.service.query.rest;

import control.tower.product.service.core.data.ProductEntity;
import control.tower.product.service.query.queries.FindAllProductsQuery;
import control.tower.product.service.query.queries.FindProductQuery;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductsQueryController {

    @Autowired
    QueryGateway queryGateway;

    @GetMapping
    public List<ProductRestModel> getProducts() {
        List<ProductEntity> productEntities = queryGateway.query(new FindAllProductsQuery(),
                ResponseTypes.multipleInstancesOf(ProductEntity.class)).join();

        return convertProductEntitiesToProductRestModels(productEntities);
    }

    @GetMapping(params = "productId")
    public ProductRestModel getProduct(String productId) {
        ProductEntity productEntity = queryGateway.query(new FindProductQuery(productId),
                ResponseTypes.instanceOf(ProductEntity.class)).join();

        return convertProductEntityToProductRestModel(productEntity);
    }

    private List<ProductRestModel> convertProductEntitiesToProductRestModels(
            List<ProductEntity> productEntities) {
        List<ProductRestModel> productRestModels = new ArrayList<>();

        for (ProductEntity productEntity : productEntities) {
            productRestModels.add(convertProductEntityToProductRestModel(productEntity));
        }

        return productRestModels;
    }

    private ProductRestModel convertProductEntityToProductRestModel(ProductEntity productEntity) {
        return new ProductRestModel(
                productEntity.getProductId(),
                productEntity.getName(),
                productEntity.getPrice(),
                productEntity.getStock()
        );
    }
}
