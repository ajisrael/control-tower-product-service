package control.tower.product.service.query;

import control.tower.product.service.core.data.ProductEntity;
import control.tower.product.service.core.data.ProductRepository;
import control.tower.product.service.query.queries.FindAllProductsQuery;
import control.tower.product.service.query.queries.FindProductQuery;
import lombok.AllArgsConstructor;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Component;

import java.util.List;

import static control.tower.product.service.core.constants.ExceptionMessages.PRODUCT_WITH_ID_DOES_NOT_EXIST;

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
        return productRepository.findById(query.getProductId()).orElseThrow(
                () -> new IllegalStateException(String.format(PRODUCT_WITH_ID_DOES_NOT_EXIST, query.getProductId())));
    }
}
