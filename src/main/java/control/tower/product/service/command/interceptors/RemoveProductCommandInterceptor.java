package control.tower.product.service.command.interceptors;

import control.tower.product.service.command.commands.RemoveProductCommand;
import control.tower.product.service.core.data.ProductLookupEntity;
import control.tower.product.service.core.data.ProductLookupRepository;
import org.axonframework.commandhandling.CommandMessage;
import org.axonframework.messaging.MessageDispatchInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.BiFunction;

import static control.tower.core.constants.LogMessages.INTERCEPTED_COMMAND;
import static control.tower.core.utils.Helper.throwExceptionIfEntityDoesNotExist;
import static control.tower.product.service.core.constants.ExceptionMessages.*;

@Component
public class RemoveProductCommandInterceptor implements MessageDispatchInterceptor<CommandMessage<?>> {

    private static final Logger LOGGER = LoggerFactory.getLogger(RemoveProductCommandInterceptor.class);

    private final ProductLookupRepository productLookupRepository;

    public RemoveProductCommandInterceptor(ProductLookupRepository productLookupRepository) {
        this.productLookupRepository = productLookupRepository;
    }

    @Override
    public BiFunction<Integer, CommandMessage<?>, CommandMessage<?>> handle(
            List<? extends CommandMessage<?>> messages) {
        return (index, command) -> {

            if (RemoveProductCommand.class.equals(command.getPayloadType())) {
                LOGGER.info(String.format(INTERCEPTED_COMMAND, command.getPayloadType()));

                RemoveProductCommand removeProductCommand = (RemoveProductCommand) command.getPayload();

                removeProductCommand.validate();

                ProductLookupEntity productLookupEntity = productLookupRepository.findByProductId(
                        removeProductCommand.getProductId());

                throwExceptionIfEntityDoesNotExist(productLookupEntity,
                        String.format(PRODUCT_WITH_ID_DOES_NOT_EXIST, removeProductCommand.getProductId()));
            }

            return command;
        };
    }
}
