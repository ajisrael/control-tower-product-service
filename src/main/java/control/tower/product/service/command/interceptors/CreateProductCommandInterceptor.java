package control.tower.product.service.command.interceptors;

import control.tower.product.service.command.commands.CreateProductCommand;
import control.tower.product.service.core.data.ProductLookupEntity;
import control.tower.product.service.core.data.ProductLookupRepository;
import org.axonframework.commandhandling.CommandMessage;
import org.axonframework.messaging.MessageDispatchInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;

import static control.tower.core.constants.LogMessages.INTERCEPTED_COMMAND;
import static control.tower.core.utils.Helper.throwExceptionIfEntityDoesExist;
import static control.tower.product.service.core.constants.ExceptionMessages.PRODUCT_ALREADY_CONFIGURED_IN_CATALOG;
import static control.tower.product.service.core.constants.ExceptionMessages.PRODUCT_WITH_ID_ALREADY_EXISTS;
import static control.tower.product.service.core.utils.ProductHasher.createProductHash;

@Component
public class CreateProductCommandInterceptor implements MessageDispatchInterceptor<CommandMessage<?>> {

    private static final Logger LOGGER = LoggerFactory.getLogger(CreateProductCommandInterceptor.class);

    private final ProductLookupRepository productLookupRepository;

    public CreateProductCommandInterceptor(ProductLookupRepository productLookupRepository) {
        this.productLookupRepository = productLookupRepository;
    }

    @Override
    public BiFunction<Integer, CommandMessage<?>, CommandMessage<?>> handle(
            List<? extends CommandMessage<?>> messages) {
        return (index, command) -> {

            if (CreateProductCommand.class.equals(command.getPayloadType())) {
                LOGGER.info(String.format(INTERCEPTED_COMMAND, command.getPayloadType()));

                CreateProductCommand createProductCommand = (CreateProductCommand) command.getPayload();

                createProductCommand.validate();

                ProductLookupEntity productLookupEntity = productLookupRepository.findByProductId(
                        createProductCommand.getProductId());

                throwExceptionIfEntityDoesExist(productLookupEntity,
                        String.format(PRODUCT_WITH_ID_ALREADY_EXISTS, createProductCommand.getProductId()));

                productLookupEntity = productLookupRepository.findByProductHash(createProductHash(createProductCommand));

                throwExceptionIfProductIsAlreadyInCatalog(productLookupEntity);
            }

            return command;
        };
    }

    private void throwExceptionIfProductIsAlreadyInCatalog(ProductLookupEntity productLookupEntity) {
        if (productLookupEntity != null) {
            throw new IllegalStateException(PRODUCT_ALREADY_CONFIGURED_IN_CATALOG);
        }
    }
}
