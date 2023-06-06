package control.tower.product.service.query.rest;

import control.tower.product.service.core.data.ProductEntity;
import control.tower.product.service.query.FindAllProductsQuery;
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
        FindAllProductsQuery findAllProductsQuery = new FindAllProductsQuery();

        List<ProductEntity> productEntities = queryGateway.query(findAllProductsQuery,
                ResponseTypes.multipleInstancesOf(ProductEntity.class)).join();

        return convertProductEntitiesToProductRestModels(productEntities);
    }

    private List<ProductRestModel> convertProductEntitiesToProductRestModels(
            List<ProductEntity> productEntities) {
        List<ProductRestModel> productRestModels = new ArrayList<>();

        for (ProductEntity productEntity : productEntities) {
            productRestModels.add(new ProductRestModel(
                    productEntity.getProductId(),
                    productEntity.getName(),
                    productEntity.getPrice(),
                    productEntity.getQuantity()
            ));
        }

        return productRestModels;
    }
}
