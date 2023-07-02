package control.tower.product.service.query.rest;

import control.tower.core.rest.PageResponseType;
import control.tower.core.rest.PaginationResponse;
import control.tower.core.utils.PaginationUtility;
import control.tower.product.service.query.queries.FindAllProductsQuery;
import control.tower.product.service.query.queries.FindProductQuery;
import control.tower.product.service.query.querymodels.ProductQueryModel;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import static control.tower.core.constants.DomainConstants.DEFAULT_PAGE;
import static control.tower.core.constants.DomainConstants.DEFAULT_PAGE_SIZE;

@RestController
@RequestMapping("/products")
@Tag(name = "Products Query API")
public class ProductsQueryController {

    @Autowired
    QueryGateway queryGateway;

    @GetMapping
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get all products")
    public CompletableFuture<PaginationResponse<ProductQueryModel>> getProducts(
            @RequestParam(defaultValue = DEFAULT_PAGE) int currentPage,
            @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize) {
        FindAllProductsQuery findAllProductsQuery = FindAllProductsQuery.builder()
                .pageable(PaginationUtility.buildPageable(currentPage, pageSize))
                .build();

        return queryGateway.query(findAllProductsQuery, new PageResponseType<>(ProductQueryModel.class))
                .thenApply(PaginationUtility::toPageResponse);
    }

    @GetMapping(params = "productId")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get product by id")
    public CompletableFuture<ProductQueryModel> getProduct(String productId) {
        return queryGateway.query(new FindProductQuery(productId),
                ResponseTypes.instanceOf(ProductQueryModel.class));
    }
}
