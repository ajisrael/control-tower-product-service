package control.tower.product.service.query;

import control.tower.product.service.core.data.ProductEntity;
import control.tower.product.service.core.data.ProductRepository;
import control.tower.product.service.query.queries.FindAllProductsQuery;
import control.tower.product.service.query.queries.FindProductQuery;
import lombok.AllArgsConstructor;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@AllArgsConstructor
public class ProductsQueryHandler {

    private final ProductRepository productRepository;

    @QueryHandler
    public List<ProductEntity> findAllProducts(FindAllProductsQuery query) {
        return productRepository.findAll();
    }

    @QueryHandler
    public ProductEntity findProduct(FindProductQuery query) {
        Optional<ProductEntity> productEntityOptional = productRepository.findById(query.getProductId());

        return productEntityOptional.orElseThrow(
                () -> new IllegalStateException(String.format("Product %s does not exist", query.getProductId())));
    }
}
