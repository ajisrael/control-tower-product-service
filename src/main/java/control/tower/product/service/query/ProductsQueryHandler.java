package control.tower.product.service.query;

import control.tower.core.query.queries.DoProductsExistQuery;
import control.tower.product.service.core.data.ProductEntity;
import control.tower.product.service.core.data.ProductRepository;
import control.tower.product.service.query.queries.FindAllProductsQuery;
import control.tower.product.service.query.queries.FindProductQuery;
import control.tower.product.service.query.querymodels.ProductQueryModel;
import lombok.AllArgsConstructor;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import static control.tower.product.service.core.constants.ExceptionMessages.PRODUCT_WITH_ID_DOES_NOT_EXIST;

@Component
@AllArgsConstructor
public class ProductsQueryHandler {

    private final ProductRepository productRepository;

    @QueryHandler
    public List<ProductQueryModel> findAllProducts(FindAllProductsQuery query) {
        List<ProductEntity> productEntities = productRepository.findAll();
        return convertProductEntitiesToProductQueryModels(productEntities);
    }

    @QueryHandler
    public ProductQueryModel findProduct(FindProductQuery query) {
        ProductEntity productEntity = productRepository.findById(query.getProductId()).orElseThrow(
                () -> new IllegalStateException(String.format(PRODUCT_WITH_ID_DOES_NOT_EXIST, query.getProductId())));
        return convertProductEntityToProductQueryModel(productEntity);
    }

    @QueryHandler
    public boolean doProductsExist(DoProductsExistQuery query) {
        List<ProductEntity> productEntities = productRepository.findAllById(query.getProductIds());

        List<String> productIds = productEntities.stream()
                .map(ProductEntity::getProductId)
                .collect(Collectors.toList());

        return new HashSet<>(productIds).containsAll(query.getProductIds());
    }

    private List<ProductQueryModel> convertProductEntitiesToProductQueryModels(
            List<ProductEntity> productEntities) {
        List<ProductQueryModel> productQueryModels = new ArrayList<>();

        for (ProductEntity productEntity : productEntities) {
            productQueryModels.add(convertProductEntityToProductQueryModel(productEntity));
        }

        return productQueryModels;
    }

    private ProductQueryModel convertProductEntityToProductQueryModel(ProductEntity productEntity) {
        return new ProductQueryModel(
                productEntity.getProductId(),
                productEntity.getName(),
                productEntity.getPrice(),
                productEntity.getStock()
        );
    }
}
