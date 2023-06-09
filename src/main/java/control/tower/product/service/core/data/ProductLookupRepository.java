package control.tower.product.service.core.data;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductLookupRepository extends JpaRepository<ProductLookupEntity, String> {

    ProductLookupEntity findByProductId(String productId);

    ProductLookupEntity findByProductHash(String productHash);
}
