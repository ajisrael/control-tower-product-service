package control.tower.product.service.core.constants;

import static control.tower.core.constants.ExceptionMessages.*;
import static control.tower.product.service.core.constants.DomainConstants.PRODUCT;

public class ExceptionMessages {

    private ExceptionMessages() {
        throw new IllegalStateException("Constants class");
    }

    public static final String PRODUCT_ID_CANNOT_BE_EMPTY = String.format(PARAMETER_CANNOT_BE_EMPTY, "productId");
    public static final String NAME_CANNOT_BE_EMPTY = String.format(PARAMETER_CANNOT_BE_EMPTY, "name");
    public static final String PRICE_CANNOT_BE_NULL = String.format(PARAMETER_CANNOT_BE_NULL, "price");

    public static final String PRODUCT_WITH_ID_ALREADY_EXISTS = String.format(ENTITY_WITH_ID_ALREADY_EXISTS, PRODUCT, "%s");
    public static final String PRODUCT_WITH_ID_DOES_NOT_EXIST = String.format(ENTITY_WITH_ID_DOES_NOT_EXIST, PRODUCT, "%s");
    public static final String PRODUCT_ALREADY_CONFIGURED_IN_CATALOG = "Product already configured in catalog";

    public static final String PRODUCT_WITH_ID_CANNOT_BE_REMOVED_WHILE_ITEMS_IN_STOCK = "Product with id %s cannot be removed. %d items in stock.";
    public static final String FAILED_TO_DECREMENT_PRODUCT_STOCK_CANNOT_BE_NEGATIVE =
            "Failed to decrement product [%s], stock cannot be negative";
}
