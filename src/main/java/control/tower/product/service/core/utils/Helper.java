package control.tower.product.service.core.utils;

import static control.tower.product.service.core.constants.ExceptionMessages.FAILED_TO_DECREMENT_PRODUCT_STOCK_CANNOT_BE_NEGATIVE;
import static control.tower.product.service.core.constants.ExceptionMessages.PRODUCT_WITH_ID_CANNOT_BE_REMOVED_WHILE_ITEMS_IN_STOCK;

public class Helper {

    public static void throwExceptionIfStockIsLessThanOrEqualToZero(Integer stock, String productId) {
        if (stock <= 0) {
            throw new IllegalStateException(String.format(FAILED_TO_DECREMENT_PRODUCT_STOCK_CANNOT_BE_NEGATIVE, productId));
        }
    }

    public static void throwExceptionIfStockIsGreaterThanZero(Integer stock, String productId) {
        if (stock > 0) {
            throw new IllegalStateException(String.format(
                    PRODUCT_WITH_ID_CANNOT_BE_REMOVED_WHILE_ITEMS_IN_STOCK, productId, stock));
        }
    }
}
