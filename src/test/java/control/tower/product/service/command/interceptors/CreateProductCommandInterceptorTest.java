package control.tower.product.service.command.interceptors;

import control.tower.product.service.command.CreateProductCommand;
import control.tower.product.service.core.data.ProductLookupEntity;
import control.tower.product.service.core.data.ProductLookupRepository;
import org.axonframework.commandhandling.CommandMessage;
import org.axonframework.commandhandling.GenericCommandMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.function.BiFunction;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

class CreateProductCommandInterceptorTest {

    private CreateProductCommandInterceptor interceptor;
    private ProductLookupRepository lookupRepository;

    @BeforeEach
    void setUp() {
        lookupRepository = mock(ProductLookupRepository.class);
        interceptor = new CreateProductCommandInterceptor(lookupRepository);
    }

    @Test
    void testHandle_ValidCommand() {
        CreateProductCommand validCommand = CreateProductCommand.builder()
                .productId("productId").name("name").build();
        CommandMessage<CreateProductCommand> commandMessage = new GenericCommandMessage<>(validCommand);

        BiFunction<Integer, CommandMessage<?>, CommandMessage<?>> result = interceptor.handle(List.of(commandMessage));

        CommandMessage<?> processedCommand = result.apply(0, commandMessage);

        assertEquals(commandMessage, processedCommand);
    }

    @Test
    void testHandle_DuplicateProductId_ThrowsException() {
        String productId = "productId";
        CreateProductCommand duplicateCommand = CreateProductCommand.builder()
                .productId(productId).name("name").build();
        CommandMessage<CreateProductCommand> commandMessage = new GenericCommandMessage<>(duplicateCommand);

        ProductLookupEntity existingEntity = new ProductLookupEntity(productId);
        when(lookupRepository.findByProductId(productId)).thenReturn(existingEntity);

        BiFunction<Integer, CommandMessage<?>, CommandMessage<?>> result = interceptor.handle(List.of(commandMessage));

        assertThrows(IllegalStateException.class, () -> result.apply(0, commandMessage));

        verify(lookupRepository).findByProductId(productId);
    }
}
