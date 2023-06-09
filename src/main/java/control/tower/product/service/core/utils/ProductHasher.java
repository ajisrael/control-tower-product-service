package control.tower.product.service.core.utils;

import control.tower.product.service.core.models.HashableProduct;
import org.springframework.beans.BeanUtils;

import static control.tower.core.utils.Helper.calculateSHA256Hash;

public class ProductHasher {

    public static String createProductHash(Object product) {
        HashableProduct hashableProduct = new HashableProduct();
        BeanUtils.copyProperties(product, hashableProduct);
        return calculateSHA256Hash(hashableProduct.getCombinedValues());
    }
}
