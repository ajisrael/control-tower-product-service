package control.tower.product.service.query;

import control.tower.product.service.core.data.ProductEntity;
import control.tower.product.service.core.data.ProductRepository;
import lombok.AllArgsConstructor;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class ProductsQueryHandler {

    private final ProductRepository productRepository;

    @QueryHandler
    public List<ProductEntity> findAllProducts(FindAllProductsQuery query) {
        return productRepository.findAll();
    }
}
